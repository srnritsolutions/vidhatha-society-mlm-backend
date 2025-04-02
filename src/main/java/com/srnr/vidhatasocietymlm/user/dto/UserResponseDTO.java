package com.srnr.vidhatasocietymlm.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import com.srnr.vidhatasocietymlm.appconstants.Role;
import com.srnr.vidhatasocietymlm.model.User;

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
public class UserResponseDTO implements Serializable {

	private String id;
	private String name;
	private String email;
	private String password;
	private Long phoneNumber;
	private String profileImage;
	private Boolean isActive;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Role role;
	private Integer userLevel;
	private Boolean paymentSuccess;
	private String reward;
	private Boolean termsAndConditions;
	private AddressResponseDTO addressResponseDTO;
	private EarningsResponseDTO earningsResponseDTO;
	private ReferralResponseDTO referralResponseDTO;
	private User parent;
	private List<User> children;

}
