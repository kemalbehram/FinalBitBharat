package com.mobiloitte.usermanagement.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.EmailSmsDetails;

public interface EmailSmsDao extends JpaRepository<EmailSmsDetails, Long> {

	Optional<EmailSmsDetails> findByUserId(Long userId);
}
