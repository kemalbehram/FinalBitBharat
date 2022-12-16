package com.mobiloitte.ohlc.dao;

import java.util.List;
import java.util.Map;

/**
 * @author Jyoti Singh
 *
 */
public interface TradingViewExchangeDao {
	List<Map<String, Object>> getOHLCData(String baseCoin, String executableCoin, long intervalInSecs, long from,
			long to);
	List<Map<String, Object>> getDepthChartDataExchange(String string, String currency, String exchangeCurrency);
	
	List<Map<String, Object>> getOHLCDataType2(String baseCoin, String executableCoin, long to, long secs);

}
