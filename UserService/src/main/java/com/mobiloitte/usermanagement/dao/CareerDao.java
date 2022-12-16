package com.mobiloitte.usermanagement.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.Career;

public interface CareerDao extends JpaRepository<Career, Long> {

	Optional<Career> findByUserId(Long userId);

}
