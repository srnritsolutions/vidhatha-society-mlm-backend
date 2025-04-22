package com.srnr.vidhatasocietymlm.user.service;

import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.UserResponseDTO;

public interface UserService 
{
      UserResponseDTO registerUser(RegistrationRequestDTO registrationRequestDTO);
      String updateUserAfterPaymentSuccess(String userId,boolean paymentSuccess);
      UserResponseDTO loginUserByEmailAndPassword(String email,String password);
}
