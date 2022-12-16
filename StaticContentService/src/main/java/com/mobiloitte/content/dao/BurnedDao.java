package com.mobiloitte.content.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.content.entities.Burned;

public interface BurnedDao extends JpaRepository<Burned, Long> {

	Optional<Burned> findByBurnedId(Long burnedid);

}
