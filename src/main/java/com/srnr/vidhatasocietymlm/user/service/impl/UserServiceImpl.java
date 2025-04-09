package com.srnr.vidhatasocietymlm.user.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.srnr.vidhatasocietymlm.exception.customexceptions.UserNotcreatedException;
import com.srnr.vidhatasocietymlm.mapper.DTOToEntity;
import com.srnr.vidhatasocietymlm.mapper.EntityToDTO;
import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.user.dao.UserDAO;
import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.UserResponseDTO;
import com.srnr.vidhatasocietymlm.user.service.UserService;

import jakarta.persistence.PessimisticLockException;

@Service
public class UserServiceImpl implements UserService
{

	private Logger logger=LoggerFactory.getLogger(UserServiceImpl.class);
	
	@Autowired
	private UserDAO userDAO;
	
	
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

}
