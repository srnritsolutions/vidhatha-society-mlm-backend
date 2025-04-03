package com.srnr.vidhatasocietymlm.mapper;

import org.springframework.beans.BeanUtils;

import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;

public class DTOToEntity 
{
	
	public static User userRequestDTOToUserEntity(RegistrationRequestDTO registrationRequestDTO)
	{
		User user=new User();
		BeanUtils.copyProperties(registrationRequestDTO, user);
		user.setPhoneNumber(Long.parseLong(registrationRequestDTO.getPhoneNumber()));
		return user;
	
	}
}
