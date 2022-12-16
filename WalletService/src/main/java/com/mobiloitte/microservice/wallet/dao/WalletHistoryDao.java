package com.mobiloitte.microservice.wallet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.WalletHistory;

/**
 * The Interface WalletHistoryDao.
 * @author Ankush Mohapatra
 */
public interface WalletHistoryDao extends JpaRepository<WalletHistory, Long>{

	/**
	 * Find by action and coin name.
	 *
	 * @param action the action
	 * @param coinName the coin name
	 * @return the list
	 */
	List<WalletHistory> findByActionAndCoinName(String action, String coinName);

	List<WalletHistory> findByActionAndCoinNameAndUserId(String string, String coinName, Long userId);

}
