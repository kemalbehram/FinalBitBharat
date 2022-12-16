package com.mobiloitte.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.UserLoginDetail;

public interface UserLoginDetailsDao extends JpaRepository<UserLoginDetail, Long> {

	List<UserLoginDetail> findByUserUserId(Long userId);

	List<UserLoginDetail> findByUserUserIdOrderByUserLoginIdDesc(Long userId);

	Optional<UserLoginDetail> findTopByUserUserIdOrderByUserLoginIdDesc(Long userId);

	List<UserLoginDetail> findByUserUserId(Long userId, Pageable of);

}
