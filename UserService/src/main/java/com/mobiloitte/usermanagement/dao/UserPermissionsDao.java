package com.mobiloitte.usermanagement.dao;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.UserPermissions;

public interface UserPermissionsDao extends JpaRepository<UserPermissions, Long> {

	List<UserPermissions> findByUserUserId(long parseLong);

	void deleteByUserUserId(long l);

	List<UserPermissions> findByUserUserIdAndIsDeletedFalse(long parseLong);

	List<UserPermissions> findByIsDeletedFalse();

	@Query(value = "select distinct fk_user_id from user_management.user_permissions where user_management.user_permissions.is_deleted=false and user_management.user_permissions.fk_user_id = ? ", nativeQuery = true)
	List<BigInteger> findByUserUserId(@Param("fk_user_id") Long userId);

	List<UserPermissions> findByIsDeleted(boolean b);

	List<UserPermissions> findByIsDeletedAndUserUserStatusNot(boolean b, UserStatus deleted);

	List<UserPermissions> findByUserUserIdAndIsDeleted(long parseLong, Boolean isDeleted);

}
