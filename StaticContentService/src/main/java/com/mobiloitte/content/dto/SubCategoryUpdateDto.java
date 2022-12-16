package com.mobiloitte.content.dto;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

public class SubCategoryUpdateDto {

	private String careerType;
	@CreationTimestamp
	private Date createTime;

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	private String description;

	private String requirement;

	private String subTeamId;
	private String subTeamName;
	private String category;
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubTeamName() {
		return subTeamName;
	}

	public void setSubTeamName(String subTeamName) {
		this.subTeamName = subTeamName;
	}

	public String getSubTeamId() {
		return subTeamId;
	}

	public void setSubTeamId(String subTeamId) {
		this.subTeamId = subTeamId;
	}

	public String getCareerType() {
		return careerType;
	}

	public void setCareerType(String careerType) {
		this.careerType = careerType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRequirement() {
		return requirement;
	}

	public void setRequirement(String requirement) {
		this.requirement = requirement;
	}

}
