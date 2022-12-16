package com.mobiloitte.microservice.wallet.utils;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.mobiloitte.microservice.wallet.dao.MarketPriceDao;
import com.mobiloitte.microservice.wallet.entities.MarketPrice;

/**
 * CryptoFiatConverter from crypto-compare.
 *
 * 
 */
@Component
@SuppressWarnings("unchecked")
public class CryptoFiatConverter {
	// private static final String COIN_MARKET_CAP_API_URL =
	// "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

	/** The market api url. */

	@Value("${market-price-api-base}")
	private String marketApiUrl;

	/** The Constant USD. */
	public static final String USD = "USD";

	public static final String BTC = "BTC";
	public static final String INR = "INR";

	public static final String EUR = "EUR";

	/** The market price dao. */
	@Autowired
	private MarketPriceDao marketPriceDao;

	/**
	 * Gets the market price.
	 *
	 * @param fromCoin the from coin
	 * @return the market price
	 */
	@Transactional
	public BigDecimal getMarketPrice(String coinName) {
		Optional<MarketPrice> getMarketPrice = marketPriceDao.findByCoinName(coinName);
		if (getMarketPrice.isPresent()) {
			return getMarketPrice.get().getPriceInUsd();
		} else {
			return null;
		}
	}

	/**
	 * Gets the market price.
	 *
	 * @param fromSymbol the from symbol
	 * @param toSymbol   the to symbol
	 * @param inr2
	 * @return the market price
	 */
	public Map<String, BigDecimal> marketPriceApi(String fromSymbol, String toSymbol, String inr2) {
		String apiBaseUri = marketApiUrl + fromSymbol + "&tsyms=" + toSymbol + "," + inr2 + ",BTC,INR,EUR";
		try {
			String responseString = new RestTemplate().getForObject(apiBaseUri, String.class);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.containsKey(USD) && allData.containsKey(BTC) && allData.containsKey(INR)
					&& allData.containsKey(EUR)) {
				Map<String, BigDecimal> respMap = new HashMap<>();
				respMap.put(USD, BigDecimal.valueOf(Double.parseDouble(String.valueOf(allData.get(USD)))));
				respMap.put(BTC, BigDecimal.valueOf(Double.parseDouble(String.valueOf(allData.get(BTC)))));
				respMap.put(INR, BigDecimal.valueOf(Double.parseDouble(String.valueOf(allData.get(INR)))));

				respMap.put(EUR, BigDecimal.valueOf(Double.parseDouble(String.valueOf(allData.get(EUR)))));
				return respMap;
			} else {
				return null;
			}
		} catch (Exception e) {
			return null;
		}

	}

}