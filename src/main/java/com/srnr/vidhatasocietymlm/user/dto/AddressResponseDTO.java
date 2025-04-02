package com.srnr.vidhatasocietymlm.user.dto;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@SuppressWarnings("serial")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class AddressResponseDTO implements Serializable {

	private String id;
	private String city;
	private String state;
	private String pinCode;
	private String fullAddress;
	private LocalDate dob;

}
