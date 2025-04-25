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
public class LoginWithEmailRequestDTO implements Serializable{

	@NotBlank(message = "Email can't be null or blank !")
	@Pattern(regexp = "^(?!\\s*$)[a-zA-Z0-9][A-Za-z0-9._%+-]+@gmail\\.com$",message = "Invalid email")
	private String email;

	@NotBlank(message = "Password can't be null or blank !")
	@Size(min = 6, message = "Password must contain atleast 6 character ")
	private String password;

}
