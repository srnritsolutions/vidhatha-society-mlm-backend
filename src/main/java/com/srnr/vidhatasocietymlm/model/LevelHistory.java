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

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "LEVEL_HISTORY")
@SuppressWarnings("serial")
public class LevelHistory implements Serializable
{
	@Id
	@Column(name = "LEVEL_HISTORY_ID")
	private String id;

	@Column(name = "USER_ID")
	private String userId;
	
	@Column(name = "LEVEL_NUM",length = 20)
	private String levelNum;
	
	@Column(name = "REWARD_NAME",length = 30)
	private String rewardName;
	
	@Column(name = "LOCAL_DATE_TIME")
	private LocalDateTime assigneDateTime;
	
	@PrePersist
	public void generateId()
	{
		if(this.id==null)
		{
			this.id="LHS_"+CustomeIdGenerator.generateCustomId();
		}
	}
}
