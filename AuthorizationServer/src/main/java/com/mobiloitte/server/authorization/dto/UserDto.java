package com.mobiloitte.server.authorization.dto;

import java.util.List;

import com.mobiloitte.server.authorization.enums.Role;
import com.mobiloitte.server.authorization.enums.UserStatus;
import com.mobiloitte.server.authorization.model.TwoFaType;

public class UserDto {

	private Long userId;
	private String username;
	private String password;
	private TwoFaType twoFaType;
	private boolean authenticated;
	private UserStatus userStatus;
	private List<String> previlage;
	private Role role;
	private String ipAdress;
	private String randomId;

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

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return "UserDto [userId=" + userId + ", username=" + username + ", password=" + password + ", twoFaType="
				+ twoFaType + ", authenticated=" + authenticated + ", userStatus=" + userStatus + ", previlage="
				+ previlage + ", role=" + role + ", ipAdress=" + ipAdress + "]";
	}

	public List<String> getPrevilage() {
		return previlage;
	}

	public void setPrevilage(List<String> previlage) {
		this.previlage = previlage;
	}

	public String getIpAdress() {
		return ipAdress;
	}

	public void setIpAdress(String ipAdress) {
		this.ipAdress = ipAdress;
	}

	public String getRandomId() {
		return randomId;
	}

	public void setRandomId(String randomId) {
		this.randomId = randomId;
	}

}
