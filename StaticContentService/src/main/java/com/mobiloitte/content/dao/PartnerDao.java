package com.mobiloitte.content.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Partnership;

public interface PartnerDao extends JpaRepository<Partnership, Long> {

	Optional<Partnership> findByPartnershipId(Long partnerId);

}
