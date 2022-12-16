package com.mobiloitte.microservice.wallet.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.Fiat;
import com.mobiloitte.microservice.wallet.enums.FiatStatus;
import com.mobiloitte.microservice.wallet.enums.TransactionType;

public interface FiatDao extends JpaRepository<Fiat, Long> {

	List<Fiat> findByUserId(Long userId);

	List<Fiat> findByUtrNoOrFiatStatus(String utrNo, FiatStatus fiatStatus);

	Optional<Fiat> findByUtrNo(String utrNo);

	Optional<Fiat> findByUtrNoAndFiatStatus(String utrNo, FiatStatus pending);

	List<Fiat> findByFiatStatus(FiatStatus fiatStatus);

	Optional<Fiat> findByUserIdAndFiatStatus(Long userId1, FiatStatus pending);

	List<Fiat> findByUserIdAndTransactionType(Long userId, TransactionType deposit, Pageable pageRequest);

	List<Fiat> findByTransactionType(Pageable of, TransactionType deposit);

	List<Fiat> findAllByTransactionType(Pageable of, TransactionType withdraw);

	List<Fiat> findByFiatStatusAndTransactionType(FiatStatus fiatStatus, TransactionType withdraw);

	List<Fiat> findByUtrNoOrFiatStatusAndTransactionType(String utrNo, FiatStatus fiatStatus, TransactionType deposit);

	List<Fiat> findAllByTransactionTypeOrFiatStatus(Pageable of, TransactionType withdraw, FiatStatus fiatStatus);

	List<Fiat> findByTransactionTypeOrFiatStatus(Pageable of, TransactionType withdraw, FiatStatus fiatStatus);

	List<Fiat> findByTransactionTypeAndFiatStatus(Pageable of, TransactionType withdraw, FiatStatus fiatStatus);

	List<Fiat> findByTransactionTypeOrFiatStatusOrUtrNo(Pageable of, TransactionType deposit, FiatStatus fiatStatus,
			String utrNo);

	List<Fiat> findAllByTransactionTypeOrFiatStatusOrUtrNo(Pageable of, TransactionType deposit, FiatStatus fiatStatus,
			String utrNo);

	List<Fiat> findByTransactionTypeAndUtrNo(Pageable of, TransactionType deposit, String utrNo);

	List<Fiat> findByTransactionTypeAndFiatStatusAndUtrNo(Pageable of, TransactionType deposit, FiatStatus fiatStatus,
			String utrNo);

	List<Fiat> findByUserIdAndTransactionTypeAndFiatStatus(Long userId, TransactionType withdraw, FiatStatus completed);

	List<Fiat> findAllByUserIdAndFiatStatus(Long userId, FiatStatus completed);

	List<Fiat> findByUserIdAndTransactionTypeAndCreateTimeBetween(Long userId, TransactionType deposit, Date date,
			Date date2);

	List<Fiat> findByUserIdAndTransactionTypeOrCreateTimeBetween(Long userId, TransactionType deposit, Date date,
			Date date2);

	List<Fiat> findByUserIdAndTransactionTypeOrCreateTimeBetweenOrEmailOrName(Long userId, TransactionType deposit,
			Date date, Date date2, String email, String name);

	List<Fiat> findByUserIdAndTransactionTypeOrCreateTimeBetweenOrEmailOrNameOrUtrNo(Long userId,
			TransactionType deposit, Date date, Date date2, String email, String name, String utrNo);

	List<Fiat> findByEmail(String email, Pageable pageRequest);

	List<Fiat> findAllByUtrNo(String utrNo, TransactionType deposit, Pageable of);

	List<Fiat> findByName(String name, Pageable of);

	List<Fiat> findByUserIdAndTransactionType(Long userId, TransactionType deposit);

	List<Fiat> findByCreateTimeBetween(Date date, Date date2, Pageable of);

	List<Fiat> findByCreateTimeBetweenAndTransactionType(Date date, Date date2, TransactionType deposit, Pageable of);

	List<Fiat> findByNameAndTransactionType(String name, TransactionType deposit, Pageable of);

	List<Fiat> findByEmailAndTransactionType(String email, TransactionType deposit, Pageable of);

	List<Fiat> findByUserIdAndFiatStatusAndTransactionType(Long userId1, FiatStatus pending, TransactionType withdraw);

	Optional<Fiat> findByUserIdAndFiatStatusAndTransactionTypeIn(Long userId1, FiatStatus pending,
			TransactionType withdraw);

	Optional<Fiat> findByUserIdAndFiatStatusAndTransactionTypeAndAmount(Long userId1, FiatStatus pending,
			TransactionType withdraw, BigDecimal amount);

	List<Fiat> findByTransactionTypeAndFiatStatusAndBank(PageRequest of, TransactionType withdraw,
			FiatStatus fiatStatus, String bankName);

	Optional<Fiat> findByFiatId(Long fiatid);

	Long countByFiatStatusAndTransactionType(FiatStatus pending, TransactionType deposit);

	List<Fiat> findByUtrNoAndTransactionType(String utrNo, TransactionType deposit, Pageable of);

	List<Fiat> findByCreateTimeBetweenAndTransactionTypeOrderByCreateTimeDesc(Date date, Date date2,
			TransactionType withdraw, Pageable of);

	List<Fiat> findByUserIdAndTransactionTypeOrderByCreateTimeDesc(Long userId, TransactionType withdraw, Pageable of);

	List<Fiat> findByUserIdAndCreateTimeBetweenAndTransactionTypeOrderByCreateTimeDesc(Long userId, Date date,
			Date date2, TransactionType withdraw, Pageable of);

	List<Fiat> findByUserIdAndCreateTimeBetweenAndTransactionType(Long userId, Date date, Date date2,
			TransactionType deposit, Pageable of);

	List<Fiat> findByAmountAndTransactionType(BigDecimal amount, TransactionType deposit, Pageable of);

	List<Fiat> findByDepositAndTransactionType(BigDecimal amount, TransactionType deposit, Pageable of);

	List<Fiat> findByWithdrawAndTransactionType(BigDecimal amount, TransactionType withdraw, Pageable of);

}
