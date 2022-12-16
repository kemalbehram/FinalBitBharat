package com.mobiloitte.usermanagement.dto;

public class ResetPasswordPhoneNo {

	private String phoneNo;

//	@NotBlank(message = "should not br blank and enter valid token")
	private String token;

//	@NotBlank(message = "should not br blank and enter valid password")
	private String password;

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
