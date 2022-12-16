package com.mobiloitte.usermanagement.dao;

import java.util.List;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.UserSecurityDetails;

public interface UserSecurityDetailsDao extends JpaRepository<UserSecurityDetails, Long> {

	List<UserSecurityDetails> findByUserUserId(Long userIdForSecurityDetails, PageRequest of);

	List<UserSecurityDetails> findByUserUserIdOrderByUserSecurityDetailsIdDesc(Long userIdForSecurityDetails);

}
