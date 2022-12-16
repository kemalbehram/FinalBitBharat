package com.mobiloitte.server.authorization.dto;

public class VerifySmsCodeDto {

	private Integer otp;
	private String source;
	private String ipAddress;

	public VerifySmsCodeDto(Integer otp) {
		this.otp = otp;
	}

	public VerifySmsCodeDto() {
		super();
	}


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

}
