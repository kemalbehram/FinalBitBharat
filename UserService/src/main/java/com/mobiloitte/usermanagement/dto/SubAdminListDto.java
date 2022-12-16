package com.mobiloitte.usermanagement.dto;

import java.util.Date;
import java.util.List;

import com.mobiloitte.usermanagement.enums.Authority;
import com.mobiloitte.usermanagement.enums.UserStatus;

public class SubAdminListDto {
	
	private Long userId;
	private String email;
	private String firstName;
	private String lastName;
	private String phoneNo;
	private Date createTime;
	private UserStatus userStatus;
	private String country;
	private List<Authority> previlage;
	private String gender;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public List<Authority> getPrevilage() {
		return previlage;
	}
	public void setPrevilage(List<Authority> previlage) {
		this.previlage = previlage;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
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
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getPhoneNo() {
		return phoneNo;
	}
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public UserStatus getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	

}
