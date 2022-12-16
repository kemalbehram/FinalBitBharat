package com.mobiloitte.server.authorization.dto;

public class VerifyGoogleCodeDto {

	private String secretKey;

	private int code;


	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public VerifyGoogleCodeDto(int code) {
		super();
		this.code = code;
	}


	



}
