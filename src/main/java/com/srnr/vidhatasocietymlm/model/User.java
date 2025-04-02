package com.srnr.vidhatasocietymlm.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.srnr.vidhatasocietymlm.appconstants.Role;
import com.srnr.vidhatasocietymlm.util.CustomeIdGenerator;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
@Table(name = "USER_TABLE")
public class User implements Serializable
{
	
	@Id
	@Column(name = "USER_ID")
	private String id;
	
	@Column(name = "USER_NAME",length = 100)
	private String name;
	
	@Column(name = "USER_EMAIL")
	private String email;
	
	@Column(name = "PASSWORD")
	private String password;
	
	@Column(name = "PHONENUMBER",length = 10)
	private Long phoneNumber;
	
	@Column(name = "PROFILE_IMAGE")
	private String profileImage;
	
	@Column(name = "IS_ACTIVE")
	private Boolean isActive;
	
	@CreationTimestamp
	@Column(name = "CREATEDAT",updatable = false,nullable = false)
	private LocalDateTime createdAt;
	
	@UpdateTimestamp
	@Column(name = "UPDATEDAT",insertable = false,nullable = false)
	private LocalDateTime updatedAt;
	
	@Column(name = "ROLE")
	private Role role;
	
	@Column(name = "USER_LEVEL")
	private Integer userLevel;
	
	@Column(name = "PAYMENT_SUCCESS")
	private Boolean paymentSuccess;
	
	@Column(name = "REWARD",length = 30)
	private String reward;
	
	private Boolean termsAndConditions;
	
	@OneToOne(mappedBy = "user",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	private Address addresses;
	
	@OneToOne(mappedBy = "user",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	private Earnings earnings;
	
	@OneToOne(mappedBy = "user",cascade = {CascadeType.MERGE,CascadeType.PERSIST})
	private Referral referral;
		
	@ManyToOne
	@JoinColumn(name = "PARENT_ID")
	private User parent;       //Self-referential relationship 

	@OneToMany(mappedBy = "parent",cascade = CascadeType.ALL)
	private List<User> children;
	
	
	@PrePersist
	public void generateId()
	{
		if(this.id==null)
		{
			this.id="UID_"+CustomeIdGenerator.generateCustomId();
		}
	}
	
	
	

}
