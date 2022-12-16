package com.mobiloitte.usermanagement.dto;

public class EmailDtoNew {
	private Integer otp;
	private String source;
	private String ipAddress;

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public EmailDtoNew() {
		super();
	}

}
