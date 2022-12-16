package com.mobiloitte.usermanagement.dto;

public class SuspendUserDto {

	private Long suspendUserId;

	private String reason;

	public Long getSuspendUserId() {
		return suspendUserId;
	}

	public void setSuspendUserId(Long suspendUserId) {
		this.suspendUserId = suspendUserId;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}