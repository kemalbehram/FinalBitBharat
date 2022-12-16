package com.mobiloitte.microservice.wallet.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mobiloitte.microservice.wallet.entities.BasicTradeHistory;
import com.mobiloitte.microservice.wallet.enums.OrderType;

/**
 * The Interface BasicTradeHistoryDao.
 */
public interface BasicTradeHistoryDao extends JpaRepository<BasicTradeHistory, Long>{

	/**
	 * Find by fk user id and order type and exec coin name.
	 *
	 * @param fkUserId the fk user id
	 * @param orderType the order type
	 * @param execCoinName the exec coin name
	 * @param pageable the pageable
	 * @return the list
	 */
	List<BasicTradeHistory> findByFkUserIdAndOrderTypeAndExecCoinName(Long fkUserId, OrderType orderType, String execCoinName, Pageable pageable);

	/**
	 * Count by fk user id and order type and exec coin name.
	 *
	 * @param fkUserId the fk user id
	 * @param orderType the order type
	 * @param execCoinName the exec coin name
	 * @return the long
	 */
	Long countByFkUserIdAndOrderTypeAndExecCoinName(Long fkUserId, OrderType orderType, String execCoinName);

	/**
	 * Find by order type.
	 *
	 * @param orderType the order type
	 * @param pageable the pageable
	 * @return the list
	 */
	List<BasicTradeHistory> findByOrderType(OrderType orderType, Pageable pageable);
	
	/**
	 * Count by order type.
	 *
	 * @param orderType the order type
	 * @return the long
	 */
	Long countByOrderType(OrderType orderType);
}
