package com.mobiloitte.usermanagement.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table
public class MenuPermission {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long menuPermissionId;

	private String menuName;

	private String subMenuName;

	private String dataMenu;

	private String dataSubMenu;

	private String path;

	private Long parentId;

	@OneToMany(mappedBy = "menuPermission", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<MasterPermissionList> masterPermissionList;

	public Long getMenuPermissionId() {
		return menuPermissionId;
	}

	public String getDataMenu() {
		return dataMenu;
	}

	public void setDataMenu(String dataMenu) {
		this.dataMenu = dataMenu;
	}

	public String getDataSubMenu() {
		return dataSubMenu;
	}

	public void setDataSubMenu(String dataSubMenu) {
		this.dataSubMenu = dataSubMenu;
	}

	public void setMenuPermissionId(Long menuPermissionId) {
		this.menuPermissionId = menuPermissionId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getSubMenuName() {
		return subMenuName;
	}

	public void setSubMenuName(String subMenuName) {
		this.subMenuName = subMenuName;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<MasterPermissionList> getMasterPermissionList() {
		return masterPermissionList;
	}

	public void setMasterPermissionList(List<MasterPermissionList> masterPermissionList) {
		this.masterPermissionList = masterPermissionList;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
}