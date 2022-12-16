package com.mobiloitte.ohlc.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.ohlc.constants.OhlcConstants;
import com.mobiloitte.ohlc.dao.TradeHistoryDao;
import com.mobiloitte.ohlc.enums.OrderSide;
import com.mobiloitte.ohlc.feign.WalletClient;
import com.mobiloitte.ohlc.model.Response;
import com.mobiloitte.ohlc.service.TradeHistoryService;

/**
 * @author Kumar Arjun
 *
 */
@Service
public class TradeHistoryServiceImpl implements TradeHistoryService, OhlcConstants {

	private static final Logger LOGGER = LogManager.getLogger(TradeHistoryServiceImpl.class);

	/** The trade history dao. */
	@Autowired
	private TradeHistoryDao tradeDao;

	/** The wallet client. */
	@Autowired
	private WalletClient walletClient;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobiloitte.ohlc.service.TradeHistoryService#getTradeHistory(java.lang.
	 * String, java.lang.Long, com.mobiloitte.ohlc.enums.OrderSide, java.lang.Long,
	 * java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Response<Object> getTradeHistory(String exeCoin, Long userId, OrderSide type, Long from, Long to,
			Integer page, Integer pageSize) {
		Response<Object> response = new Response<>();
		try {
			// Get coinpair list from wallet microservice.
			List<Object> getAllCoinPair = walletClient.getCoinPairSymbolList(exeCoin).getData();

			List<Map<String, Object>> tradeHistory = tradeDao.coinPairTradeHistory(getAllCoinPair, userId, type, from,
					to, page, pageSize);

			if (!tradeHistory.isEmpty()) {
				int totalHit = (int) tradeHistory.get(tradeHistory.size() - 1).get("totalHit");
				tradeHistory.remove(tradeHistory.size() - 1);
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("tradeHistory", tradeHistory);
				responseMap.put("totalHit", totalHit);
				response = new Response<>(SUCCESS_CODE, SUCCESS, responseMap);
			} else {
				response = new Response<>(NO_DATA_FOUND_CODE, NO_DATA_FOUND);
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			response = new Response<>(NOT_FOUND_CODE, COINPAIR_LIST_NOT_FOUND);
		}

		return response;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobiloitte.ohlc.service.TradeHistoryService#getTradeDetail(java.lang.
	 * String, java.lang.String, java.lang.Long)
	 */
	@Override
	public Response<Object> getTradeDetail(String exeCoin, String baseCoin, Long transactionId) {
		Map<String, Object> data = null;
		try {
			data = tradeDao.getTradeDetail(exeCoin, baseCoin, transactionId);
			return new Response<>(SUCCESS_CODE, SUCCESS, data);
		} catch (Exception e) {
			LOGGER.catching(e);
			return new Response<>(FAILURE_CODE, FAILURE, data);
		}
	}
}
