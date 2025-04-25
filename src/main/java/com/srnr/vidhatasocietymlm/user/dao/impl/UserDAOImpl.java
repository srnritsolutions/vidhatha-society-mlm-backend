package com.srnr.vidhatasocietymlm.user.dao.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import com.srnr.vidhatasocietymlm.appconstants.Role;
import com.srnr.vidhatasocietymlm.exception.customexceptions.InvalidEmailAndPasswordException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.InvalidReferralException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserAlreadyExistException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotFoundException;
import com.srnr.vidhatasocietymlm.model.Address;
import com.srnr.vidhatasocietymlm.model.Earnings;
import com.srnr.vidhatasocietymlm.model.Level;
import com.srnr.vidhatasocietymlm.model.Referral;
import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.repository.EarningsRepository;
import com.srnr.vidhatasocietymlm.repository.LevelRepository;
import com.srnr.vidhatasocietymlm.repository.ReferralRepository;
import com.srnr.vidhatasocietymlm.repository.UserRepository;
import com.srnr.vidhatasocietymlm.user.dao.UserDAO;
import com.srnr.vidhatasocietymlm.util.FileStorageProperties;
import com.srnr.vidhatasocietymlm.util.ImageFileNameGenerator;
import jakarta.transaction.Transactional;

@Component
public class UserDAOImpl implements UserDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private FileStorageProperties fileStorageProperties;

	@Autowired
	private ReferralRepository referralRepository;

	@Autowired
	private EarningsRepository earningsRepository;

	@Autowired
	private LevelRepository levelRepository;

	@Transactional
	@Retryable(retryFor = {
			PessimisticLockingFailureException.class }, maxAttempts = 3, backoff = @Backoff(delay = 3000))
	@Override
	public Optional<User> saveUser(User user, String referralCode)// sujit +without ref
	{
		User savedUser = null;
		if (user == null) {
			logger.error("User cannot be null!");
			throw new UserNotFoundException("User cannot be null!");
		}
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			logger.warn("User already exists with email: {}", user.getEmail());
			throw new UserAlreadyExistException("User already exists with email: " + user.getEmail());
		}
		if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
			logger.warn("User already exists with phone number: {}", user.getPhoneNumber());
			throw new UserAlreadyExistException("User already exists with this phone number: " + user.getPhoneNumber());
		}
		Earnings earnings = new Earnings();
		Referral referral = new Referral();

		user.setIsActive(false);
		user.setProfileImage("default.png");
		user.setPaymentSuccess(false);
		user.setUserLevel(0);
		earnings.setUser(user);
		referral.setUser(user);
		user.setEarnings(earnings);
		user.setReferral(referral);
		user.setRole(Role.USER);

		earnings.setUser(user);
		referral.setUser(user);
		logger.info("User registration initiated for email: {}", user.getEmail());
		if (referralCode != null && !referralCode.isBlank()) {
			logger.debug("Validating referral code: {}", referralCode);
			// Retrieve Parent User by Referral Code
			Referral parentReferral = referralRepository.findByReferalCode(referralCode).orElseThrow(() -> {
				logger.error("Invalid referral code: {}", referralCode);
				return new InvalidReferralException("Invalid referral code : " + referralCode);
			});
			User getUser = parentReferral.getUser();
			if (getUser != null && getUser.getChildren() != null) {
				parentReferral.setUsedTimes(getUser.getChildren().size() + 1);
			}

			this.referralRepository.save(parentReferral);
			User parentUser = parentReferral.getUser();
			// lock parentUser record
			parentUser = userRepository.lockByUserId(parentUser.getId()).orElseThrow(() -> {
				logger.error("ParentUser not found with referral code: {}", referralCode);
				return new RuntimeException("ParentUser does not exist with referralCode: " + referralCode);
			});
			if (!parentReferral.getIsActive()) {
				logger.error("Referral code {} is inactive", referralCode);
				throw new IllegalStateException("Referral code is inactive.");
			}
			if (parentUser.getChildren().size() <= 3) {
				user.setParent(parentUser);
				parentUser.getChildren().add(user);

				if (parentUser.getChildren().size() == 3) {
					parentUser.getReferral().setIsActive(false);
				}
				savedUser = userRepository.save(user);
				logger.info("Congratulation {} , your registration successful with referal code : {}",
						savedUser.getName(), referralCode);
			}
		} else {
			savedUser = userRepository.save(user);
			logger.info("Congratulation {}, your registration successful...", savedUser.getName());
		}
		return savedUser != null ? Optional.of(savedUser) : Optional.empty();
	}

	@Override
	@Transactional
	public Optional<User> updateUserAfterPaymentSuccess(String userId, boolean paymentSuccess) {
		if (!paymentSuccess) {
			logger.warn("User update is not possible due to unsuccessful payment for userId: {}", userId);
			throw new RuntimeException("User update is not possible due to unsuccessful payment");
		}
		if (userId == null || userId.isBlank()) {
			throw new IllegalArgumentException("User ID cannot be null or blank");
		}
		User user = this.userRepository.getById(userId);
		if (user == null) {
			throw new UserNotFoundException("User not found with ID: " + userId);
		}
		System.out.println("USer Name:" + user.getName());
		User parent = user.getParent();
		String referralCode = UUID.randomUUID().toString().replace("-", "").substring(0, 5);

		if (parent != null) {
			parent = userRepository.lockByUserId(parent.getId())
					.orElseThrow(() -> new IllegalStateException("Parent not found"));

			if (parent.getChildren().size() == 3) {
				// Update referral status
				Referral referral = parent.getReferral();
				if (referral != null) {
					referral.setIsActive(false);
					referralRepository.save(referral);
				}
			}
		}
		user.setPaymentSuccess(true);
		user.setIsActive(true);
		Referral userReferral = user.getReferral();
		// Create and save referral
		userReferral.setReferalCode(referralCode);
		userReferral.setUser(user);
		userReferral.setIsActive(true);
		userReferral.setCreatedAt(LocalDateTime.now());
		referralRepository.save(userReferral);
		Optional<Level> byLevelNum = this.levelRepository.findByLevelNum(user.getUserLevel());
		user.setReward(byLevelNum.get().getRewards());

		user.setReferral(userReferral);
		userRepository.save(user);
		return Optional.of(user);
	}

	@Override
	public Optional<User> loginByEmailAndPassword(String userEmail, String userPassword) {
		Optional<User> user = userRepository.findByEmailAndPassword(userEmail, userPassword);

		if (user.isPresent()) {
			return user;
		} else
			throw new InvalidEmailAndPasswordException("Invalid credentials: email or password incorrect.");
	}

	@Override
	public Optional<User> loginByPhoneNumberAndPassword(Long phoneNumber, String userPassword) {
		Optional<User> user = userRepository.findByPhoneNumberAndPassword(null, userPassword);
		if (user.isPresent()) {
			return user;
		} else
			throw new InvalidEmailAndPasswordException("Invalid credentials: phonenumber or password incorrect.");
	}

	@Override
	public Optional<User> findByUserEmail(String userEmail) {
		if (userEmail != null && !userEmail.isBlank()) {
			Optional<User> byEmail = userRepository.findByEmail(userEmail);
			if (byEmail != null) {
				User user = byEmail.get();
				if (user.getIsActive()) {
					return Optional.of(user);
				} else
					throw new RuntimeException("User is not active.");
			} else
				throw new UserNotFoundException("user not exist with email : " + userEmail);
		} else
			throw new RuntimeException("user Email must not be null or blank!.");
	}

	@Override
	public Optional<User> findByUserPhoneNumber(Long userPhoneNumber) {
		Optional<User> byPhoneNumber = userRepository.findByPhoneNumber(userPhoneNumber);
		if (byPhoneNumber.isPresent()) {
			return byPhoneNumber;
		} else {
			return Optional.empty();
		}
	}

	@Override
	public Optional<User> updatePassword(String userEmail, String newPassword) {
		Optional<User> byUserEmail = userRepository.findByEmail(userEmail);
		if (byUserEmail.isPresent()) {
			User user = byUserEmail.get();

			if (user.getIsActive()) {
				user.setPassword(newPassword);
				User updatePassword = userRepository.save(user);
				return updatePassword != null ? Optional.of(updatePassword) : Optional.empty();
			} else
				throw new RuntimeException("User is not active.");
		} else
			throw new RuntimeException("User not exist with email : " + userEmail);
	}

	@Override
	public Optional<User> editImage(MultipartFile file, String userId) {
		Optional<User> userOptional = userRepository.findById(userId);
		if (!userOptional.isPresent())
			throw new UserNotFoundException("User does not exist with ID: " + userId);
		User user = userOptional.get();
		if (!user.getIsActive())
			throw new RuntimeException("User is not active ");
		String oldImageFileName = user.getProfileImage();
		String fileName = ImageFileNameGenerator.getNewFileName(file.getOriginalFilename());
		user.setProfileImage(fileName);
		User updatedUser = userRepository.save(user);
		try {
			if (updatedUser != null) {
				// now update image in local driver
				String targetDirectory = this.fileStorageProperties.getImageStoragePath();
				// create directories if not exist
				Path path = Paths.get(targetDirectory);
				if (!Files.exists(path)) {
					Files.createDirectories(path);
				}
				// save the file with new image file name
				Path targetLocation = path.resolve(fileName);
				File oldFile = new File(targetDirectory.concat(oldImageFileName));

				if (oldFile.exists() && !oldImageFileName.equals("default.png"))
					oldFile.delete();
				long copied = Files.copy(file.getInputStream(), targetLocation);
				return copied > 0 ? Optional.of(updatedUser) : Optional.empty();
			} else
				throw new RuntimeException("User profile not updated successfully!.");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	public Optional<User> findByUserId(String id) {
		Optional<User> byId = userRepository.findByIdAndIsActiveTrue(id);
		if (byId.isPresent()) {
			User user = byId.get();
			if (user.getIsActive()) {
				return user != null ? Optional.of(user) : Optional.empty();
			} else
				throw new RuntimeException("user is not active.");
		} else
			throw new UserNotFoundException("user not found with userId : " + id);
	}

	@Override
	public Optional<User> updateByUserId(User user, String userId) {
		boolean flag = false;
		if (user != null) {
			if (userId != null && !userId.isBlank()) {
				Optional<User> byId = userRepository.findById(userId);

				if (byId.isPresent()) {
					User oldUser = byId.get();
					if (oldUser.getIsActive()) {
						if (user.getEmail() != null && !user.getEmail().isBlank()) {
							if (!user.getEmail().equals(oldUser.getEmail())) {
								flag = true;
								if (userRepository.findByEmail(user.getEmail()).isPresent()) {
									throw new UserAlreadyExistException(
											"User already exists with email: " + user.getEmail());
								}
								oldUser.setEmail(user.getEmail());
							}
						} else
							throw new RuntimeException("user email must not be null or blank!.");

						String newPhonNo = String.valueOf(user.getPhoneNumber());

						if (user.getPhoneNumber() != null && (newPhonNo.length() == 10 && !newPhonNo.startsWith("0"))) {
							if (!user.getPhoneNumber().equals(oldUser.getPhoneNumber())) {
								flag = true;
								if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
									throw new UserAlreadyExistException(
											"User already exists with phonenumber: " + user.getPhoneNumber());
								}
								oldUser.setPhoneNumber(user.getPhoneNumber());
							}
						} else
							throw new RuntimeException(
									"user phoneNo must not be null and not start with '0' and length must be 10 digits.");

						if (user.getName() != null && !user.getName().isBlank()) {
							if (!user.getName().equals(oldUser.getName())) {
								flag = true;
								oldUser.setName(user.getName());
							}
						} else
							throw new RuntimeException("user name must be null or blank!.");
						Address newAddress = user.getAddresses();
						Address oldAddress = oldUser.getAddresses();
						if (newAddress != null) {
							if (oldAddress == null) {
								oldAddress = new Address();
								oldAddress.setUser(oldUser); // establishing the bidirectional link
							}
							// City
							if (newAddress.getCity() != null && !newAddress.getCity().isBlank()) {
								if (!newAddress.getCity().equals(oldAddress.getCity())) {
									oldAddress.setCity(newAddress.getCity());
									flag = true;
								}
							}
							// State
							if (newAddress.getState() != null && !newAddress.getState().isBlank()) {
								if (!newAddress.getState().equals(oldAddress.getState())) {
									oldAddress.setState(newAddress.getState());
									flag = true;
								}
							}
							// Pincode
							if (newAddress.getPinCode() != null && !newAddress.getPinCode().isBlank()) {
								if (!newAddress.getPinCode().equals(oldAddress.getPinCode())) {
									oldAddress.setPinCode(newAddress.getPinCode());
									flag = true;
								}
							}
							// Full Address
							if (newAddress.getFullAddress() != null && !newAddress.getFullAddress().isBlank()) {
								if (!newAddress.getFullAddress().equals(oldAddress.getFullAddress())) {
									oldAddress.setFullAddress(newAddress.getFullAddress());
									flag = true;
								}
							}
							// DOB
							if (newAddress.getDob() != null) {
								if (!newAddress.getDob().equals(oldAddress.getDob())) {
									oldAddress.setDob(newAddress.getDob());
									flag = true;
								}
							}
							oldUser.setAddresses(oldAddress);
						}

						if (flag)
							oldUser = userRepository.save(oldUser);
						return oldUser != null ? Optional.of(oldUser) : Optional.empty();

					} else
						throw new RuntimeException("user is not active");
				} else
					throw new UserNotFoundException("User not exist with id : " + userId);

			} else
				throw new UserNotFoundException("User id can't be null or blank!");

		} else
			throw new RuntimeException("User can't be null!");
	}

}
