package com.srnr.vidhatasocietymlm.user.dto;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginWithPhoneRequestDTO implements Serializable
{
	@NotBlank(message="Phone Number can't Be blank or null !")
	@Pattern(regexp = "^[1-9][0-9]{9}$",message = "Phone Number must be 10 digits and not started with 0")
	private String phoneNumber;
	
	@NotBlank(message = "Password can't be null or blank !")
	@Size(min = 6, message = "Password must contain atleast 6 character ")
	private String password;
}
