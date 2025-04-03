package com.srnr.vidhatasocietymlm.user.dao.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PessimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import com.srnr.vidhatasocietymlm.appconstants.Role;
import com.srnr.vidhatasocietymlm.exception.customexceptions.InvalidReferralException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserAlreadyExistEmailException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserAlreadyExistPhoneNumberException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotFoundException;
import com.srnr.vidhatasocietymlm.model.Earnings;
import com.srnr.vidhatasocietymlm.model.Referral;
import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.repository.ReferralRepository;
import com.srnr.vidhatasocietymlm.repository.UserRepository;
import com.srnr.vidhatasocietymlm.user.dao.UserDAO;

import jakarta.transaction.Transactional;
@Component
public class UserDAOImpl implements UserDAO
{
	private static final Logger logger = LoggerFactory.getLogger(UserDAOImpl.class);
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ReferralRepository referralRepository;

	@Transactional
	@Retryable(retryFor = {PessimisticLockingFailureException.class},
			   maxAttempts =3,
			   backoff = @Backoff(delay = 3000) )
	@Override
	public Optional<User> saveUser(User user, String referralCode)//sujit +without ref
	{
		
		User savedUser=null;
        if (user == null) 
        {
            logger.error("User cannot be null!");
            throw new UserNotFoundException("User cannot be null!");
        }

        if (userRepository.findByEmail(user.getEmail()).isPresent()) 
        {
            logger.warn("User already exists with email: {}", user.getEmail());
            throw new UserAlreadyExistEmailException("User already exists with email: " + user.getEmail());
        }

        if (userRepository.findByPhoneNumber(user.getPhoneNumber()).isPresent()) 
        {
            logger.warn("User already exists with phone number: {}", user.getPhoneNumber());
            throw new UserAlreadyExistPhoneNumberException("User already exists with this phone number: " + user.getPhoneNumber());
        }

        Earnings earnings = new Earnings();
        Referral referral = new Referral();
        user.setIsActive(false);
        user.setProfileImage("default.png");
        user.setPaymentSuccess(false);
        user.setUserLevel(0);
        user.setEarnings(earnings);
        user.setReferral(referral);
        user.setRole(Role.USER);

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

            if (!parentReferral.getIsActive()) {
                logger.warn("Referral code {} is inactive", referralCode);
                throw new IllegalStateException("Referral code is inactive.");
            }

            user.setParent(parentUser);
            parentUser.getChildren().add(user);

            if (parentUser.getChildren().size() == 3) {
                parentUser.setIsActive(false);
            }

            savedUser=userRepository.save(user);
            logger.info("Congratulation {} , your registration successful with referal code : {}",savedUser.getName(),referralCode);
        }

        else {
        	savedUser = userRepository.save(user);
        	logger.info("Congratulation {}, your registration successful...",savedUser.getName());
		}
        
       return savedUser!=null?Optional.of(savedUser):Optional.empty(); 
        
        
    }
}

