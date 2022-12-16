package com.mobiloitte.usermanagement.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mobiloitte.usermanagement.enums.Authority;
import com.mobiloitte.usermanagement.enums.UserStatus;

@Entity
@Table
@Where(clause = "user_status!='DELETED'")
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	private String email;

	private String password;

	@Column(columnDefinition = "varchar(32) default 'UNVERIFIED'")
	@Enumerated(EnumType.STRING)
	private UserStatus userStatus = UserStatus.UNVERIFIED;

	@CreationTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date createTime;

	@UpdateTimestamp
	@Temporal(TemporalType.TIMESTAMP)
	private Date updateTime;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_user_detail")
	private UserDetail userDetail;

	@JsonIgnore
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<KYC> kyc;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "fk_user_Role")
	private Role role;

	private Date emailVerificationTime;
	
	private  String randomId;

	public Date getEmailVerificationTime() {
		return emailVerificationTime;
	}

	public void setEmailVerificationTime(Date emailVerificationTime) {
		this.emailVerificationTime = emailVerificationTime;
	}

	@ElementCollection
	//@Enumerated(EnumType.STRING)
	private List<Authority> previlage;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserSecurityDetails> userSecurityDetails;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<UserLoginDetail> userLoginDetails;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<DeviceTokenDetails> deviceTokenDetails;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<UserPermissions> userPermissions;

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
	private List<Feedback> feedback;

	public List<UserSecurityDetails> getUserSecurityDetails() {
		return userSecurityDetails;
	}

	public void setUserSecurityDetails(List<UserSecurityDetails> userSecurityDetails) {
		this.userSecurityDetails = userSecurityDetails;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
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

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}

	public List<KYC> getKyc() {
		return kyc;
	}

	public void setKyc(List<KYC> kyc) {
		this.kyc = kyc;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public List<Authority> getPrevilage() {
		return previlage;
	}

	public void setPrevilage(List<Authority> previlage) {
		this.previlage = previlage;
	}

	public List<UserLoginDetail> getUserLoginDetails() {
		return userLoginDetails;
	}

	public void setUserLoginDetails(List<UserLoginDetail> userLoginDetails) {
		this.userLoginDetails = userLoginDetails;
	}

	public List<DeviceTokenDetails> getDeviceTokenDetails() {
		return deviceTokenDetails;
	}

	public void setDeviceTokenDetails(List<DeviceTokenDetails> deviceTokenDetails) {
		this.deviceTokenDetails = deviceTokenDetails;
	}

	public List<UserPermissions> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(List<UserPermissions> userPermissions) {
		this.userPermissions = userPermissions;
	}

	public List<Feedback> getFeedback() {
		return feedback;
	}

	public void setFeedback(List<Feedback> feedback) {
		this.feedback = feedback;
	}

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

}