package com.mobiloitte.p2p.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.model.Escrow;

public interface EscrowDao extends JpaRepository<Escrow, Long> {

	Optional<Escrow> findByTradeId(String tradeId);

	List<Escrow> findByTradeStatus(TradeStatus pending);


	List<Escrow> findByFkUserIdAndCoinName(Long userId, String coinName);

	List<Escrow> findByBlockBalanceUserIdAndCoinName(Long userId, String coinName);
	


	

}
