package com.srnr.vidhatasocietymlm.user.service;

import org.springframework.web.multipart.MultipartFile;

import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.user.dto.ChangePasswordRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.EmailRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.LoginWithPhoneRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.UpdateUserRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.UserResponseDTO;
import com.srnr.vidhatasocietymlm.user.dto.VerifyOTPRequestDTO;
import com.srnr.vidhatasocietymlm.util.Message;

public interface UserService 
{
      UserResponseDTO registerUser(RegistrationRequestDTO registrationRequestDTO);
      String updateUserAfterPaymentSuccess(String userId,boolean paymentSuccess);
      String loginUserByEmailAndPassword(String email,String password);
      String updatePassword(ChangePasswordRequestDTO changePasswordRequestDTO);
      Message verifyUserByEmail(EmailRequestDTO emailRequestDTO);
  	  Message verifyOTP(VerifyOTPRequestDTO verifyOTPRequestDTO);
  	  Message editUserImage(MultipartFile file, String userId);
  	  User fetchUserById(String userId);
  	  Message updateUserByUserId(UpdateUserRequestDTO userRequestDTO,String userId);
  	  Message loginWithPhoneAndPassword(LoginWithPhoneRequestDTO loginWithPhoneRequestDTO);
}
