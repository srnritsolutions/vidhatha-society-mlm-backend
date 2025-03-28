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
@Table(name = "ADDRESS_TABLE")
public class Address implements Serializable
{
	@Id
	@Column(name = "ADDRES_ID")
	private String id;
	
	@Column(name = "CITY",length = 30)
	private String city;
	
	@Column(name = "STATE",length = 30)
	private String state;
	
	@Column(name = "PINCODE",length = 10)
	private String pinCode;
	
	@Column(name = "REFERRED_FROM",length = 200)
	private String fullAddress;
	
	@Column(name = "DOB",length = 30)
	private LocalDateTime dob;
	
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "USER_ID",referencedColumnName = "USER_ID")
	private User user;
	
	@PrePersist
	public void generateId()
	{
		if(this.id==null)
		{
			this.id="ADDRESS_"+CustomeIdGenerator.generateCustomId();
		}
	}

}
