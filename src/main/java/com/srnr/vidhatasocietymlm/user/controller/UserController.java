package com.srnr.vidhatasocietymlm.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.srnr.vidhatasocietymlm.repository.UserRepository;
import com.srnr.vidhatasocietymlm.user.dto.EmailRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.LoginRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.UserResponseDTO;
import com.srnr.vidhatasocietymlm.user.dto.VerifyOTPRequestDTO;
import com.srnr.vidhatasocietymlm.user.service.UserService;
import com.srnr.vidhatasocietymlm.util.Message;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@PostMapping(value = "/create", consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> createUser(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO)
	{
		UserResponseDTO registerUser = this.userService.registerUser(registrationRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
	}

	@PostMapping(value = "/verifyEmail", consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO)
	{
		return ResponseEntity.ok("Email Verification API is Working");
	}

	@PostMapping(value = "/VerifyOTP", consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyOTPRequestDTO verifyOTPRequestDTO) 
	{
		return ResponseEntity.ok("OTP Verification API is working");
	}


	@PostMapping(value = "/UpdateUserAfterPayment")
	public ResponseEntity<?> updateUserAfterPayment(@RequestParam String userId,@RequestParam boolean paymentSuccess )
	{
		String updateUserAfterPaymentSuccess = this.userService.updateUserAfterPaymentSuccess(userId, paymentSuccess);
		return ResponseEntity.status(HttpStatus.OK).body(new Message(updateUserAfterPaymentSuccess));
	}

	@PostMapping(
		    value = "/login",
		    consumes = MediaType.APPLICATION_JSON_VALUE,
		    produces = MediaType.APPLICATION_JSON_VALUE
		)
		public ResponseEntity<?> userLoginByEmailAndPassword(@Valid @RequestBody LoginRequestDTO dto) {
		     String loginUserByEmailAndPassword = userService.loginUserByEmailAndPassword(dto.getEmail(), dto.getPassword());
		    return ResponseEntity.status(HttpStatus.OK).body(new Message(loginUserByEmailAndPassword));
		}

}
