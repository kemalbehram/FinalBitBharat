package com.mobiloitte.content.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.HoldXindia;

//import com.mobiloitte.content.entities.HoldXindia;

public interface HoldXindiaDao extends JpaRepository<HoldXindia, Long> {

	Optional<HoldXindia> findByHoldXindiaId(Long holdxindiaId);

}
