package com.srnr.vidhatasocietymlm.model;

import java.io.Serializable;

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

@SuppressWarnings("serial")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LEVEL_TABLE")
public class Level implements Serializable
{
	@Id
	@Column(name = "LEVEL_ID")
	private String id;
	
	@Column(name = "LEVEL_NO")
	private Integer levelNum;
	
	@Column(length = 30)
	private String rewards;
	
	@PrePersist
	public void generateId()
	{
		if(this.id==null)
		{
			this.id="LVL_"+CustomeIdGenerator.generateCustomId();
		}
	}

}
