package com.mobiloitte.server.authorization.model;

import java.util.Collection;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.mobiloitte.server.authorization.enums.Role;

public class User implements UserDetails, CredentialsContainer {
	private static final long serialVersionUID = 1L;
	private Long userId;
	private String email;
	private String password;
	private Collection<? extends GrantedAuthority> authorities;
	private boolean enabled;
	private TwoFaType twoFaType;
	private boolean authenticated;
	private boolean blocked;
	private Role role;
	private String ipAdress;
	private String deviceToken;
	private String deviceType;
	private String randomId;
	private String userAgent;
	private String location;
	
	
	
	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}

	public String getDeviceToken() {
		return deviceToken;
	}

	public void setDeviceToken(String deviceToken) {
		this.deviceToken = deviceToken;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public void eraseCredentials() {
		this.password = null;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return !blocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	public TwoFaType getTwoFaType() {
		return twoFaType;
	}

	public void setTwoFaType(TwoFaType twoFaType) {
		this.twoFaType = twoFaType;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setAuthenticated(boolean authenticated) {
		this.authenticated = authenticated;
	}

	public boolean isBlocked() {
		return blocked;
	}

	public void setBlocked(boolean blocked) {
		this.blocked = blocked;
	}

	


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", email=" + email + ", password=" + password + ", authorities=" + authorities
				+ ", enabled=" + enabled + ", twoFaType=" + twoFaType + ", authenticated=" + authenticated
				+ ", blocked=" + blocked + ", role=" + role + ", ipAdress=" + ipAdress + ", deviceToken=" + deviceToken
				+ ", deviceType=" + deviceType + ", randomId=" + randomId + ", userAgent=" + userAgent + ", location="
				+ location + "]";
	}

}
