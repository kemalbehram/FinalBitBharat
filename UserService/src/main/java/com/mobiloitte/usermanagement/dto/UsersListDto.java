package com.mobiloitte.usermanagement.dto;

import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.Role;
import com.mobiloitte.usermanagement.model.UserDetail;
import com.mobiloitte.usermanagement.model.UserLoginDetail;

public class UsersListDto {

	private Long userId;

	private String email;
	
	private Role role;

	private UserLoginDetail userLoginDetail;
	
	private UserStatus userStatus;
	
	private UserDetail userDetail;

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


	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}


	public UserLoginDetail getUserLoginDetail() {
		return userLoginDetail;
	}

	public void setUserLoginDetail(UserLoginDetail userLoginDetail) {
		this.userLoginDetail = userLoginDetail;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public UserDetail getUserDetail() {
		return userDetail;
	}

	public void setUserDetail(UserDetail userDetail) {
		this.userDetail = userDetail;
	}


	
}
