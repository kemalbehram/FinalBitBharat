package com.mobiloitte.ohlc.dao;

import java.util.List;
import java.util.Map;

import com.mobiloitte.ohlc.dto.OrderDetailDto;

import com.mobiloitte.ohlc.dto.SearchAndFilterDto;

public interface OrderDao {

	List<Map<String, Object>> coinPairOrderHistory(Long userId, String table);

	List<OrderDetailDto> getAlltrade(Long userId, String transaction, String order, SearchAndFilterDto searchAndFilterDto);

	Object activeOrderHistory(Long userId, String table, SearchAndFilterDto searchAndFilterDto);

	Object OrderHistory(Long userId, String table, SearchAndFilterDto searchAndFilterDto);

	Object getAlltradeForAllUser(String transaction, String order, SearchAndFilterDto searchAndFilterDto);

}
