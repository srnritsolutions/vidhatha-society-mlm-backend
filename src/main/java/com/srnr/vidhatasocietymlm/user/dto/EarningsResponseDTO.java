package com.srnr.vidhatasocietymlm.user.dto;

import java.io.Serializable;

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
public class EarningsResponseDTO implements Serializable{

	private String id;
	private Double totalEarnings;
	private Double withdrawableAmount;
	private Double availableAmount;

}
