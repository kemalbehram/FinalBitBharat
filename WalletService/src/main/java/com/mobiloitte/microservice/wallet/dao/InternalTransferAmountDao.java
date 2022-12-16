package com.mobiloitte.microservice.wallet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.InternalTransferAmount;

public interface InternalTransferAmountDao extends JpaRepository<InternalTransferAmount, Long> {

	List<InternalTransferAmount> findByFromUserId(Long userId);

	List<InternalTransferAmount> findByToUserId(Long userId);

	List<InternalTransferAmount> findByToUserIdAndCoinType(Long userId, String coinName);

	List<InternalTransferAmount> findByStatus(String complete);


}
