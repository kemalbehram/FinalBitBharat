package com.mobiloitte.notification.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mobiloitte.notification.model.ChatDetails;

@Repository
public interface ChatDao extends JpaRepository<ChatDetails, Long> {

	Page<ChatDetails> findByTradeId(String tradeId, Pageable pageable);

	Long countByTradeId(String tradeId);

	List<ChatDetails> findByTradeIdAndIsSeenTrue(String tradeId);

	List<ChatDetails> findByTradeId(String tradeId);

	List<ChatDetails> findByTradeIdAndIsSeenFalse(String tradeId);

	List<ChatDetails> findByTradeIdAndToUserIdAndFromUserIdAndIsSeenFalse(String tradeId, Long toUserId, Long userId);

	List<ChatDetails> findByFromUserId(Long userId);

	
	List<ChatDetails> findByFromUserIdIn(List<Long> groups);

	List<ChatDetails> findByToUserIdInAndFromUserIdInAndTradeId(List<Long> asList, List<Long> asList2, String tradeId);

	Optional<ChatDetails> findByTradeIdAndRole(String tradeId, String role);

	List<ChatDetails> findByTradeIdAndFromUserId(String tradeId, Long userId);

	List<ChatDetails> findByToUserId(Long userId);
	
	

}
