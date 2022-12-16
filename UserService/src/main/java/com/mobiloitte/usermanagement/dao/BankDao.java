package com.mobiloitte.usermanagement.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.usermanagement.model.BankDetails;

public interface BankDao extends JpaRepository<BankDetails, Long> {

	Optional<BankDetails> findByAccountNoAndIsDeletedFalse(String accountNo);

	Optional<BankDetails> findByBankDetailIdAndUserUserIdAndIsDeletedFalse(Long bankDetailId, Long userId);

	List<BankDetails> findByUserUserIdAndIsDeletedFalse(Long userId, Pageable of);

	List<BankDetails> findByUserUserIdAndIsDeletedFalse(Long userId);

	Optional<BankDetails> findByBankDetailId(Long bankDetailId);

	Long countByUserUserIdAndIsDeletedFalse(Long userId);

	Optional<BankDetails> findByBankNameAndUserUserId(String bankName, Long userId);

}