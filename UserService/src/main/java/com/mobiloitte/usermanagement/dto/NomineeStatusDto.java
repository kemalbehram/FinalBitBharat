package com.mobiloitte.usermanagement.dto;

import com.mobiloitte.usermanagement.enums.NomineeStatus;

public class NomineeStatusDto {
	private Long nomineeId;
	private Long userId;
	private String reason;

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	private NomineeStatus status;

	public Long getNomineeId() {
		return nomineeId;
	}

	public void setNomineeId(Long nomineeId) {
		this.nomineeId = nomineeId;
	}

	public NomineeStatus getStatus() {
		return status;
	}

	public void setStatus(NomineeStatus status) {
		this.status = status;
	}

}
