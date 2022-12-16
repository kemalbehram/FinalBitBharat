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

@Component
@SuppressWarnings("unchecked")
public class SOLANAUtil implements CryptoCoinService, WalletConstants, OtherConstants {

	public static final int SOLANA_TIMEOUT = 10 * 10000;

	@Autowired
	private ApiUrlConstants apiUrl;

	private static final Logger LOGGER = LogManager.getLogger(SOLANAUtil.class);

	@Override
	public CryptoResponseModel getAddressAPI(String account) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		try {
			String apiBaseUri = apiUrl.getSOLANA_API_BASE() + SOLANA_LOWER_CASE + "/walletGenerate";
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, SOLANA_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);

			if (allData.containsKey("responseResult")) {
				Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");
				wallet.setAddress(String.valueOf(resultSet.get("address")));
				wallet.setPrivateKey(String.valueOf(resultSet.get("privateKey")));

			}
		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

	@Override
	public BigDecimal getBalanceAPI(String address) {
		try {
			String apiBaseUri = apiUrl.getSOLANA_API_BASE() + SOLANA_LOWER_CASE + "/getBalance?address=" + address;
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, SOLANA_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.containsKey("responseResult")) {
				Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");
				return BigDecimal.valueOf(Double.valueOf(String.valueOf(resultSet.get("balance"))));
			} else {
				return DEFAULT_BALANCE;
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			return DEFAULT_BALANCE;
		}
	}

	@Override
	public CryptoResponseModel internalTransferAPI(CryptoRequestModel coinRequest) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair("senderAddress", coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair("privateKey", coinRequest.getWalletFile()));
		formparams.add(new BasicNameValuePair("recieverAddress", coinRequest.getToAddress()));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getSOLANA_API_BASE() + SOLANA_LOWER_CASE + "/transfer";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, SOLANA_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.containsKey("responseResult")) {
				Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");

				wallet.setTxnHash(String.valueOf(resultSet.get("Hash")));
				wallet.setCoinType(SOLANA);
				wallet.setStorageType(coinRequest.getStorageType());
				wallet.setTxnType(DEPOSIT);
				wallet.setAddress(coinRequest.getToAddress());
				String apiBaseUri1 = "http://13.233.44.156:3015/eth/getMarketPriceById?ids=SOL";
				String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, SOLANA_TIMEOUT);
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
		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

	@Override
	public CryptoResponseModel withdrawAPI(CryptoRequestModel coinRequest) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair("senderAddress", coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair("privateKey", coinRequest.getWalletFile()));
		formparams.add(new BasicNameValuePair("recieverAddress", coinRequest.getToAddress()));
		formparams.add(new BasicNameValuePair("amountToSend", roundOff(coinRequest.getAmount().toPlainString())));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getSOLANA_API_BASE() + SOLANA_LOWER_CASE + "/withdrawSOL";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, SOLANA_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.get("responseCode").equals(200)) {

				if (allData.containsKey("responseResult")) {
					Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");
					wallet.setTxnHash(String.valueOf(resultSet.get("Hash")));
					wallet.setCoinType(SOLANA);
					wallet.setToAddress(coinRequest.getToAddress());
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

}