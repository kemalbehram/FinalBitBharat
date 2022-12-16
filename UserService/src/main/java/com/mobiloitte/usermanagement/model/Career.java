package com.mobiloitte.usermanagement.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import com.mobiloitte.usermanagement.enums.CareerStatus;

@Entity
@Table
public class Career {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long careerId;

	private String careerName;

	private String careerEmail;

	private String careerTitle;

	@Lob
	@Column(length = 20971520)
	private String careerMessage;

	private Long userId;
	
	@Enumerated(EnumType.STRING)
	private CareerStatus careerStatus;
	

	public CareerStatus getCareerStatus() {
		return careerStatus;
	}

	public void setCareerStatus(CareerStatus careerStatus) {
		this.careerStatus = careerStatus;
	}

	public Long getCareerId() {
		return careerId;
	}

	public void setCareerId(Long careerId) {
		this.careerId = careerId;
	}

	public String getCareerName() {
		return careerName;
	}

	public void setCareerName(String careerName) {
		this.careerName = careerName;
	}

	public String getCareerEmail() {
		return careerEmail;
	}

	public void setCareerEmail(String careerEmail) {
		this.careerEmail = careerEmail;
	}

	public String getCareerTitle() {
		return careerTitle;
	}

	public void setCareerTitle(String careerTitle) {
		this.careerTitle = careerTitle;
	}

	public String getCareerMessage() {
		return careerMessage;
	}

	public void setCareerMessage(String careerMessage) {
		this.careerMessage = careerMessage;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
