package com.mobiloitte.usermanagement.dto;

import java.util.Date;

public class BlockedByListDto {

	private Long blockedId;

	private String userName;

	private Date dateTime;

	public Long getBlockedId() {
		return blockedId;
	}

	public void setBlockedId(Long blockedId) {
		this.blockedId = blockedId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

}