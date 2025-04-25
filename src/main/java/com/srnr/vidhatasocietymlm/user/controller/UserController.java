package com.srnr.vidhatasocietymlm.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.srnr.vidhatasocietymlm.model.User;
import com.srnr.vidhatasocietymlm.user.dto.ChangePasswordRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.EmailRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.LoginRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;
import com.srnr.vidhatasocietymlm.user.dto.UpdateUserRequestDTO;
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

	@PostMapping(value = "/create", consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> createUser(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO)
	{
		UserResponseDTO registerUser = this.userService.registerUser(registrationRequestDTO);
		return ResponseEntity.status(HttpStatus.CREATED).body(registerUser);
	}

	@PostMapping(value = "/updateUserAfterPayment")
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

	@PostMapping(value = "/verifyEmail", consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> verifyEmail(@Valid @RequestBody EmailRequestDTO emailRequestDTO)
	{
		Message verifyUserByEmail = this.userService.verifyUserByEmail(emailRequestDTO);
		return new ResponseEntity<Message>(verifyUserByEmail,HttpStatus.OK);
	}

	@PostMapping(value = "/verifyOTP", consumes = { MediaType.APPLICATION_JSON_VALUE }, 
			produces = {MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> verifyOTP(@Valid @RequestBody VerifyOTPRequestDTO verifyOTPRequestDTO) 
	{
		Message verifyOTP = this.userService.verifyOTP(verifyOTPRequestDTO);
		return new ResponseEntity<Message>(verifyOTP,HttpStatus.OK);
	}

	@PostMapping(value="/updatePassword", 
			consumes = { MediaType.APPLICATION_JSON_VALUE},
			produces = { MediaType.APPLICATION_JSON_VALUE }
			)
	public ResponseEntity<?> updatePassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequestDTO) 
	{
		String updatePassword = userService.updatePassword(changePasswordRequestDTO);
		return ResponseEntity.status(HttpStatus.OK).body(new Message(updatePassword));
	}

	@PutMapping(value = "/editProfileImage",
			produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> editProfileImage(@RequestParam MultipartFile file,@RequestParam String userId) 
	{
		Message editUserImage = this.userService.editUserImage(file, userId);
		return  ResponseEntity.status(HttpStatus.OK).body(editUserImage);
	}


	@GetMapping(value = "/getUserById", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getUserByUserId(@RequestParam String userId) {
		User fetchUserById = this.userService.fetchUserById(userId);
		return ResponseEntity.status(HttpStatus.FOUND).body(fetchUserById);              

	}

	@PostMapping(value = "/updateUserById")
	public ResponseEntity<?> updateUserById(@RequestBody UpdateUserRequestDTO updateUserRequestDTO,@RequestParam String userId) {
		return null;
	}
}
