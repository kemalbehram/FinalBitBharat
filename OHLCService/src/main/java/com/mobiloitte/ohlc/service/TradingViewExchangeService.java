package com.mobiloitte.ohlc.service;

import java.util.List;
import java.util.Map;

import com.mobiloitte.ohlc.model.Response;

/**
 * @author Jyoti Singh
 *
 */
public interface TradingViewExchangeService {

	Map<String, Object> getOHLCData(String symbol, String resolution, long from, long to);

	Map<String, Object> getSymbol(String symbol);

	Map<String, Object> getSearchResults(String coin);

	Map<String, Object> getDataFeedConfig();

	Response<Object> getDepthChartDataExchange(String currency, String exchangeCurrency);

	Response<List<Map<String, Object>>> getOHLCData2(String symbol, String resolution, Long to);
}
