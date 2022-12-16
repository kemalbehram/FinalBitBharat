package com.mobiloitte.microservice.wallet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.P2pHistory;

public interface P2pHistoryDao extends JpaRepository<P2pHistory, Long>{

	List<P2pHistory> findByUserId(Long userId);

	List<P2pHistory> findByUserIdAndCoinName(Long userId, String coinName);

}
