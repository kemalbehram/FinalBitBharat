package com.mobiloitte.ohlc.service;

import com.mobiloitte.ohlc.enums.OrderSide;
import com.mobiloitte.ohlc.model.Response;

/**
 * @author Jyoti Singh
 *
 */
public interface TradeHistoryService {

	/**
	 * Get trade history.
	 * 
	 * @param baseCoin
	 * @param userId
	 * @param type
	 * @param from
	 * @param to
	 * @param page
	 * @param pageSize
	 * @return
	 */
	Response<Object> getTradeHistory(String baseCoin, Long userId, OrderSide type, Long from, Long to, Integer page,
			Integer pageSize);

	Response<Object> getTradeDetail(String exeCoin, String baseCoin, Long transactionId);

}
