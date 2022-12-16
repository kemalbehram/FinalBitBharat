package com.mobiloitte.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class UserPermissions {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userPermissionsId;

	private Boolean isDeleted;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "master_permissions_id")
	private MasterPermissionList masterPermissionList;

	@ManyToOne(fetch = FetchType.EAGER)
	@JsonIgnore
	@JoinColumn(name = "fk_user_Id")
	private User user;

	public Long getUserPermissionsId() {
		return userPermissionsId;
	}

	public void setUserPermissionsId(Long userPermissionsId) {
		this.userPermissionsId = userPermissionsId;
	}

	public MasterPermissionList getMasterPermissionList() {
		return masterPermissionList;
	}

	public void setMasterPermissionList(MasterPermissionList masterPermissionList) {
		this.masterPermissionList = masterPermissionList;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}
}