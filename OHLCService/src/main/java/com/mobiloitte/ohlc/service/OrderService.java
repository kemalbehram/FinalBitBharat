package com.mobiloitte.ohlc.service;

import java.util.List;

import com.mobiloitte.ohlc.dto.SearchAndFilterDto;
import com.mobiloitte.ohlc.model.MarketData;
import com.mobiloitte.ohlc.model.Response;

public interface OrderService {

	Response<Object> getOrderHistoryByUserId(Long userId);

	Response<List<MarketData>> getMarketData(String baseCoin, String exeCoin);

	Response<List<Object>> getActiveOrders(String baseCoin, String exeCoin, Long userId);

	Response<List<Object>> getOrderHistory(String baseCoin, String exeCoin, Long userId);

	Response<Object> getTrade(SearchAndFilterDto searchAndFilterDto);

	Response<Object> getActiveOrderHistory(Long userId, SearchAndFilterDto searchAndFilterDto);

	Response<Object> getOrderHistory(Long userId, SearchAndFilterDto searchAndFilterDto);

	Response<Object> getTradeHistoryAllUser(SearchAndFilterDto searchAndFilterDto);

}
