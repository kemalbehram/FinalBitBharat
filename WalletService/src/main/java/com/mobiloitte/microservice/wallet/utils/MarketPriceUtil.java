
package com.mobiloitte.microservice.wallet.utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.dao.MarketPriceDao;
import com.mobiloitte.microservice.wallet.entities.MarketPrice;

/**
 * The Class MarketPriceUtil.
 * 
 * @author Ankush Mohapatra
 */

@SuppressWarnings("unchecked")

@Component
public class MarketPriceUtil implements WalletConstants, OtherConstants {

	/** The market price dao. */

	@Autowired
	private MarketPriceDao marketPriceDao;

	/** The Constant LOGGER. */

	private static final Logger LOGGER = LogManager.getLogger(MarketPriceUtil.class);

	/** The Constant MARKET_PRICE_API_URL. */

	private static final String COIN_MARKET_CAP_API_URL = "https://pro-api.coinmarketcap.com/v1/tools/price-conversion";
	// private static final String COIN_MARKET_CAP_API_URL =
	// "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest";

	/** The Constant COIN_MARKET_CAP_API_KEY. */

	private static final String COIN_MARKET_CAP_API_KEY = "f4d52670-0c10-4cff-ba45-f88e1115c0b4";

	/** The Constant COIN_MARKET_CAP_API_KEY. */

//	private static final String COIN_MARKET_CAP_API_KEY = "da21ff41-9fd3-4c58-88ed-71e1615d6e2e";

	/** The Constant COIN_MARKET_CAP_HEADER_NAME. */

	private static final String COIN_MARKET_CAP_HEADER_NAME = "X-CMC_PRO_API_KEY";

	/** The Constant STATUS_RESPONSE. */

	private static final String STATUS_RESPONSE = "status";

	/** The Constant ERROR_CODE. */

	private static final String ERROR_CODE = "error_code";

	/** The Constant DATA. */

	private static final String DATA = "data";

	/** The Constant QUOTE. */

	private static final String QUOTE = "quote";

	/** The Constant PRICE. */

	private static final String PRICE = "price";

	/** The Constant ONE. */

	private static final String ONE = "1";

	/** The Constant REQUEST_AMOUNT. */

	private static final String REQUEST_AMOUNT = "amount";

	/** The Constant REQUEST_SYMBOL. */

	private static final String REQUEST_SYMBOL = "symbol";

	/** The Constant REQUEST_CONVERT. */

	private static final String REQUEST_CONVERT = "convert";
	/*private static final String PERCENT_CHANGE_24H = "percent_change_24h";
	private static final String PERCENT_CHANGE_1H = "percent_change_1h";
	private static final String PERCENT_CHANGE_7D = "percent_change_7d";*/

	/**
	 * Instantiates a new market price util.
	 */

	protected MarketPriceUtil() {
	}

	/**
	 * Gets the market price.
	 *
	 * @param execCoin the exec coin
	 * @param baseCoin the base coin
	 * @return the market price
	 */

	@Transactional
	public BigDecimal getMarketPrice(String execCoin, String baseCoin) {
		Optional<MarketPrice> getMarketPrice = marketPriceDao.findByCoinName(execCoin);
		if (getMarketPrice.isPresent()) {
			return getMarketPrice.get().getPriceInUsd();

		} else {
			LOGGER.debug("No such coin found in table MarketPrice: " + execCoin);
			return null;
		}
	}

	/**
	 * Gets the market price.
	 *
	 * @param execCoin the exec coin
	 * @return the market price
	 */

	public static Map<String, BigDecimal> getMarketPrice(String execCoin) {
		Map<String, BigDecimal> responseMap = new HashMap<>();
		try {
			String apiBaseUri = COIN_MARKET_CAP_API_URL;
			List<NameValuePair> paratmers = new ArrayList<>();

			paratmers.add(new BasicNameValuePair(REQUEST_AMOUNT, ONE));
			paratmers.add(new BasicNameValuePair(REQUEST_SYMBOL, execCoin));
			paratmers.add(new BasicNameValuePair(REQUEST_CONVERT, USD));

			paratmers.add(new BasicNameValuePair("start", "1"));
			paratmers.add(new BasicNameValuePair("limit", "5000"));
			paratmers.add(new BasicNameValuePair("convert", "USD"));

			String responseString = makeAPICallForCoinMarketCap(apiBaseUri, paratmers);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			Map<String, Object> getApiStatus = (Map<String, Object>) allData.get(STATUS_RESPONSE);
			if (Integer.parseInt(String.valueOf(getApiStatus.get(ERROR_CODE))) == 0) {
				Map<String, Object> data = (Map<String, Object>) allData.get(DATA);
				Map<String, Object> quote = (Map<String, Object>) data.get(QUOTE);

				Map<String, Object> usd = (Map<String, Object>) quote.get(USD);

				responseMap.put(USD, BigDecimal.valueOf(Double.parseDouble(String.valueOf(usd.get(PRICE)))));

				responseMap.put("Price_24Hour",
						BigDecimal.valueOf(Double.parseDouble(String.valueOf(usd.get(percent_change_24h)))));
				responseMap.put("percent_change_1h",
						BigDecimal.valueOf(Double.parseDouble(String.valueOf(usd.get(percent_change_1h)))));
				responseMap.put("percent_change_7d",
						BigDecimal.valueOf(Double.parseDouble(String.valueOf(usd.get(percent_change_7d)))));

				return responseMap;
			} else {
				return null;
			}
		} catch (IOException e) {
			LOGGER.debug("Error: cannont access content - " + e.toString());
			return null;
		} catch (URISyntaxException e) {
			LOGGER.debug("Error: Invalid URL " + e.toString());
			return null;
		}
	}

	/**
	 * Make API call for coin market cap.
	 *
	 * @param uri        the uri
	 * @param parameters the parameters
	 * @return the string
	 * @throws URISyntaxException the URI syntax exception
	 * @throws IOException        Signals that an I/O exception has occurred.
	 */
	public static String makeAPICallForCoinMarketCap(String uri, List<NameValuePair> parameters)
			throws URISyntaxException, IOException {
		String response_content = BLANK;
		try (CloseableHttpClient client = HttpClients.createDefault()) {
			URIBuilder query = new URIBuilder(uri);
			query.addParameters(parameters);

			HttpGet request = new HttpGet(query.build());

			request.setHeader(HttpHeaders.ACCEPT, "application/json");
			request.addHeader(COIN_MARKET_CAP_HEADER_NAME, COIN_MARKET_CAP_API_KEY);

			CloseableHttpResponse response = client.execute(request);

			HttpEntity entity = response.getEntity();
			response_content = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} catch (URISyntaxException | IOException e) {
			LOGGER.catching(e);
			return null;
		}

		return response_content;
	}

}
