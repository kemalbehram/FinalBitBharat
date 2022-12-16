package com.mobiloitte.microservice.wallet.cryptocoins;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mobiloitte.microservice.wallet.constants.ApiUrlConstants;
import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.model.CryptoRequestModel;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.service.CryptoCoinService;
import com.mobiloitte.microservice.wallet.utils.APIUtils;

/**
 * The Class BTCUtil.
 * 
 * @author Ankush Mohapatra
 */
@Component
@SuppressWarnings("unchecked")
public class BTCUtil implements CryptoCoinService, WalletConstants, OtherConstants {

	/** The Constant BTC_TIMEOUT. */
	public static final int BTC_TIMEOUT = 10 * 10000;

	/** The api url. */
	@Autowired
	private ApiUrlConstants apiUrl;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(BTCUtil.class);

	/**
	 * Gets the address API.
	 *
	 * @param account the account
	 * @return the address API
	 */
	@Override
	public CryptoResponseModel getAddressAPI(String account) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		try {
			String apiBaseUri = apiUrl.getBTC_API_BASE() + BTC_LOWER_CASE + "/newaddress/" + account;
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, BTC_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.containsKey(ADDRESS)) {
				wallet.setAddress(String.valueOf(allData.get(ADDRESS)));
			}

		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

	/**
	 * Gets the balance API.
	 *
	 * @param address the address
	 * @return the balance API
	 */
	@Override
	public BigDecimal getBalanceAPI(String address) {
		try {
			String apiBaseUri = apiUrl.getBTC_API_BASE() + BTC_LOWER_CASE + "/addr_balance/" + address;
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, BTC_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.containsKey(BALANCE)) {
				return BigDecimal.valueOf(Double.valueOf(String.valueOf(allData.get(BALANCE))));
			} else {
				return DEFAULT_BALANCE;
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			return DEFAULT_BALANCE;
		}
	}

	/**
	 * Internal transfer API.
	 *
	 * @param coinRequest the coin request
	 * @return the crypto response model
	 */
	@Override
	public CryptoResponseModel internalTransferAPI(CryptoRequestModel coinRequest) {
		CryptoResponseModel wallet = null;
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair(SENDFROM, coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair(SENDTO, coinRequest.getToAddress()));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getBTC_API_BASE() + BTC_LOWER_CASE + "/transfer";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, BTC_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.get(CODE).equals(200)) {
				wallet = new CryptoResponseModel();
				if (allData.containsKey(TX_HASH)) {
					wallet.setTxnHash(String.valueOf(allData.get(TX_HASH)));
					BigDecimal toHotWallet = BigDecimal
							.valueOf(Double.parseDouble(String.valueOf(allData.get(SENT_AMOUNT))))
							.subtract(BigDecimal.valueOf(Double.parseDouble(String.valueOf(allData.get(FEE)))));
					wallet.setActualAmount(toHotWallet);
					wallet.setCoinType(BTC);
					wallet.setStorageType(coinRequest.getStorageType());
					wallet.setTxnType(DEPOSIT);
					wallet.setToAddress(coinRequest.getToAddress());
//					String apiBaseUri1 = "http://13.233.44.156:3015/eth/getMarketPriceById?ids=bitcoin";
//					String responseString1 = APIUtils.extractPostAPIData(apiBaseUri1, BTC_TIMEOUT, entity);
//					LOGGER.info(responseString1);
//					Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1, Map.class);
//					if(allData1.containsKey("current_price")) {
//						Map<String, Object> resultSet1 = (Map<String, Object>) allData1.get("current_price");
//						wallet.setDepositeCurrentPrice(BigDecimal.valueOf(resultSet1.get("current_price")));
//					}
					String apiBaseUri1 = "http://13.233.44.156:3015/eth/getMarketPriceById?ids=BTC";
					String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, BTC_TIMEOUT);
					LOGGER.info(responseString1);
					Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1, Map.class);
					if (allData1.containsKey("USD")) {
						LOGGER.info(allData1.get("USD"));
						BigDecimal value = BigDecimal.valueOf(Double.valueOf(String.valueOf(allData1.get("USD"))));
						LOGGER.info(value);
						wallet.setLivePrice(value);
						wallet.setDepositeCurrentPrice((coinRequest.getAmount().multiply(value)));
					}
				}
			}

		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

	/**
	 * Withdraw API.
	 *
	 * @param coinRequest the coin request
	 * @return the crypto response model
	 */
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.CoinService#withdrawAPI(com.
	 * mobiloitte.microservice.wallet.model.CryptoRequestModel)
	 */
	@Override
	public CryptoResponseModel withdrawAPI(CryptoRequestModel coinRequest) {
		CryptoResponseModel wallet = null;
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair(SENDTO, coinRequest.getToAddress()));
		formparams.add(new BasicNameValuePair(SENDFROM, coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair(CHANGEADDRESS, coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair(AMOUNTTOTRANSFER, roundOff(coinRequest.getAmount().toPlainString())));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getBTC_API_BASE() + BTC_LOWER_CASE + "/withdraw";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, BTC_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.get(CODE).equals(200)) {
				wallet = new CryptoResponseModel();
				if (allData.containsKey(TX_HASH)) {
					wallet.setTxnHash(String.valueOf(allData.get(TX_HASH)));
					BigDecimal toHotWallet = coinRequest.getAmount()
							.subtract(BigDecimal.valueOf(Double.parseDouble(String.valueOf(allData.get(FEE)))));
					wallet.setActualAmount(toHotWallet);
					wallet.setCoinType(BTC);
					wallet.setTxnFee(BigDecimal.valueOf(Double.valueOf(String.valueOf(allData.get(FEE)))));
					wallet.setToAddress(coinRequest.getToAddress());
					wallet.setFromAddress(coinRequest.getFromAddress());
					wallet.setStorageType(coinRequest.getStorageType());
					wallet.setTxnType(WITHDRAW);
				}
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

	/**
	 * Round off.
	 *
	 * @param amount the amount
	 * @return the string
	 */
	private String roundOff(String amount) {
		if (amount.contains(".")) {
			String[] arrOfStr = amount.split("\\.");
			if (arrOfStr[1].length() > 8) {
				BigDecimal b1 = new BigDecimal(amount);
				MathContext m = new MathContext(8);
				return String.valueOf(b1.round(m));
			} else {
				return amount;
			}
		} else {
			return amount;
		}

	}

	public BigDecimal getNetworkBalance(String address) {
		try {
			String apiBaseUri = "https://api.blockcypher.com/v1/btc/main/addrs/" + address + "/balance";
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, BTC_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			return BigDecimal.valueOf(Double.valueOf(String.valueOf(allData.get("final_balance"))) / Math.pow(10, 8));
		} catch (Exception e) {
			LOGGER.catching(e);
			return DEFAULT_BALANCE;
		}
	}

}
