package com.mobiloitte.p2p.content.dto;

import java.util.Date;

import com.mobiloitte.p2p.content.enums.TwoFaType;

public class UserEmailAndNameDto {

	private Long userId;
	private String email;
	private String name;
	private TwoFaType twoFaType;
	private String imageUrl;
	private String kyc;
	private String joinigDate;
	private Long joinDays;

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

	public String getKyc() {
		return kyc;
	}

	public void setKyc(String kyc) {
		this.kyc = kyc;
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

	public TwoFaType getTwoFaType() {
		return twoFaType;
	}

	public void setTwoFaType(TwoFaType twoFaType) {
		this.twoFaType = twoFaType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

}
