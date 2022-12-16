package com.mobiloitte.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.enums.NomineeStatus;
import com.mobiloitte.usermanagement.model.AdminReferal;

public interface AdminreferalDao extends JpaRepository<AdminReferal, Long> {

	Optional<AdminReferal> findByUserId(Long userId);

}
