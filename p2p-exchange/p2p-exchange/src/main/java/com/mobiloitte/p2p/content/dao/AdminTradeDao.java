package com.mobiloitte.p2p.content.dao;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.model.Trading;

@Repository
public interface AdminTradeDao extends JpaRepository<Trading, Long> {

	Page<Trading> findByTradeStatus(TradeStatus tradeStatus, Pageable of);

	Long countByTradeStatus(TradeStatus tradeStatus);

	Page<Trading> findByCreationTimeBetween(Date l1, Date l2, Pageable of);

}
