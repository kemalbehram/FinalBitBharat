package com.mobiloitte.ohlc.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mobiloitte.ohlc.dao.TradingViewExchangeDao;

/**
 * @author Kumar Arjun
 *
 */
@Repository
public class TradingViewExchangeDaoImpl implements TradingViewExchangeDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Override
	public List<Map<String, Object>> getOHLCData(String baseCoin, String executableCoin, long intervalInSecs, long from,
			long to) {

		String query = String.format(
				"select (round(t/%d,0)*%d) t,SUBSTRING_INDEX(GROUP_CONCAT(o ORDER BY t ASC SEPARATOR ','), ',', 1) o ,SUBSTRING_INDEX(GROUP_CONCAT(c ORDER BY t DESC SEPARATOR ','), ',', 1) c, max(h) h ,min(l) l , sum(v) v from ohlc_%s_%s where t between ? and ? GROUP BY t",
				intervalInSecs, intervalInSecs, executableCoin, baseCoin);
		return jdbcTemplate.queryForList(query, from, to);
	}
	
	

	

	@Override
	public List<Map<String, Object>> getDepthChartDataExchange(String type, String currency, String exchangeCurrency) {
		String query = "";
		if (type.equals("BUY")) {
			query = String.format(
					"Select round(quantity,8) as volume, round(limit_price,8) as price from order_%s_%s where order_side='0' and active=true order by creation_time desc limit 500",
					exchangeCurrency, currency);
		} else {
			query = String.format(
					"Select round(quantity,8) as volume, round(limit_price,8) as price from order_%s_%s where order_side='1' and active=true order by creation_time desc limit 500",
					exchangeCurrency, currency);
		}
		return jdbcTemplate.queryForList(query);
	}



	@Override
	public List<Map<String, Object>> getOHLCDataType2(String baseCoin, String executableCoin, long to, long secs) {
		long from = (new Date().getTime()-(secs*1000)) / 1000;
		String query = String.format("select t,o,l,h,c,v from ohlc_%s_%s where t between ? and ?", executableCoin, baseCoin);
		return jdbcTemplate.queryForList(query,from,to);
	}
}
