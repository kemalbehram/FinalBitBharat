package com.mobiloitte.usermanagement.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table
public class RolePermission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long rolePermissionId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_master_permission_list_id")
	private MasterPermissionList masterPermissionList;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_role_id")
	private Role roles;

	private Boolean isDeleted;

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getRolePermissionId() {
		return rolePermissionId;
	}

	public void setRolePermissionId(Long rolePermissionId) {
		this.rolePermissionId = rolePermissionId;
	}

	public Role getRole() {
		return roles;
	}

	public void setRole(Role role) {
		this.roles = role;
	}

	public MasterPermissionList getMasterPermissionList() {
		return masterPermissionList;
	}

	public void setMasterPermissionList(MasterPermissionList masterPermissionList) {
		this.masterPermissionList = masterPermissionList;
	}

}