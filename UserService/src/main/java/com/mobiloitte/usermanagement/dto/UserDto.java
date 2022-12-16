package com.mobiloitte.usermanagement.dto;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.model.UserDetail;

public class UserDto {
	private long userId;
	private Long userDetailId;
	private Long roleId;
	private RoleStatus roleStatus;
	private String firstName;
	private String middleName;
	private String lastName;
	private String email;
	private String password;
	private String phoneNo;
	private String userStatus = "UNVERIFIED";
	private UserDetail userDetails;
	private String google2faEnabled;
	private String google2faQrcodeUrl;
	private String google2faSecretKey;
	private String otp;
	private String secretKey;
	private String enabled;
	private String oldPassword;
	
	

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public Long getUserDetailId() {
		return userDetailId;
	}

	public void setUserDetailId(Long userDetailId) {
		this.userDetailId = userDetailId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public RoleStatus getRoleStatus() {
		return roleStatus;
	}

	public void setRoleStatus(RoleStatus roleStatus) {
		this.roleStatus = roleStatus;
	}

	public UserDetail getUserDetails() {
		return userDetails;
	}

	public void setUserDetails(UserDetail userDetails) {
		this.userDetails = userDetails;
	}

	public String getGoogle2faEnabled() {
		return google2faEnabled;
	}

	public void setGoogle2faEnabled(String google2faEnabled) {
		this.google2faEnabled = google2faEnabled;
	}

	public String getGoogle2faQrcodeUrl() {
		return google2faQrcodeUrl;
	}

	public void setGoogle2faQrcodeUrl(String google2faQrcodeUrl) {
		this.google2faQrcodeUrl = google2faQrcodeUrl;
	}

	public String getGoogle2faSecretKey() {
		return google2faSecretKey;
	}

	public void setGoogle2faSecretKey(String google2faSecretKey) {
		this.google2faSecretKey = google2faSecretKey;
	}

	public String getOtp() {
		return otp;
	}

	public void setOtp(String otp) {
		this.otp = otp;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@CreationTimestamp
	private Date createTime;

	@UpdateTimestamp
	private Date updateTime;

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

}
