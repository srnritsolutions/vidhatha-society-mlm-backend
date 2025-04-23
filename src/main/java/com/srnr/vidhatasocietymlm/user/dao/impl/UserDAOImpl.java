package com.srnr.vidhatasocietymlm.user.dao.impl;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import com.srnr.vidhatasocietymlm.appconstants.Role;
import com.srnr.vidhatasocietymlm.exception.customexceptions.InvalidEmailAndPasswordException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.InvalidReferralException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserAlreadyExistEmailException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserAlreadyExistPhoneNumberException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotFoundException;
import com.srnr.vidhatasocietymlm.model.Earnings;
import com.srnr.vidhatasocietymlm.model.Referral;
import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.repository.EarningsRepository;
import com.srnr.vidhatasocietymlm.repository.ReferralRepository;
import com.srnr.vidhatasocietymlm.repository.UserRepository;
import com.srnr.vidhatasocietymlm.user.dao.UserDAO;
import jakarta.transaction.Transactional;

@Component
public class UserDAOImpl implements UserDAO {
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	@SuppressWarnings("unused")
	private static final double REFERRAL_EARNING_AMOUNT = 750.0;
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ReferralRepository referralRepository;

	@Autowired
	private EarningsRepository earningsRepository;

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
			throw new UserAlreadyExistEmailException("User already exists with email: " + user.getEmail());
		}
		if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) {
			logger.warn("User already exists with phone number: {}", user.getPhoneNumber());
			throw new UserAlreadyExistPhoneNumberException(
					"User already exists with this phone number: " + user.getPhoneNumber());
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
		if (referralCode != null && !referralCode.isBlank()) 
		{
			logger.debug("Validating referral code: {}", referralCode);
			// Retrieve Parent User by Referral Code
			Referral parentReferral = referralRepository.findByReferalCode(referralCode)
					.orElseThrow(() -> {
						logger.error("Invalid referral code: {}", referralCode);
						return new InvalidReferralException("Invalid referral code : "+referralCode);
					}
							);
			User parentUser = parentReferral.getUser();
			// lock parentUser record
			parentUser = userRepository.lockByUserId(parentUser.getId())
					.orElseThrow(() -> {
						logger.error("ParentUser not found with referral code: {}", referralCode);
						return new RuntimeException("ParentUser does not exist with referralCode: " + referralCode);
					});
			if (!parentReferral.getIsActive()) 
			{
				logger.error("Referral code {} is inactive", referralCode);
				throw new IllegalStateException("Referral code is inactive.");
			}
			if(parentUser.getChildren().size()<=3)
			{
				user.setParent(parentUser);
				parentUser.getChildren().add(user);

				if (parentUser.getChildren().size() == 3) 
				{
					parentUser.getReferral().setIsActive(false);
				}
				savedUser=userRepository.save(user);
				logger.info("Congratulation {} , your registration successful with referal code : {}",savedUser.getName(),referralCode);

			}
		}
		else 
		{
			savedUser = userRepository.save(user);
			logger.info("Congratulation {}, your registration successful...",savedUser.getName());
		}  
		return savedUser!=null?Optional.of(savedUser):Optional.empty();     
	}



	@Override
	@Transactional
	public Optional<User> updateUserAfterPaymentSuccess(String userId, boolean paymentSuccess) 
	{

		if (!paymentSuccess) 
		{
			logger.warn("User update is not possible due to unsuccessful payment for userId: {}", userId);
			throw new RuntimeException("User update is not possible due to unsuccessful payment");
		}

		if (userId == null || userId.isBlank()) 
		{
			throw new IllegalArgumentException("User ID cannot be null or blank");
		}

		// Optional<User> optionalUser = this.userRepository.findById(userId);
		//User user = this.userRepository.getReferenceById(userId);
		User user = this.userRepository.getById(userId);
		/*if (optionalUser.isEmpty()) {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }*/
		if(user == null) {
			throw new UserNotFoundException("User not found with ID: " + userId);
		}
		System.out.println("USer Name:"+user.getName());

		//User user = optionalUser.get();
		User parent = user.getParent();
		String referralCode = UUID.randomUUID().toString().replace("-", "").substring(0, 5);

		if (parent != null) 
		{
			parent = userRepository.lockByUserId(parent.getId()).orElseThrow(() -> 
			new IllegalStateException("Parent not found"));

			if (parent.getChildren().size() == 3) 
			{
				// Update referral status
				Referral referral = parent.getReferral();
				if (referral != null) 
				{
					referral.setIsActive(false);
					referralRepository.save(referral);
				}

				// Update earnings
				Earnings earnings = earningsRepository.findById(parent.getId())
						.orElse(new Earnings(parent));
				double earningsAmount = 250.0; // Example earnings calculation
				earnings.setTotalEarnings(earnings.getTotalEarnings() + earningsAmount);
				earningsRepository.save(earnings);
			}
		}

		user.setPaymentSuccess(true);
		user.setIsActive(true);


		Referral userReferral = user.getReferral();

		// Create and save referral
		// Referral userReferral = new Referral();
		userReferral.setReferalCode(referralCode);
		userReferral.setUser(user);
		userReferral.setIsActive(true);
		referralRepository.save(userReferral);

		user.setReferral(userReferral);
		userRepository.save(user);

		return Optional.of(user);
	}


	@Override
	public Optional<User> loginByEmailAndPassword(String userEmail, String userPassword) 
	{
		Optional<User> user = userRepository.findByEmailAndPassword(userEmail, userPassword);

		if (user.isPresent()) 
		{
			return user;
		} 
		else throw new InvalidEmailAndPasswordException("Invalid credentials: email or password incorrect.");
	}
	
	@Override
	public Optional<User> findByUserEmail(String userEmail) 
	{
		if(userEmail!=null && !userEmail.isBlank())
		{
			Optional<User> byEmail = userRepository.findByEmail(userEmail);
			if(byEmail!=null)
			{
				User user = byEmail.get();
				if(user.getIsActive())
				{
					return Optional.of(user);
				}
				else throw new RuntimeException("User is not active.");  
			}
			else throw new UserNotFoundException("user not exist with email : "+userEmail);
		}
		else throw new RuntimeException("user Email must not be null or blank!.");	
	}

	@Override
	public Optional<User> findByUserPhoneNumber(Long userPhoneNumber) 
	{
		User byPhoneNumber = userRepository.findByPhoneNumber(userPhoneNumber);
		return byPhoneNumber!=null?Optional.of(byPhoneNumber):Optional.empty();
	}

	@Override
	public Optional<User> updatePassword(String userEmail, String newPassword) 
	{
		Optional<User> byUserEmail = userRepository.findByEmail(userEmail);
		  if(byUserEmail.isPresent())
		  {
			  User user = byUserEmail.get();
			  
			  if(user.getIsActive())
			  {
				  user.setPassword(newPassword);
				  User updatePassword = userRepository.save(user);
				  return updatePassword!=null?Optional.of(updatePassword):Optional.empty();
			  }
			  else throw new RuntimeException("User is not active.");  
		  }
		  else throw new RuntimeException("User not exist with email : "+userEmail);
	}



	
}
