package com.srnr.vidhatasocietymlm.user.dto;

import java.io.Serializable;

import jakarta.validation.Valid;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegistrationRequestDTO implements Serializable{

	@NotBlank(message = "Name can't be blank or null !")
	@Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
	@Pattern(regexp = "^(?!\\s*$)[A-Za-z ]+$",message = "Name must contain only alphabets")
	private String name;
	
	@NotBlank(message = "Email can't be blank or null !")
	@Pattern(regexp = "^(?!\\s*$)[a-zA-Z][A-Za-z0-9._%+-]+@gmail\\.com$",message = "Invalid email")
	private String email;
	
	@NotBlank(message = "Password can't be blank or null !")
	@Size(min=6,message = "Password must be at least 6 characters")
	private String password;
	
	@NotBlank(message = "Mobile number can't be blank or null !")
	@Pattern(regexp = "^[1-9][0-9]{9}$",message = "Mobile number must be 10 digits and must not be started with 0")
	private String phoneNumber;
	
	private String referralCode;
	
	@AssertTrue(message = "You must accept the terms and conditions.")
	private Boolean termsAndConditions;
	
	@NotNull(message = "Address must not be null or blank !")
	@Valid
	private AddressRequestDTO addressRequestDTO;
	
}
