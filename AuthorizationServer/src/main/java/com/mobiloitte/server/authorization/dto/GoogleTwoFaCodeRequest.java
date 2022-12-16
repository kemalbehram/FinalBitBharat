package com.mobiloitte.server.authorization.dto;

import javax.validation.constraints.NotBlank;

public class GoogleTwoFaCodeRequest {
	
	@NotBlank
	private int otp;
	private String source;
	private String ipAddress;

	

	public int getOtp() {
		return otp;
	}

	public void setOtp(int otp) {
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
	
}
