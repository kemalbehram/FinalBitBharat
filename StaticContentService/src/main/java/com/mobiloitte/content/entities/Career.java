package com.mobiloitte.content.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.mobiloitte.content.enums.CareerStatus;

@Entity
@Table

public class Career {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)	
	private Long teamId;
	
	private String teamName;
	
	private String image;
	
	@CreationTimestamp
	private Date createTime;
	
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Enumerated(EnumType.STRING)
	private CareerStatus careerStatus;
	
	
	public CareerStatus getCareerStatus() {
		return careerStatus;
	}

	public void setCareerStatus(CareerStatus careerStatus) {
		this.careerStatus = careerStatus;
	}

	public Long getTeamId() {
		return teamId;
	}

	public void setTeamId(Long teamId) {
		this.teamId = teamId;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}
	
	

}
