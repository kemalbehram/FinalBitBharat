package com.mobiloitte.p2p.content.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobiloitte.p2p.content.model.AdminCharge;

@Repository
public interface AdminChargeDao extends JpaRepository<AdminCharge, Long> {

	Optional<AdminCharge> findByCoinName(String coinName);

}
