package com.mobiloitte.p2p.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.p2p.content.enums.StatusType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.enums.TransactionStatus;
import com.mobiloitte.p2p.content.model.Escrow;

public interface EscrowDao extends JpaRepository<Escrow, Long> {

	Optional<Escrow> findByTradeId(String tradeId);

	List<Escrow> findByTradeStatus(TradeStatus pending);


	List<Escrow> findByFkUserIdAndCoinName(Long userId, String coinName);

	List<Escrow> findByBlockBalanceUserIdAndCoinName(Long userId, String coinName);

//	List<Escrow> findByStatusTypeOrTradingPartner(Pageable of, StatusType statustype, String tradingPartner);

	List<Escrow> findByStatusType(Pageable of, StatusType statustype
			);

	List<Escrow> findByTradingPartner(Pageable of, String tradingPartner);

	List<Escrow> findByCoinName(Pageable of, String coinName);

	List<Escrow> findByTransactionStatus(Pageable of, TransactionStatus transactionStatus);

	List<Escrow> findByTransactionStatusOrStatusTypeOrCoinNameOrTradingPartner(Pageable of,
			TransactionStatus transactionStatus, StatusType statustype, String coinName, String tradingPartner);

//	void deleteByTradeId(String tradeId);

//	List<Escrow> findAllByStatusType(PageRequest of, StatusType statusType);

	

}
