package com.mobiloitte.ohlc.dao;

import java.util.List;
import java.util.Map;

import com.mobiloitte.ohlc.enums.OrderSide;

/**
 * @author Jyoti Singh
 *
 */
public interface TradeHistoryDao {

	/**
	 * Get trade history list.
	 * 
	 * @param getAllCoinPair
	 * @param userId
	 * @param type
	 * @param from
	 * @param to
	 * @param page
	 * @param pageSize
	 * @return trade history list.
	 */
	List<Map<String, Object>> coinPairTradeHistory(List<Object> getAllCoinPair, Long userId, OrderSide type, Long from, Long to, Integer page, Integer pageSize);

	Map<String, Object> getTradeDetail(String exeCoin, String baseCoin, Long transactionId);

}
