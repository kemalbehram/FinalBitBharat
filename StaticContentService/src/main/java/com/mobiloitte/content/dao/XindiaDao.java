package com.mobiloitte.content.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Xindia;

public interface XindiaDao extends JpaRepository<Xindia, Long> {

	Optional<Xindia> findByXindiaId(Long xindiaId);

}
