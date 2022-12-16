package com.mobiloitte.usermanagement.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mobiloitte.usermanagement.model.RolePermission;

public interface RolePermissionDao extends JpaRepository<RolePermission, Long> {

	List<RolePermission> findByIsDeleted(Boolean b);

	List<RolePermission> findByIsDeletedFalse();

	List<RolePermission> findByRolesRoleId(Long rolesId);

	List<RolePermission> findByRolesRoleIdAndIsDeletedFalse(Long userRole);

	@Query(value = "select * from user_management.role_permission where user_management.role_permission.fk_role_id = ? and user_management.role_permission.is_deleted=false order by fk_master_permission_id ", nativeQuery = true)
	List<RolePermission> findByRolesRoleIdAndIsDeleted(@Param("fk_role_id") Long userRole, boolean b);

}
