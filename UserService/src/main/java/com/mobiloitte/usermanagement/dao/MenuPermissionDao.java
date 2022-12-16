package com.mobiloitte.usermanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.MenuPermission;

public interface MenuPermissionDao extends JpaRepository<MenuPermission, Long> {

	List<MenuPermission> findByMasterPermissionListRolePermissionIsDeletedFalse();

	List<MenuPermission> findByParentId(Long parentId);

}
