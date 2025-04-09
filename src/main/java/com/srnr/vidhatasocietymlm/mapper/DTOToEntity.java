package com.srnr.vidhatasocietymlm.mapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.BeanUtils;

import com.srnr.vidhatasocietymlm.model.Address;
import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.user.dto.AddressRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;

public class DTOToEntity 
{
	
	public static User userRequestDTOToUserEntity(RegistrationRequestDTO registrationRequestDTO)
	{
		User user=new User();
		
		AddressRequestDTO addressRequestDTO = registrationRequestDTO.getAddressRequestDTO();
		Address address = new Address();
		BeanUtils.copyProperties(addressRequestDTO, address);
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate dob = LocalDate.parse(registrationRequestDTO.getAddressRequestDTO().getDateOfBirth().toString(),formatter);
		address.setDob(dob);
		address.setUser(user);
		user.setAddresses(address);
		user.setChildren(new ArrayList<>());
		user.setEmail(registrationRequestDTO.getEmail());
		user.setName(registrationRequestDTO.getName());
		user.setPassword(registrationRequestDTO.getPassword());
		user.setPhoneNumber( Long.parseLong(registrationRequestDTO.getPhoneNumber()));
		user.setTermsAndConditions(registrationRequestDTO.getTermsAndConditions());
		
		
		return user;
	
	}
}
