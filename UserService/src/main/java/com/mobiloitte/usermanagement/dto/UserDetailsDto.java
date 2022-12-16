package com.mobiloitte.usermanagement.dto;

import java.util.Date;
import java.util.List;

import com.mobiloitte.usermanagement.enums.Authority;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.util.TwoFaType;

public class UserDetailsDto {

	private Long userId;

	private String username;

	private String password;

	private boolean enabled;

	private String phoneNo;

	private TwoFaType twoFaType;

	private boolean authenticated;

	private UserStatus userStatus;

	private RoleStatus role;

	private List<Authority> previlage;

	private String ipAdress;

	private Date userLastLoginTime;

	private String userLastLoginIpAddress;

	private String userLastLoginUserAgent;

	private String userLastLoginBrowserPrint;
	private String referredCode;
	private String zipCode;
	private Date lastTransactionDate;
	private String randomId;

	public String getReferredCode() {
		return referredCode;
	}

	public void setReferredCode(String referredCode) {
		this.referredCode = referredCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Date getLastTransactionDate() {
		return lastTransactionDate;
	}

	public void setLastTransactionDate(Date lastTransactionDate) {
		this.lastTransactionDate = lastTransactionDate;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public Date getUserLastLoginTime() {
		return userLastLoginTime;
	}

	public void setUserLastLoginTime(Date userLastLoginTime) {
		this.userLastLoginTime = userLastLoginTime;
	}

	public String getUserLastLoginIpAddress() {
		return userLastLoginIpAddress;
	}

	public void setUserLastLoginIpAddress(String userLastLoginIpAddress) {
		this.userLastLoginIpAddress = userLastLoginIpAddress;
	}

	public String getUserLastLoginUserAgent() {
		return userLastLoginUserAgent;
	}

	public void setUserLastLoginUserAgent(String userLastLoginUserAgent) {
		this.userLastLoginUserAgent = userLastLoginUserAgent;
	}

	public String getUserLastLoginBrowserPrint() {
		return userLastLoginBrowserPrint;
	}

	public void setUserLastLoginBrowserPrint(String userLastLoginBrowserPrint) {
		this.userLastLoginBrowserPrint = userLastLoginBrowserPrint;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public List<Authority> getPrevilage() {
		return previlage;
	}

	public void setPrevilage(List<Authority> previlage) {
		this.previlage = previlage;
	}

	public RoleStatus getRole() {
		return role;
	}

	public void setRole(RoleStatus role) {
		this.role = role;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
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

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

}
