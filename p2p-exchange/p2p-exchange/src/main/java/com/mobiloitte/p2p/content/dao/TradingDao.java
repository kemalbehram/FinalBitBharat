package com.mobiloitte.p2p.content.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.p2p.content.enums.DisputeStatus;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.model.Trading;

public interface TradingDao extends JpaRepository<Trading, Long> {

	Optional<Trading> findByTradeId(String tradeId);

	Optional<Trading> findByTradeIdAndFkUserId(String tradeId, Long userId);

	Page<Trading> findByTradeStatusAndFkUserId(TradeStatus tradeStatus, Long userId, Pageable pageable);

	Long countByTradeStatusAndFkUserId(TradeStatus tradeStatus, Long userId);

	Optional<Trading> findBypeerToPeerExchangeId(Long peerToPeerExchangeId);

	Page<Trading> findAllByFkUserId(Long userId, Pageable pageable);

	List<Trading> findByFkUserId(Long userId);

	Page<Trading> findByTradeStatusAndFkUserIdAndPartnerId(TradeStatus tradeStatus, Long userId, Long partnerId,
			Pageable pageable);

	Page<Trading> findByTradeStatusAndPartnerId(TradeStatus tradeStatus, Long userId, Pageable pageable);

	Long countByTradeStatusAndPartnerId(TradeStatus tradeStatus, Long userId);

	Page<Trading> findAllByFkUserIdOrPartnerId(Long userId, Long userId2, Pageable pageable);

	List<Trading> findByFkUserIdAndTradeStatusAndIsDeletedFalse(Long userId, TradeStatus complete);

	Long countByFkUserIdAndTradeStatusAndIsDeletedFalse(Long userId, TradeStatus complete);

	List<Trading> findByFkUserIdAndTradeStatusAndIsDeletedFalseOrderByCreationTimeAsc(Long userId,
			TradeStatus complete);

	List<Trading> findByFkUserIdAndPartnerIdAndTradeStatusAndCryptoCoin(Long userId, Long userId2, TradeStatus complete,
			String cryptoCoin);

	List<Trading> findByFkUserIdAndPartnerIdAndTradeStatus(Long userId, Long userId2, TradeStatus complete);

	long countByTradeStatus(TradeStatus cancel);

	long countByDisputeStatus(DisputeStatus raised);

	List<Trading> findByTradeStatus(TradeStatus complete);

	Optional<Trading> findByDisputeId(String disputeId);

	List<Trading> findByTradeStatusIn(List<TradeStatus> asList);

	Long countByFkUserIdInAndPartnerIdIn(List<Long> asList, List<Long> asList2);

	Long countByFkUserIdInAndPartnerIdInAndDisputeStatusIn(List<Long> asList, List<Long> asList2,
			List<DisputeStatus> asList3);

	List<Trading> findByFkUserIdInAndPartnerIdInAndDisputeStatusIn(List<Object> asList, List<Long> asList2,
			List<DisputeStatus> asList3, Pageable of);

	List<Trading> findByFkUserIdInAndPartnerIdIn(List<Long> asList, List<Long> asList2, Pageable of);

	Long countByFkUserId(Long userId);

	List<Trading> findAllByFkUserId(Long userId);

}
