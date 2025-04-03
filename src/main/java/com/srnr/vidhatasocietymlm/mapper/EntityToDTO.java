package com.srnr.vidhatasocietymlm.mapper;

import org.springframework.beans.BeanUtils;

import com.srnr.vidhatasocietymlm.model.Address;
import com.srnr.vidhatasocietymlm.model.Earnings;
import com.srnr.vidhatasocietymlm.model.Referral;
import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.user.dto.AddressResponseDTO;
import com.srnr.vidhatasocietymlm.user.dto.EarningsResponseDTO;
import com.srnr.vidhatasocietymlm.user.dto.ReferralResponseDTO;
import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.UserResponseDTO;

public class EntityToDTO 
{
	
	public static UserResponseDTO UserEntityToUserRequestDTO(User user)
	{
		
		UserResponseDTO userResponseDTO=new UserResponseDTO();
		BeanUtils.copyProperties(user, userResponseDTO);
		
		AddressResponseDTO addressResponseDTO=new AddressResponseDTO();
		BeanUtils.copyProperties(user.getAddresses(), addressResponseDTO);
		
		EarningsResponseDTO earningsResponseDTO=new EarningsResponseDTO();
		BeanUtils.copyProperties(user.getEarnings(),earningsResponseDTO);
		
		ReferralResponseDTO referralResponseDTO=new ReferralResponseDTO();
		BeanUtils.copyProperties(user.getReferral(), referralResponseDTO);
		
		
		
		userResponseDTO.setAddressResponseDTO(addressResponseDTO);
		userResponseDTO.setEarningsResponseDTO(earningsResponseDTO);
		userResponseDTO.setReferralResponseDTO(referralResponseDTO);
		
		
		
		return userResponseDTO;
		
		
	}
}
