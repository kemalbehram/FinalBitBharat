package com.mobiloitte.microservice.wallet.service;

import java.util.Map;

import com.mobiloitte.microservice.wallet.enums.OrderType;
import com.mobiloitte.microservice.wallet.model.Response;

/**
 * The Interface AdminBasicExchangeService.
 */
public interface AdminBasicExchangeService {

	/**
	 * Gets the all exchange history.
	 *
	 * @param orderType the order type
	 * @param page the page
	 * @param pageSize the page size
	 * @return the all exchange history
	 */
	Response<Map<String, Object>> getAllExchangeHistory(OrderType orderType, Integer page, Integer pageSize);
}



