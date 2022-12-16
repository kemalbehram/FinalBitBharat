package com.mobiloitte.ohlc.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.ohlc.constants.OhlcConstants;
import com.mobiloitte.ohlc.dao.TradingViewExchangeDao;
import com.mobiloitte.ohlc.feign.WalletClient;
import com.mobiloitte.ohlc.model.Response;
import com.mobiloitte.ohlc.service.TradingViewExchangeService;

/**
 * @author Kumar Arjun
 *
 */
@Service
public class TradingViewExchangeServiceImpl implements TradingViewExchangeService, OhlcConstants {
	private static final Logger LOGGER = LogManager.getLogger(TradingViewExchangeServiceImpl.class);
	@Autowired
	TradingViewExchangeDao tradingViewDao;

	@Autowired
	WalletClient walletClient;

	@Override
	public Map<String, Object> getOHLCData(String symbol, String resolution, long from, long to) {
		String[] symbolArray = symbol.split("/");
		List<Map<String, Object>> ourData = tradingViewDao.getOHLCData(symbolArray[1], symbolArray[0],
				getIntervalInSec(resolution), from, to);
		Map<String, Object> data = new HashMap<>();
		if (!ourData.isEmpty()) {
			ArrayList<Object> t = new ArrayList<>();
			ArrayList<Object> c = new ArrayList<>();
			ArrayList<Object> o = new ArrayList<>();
			ArrayList<Object> h = new ArrayList<>();
			ArrayList<Object> l = new ArrayList<>();
			ArrayList<Object> v = new ArrayList<>();
			for (int i = 0; i < ourData.size(); i++) {
				c.add(ourData.get(i).get("c"));
				o.add(ourData.get(i).get("o"));
				h.add(ourData.get(i).get("h"));
				l.add(ourData.get(i).get("l"));
				v.add(ourData.get(i).get("v"));
				t.add(ourData.get(i).get("t"));
			}
			data.put("s", "ok");
			data.put("t", t);
			data.put("c", c);
			data.put("o", o);
			data.put("h", h);
			data.put("l", l);
			data.put("v", v);
		} else {
			data.put("s", "no_data");
		}
		return data;
	}

	private long getIntervalInSec(String resolution) {
		switch (resolution) {
		case "1":
			return 60L;
		case "5":
			return 300L;
		case "15":
			return 900L;
		case "30":
			return 1800L;
		case "60":
			return 3600L;
		case "180":
			return 10800L;
		case "360":
			return 21600L;
		case "720":
			return 43200L;
		case "D":
			return 86400L;
		case "W":
			return 604800L;
		default:
			throw new IllegalArgumentException("Resolution not supported");
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSymbol(String symbol) {
		String[] symbolArray = symbol.split("/");
		Map<String, Object> particularCoinResponse1 = (Map<String, Object>) (walletClient
				.getParticularCoinDetails(symbolArray[0]).getData());
		Map<String, Object> particularCoinResponse2 = (Map<String, Object>) (walletClient
				.getParticularCoinDetails(symbolArray[1]).getData());

		Map<String, Object> data = new HashMap<>();
		data.put("name",
				particularCoinResponse1.get("coinFullName") + "/" + particularCoinResponse2.get("coinFullName"));
		data.put("exchange-traded", "Exchange");
		data.put("exchange-listed", "Exchange");
		Calendar now = Calendar.getInstance();
		TimeZone timeZone = now.getTimeZone();
		data.put("timezone", timeZone.getDisplayName());
		data.put("minmov", 0.00000001);
		data.put("minmov2", 0);
		data.put("pointvalue", 1);
		data.put("session", "0930-1630");
		data.put("has_intraday", false);
		data.put("has_no_volume", false);
		data.put("description",
				particularCoinResponse1.get("coinFullName") + "/" + particularCoinResponse2.get("coinFullName"));
		data.put("type", "crypto");
		ArrayList<String> resolutions = new ArrayList<>();
		resolutions.add("1");
		resolutions.add("5");
		resolutions.add("15");
		resolutions.add("30");
		resolutions.add("60");
		resolutions.add("180");
		resolutions.add("360");
		resolutions.add("720");
		resolutions.add("D");
		resolutions.add("W");
		data.put("supported_resolutions", resolutions);
		data.put("pricescale", 100);
		data.put("ticker", symbolArray[1] + "/" + symbolArray[0]);
		return data;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getSearchResults(String coinName) {
		Response<Object> coinListResponse = new Response<>(walletClient.getCoinList().getData());
		Response<Object> particularCoinResponse = new Response<>(
				walletClient.getParticularCoinDetails(coinName).getData());
		ArrayList<Map<String, Object>> coinList = (ArrayList<Map<String, Object>>) coinListResponse.getData();
		Map<String, Object> cryptos = (Map<String, Object>) particularCoinResponse.getData();

		for (int i = 0; i < coinList.size(); i++) {
			for (int j = 0; j < coinList.size(); j++) {
				if (!coinList.get(i).get("coinShortName").toString()
						.equalsIgnoreCase(coinList.get(j).get("coinShortName").toString())) {
					Map<String, Object> exchange = new HashMap<>();
					exchange.put("symbol", coinList.get(i).get("coinShortName") + "/" + cryptos.get("coinShortName"));
					exchange.put("full_name",
							coinList.get(i).get("coinShortName") + "/" + cryptos.get("coinShortName"));
					exchange.put("description",
							coinList.get(i).get("coinFullName") + "/" + cryptos.get("coinFullName"));
					exchange.put("exchange", "Exchange");
					exchange.put("type", "crypto exchange");
					return exchange;
				}
			}
		}
		return null;
	}

	@Override
	public Map<String, Object>  getDataFeedConfig() {
		Response<Object> coinListResponse = new Response<>(walletClient.getCoinList().getData());
		@SuppressWarnings("unchecked")
		ArrayList<Map<String, Object>> coinList = (ArrayList<Map<String, Object>>) coinListResponse.getData();

		ArrayList<String> symbol = new ArrayList<>();
		ArrayList<String> description = new ArrayList<>();

		for (int i = 0; i < coinList.size(); i++) {
			for (int j = 0; j < coinList.size(); j++) {
				if (!coinList.get(i).get("coinShortName").toString()
						.equalsIgnoreCase(coinList.get(j).get("coinShortName").toString())) {
					symbol.add(coinList.get(i).get("coinShortName").toString() + "/"
							+ coinList.get(j).get("coinShortName").toString());
					description.add(coinList.get(i).get("coinFullName").toString() + "/"
							+ coinList.get(j).get("coinFullName").toString());
				}
			}
		}

		Map<String, Object> data = new HashMap<>();
		data.put("symbol", symbol);
		data.put("description", description);
		data.put("exchange-listed", "Exchange");
		data.put("exchange-traded", "Exchange");
		data.put("minmovement", 0.00000001);
		data.put("minmovement2", 0);
		data.put("pricescale", 100000000);
		data.put("has-dwm", true);
		data.put("has-intraday", true);
		ArrayList<Boolean> hasNoVolume = new ArrayList<>();
		hasNoVolume.add(false);
		hasNoVolume.add(false);
		hasNoVolume.add(true);
		data.put("has-no-volume", hasNoVolume);
		ArrayList<String> type = new ArrayList<>();
		type.add("exchange");
		type.add("exchange");
		type.add("exchange");
		data.put("type", type);
		return data;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.ohlc.service.TradingViewExchangeService#
	 * getDepthChartDataExchange(java.lang.String, java.lang.String)
	 */
	@Override
	public Response<Object> getDepthChartDataExchange(String currency, String exchangeCurrency) {
		Response<Object> response = null;
		Map<String, Object> data = new HashMap<>();
		try {
			List<Map<String, Object>> bidsList = tradingViewDao.getDepthChartDataExchange("BUY", currency,
					exchangeCurrency);
			List<Map<String, Object>> asksList = tradingViewDao.getDepthChartDataExchange("SELL", currency,
					exchangeCurrency);
			List<ArrayList<Object>> bids = new ArrayList<>();
			if (!bidsList.isEmpty()) {
				for (int i = 0; i < bidsList.size(); i++) {
					ArrayList<Object> innerData = new ArrayList<>();
					innerData.add((Object) bidsList.get(i).get("price"));
					innerData.add((Object) bidsList.get(i).get("volume"));
					bids.add(innerData);
				}
			}
			data.put("bids", bids);
			List<ArrayList<Object>> asks = new ArrayList<>();
			if (!asksList.isEmpty()) {
				for (int i = 0; i < asksList.size(); i++) {
					ArrayList<Object> innerData = new ArrayList<>();
					innerData.add((Object) asksList.get(i).get("price"));
					innerData.add((Object) asksList.get(i).get("volume"));
					asks.add(innerData);
				}
			}
			data.put("asks", asks);
			response = new Response<>(SUCCESS_CODE, SUCCESS, data);
		} catch (Exception e) {
			LOGGER.catching(e);
			response = new Response<>(NO_DATA_FOUND_CODE, NO_DATA_FOUND, data);
		}
		return response;
	}

	@Override
	public Response<List<Map<String, Object>>> getOHLCData2(String symbol, String resolution, Long to) {
		List<Map<String, Object>> ohlcData = new LinkedList<>();
		String[] symbolArray = symbol.split("/");
		List<Map<String, Object>> ourData = tradingViewDao.getOHLCDataType2(symbolArray[1], symbolArray[0], to, getIntervalInSec(resolution));
		if(ourData.isEmpty())
			return new Response<>(SUCCESS_CODE, "no_data", ourData);
		else
		{
			for (Map<String, Object> o : ourData) {
				Map<String, Object> respObj = new HashMap<>();
				respObj.put("t", o.get("t"));
				respObj.put("o", o.get("o"));
				respObj.put("l", o.get("l"));
				respObj.put("h", o.get("h"));
				respObj.put("c", o.get("c"));
				respObj.put("v", o.get("v"));
				
				ohlcData.add(respObj);
			}

			return new Response<>(SUCCESS_CODE, "ok", ohlcData);
		}
	}

}
