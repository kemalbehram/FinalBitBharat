package com.mobiloitte.content.dto;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

public class CareerUpdateDto {

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
