package com.srnr.vidhatasocietymlm.mapper;

import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.user.dto.UserResponseDTO;

public class EntityToDTO 
{
	
	public static UserResponseDTO UserEntityToUserRequestDTO(User user)
	{
		UserResponseDTO userResponseDTO = new UserResponseDTO();
		userResponseDTO.setId(user.getId());
		userResponseDTO.setMessage("User registration successful !");
		return userResponseDTO;
	}
}
