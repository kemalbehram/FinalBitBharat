package com.mobiloitte.ohlc.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.ohlc.constants.OhlcConstants;
import com.mobiloitte.ohlc.dao.OrderDao;
import com.mobiloitte.ohlc.dto.OrderDetailDto;
import com.mobiloitte.ohlc.dto.SearchAndFilterDto;
import com.mobiloitte.ohlc.feign.OrderClient;
import com.mobiloitte.ohlc.feign.WalletClient;
import com.mobiloitte.ohlc.model.CoinPair;
import com.mobiloitte.ohlc.model.MarketData;
import com.mobiloitte.ohlc.model.Response;
import com.mobiloitte.ohlc.service.OrderService;

/**
 * @author Kumar Arjun
 *
 */
@Service
public class OrderServiceImpl implements OrderService, OhlcConstants {

	private static final Logger LOGGER = LogManager.getLogger(OrderServiceImpl.class);
	private static final String COIN_PAIR = "coinPair";
	private static final String HISTORY = "history";

	/** The wallet client */
	@Autowired
	private WalletClient walletClient;

	@Autowired
	private OrderClient orderClient;

	/** The OrderDao */
	@Autowired
	private OrderDao orderDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobiloitte.ohlc.service.OrderService#getOrderHistoryByUserId(java.lang.
	 * Long)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Response<Object> getOrderHistoryByUserId(Long userId) {
		Response<Object> response = new Response<>();
		try {
			// Get coin pair symbol list from from wallet client
			List<Object> getAllCoinPair = walletClient.getCoinPairSymbolList(null).getData();
			List<Map<String, Object>> orderHistory = new ArrayList<>();
			for (int i = 0; i < getAllCoinPair.size(); i++) {
				Map<String, Object> coinPair = (Map<String, Object>) getAllCoinPair.get(i);
				String table = "order_" + coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin");
				List<Map<String, Object>> orderHistoryOfCoinPair = orderDao.coinPairOrderHistory(userId, table);
				if (orderHistoryOfCoinPair != null) {
					orderHistory.addAll(orderHistoryOfCoinPair);
				}

			}
			response = new Response<>(SUCCESS_CODE, SUCCESS, orderHistory);
		} catch (Exception e) {
			LOGGER.catching(e);
			response = new Response<>(NO_DATA_FOUND_CODE, COINPAIR_LIST_NOT_FOUND);
		}
		return response;
	}

	@Override
	public Response<List<MarketData>> getMarketData(String baseCoin, String exeCoin) {
		List<String> symbols = getSymbols(baseCoin, exeCoin);
		List<MarketData> list = symbols.parallelStream().map(s -> {
			MarketData data;
			try {
				Response<MarketData> response = orderClient.getMarketData(s);
				data = response.getData();
				if (data == null)
					throw new RuntimeException(String.format("Failed to fetch market data: (%d)- %s",
							response.getStatus(), response.getMessage()));
			} catch (Exception e) {
				LOGGER.catching(e);
				data = new MarketData();
			}
			data.setSymbol(s);
			return data;
		}).collect(Collectors.toList());
		return new Response<>(list);

	}

	private List<String> getSymbols(String baseCoin, String exeCoin) {
		List<CoinPair> pairs = walletClient.getAllCoinPairSymbols().getData();
		return pairs.parallelStream()
				.filter(p -> (baseCoin == null || p.getBaseCoin().equalsIgnoreCase(baseCoin))
						&& (exeCoin == null || p.getExecutableCoin().equalsIgnoreCase(exeCoin)))
				.map(p -> String.join("_", p.getExecutableCoin(), p.getBaseCoin())).collect(Collectors.toList());
	}

	@Override
	public Response<List<Object>> getActiveOrders(String baseCoin, String exeCoin, Long userId) {
		List<String> symbols = getSymbols(baseCoin, exeCoin);
		List<Object> list = symbols.parallelStream().flatMap(s -> {
			List<Object> data;
			try {
				Response<List<Object>> response = orderClient.getActiveOrders(s, userId);
				data = response.getData();
				if (data == null)
					throw new RuntimeException(String.format("Failed to fetch active orders: (%d)- %s",
							response.getStatus(), response.getMessage()));
			} catch (Exception e) {
				LOGGER.catching(e);
				data = new ArrayList<>();
			}
			return data.stream();
		}).collect(Collectors.toList());
		return new Response<>(list);
	}

	@Override
	public Response<List<Object>> getOrderHistory(String baseCoin, String exeCoin, Long userId) {
		List<String> symbols = getSymbols(baseCoin, exeCoin);
		List<Object> list = symbols.parallelStream().flatMap(s -> {
			List<Object> data;
			try {
				Response<List<Object>> response = orderClient.getOrderHistory(s, userId);
				data = response.getData();
				if (data == null)
					throw new RuntimeException(String.format("Failed to fetch order history: (%d)- %s",
							response.getStatus(), response.getMessage()));
			} catch (Exception e) {
				LOGGER.catching(e);
				data = new ArrayList<>();
			}
			return data.stream();
		}).collect(Collectors.toList());
		return new Response<>(list);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> getTrade(SearchAndFilterDto searchAndFilterDto) {
		List<OrderDetailDto> response = new ArrayList<>();

		List<OrderDetailDto> orderHistoryOfCoinPair = new ArrayList<>();
		Long id = 52L;
		List<Object> getAllCoinPair = walletClient.getCoinPairSymbolListOhlc(null).getData();
		for (int i = 0; i < getAllCoinPair.size(); i++) {
			Map<String, Object> coinPair = (Map<String, Object>) getAllCoinPair.get(i);
			String order = "order_" + coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin");
			String transaction = "transaction_" + coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin");
			orderHistoryOfCoinPair = orderDao.getAlltrade(searchAndFilterDto.getUserId(), transaction, order,
					searchAndFilterDto);
			response.addAll(orderHistoryOfCoinPair);

		}
		if (searchAndFilterDto.getTransactionId() == null) {

			return new Response<Object>(SUCCESS_CODE, SUCCESS, response);
		} else {
			return new Response<Object>(SUCCESS_CODE, SUCCESS,
					response.parallelStream()
							.filter(a -> searchAndFilterDto.getTransactionId().equals(a.getTransactionId()))
							.collect(Collectors.toList()));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> getActiveOrderHistory(Long userId, SearchAndFilterDto searchAndFilterDto) {

		Response<Object> response = new Response<>();
		try {
			List<Object> getAllCoinPair = walletClient.getCoinPairSymbolListOhlc(null).getData();
			List<Map<String, Object>> respList = new LinkedList<>();

			for (int i = 0; i < getAllCoinPair.size(); i++) {
				Map<String, Object> tempMap = new HashMap<>();

				Map<String, Object> coinPair = (Map<String, Object>) getAllCoinPair.get(i);

				String table = "order_" + coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin");
				tempMap.put(COIN_PAIR, coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin"));
				tempMap.put(HISTORY, orderDao.activeOrderHistory(userId, table, searchAndFilterDto));

				respList.add(tempMap);
			}
			response = new Response<>(SUCCESS_CODE, SUCCESS, respList);
		} catch (Exception e) {
			LOGGER.catching(e);
			response = new Response<>(NO_DATA_FOUND_CODE, COINPAIR_LIST_NOT_FOUND);
		}
		return response;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> getOrderHistory(Long userId, SearchAndFilterDto searchAndFilterDto) {

		Response<Object> response = new Response<>();
		try {
			List<Object> getAllCoinPair = walletClient.getCoinPairSymbolListOhlc(null).getData();
			List<Map<String, Object>> respList = new LinkedList<>();

			for (int i = 0; i < getAllCoinPair.size(); i++) {
				Map<String, Object> tempMap = new HashMap<>();

				Map<String, Object> coinPair = (Map<String, Object>) getAllCoinPair.get(i);
				String table = "order_" + coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin");
				tempMap.put(COIN_PAIR, coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin"));
				tempMap.put(HISTORY, orderDao.OrderHistory(userId, table, searchAndFilterDto));

				respList.add(tempMap);
			}
			response = new Response<>(SUCCESS_CODE, SUCCESS, respList);
		} catch (Exception e) {
			LOGGER.catching(e);
			response = new Response<>(NO_DATA_FOUND_CODE, COINPAIR_LIST_NOT_FOUND);
		}
		return response;

	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> getTradeHistoryAllUser(SearchAndFilterDto searchAndFilterDto) {

		Response<Object> response = new Response<>();
		try {

			List<Object> getAllCoinPair = walletClient.getCoinPairSymbolListOhlc(null).getData();
			List<Map<String, Object>> respList = new LinkedList<>();

			for (int i = 0; i < getAllCoinPair.size(); i++) {
				Map<String, Object> tempMap = new HashMap<>();

				Map<String, Object> coinPair = (Map<String, Object>) getAllCoinPair.get(i);
				String order = "order_" + coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin");
				String transaction = "transaction_" + coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin");
				tempMap.put(COIN_PAIR, coinPair.get("executableCoin") + "_" + coinPair.get("baseCoin"));
				tempMap.put(HISTORY, orderDao.getAlltradeForAllUser(transaction, order, searchAndFilterDto));

				respList.add(tempMap);
			}

			response = new Response<>(SUCCESS_CODE, SUCCESS, respList);
		} catch (Exception e) {
			LOGGER.catching(e);
			response = new Response<>(NO_DATA_FOUND_CODE, COINPAIR_LIST_NOT_FOUND);
		}
		return response;

	}
}
