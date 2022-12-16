package com.mobiloitte.microservice.wallet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.BankDetails;

public interface BankDao extends JpaRepository<BankDetails, Long> {

	Optional<BankDetails> findByAccountNoAndIsDeletedFalse(String accountNo);

	Optional<BankDetails> findByBankDetailIdAndUserIdAndIsDeletedFalse(Long bankDetailId, Long userId);

	List<BankDetails> findByUserIdAndIsDeletedFalse(Long userId, Pageable of);

	List<BankDetails> findByUserIdAndIsDeletedFalse(Long userId);

	Optional<BankDetails> findByBankDetailId(Long bankDetailId);

	Long countByUserIdAndIsDeletedFalse(Long userId);

	List<BankDetails> findByBankName(String bank);

	Optional<BankDetails> findByBankNameAndUserId(String bank, Long userId);

}