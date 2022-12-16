package com.mobiloitte.usermanagement.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.User;

public interface AdminDao extends JpaRepository<User, Long> {

	long countByUserStatusAndRoleRole(UserStatus userStatus,RoleStatus roleStatus);

	long countByRoleRole(RoleStatus user);

}