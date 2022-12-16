package com.mobiloitte.content.dto;

import com.mobiloitte.content.enums.CareerStatus;

public class CareerDto {
	
	private String teamName;
	
	private String image;
	
	private CareerStatus careerStatus;
	

	public CareerStatus getCareerStatus() {
		return careerStatus;
	}

	public void setCareerStatus(CareerStatus careerStatus) {
		this.careerStatus = careerStatus;
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
