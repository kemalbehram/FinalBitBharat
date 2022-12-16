package com.mobiloitte.usermanagement.dto;

import com.mobiloitte.usermanagement.enums.CareerStatus;

public class CareerDto {

	private Long careerId;

	private String careerName;

	private String careerEmail;

	private String careerTitle;

	private String careerMessage;

	private Long userId;
	
	private CareerStatus careerStatus;

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

	public CareerStatus getCareerStatus() {
		return careerStatus;
	}

	public void setCareerStatus(CareerStatus careerStatus) {
		this.careerStatus = careerStatus;
	}

}
