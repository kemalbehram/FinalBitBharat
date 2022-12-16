package com.mobiloitte.microservice.wallet.cryptocoins;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.mobiloitte.microservice.wallet.dao.MnemonicDao;
import com.mobiloitte.microservice.wallet.entities.Mnemonic;
import com.mobiloitte.microservice.wallet.model.CryptoRequestModel;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.service.CryptoCoinService;
import com.mobiloitte.microservice.wallet.utils.APIUtils;

/**
 * The Class ETHUtil.
 * 
 * @author Priyank Mishra
 */
@Component
@SuppressWarnings("unchecked")
public class ETHUtil implements CryptoCoinService, WalletConstants, OtherConstants {

	public static final int ETH_TIMEOUT = 132000;

	@Autowired
	private ApiUrlConstants apiUrl;

	private static final Logger LOGGER = LogManager.getLogger(ETHUtil.class);
	@Autowired
	private MnemonicDao mnemonicDao;

	@Override
	public CryptoResponseModel getAddressAPI(String account) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		try {
			String mnemonic = mnemonic();
			String apiBaseUri = apiUrl.getETH_NODE_API_BASE() + ETH_LOWER_CASE + "/generateWallet?userId=" + account
					+ "&mnemonic=" + mnemonic;
			apiBaseUri.replace(" ", "%20");
			String responseString = APIUtils.extractGetAPIData(apiBaseUri.replace(" ", "%20"), ETH_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);

			if (allData.containsKey("address") && allData.containsKey("privateKey")) {
				wallet.setAddress(String.valueOf(allData.get("address")));
				wallet.setPrivateKey(String.valueOf(allData.get("privateKey")));
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
			String apiBaseUri = apiUrl.getETH_NODE_API_BASE() + ETH_LOWER_CASE + "/getBalance?address=" + address;
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, ETH_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.containsKey("Balance")) {
				return BigDecimal.valueOf(Double.valueOf(String.valueOf(allData.get("Balance"))));
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
		CryptoResponseModel wallet = null;
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair("fromAddress", coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair("toAddress", coinRequest.getToAddress()));
		formparams.add(new BasicNameValuePair("fromPrivateKey", coinRequest.getWalletFile()));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getETH_NODE_API_BASE() + ETH_LOWER_CASE + "/transfer";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, ETH_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.get("responseCode").equals(200)) {
				wallet = new CryptoResponseModel();
				if (allData.containsKey("Hash")) {
					String a = String.valueOf(allData.get("Hash"));
					wallet.setTxnHash(a);
					wallet.setActualAmount(coinRequest.getAmount());
					wallet.setCoinType(ETH);
					wallet.setToAddress(coinRequest.getToAddress());
					wallet.setStorageType(coinRequest.getStorageType());
					wallet.setTxnType(DEPOSIT);
					String apiBaseUri1 = "http://13.233.44.156:3015/eth/getMarketPriceById?ids=ETH";
					String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, ETH_TIMEOUT);
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

	@Override
	public CryptoResponseModel withdrawAPI(CryptoRequestModel coinRequest) {
		CryptoResponseModel wallet = null;
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair("fromAddress", coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair("toAddress", coinRequest.getToAddress()));
		formparams.add(new BasicNameValuePair("amountToSend", coinRequest.getAmount().toPlainString()));
		formparams.add(new BasicNameValuePair("fromPrivateKey", coinRequest.getWalletFile()));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getETH_NODE_API_BASE() + ETH_LOWER_CASE + "/withdraw";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, ETH_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.get("responseCode").equals(200)) {
				wallet = new CryptoResponseModel();
				if (allData.containsKey("hash")) {
					wallet.setTxnHash(String.valueOf(allData.get("hash")));
					BigDecimal toHotWallet = coinRequest.getAmount();
					wallet.setActualAmount(toHotWallet);
					wallet.setCoinType(ETH);
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
	 * Gets the transfer funds.
	 *
	 * @param toAddress   the to address
	 * @param fromAddress the from address
	 * @param privateKey  the private key
	 * @return the transfer funds
	 */
	public String getTransferFunds(String toAddress, String fromAddress, String privateKey) {
		String response = null;
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair("fromAddress", fromAddress));
		formparams.add(new BasicNameValuePair("toAddress", toAddress));
		formparams.add(new BasicNameValuePair("fromPrivateKey", privateKey));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getETH_NODE_API_BASE() + ETH_LOWER_CASE + "/transfer";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, ETH_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.get("responseCode").equals(200) && allData.containsKey("Hash")) {
				response = String.valueOf(allData.get("Hash"));
			}

		} catch (Exception e) {
			LOGGER.catching(e);
		}
		return response;
	}

	public String transferFundsToToken(String toAddress, String fromAddress, String privateKey) {
		String response = null;
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair(FROM_ETH_ADDRESS, fromAddress));
		formparams.add(new BasicNameValuePair(TO_ETH_ADDRESS, toAddress));
		formparams.add(new BasicNameValuePair(ETH_VALUE, "0.01"));
		formparams.add(new BasicNameValuePair(PRIVATE_KEY, privateKey));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
			String apiBaseUri = apiUrl.getETH_NODE_API_BASE() + ETH_LOWER_CASE + "/payment";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, ETH_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.get(CODE).equals(200) && allData.containsKey(ETH_TXN_HASH)) {
				response = String.valueOf(allData.get(ETH_TXN_HASH));
			}

		} catch (Exception e) {
			LOGGER.catching(e);
		}
		return response;
	}

	private String mnemonic() {
		String mnemonic = null;
		Optional<Mnemonic> trx = mnemonicDao.findByCoinName("ETH");
		if (!trx.isPresent()) {
			mnemonic = "";
		} else {

			mnemonic = trx.get().getMnemonic();
		}
		return mnemonic;
	}
}