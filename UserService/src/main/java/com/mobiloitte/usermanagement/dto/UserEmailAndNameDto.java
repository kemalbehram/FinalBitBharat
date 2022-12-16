package com.mobiloitte.usermanagement.dto;

import java.util.Date;

import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.util.TwoFaType;

public class UserEmailAndNameDto {

	private Long userId;
	private String email;
	private String name;
	private TwoFaType twoFaType;
	private UserStatus userStatus;
	private String joinigDate;
	private Long joinDays;
	private String imageUrl;

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public Long getJoinDays() {
		return joinDays;
	}

	public void setJoinDays(Long joinDays) {
		this.joinDays = joinDays;
	}

	public String getJoinigDate() {
		return joinigDate;
	}

	public void setJoinigDate(String joinigDate) {
		this.joinigDate = joinigDate;
	}

	private String kyc;

	public String getKyc() {
		return kyc;
	}

	public void setKyc(String kyc) {
		this.kyc = kyc;
	}

	public TwoFaType getTwoFaType() {
		return twoFaType;
	}

	public void setTwoFaType(TwoFaType twoFaType) {
		this.twoFaType = twoFaType;
	}

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

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

}
