package com.mobiloitte.usermanagement.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class MasterPermissionList {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long masterPermissionListId;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "fk_menu_permission_id")
	private MenuPermission menuPermission;

	@OneToMany(mappedBy = "masterPermissionList", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<RolePermission> rolePermission;

	@OneToMany(mappedBy = "masterPermissionList", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<UserPermissions> userPermissions;

	private Boolean isMenu;

	private Long parentId;

	public Boolean getIsMenu() {
		return isMenu;
	}

	public void setIsMenu(Boolean isMenu) {
		this.isMenu = isMenu;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getMasterPermissionListId() {
		return masterPermissionListId;
	}

	public void setMasterPermissionListId(Long masterPermissionListId) {
		this.masterPermissionListId = masterPermissionListId;
	}

	public List<UserPermissions> getUserPermissions() {
		return userPermissions;
	}

	public void setUserPermissions(List<UserPermissions> userPermissions) {
		this.userPermissions = userPermissions;
	}

	public List<RolePermission> getRolePermission() {
		return rolePermission;
	}

	public void setRolePermission(List<RolePermission> rolePermission) {
		this.rolePermission = rolePermission;
	}

	public MenuPermission getMenuPermission() {
		return menuPermission;
	}

	public void setMenuPermission(MenuPermission menuPermission) {
		this.menuPermission = menuPermission;
	}

}