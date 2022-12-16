package com.mobiloitte.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.UserDetail;

public interface UserDetailsDao extends JpaRepository<UserDetail, Long> {

	boolean existsByPhoneNo(String phoneNo);

	Optional<UserDetail> findByPhoneNo(String phoneNo);

	boolean existsByPhoneNoAndUserUserStatus(String phoneNo, UserStatus active);

	Optional<UserDetail> findByUserUserId(Long userId);

	Optional<UserDetail> findByUserDetailId(Long userDetailId);

	List<UserDetail> findAll();

	Optional<UserDetail> findByMyRefferalCode(String referredCode);

	List<UserDetail> findByReferredCode(String refferal);

	List<UserDetail> findByFinalReferal(String myReferralCode);

}
