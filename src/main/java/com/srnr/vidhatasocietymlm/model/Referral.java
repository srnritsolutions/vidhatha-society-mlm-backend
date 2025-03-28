package com.srnr.vidhatasocietymlm.model;

import java.io.Serializable;
import java.time.LocalDateTime;

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
@Table(name = "REFERRAL_TABLE")
public class Referral implements Serializable
{
	
	@Id
	@Column(name = "REFERAL_ID")
	private String id;
	
	@Column(name = "REFERRAL_CODE",length = 30)
	private String referalCode;
	
	@Column(name = "USED_TIMES")
	private Integer usedTimes;
	
	@Column(name = "CREATEDAT")
	private LocalDateTime createdAt;
	
	@Column(name = "ISACTIVE")
	private Boolean isActive;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID",referencedColumnName = "USER_ID")
	private User user;
	
	@PrePersist
	public void generateId()
	{
		if(this.id==null)
		{
			this.id="REFL_"+CustomeIdGenerator.generateCustomId();
		}
	}

}
