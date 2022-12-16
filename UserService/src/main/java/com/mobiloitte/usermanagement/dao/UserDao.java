package com.mobiloitte.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.User;

public interface UserDao extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

	Optional<User> findByEmail(String email);

	Optional<User> findByEmailOrUserDetailPhoneNo(String email, String phoneNo);

	Optional<User> findByUserId(Long userId);

	boolean existsByEmail(String email);

	List<User> findUserByRoleRole(RoleStatus role);

	List<User> findUserByUserStatus(UserStatus userStatus);

	List<User> findUserByRoleRoleOrderByUserId(RoleStatus user);

	boolean existsByEmailAndUserStatus(String email, UserStatus active);

	Optional<User> findByEmailAndUserStatus(String email, UserStatus active);

	List<User> findByUserStatusAndRoleRole(UserStatus active, RoleStatus user);

	Optional<User> findByRandomId(String oldRandomId);

	Optional<User> findByUserDetailMyRefferalCode(String referredCode);

	Optional<User> findByUserIdAndUserStatus(Long userId, UserStatus active);

	Optional<User> findByUserDetailPhoneNo(String phoneNo);

	Optional<User> findByUserStatusAndEmail(UserStatus unverified, String email);

	Optional<User> findByUserStatusAndUserId(UserStatus active, Long userId);

	List<User> findByUserStatus(UserStatus active);

	List<User> findByUserDetailCountry(String country);

}
