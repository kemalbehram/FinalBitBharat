package com.mobiloitte.order.model;

import java.util.Date;

public class ErrorResponse {
	private int status;
	private String message;
	private String error;
	private Date timestamp;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public ErrorResponse() {
		super();
		this.timestamp = new Date();
	}

	public ErrorResponse(int status, String message, String error) {
		this();
		this.status = status;
		this.message = message;
		this.error = error;
	}

	@Override
	public String toString() {
		return "ErrorResponse [status=" + status + ", message=" + message + ", error=" + error + ", timestamp="
				+ timestamp + "]";
	}
}
