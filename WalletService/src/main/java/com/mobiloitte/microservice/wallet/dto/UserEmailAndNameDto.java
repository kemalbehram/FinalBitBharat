package com.mobiloitte.microservice.wallet.dto;

import com.mobiloitte.microservice.wallet.enums.TwoFaType;
import com.mobiloitte.microservice.wallet.enums.UserStatus;

public class UserEmailAndNameDto {
	
	private Long userId;
	private String email;
	private String name;
	private TwoFaType twoFaType;
	private UserStatus userStatus;
	
	
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public TwoFaType getTwoFaType() {
		return twoFaType;
	}
	public void setTwoFaType(TwoFaType twoFaType) {
		this.twoFaType = twoFaType;
	}
	public UserStatus getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}
	
	

}
