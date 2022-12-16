package com.mobiloitte.usermanagement.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.FCMKeyDetails;

public interface FCMKeyDao extends JpaRepository<FCMKeyDetails, Long> {

	Optional<FCMKeyDetails> findByAdminId(Long adminId);

}
