package com.mobiloitte.microservice.wallet.dao;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;

/**
 * The Interface CoinDepositWithdrawalDao.
 * 
 * @author Ankush Mohapatra
 */
public interface CoinDepositWithdrawalDao extends JpaRepository<CoinDepositWithdrawal, Long> {

	/**
	 * Find by coin type and txn type and fk user id.
	 *
	 * @param coinType the coin type
	 * @param txnType  the txn type
	 * @param fkUserId the fk user id
	 * @return the list
	 */
	List<CoinDepositWithdrawal> findByCoinTypeAndTxnTypeAndFkUserId(String coinType, String txnType, Long fkUserId,
			Pageable pageable);

	List<CoinDepositWithdrawal> findByCoinTypeAndTxnTypeAndFkUserId(String coinType, String txnType, Long fkUserId);

	List<CoinDepositWithdrawal> findByCoinTypeAndTxnTypeAndFkUserIdOrderByTxnIdDesc(String coinType, String txnType,
			Long fkUserId);

	Long countByCoinTypeAndTxnTypeAndFkUserId(String coinType, String txnType, Long fkUserId);

	/**
	 * Find by fk user id.
	 *
	 * @param fkUserId the fk user id
	 * @return the list
	 */
	List<CoinDepositWithdrawal> findByFkUserId(Long fkUserId);

	/**
	 * Find by txn type.
	 *
	 * @param txnType  the txn type
	 * @param coinType the coin type
	 * @return the list
	 */
	List<CoinDepositWithdrawal> findByTxnTypeAndCoinType(String txnType, String coinType);

	List<CoinDepositWithdrawal> findByFkUserIdAndTxnTypeAndStatusAndCoinTypeAndTxnTimeBetween(Long fkUserId,
			String transactionType, String status, String coinType, Date txnTimeStart, Date txnTimeEnd);

	List<CoinDepositWithdrawal> findByCoinTypeAndTxnTypeAndFkUserIdAndTxnTimeBetween(String coinName, String withdraw,
			Long userId, Date fromDate, Date toDate, Pageable pageable);

	Long countByCoinTypeAndTxnTypeAndFkUserIdAndTxnTimeBetween(String coinName, String withdraw, Long userId, Date date,
			Date date2);

	Long countByTxnType(String deposit);

	List<CoinDepositWithdrawal> findBycoinTypeAndTxnType(String coinName, String string, Pageable pageRequest);

	Optional<CoinDepositWithdrawal> findTopByFkUserId(Long userId);

	List<CoinDepositWithdrawal> findByCoinTypeAndTxnTypeAndFkUserIdOrAddressOrTxnHash(String coinName, String deposit,
			Long fkUserId, String address, String txnHash, PageRequest of);

	Long countByCoinTypeAndTxnTypeAndFkUserIdOrAddressOrTxnHash(String coinName, String address, String txnHash,
			String deposit, Long fkUserId);

	List<CoinDepositWithdrawal> findByFkUserIdAndCoinType(Long userId, String coinName);

	List<CoinDepositWithdrawal> findBycoinTypeAndTxnType(String coinName, String txnType);

	List<CoinDepositWithdrawal> findBycoinTypeAndTxnTypeAndStatus(String coinName, String txnType, String status);

	List<CoinDepositWithdrawal> findByCoinTypeAndTxnTypeAndFkUserIdAndStatus(String coinName, String string,
			Long fkUserId, String string2);

	List<CoinDepositWithdrawal> findByTxnTypeAndFkUserIdAndStatus(String string, Long userId, String string2);

	List<CoinDepositWithdrawal> findByCoinTypeAndTxnTypeAndStatus(String coinShortName, String string, String string2);

	List<CoinDepositWithdrawal> findByCoinTypeAndStatus(String coinShortName, String string);

	Long countByTxnTypeAndStatus(String withdraw, String string);

}
