package com.srnr.vidhatasocietymlm.model;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.srnr.vidhatasocietymlm.util.CustomeIdGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "EARNINGS_HISTORY")
@SuppressWarnings("serial")
public class EarningHistory implements Serializable
{
	@Id
	@Column(name = "EARNING_HISTORY_ID")
	private String id;
	
	@Column(name = "REFERRED_FROM",length = 100)
	private String reffredFrom;
	
	@Column(name = "REFERRED_TO",length = 100)
	private String refferedTo;
	
	@Column(name = "USERID")
	private String userId;
	
	@Column(name = "AMOUNT")
	private Double amount;
	
	@Column(name = "EARNED_AT")
	private LocalDateTime earnedAt;
	
	@Column(name = "IS_SUCCESS")
	private Boolean isSuccess;
	
	
	@PrePersist
	public void generateId()
	{
		if(this.id==null)
		{
			this.id="EHID_"+CustomeIdGenerator.generateCustomId();
		}
	}

}
