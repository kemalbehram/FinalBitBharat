package com.mobiloitte.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.BlockedP2pUser;

public interface BlockedP2pUserDao extends JpaRepository<BlockedP2pUser, Long> {

	Long countByBlockedUserId(Long userId);

	List<BlockedP2pUser> findByBlockedUserId(Long blockedUserId, Pageable of);

	Optional<BlockedP2pUser> findByBlockedByAndBlockedUserId(Long userId, Long blockUserId);

}