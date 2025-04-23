package com.srnr.vidhatasocietymlm.user.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.srnr.vidhatasocietymlm.exception.GlobalExceptionHandler;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotFoundException;
import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotcreatedException;
import com.srnr.vidhatasocietymlm.mapper.DTOToEntity;
import com.srnr.vidhatasocietymlm.mapper.EntityToDTO;
import com.srnr.vidhatasocietymlm.model.User;

import com.srnr.vidhatasocietymlm.user.dao.UserDAO;
import com.srnr.vidhatasocietymlm.user.dto.ChangePasswordRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.EmailRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.UserResponseDTO;
import com.srnr.vidhatasocietymlm.user.dto.VerifyOTPRequestDTO;
import com.srnr.vidhatasocietymlm.user.service.UserService;
import com.srnr.vidhatasocietymlm.util.Message;
import com.srnr.vidhatasocietymlm.util.OTPOperation;

import jakarta.persistence.PessimisticLockException;

@Service
public class UserServiceImpl implements UserService
{

	private final GlobalExceptionHandler globalExceptionHandler;

	private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private OTPOperation otpOperation;

	UserServiceImpl(GlobalExceptionHandler globalExceptionHandler) {
		this.globalExceptionHandler = globalExceptionHandler;
	}


	@Override
	public UserResponseDTO registerUser(RegistrationRequestDTO registrationRequestDTO)
	{

		try 
		{
			if(registrationRequestDTO==null)
			{
				logger.warn("RegistrationRequestDTO can't be null");
				throw new RuntimeException("RegistrationRequestDTO can't be null");
			}

			User user = DTOToEntity.userRequestDTOToUserEntity(registrationRequestDTO);
			if(user==null)
			{
				logger.warn("Something went wrong ,while Converting RequestDTO to User Entity");
				throw new RuntimeException("Something went wrong ,while Converting RequestDTO to User Entity");
			}

			Optional<User> saveUser = userDAO.saveUser(user, registrationRequestDTO.getReferralCode());

			if(saveUser.isPresent())
			{
				UserResponseDTO userResponseDTO = EntityToDTO.UserEntityToUserRequestDTO(saveUser.get());
				if(userResponseDTO!=null)
				{
					return userResponseDTO;
				}
				else
				{
					logger.warn("Something went wrong ,while Converting User Entity to UserResponseDTO");
					throw new RuntimeException("Something went wrong ,while Converting User Entity to UserResponseDTO");
				}
			}
			else
			{
				logger.warn("User Not Registed !");
				throw new UserNotcreatedException("User Not Registed !");
			}

		} 
		catch (PessimisticLockException e)
		{
			logger.warn("Someone is already using this referral code. Please try again some time");
			throw new RuntimeException("Someone is already using this referral code. Please try again some time");
		}

	}


	@Override
	public String updateUserAfterPaymentSuccess(String userId, boolean paymentSuccess) 
	{
		System.out.println(userId);
		if(userId!=null &&  ! userId.isBlank())
		{
			Optional<User> updateUser = this.userDAO.updateUserAfterPaymentSuccess(userId, paymentSuccess);
			if(updateUser.isPresent())
			{
				return "Payment Successfull with User Id :  "+userId;
			}
			else
			{
				throw new RuntimeException("Something went problem ,try again after some time !");
			}
		}
		else
		{
			throw new RuntimeException("user id can't be null or blank");
		}
	}



	@Override
	public String loginUserByEmailAndPassword(String email,String password)
	{
		if((email!=null &&  ! email.isBlank()) && (password!=null && ! password.isBlank()))
		{
			if(email.matches("^[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$"))
			{
				if(password.length()>=6)
				{
					Optional<User> userFetchedByEmailAndPassword = this.userDAO.loginByEmailAndPassword(email,password);
					if(userFetchedByEmailAndPassword.isPresent())
					{
						return "login Succesfull";
					}
					else throw new RuntimeException("User is not active");
				}
				else throw new IllegalArgumentException("Password Must be at least 6 characters");
			}
			else throw new IllegalArgumentException("Invalid Email Format");
		}
		else throw new IllegalArgumentException("Email and Password must not be null or blank");
	}

	@Override
	public Message verifyUserByEmail(EmailRequestDTO emailRequestDTO)
	{
		
		if(emailRequestDTO!=null)
		{
			Optional<User> optionalUser = this.userDAO.findByUserEmail(emailRequestDTO.getEmail());
			System.out.println(optionalUser.get()+emailRequestDTO.getEmail());
			if(optionalUser.isPresent())
			{
				System.out.println("inside if");
			   	String otp = this.otpOperation.getOTP();
			   	System.out.println(otp);
			   	boolean otpIsSendedToEmail = EmailSender.sendOTPToEmail(emailRequestDTO.getEmail(), otp);
			   	if(otpIsSendedToEmail)
			   	{
			   		this.otpOperation.storeOTP(emailRequestDTO.getEmail(), otp);
			   		return new  Message("OTP Sended Successfully.");
			   	}
			   	else throw new RuntimeException("something went wrong! try again after some time.");
			}
			else throw new RuntimeException("something went wrong! try again after some time.");	
		}
		else throw new RuntimeException("Email can't be null");
	}

	@Override
	public Message verifyOTP(VerifyOTPRequestDTO verifyOTPRequestDTO)
	{
		if(verifyOTPRequestDTO!=null)
		{		
			Optional<User> optionalUser = this.userDAO.findByUserEmail(verifyOTPRequestDTO.getEmail());
			if(optionalUser.isPresent())
			{
			   Optional<String> validateOTP = this.otpOperation.validateOTP(verifyOTPRequestDTO.getEmail(), verifyOTPRequestDTO.getOtp());
			   if(validateOTP.isPresent())
			         return new Message(validateOTP.get());
			   else throw new InvalideOTPException("Invalid OTP!");
			}
			else throw new RuntimeException("something went wrong! try again after some time.");
		}
		else throw new RuntimeException("something went wrong! try again after some time.");
	} 
	
	
	
	
	
	
	@Override
	public String updatePassword(ChangePasswordRequestDTO changePasswordRequestDTO) 
	{
		
		if(changePasswordRequestDTO!=null)
		{
			if(changePasswordRequestDTO.getEmail()!=null && !changePasswordRequestDTO.getEmail().isBlank())
			{
				if(changePasswordRequestDTO.getNewPassword()!=null && ! changePasswordRequestDTO.getNewPassword().isBlank())
				{
					if(changePasswordRequestDTO.getConfirmPassword()!=null && ! changePasswordRequestDTO.getConfirmPassword().isBlank())
					{
						if(changePasswordRequestDTO.getNewPassword().equals(changePasswordRequestDTO.getConfirmPassword()))
						{
							Optional<User> optionalUser = this.userDAO.updatePassword(changePasswordRequestDTO.getEmail(),changePasswordRequestDTO.getNewPassword());
							if(optionalUser.isPresent())
							{						
								     return "Password changed successfully";								
							}
							else throw new UserNotFoundException("User password not updated !");
						}
						else throw new RuntimeException("New Password and Confirm Password Should be Same.");
					}
					else throw new RuntimeException("confirm password can't be null or blank");
				}
				else throw new RuntimeException("New Password must not be null and blank");
			}
			else throw new RuntimeException("User Email can't be null or blank");
		}
		else throw new RuntimeException("Password Credential Can't be null");		
	}
}
