package com.mobiloitte.microservice.wallet.dao;

import java.math.BigDecimal;
import java.util.List;
import java.util.ListIterator;
import java.util.Optional;

import javax.persistence.LockModeType;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.mobiloitte.microservice.wallet.entities.Wallet;

/**
 * The Interface WalletDao.
 * 
 * @author Ankush Mohapatra
 */
public interface WalletDao extends JpaRepository<Wallet, Long> {

	/**
	 * Find by coin name and fk user id.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the optional
	 */
//	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Wallet> findByCoinNameAndFkUserId(String coinName, Long fkUserId);

	/**
	 * Find by coin coin id and fk user id.
	 *
	 * @param coinId   the coin id
	 * @param fkUserId the fk user id
	 * @return the optional
	 */
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	Optional<Wallet> findByCoinCoinIdAndFkUserId(Long coinId, Long fkUserId);

	/**
	 * Find by fk user id.
	 *
	 * @param fkUserId the fk user id
	 * @return the list
	 */
	List<Wallet> findByFkUserId(Long fkUserId);

	Boolean existsByEosAccountName(String accountName);

	List<Wallet> findByCoinNameIn(List<String> coinName);

	Optional<Wallet> findByWalletAddressAndTag(String walletAddress, String tag);

	Optional<Wallet> findByCoinNameAndWalletAddress(String coinName, String fromCoinAddress);

	Optional<Wallet> findByWalletAddress(String toAddress);

	Optional<Wallet> findByCoinNameAndWalletAddressAndTag(String coinName, String toCoinAddress, String tagId);

	Optional<Wallet> findByCoinNameAndRandomId(String coinName, String toCoinAddress);

	Optional<Wallet> findByRandomId(String oldRandomId);

	List<Wallet> findByUsdAmountLessThanEqual(BigDecimal bigDecimal);

	Optional<Wallet> findByCoinNameAndFkUserIdAndUsdAmountLessThanEqual(String string, Long userId,
			BigDecimal bigDecimal);

	List<Wallet> findByCoinNameAndFkUserIdAndUsdAmountLessThanEqual(ListIterator<String> listIterator, Long userId,
			BigDecimal bigDecimal);

	List<Wallet> findByFkUserIdAndUsdAmountLessThanEqual(Long userId, BigDecimal bigDecimal);

	List<Wallet> findByFkUserIdAndCoinIsVisibleTrue(Long userId);

	List<Wallet> findByFkUserIdAndCoinIsVisibleTrueOrderByWalletBalanceDesc(Long userId);

}
