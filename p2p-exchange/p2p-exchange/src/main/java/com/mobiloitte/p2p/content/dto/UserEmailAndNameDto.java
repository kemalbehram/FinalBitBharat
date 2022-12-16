package com.mobiloitte.p2p.content.dto;

import com.mobiloitte.p2p.content.enums.TwoFaType;

public class UserEmailAndNameDto {
	
	private Long userId;
	private String email;
	private String name;
	private TwoFaType twoFaType;
	private String imageUrl;
	
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
