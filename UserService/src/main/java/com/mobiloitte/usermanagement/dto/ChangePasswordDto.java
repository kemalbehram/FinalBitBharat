package com.mobiloitte.usermanagement.dto;

public class ChangePasswordDto {

//	@NotBlank(message = "should not be blank and enter valid old password")
	private String oldPassword;

//	@NotBlank(message = "should not be blank and enter valid new password")
	private String newPassword;

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

}
