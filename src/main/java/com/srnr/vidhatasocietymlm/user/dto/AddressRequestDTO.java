package com.srnr.vidhatasocietymlm.user.dto;

import java.io.Serializable;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
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
public class AddressRequestDTO implements Serializable {

	@NotBlank(message = "City can't be blank or null !")
	@Size(min = 3, max = 50, message = "City must be between 3 and 50 characters")
	@Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", 
    message = "City must contain only letters, spaces, or hyphens")
	private String city;

	@NotBlank(message = "State can't be blank or null !")
	@Size(min = 3, max = 50, message = "State must be between 3 and 50 characters")
	@Pattern(regexp = "^[a-zA-Z]+(?:[\\s-][a-zA-Z]+)*$", 
    message = "State must contain only letters, spaces, or hyphens")
	private String state;

	@NotBlank(message = "Pin code can't be blank or null !")
	@Pattern(regexp = "^[1-9][0-9]{5}$", message = "Pin code must be 6 digits and must not start with 0")
	private String pinCode;

	@NotBlank(message = "Full address can't be blank or null !")
	@Size(min = 10, max = 200, message = "Full address must be between 10 and 200 characters")
    @Pattern(regexp = "^[A-Za-z0-9 ,.-]{10,200}$", message = "Full address must only contain letters, numbers, spaces, commas, dots, and hyphens, between 10 and 200 characters")
	private String fullAddress;

	@NotNull(message = "Date of birth not be null or blank")
	@Past(message = "Date of birth must be in the past")
	private LocalDate dateOfBirth;

}
