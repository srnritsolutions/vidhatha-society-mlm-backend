package com.srnr.vidhatasocietymlm.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.srnr.vidhatasocietymlm.user.dto.RegistrationRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

	@PostMapping(value = "/create")
	public ResponseEntity<?> createUser(@Valid @RequestBody RegistrationRequestDTO registrationRequestDTO) 
	{
	    System.out.println(registrationRequestDTO);	
	    return ResponseEntity.status(HttpStatus.CREATED).body(registrationRequestDTO);
	}
	
	
	

}
