package com.mobiloitte.usermanagement.dto;

public class ResetPasswordDto {

	private String email;

//	@NotBlank(message = "should not br blank and enter valid token")
	private String token;

//	@NotBlank(message = "should not br blank and enter valid password")
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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
