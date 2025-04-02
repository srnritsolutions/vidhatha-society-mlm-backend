package com.srnr.vidhatasocietymlm.model;

import java.io.Serializable;

import com.srnr.vidhatasocietymlm.util.CustomeIdGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@SuppressWarnings("serial")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EARNINGS_TABLE")
public class Earnings implements Serializable
{
	@Id
	@Column(name = "EARNING_ID")
	private String id;
	
	@Column(name = "TOTAL_EARNINGS")
	private Double totalEarnings;
	
	@Column(name = "WITHDRAWABLE_AMOUNT")
	private Double withdrawableAmount;
	
	@Column(name = "AVAILABLE_AMOUNT")
	private Double availableAmount;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID",referencedColumnName = "USER_ID")
	private User user;
	
	@PrePersist
	public void generateId()
	{
		if(this.id==null)
		{
			this.id="EID_"+CustomeIdGenerator.generateCustomId();
		}
	}

}
