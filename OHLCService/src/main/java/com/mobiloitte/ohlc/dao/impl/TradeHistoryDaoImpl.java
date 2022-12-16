package com.mobiloitte.ohlc.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.mobiloitte.ohlc.dao.TradeHistoryDao;
import com.mobiloitte.ohlc.enums.OrderSide;

/**
 * @author Kumar Arjun
 *
 */
@Repository
public class TradeHistoryDaoImpl implements TradeHistoryDao {

	/** The JdbcTemplate */
	@Autowired
	private JdbcTemplate jdbcTemplate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobiloitte.ohlc.dao.TradeHistoryDao#coinPairTradeHistory(java.util.List,
	 * java.lang.Long, com.mobiloitte.ohlc.enums.OrderSide, java.lang.Long,
	 * java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public List<Map<String, Object>> coinPairTradeHistory(List<Object> getAllCoinPair, Long userId, OrderSide type,
			Long from, Long to, Integer page, Integer pageSize) {
		StringBuilder query = new StringBuilder("select * from ( ");

		// Get query for each coinpair table.
		for (int i = 0; i < getAllCoinPair.size(); i++) {
			@SuppressWarnings("unchecked")
			Map<String, Object> coinPair = (Map<String, Object>) getAllCoinPair.get(i);
			String exeCoin = coinPair.get("executableCoin").toString();
			String baseCoin = coinPair.get("baseCoin").toString();
			query.append(getTransactionQuery(userId, exeCoin, baseCoin, type, from, to));
			if (i != (getAllCoinPair.size() - 1)) {
				query.append(" union ");
			}
		}

		List<Map<String, Object>> totalHitData = jdbcTemplate
				.queryForList(query.toString() + ") a order by transaction_id");

		// adding pagination
		if (page != null && pageSize != null) {
			Integer startPage = (page - 1) * pageSize;
			query.append(") a order by transaction_id limit " + startPage + "," + pageSize);
		}
		List<Map<String, Object>> orderHistory = jdbcTemplate.queryForList(query.toString());
		if (!totalHitData.isEmpty()) {
			Map<String, Object> noOfPages = new HashMap<>();
			Integer totalHit = totalHitData.size();
			noOfPages.put("totalHit", totalHit);
			orderHistory.add(noOfPages);
		}
		return orderHistory;
	}

	/**
	 * Get transaction query for each table
	 * 
	 * @param userId
	 * @param table
	 * @param type
	 * @param from
	 * @param to
	 * @return string
	 */
	private String getTransactionQuery(Long userId, String exeCoin, String baseCoin, OrderSide type, Long from,
			Long to) {
		String table = "transaction_" + exeCoin + "_" + baseCoin;
		String query = "select '" + exeCoin + "' exeCoin, '" + baseCoin
				+ "' baseCoin, transaction_id, execution_time, order_id, price, quantity, side, user_id from " + table;
		if (userId != null || type != null || from != null) {
			query += " where ";
		}
		if (userId != null) {
			query += "user_id=" + userId;
			if (type != null || from != null) {
				query += " and ";
			}
		}
		if (type != null) {
			if (type.equals(OrderSide.BUY)) {
				query += "side=0";
			} else {
				query += "side=1";
			}
			if (from != null) {
				query += " and ";
			}
		}
		if (from != null && to == null)
			query += "execution_time>=FROM_UNIXTIME(" + from / 1000 + ")";
		if (to != null)
			query += "execution_time between FROM_UNIXTIME(" + from / 1000 + ") and FROM_UNIXTIME(" + to / 1000 + ")";
		return query;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.ohlc.dao.TradeHistoryDao#getTradeDetail(java.lang.String,
	 * java.lang.String, java.lang.Long)
	 */
	@Override
	public Map<String, Object> getTradeDetail(String exeCoin, String baseCoin, Long transactionId) {
		String query = String.format(
				"select '%s' exeCoin, '%s' baseCoin, transaction_id, execution_time, order_id, price, quantity, side, user_id from transaction_%s_%s where transaction_id=?",
				exeCoin, baseCoin, exeCoin, baseCoin);
		return jdbcTemplate.queryForMap(query, transactionId);
	}
}
