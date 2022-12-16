
package com.mobiloitte.microservice.wallet.cryptocoins;

import java.math.BigDecimal;
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
import com.mobiloitte.microservice.wallet.model.XRPModel;
import com.mobiloitte.microservice.wallet.service.CryptoCoinService;
import com.mobiloitte.microservice.wallet.utils.APIUtils;

/**
 * The Class XRPUtil.
 * 
 * @author Ankush Mohapatra
 */
@Component
@SuppressWarnings("unchecked")
public class XRPUtil implements CryptoCoinService, WalletConstants, OtherConstants {

	/** The api url. */
	@Autowired
	private ApiUrlConstants apiUrl;

	/** The Constant XRP_TIMEOUT. */
	public static final int XRP_TIMEOUT = 10 * 10000;

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(XRPUtil.class);

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.CryptoCoinService
	 * #getAddressAPI(java.lang.String)
	 */
	@Override
	public CryptoResponseModel getAddressAPI(String account) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		try {
			String apiBaseUri = apiUrl.getXRP_API_BASE() + "xrp" + "/generateAddress";
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, XRP_TIMEOUT);
			LOGGER.info(responseString);
			XRPModel allData = APIUtils.getJavaObjectFromJsonString(responseString, XRPModel.class);
			if (allData.getData().containsKey(ADDRESS)) {
				wallet.setAddress(String.valueOf(allData.getData().get(ADDRESS)));
				wallet.setPrivateKey(String.valueOf(allData.getData().get(SECRET)));
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.CryptoCoinService
	 * #getBalanceAPI(java.lang.String)
	 */
	@Override
	public BigDecimal getBalanceAPI(String address) {
		try {
			// get_addr_info?addr=r92smx6Mk7GpP7itTktAEV6aHfRgtiMfbj
			String apiBaseUri = apiUrl.getXRP_API_BASE() + "xrp" + "/getBalance?addr=" + address;
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, XRP_TIMEOUT);
			LOGGER.info(responseString);
			XRPModel allData = APIUtils.getJavaObjectFromJsonString(responseString, XRPModel.class);
			if (allData.getData().containsKey(XRP_BALANCE)) {
				return BigDecimal.valueOf(Double.valueOf(String.valueOf(allData.getData().get(XRP_BALANCE))));
			} else {
				return DEFAULT_BALANCE;
			}
		} catch (Exception e) {
			LOGGER.catching(e);
			return DEFAULT_BALANCE;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.CryptoCoinService
	 * #internalTransferAPI(com.mobiloitte.microservice.wallet.model.
	 * CryptoRequestModel)
	 */
	@Override
	public CryptoResponseModel internalTransferAPI(CryptoRequestModel coinRequest) {
		CryptoResponseModel wallet = null;
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair(XRP_DEPOSIT_ADDRESS, coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair(XRP_DEPOSIT_TAG_ID, coinRequest.getTag()));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getXRP_API_BASE() + "xrp" + "/deposits";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, XRP_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.get(XRP_RESPONSE_CODE).equals(200)) {
				wallet = new CryptoResponseModel();
				wallet.setDepositList((List<Map<String, Object>>) allData.get(XRP_DATA));
			}

		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.CryptoCoinService
	 * #withdrawAPI(com.mobiloitte.microservice.wallet.model.CryptoRequestModel)
	 */
	@Override
	public CryptoResponseModel withdrawAPI(CryptoRequestModel coinRequest) {
		CryptoResponseModel wallet = null;
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair(XRP_DESTINATION_ADDRESS, coinRequest.getToAddress()));
		formparams.add(new BasicNameValuePair(XRP_SOURCE_ADDRESS, coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair(XRP_TO_TAG_ID, coinRequest.getTag()));
		formparams.add(new BasicNameValuePair(SECRET, coinRequest.getWalletFile()));
		formparams.add(new BasicNameValuePair(AMOUNT, coinRequest.getAmount().toPlainString()));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getXRP_API_BASE() + "xrp" + "/withdraw";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, XRP_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.get(XRP_RESPONSE_CODE).equals(200)) {
				wallet = new CryptoResponseModel();
				wallet.setTxnHash(String.valueOf(allData.get(XRP_DATA)));
				wallet.setActualAmount(coinRequest.getAmount());
				wallet.setCoinType(XRP);
				wallet.setToAddress(coinRequest.getToAddress());
				wallet.setStorageType(coinRequest.getStorageType());
				wallet.setTxnType(WITHDRAW);
			}

		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;

	}

}
