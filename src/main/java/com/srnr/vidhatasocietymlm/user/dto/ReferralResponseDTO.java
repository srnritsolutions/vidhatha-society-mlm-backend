package com.srnr.vidhatasocietymlm.user.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

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
public class ReferralResponseDTO implements Serializable{

	private String id;
	private String referalCode;
	private Integer usedTimes;
	private LocalDateTime createdAt;
	private Boolean isActive;

}
