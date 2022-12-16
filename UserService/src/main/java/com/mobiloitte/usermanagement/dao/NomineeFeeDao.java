package com.mobiloitte.usermanagement.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.NomineeFee;

public interface NomineeFeeDao extends JpaRepository<NomineeFee, Long>{

	Optional<NomineeFee> findByUserId(Long userId);

}
