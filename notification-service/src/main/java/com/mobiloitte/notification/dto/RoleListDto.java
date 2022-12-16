package com.mobiloitte.notification.dto;

import java.util.List;

public class RoleListDto {
	private Long roleID;
	private List<Long> userId;

	public Long getRoleID() {
		return roleID;
	}

	public void setRoleID(Long roleID) {
		this.roleID = roleID;
	}

	public List<Long> getUserId() {
		return userId;
	}

	public void setUserId(List<Long> userId) {
		this.userId = userId;
	}

	public RoleListDto(Long roleID, List<Long> userId) {
		super();
		this.roleID = roleID;
		this.userId = userId;
	}

	public RoleListDto() {
		super();
	}

}
