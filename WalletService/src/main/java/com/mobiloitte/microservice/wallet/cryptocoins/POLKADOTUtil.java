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
import com.mobiloitte.microservice.wallet.dao.StorageDetailsDao;
import com.mobiloitte.microservice.wallet.dao.WalletDao;
import com.mobiloitte.microservice.wallet.entities.Mnemonic;
import com.mobiloitte.microservice.wallet.entities.StorageDetail;
import com.mobiloitte.microservice.wallet.model.CryptoRequestModel;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.service.CryptoCoinService;
import com.mobiloitte.microservice.wallet.utils.APIUtils;

@Component
@SuppressWarnings("unchecked")
public class POLKADOTUtil implements CryptoCoinService, WalletConstants, OtherConstants {
	public static final int POLKADOT_TIMEOUT = 10 * 10000;
	private static final Logger LOGGER = LogManager.getLogger(BTCUtil.class);

	@Autowired
	private ApiUrlConstants apiUrl;

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private StorageDetailsDao storageDetailsDao;
	@Autowired
	private MnemonicDao mnemonicDao;

	@Override
	public CryptoResponseModel getAddressAPI(String password) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		try {
			String apiBaseUri = apiUrl.getPOLKADOT_API_BASE() + POLKADOT_LOWER_CASE + "/generatePolkaAddress";
			apiBaseUri.replace(" ", "%20");
			String responseString = APIUtils.extractGetAPIData(apiBaseUri.replace(" ", "%20"), POLKADOT_TIMEOUT);
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

			String apiBaseUri = apiUrl.getPOLKADOT_API_BASE() + POLKADOT_LOWER_CASE + "/getBalance?address=" + address;
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, POLKADOT_TIMEOUT);
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
		try {

			String apiBaseUri = apiUrl.getPOLKADOT_API_BASE() + POLKADOT_LOWER_CASE + "/transfer";

			List<NameValuePair> formparams = new ArrayList<>();
			formparams.add(new BasicNameValuePair("senderAddress", coinRequest.getFromAddress()));
			formparams.add(new BasicNameValuePair("privateKey", coinRequest.getWalletFile()));
			formparams.add(new BasicNameValuePair("recieverAddress", coinRequest.getToAddress()));

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, POLKADOT_TIMEOUT, entity);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			System.out.println("allDataallDataallData " + allData);
			if (allData.containsKey("responseResult")) {
				Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");
				wallet.setTxnHash(String.valueOf(resultSet.get("Hash")));

				BigDecimal b = coinRequest.getAmount();
				wallet.setActualAmount(b);
				wallet.setCoinType(POLKADOT);

				wallet.setToAddress(coinRequest.getToAddress());
				wallet.setFromAddress(coinRequest.getFromAddress());
				wallet.setStorageType(coinRequest.getStorageType());
				wallet.setTxnType(DEPOSIT);
				String apiBaseUri1 = "http://13.233.44.156:3015/eth/getMarketPriceById?ids=DOT";
				String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, POLKADOT_TIMEOUT);
				LOGGER.info(responseString1);
				Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1, Map.class);
				if (allData1.containsKey("USD")) {
					LOGGER.info(allData1.get("USD"));
					BigDecimal value = BigDecimal.valueOf(Double.valueOf(String.valueOf(allData1.get("USD"))));
					LOGGER.info(value);
					wallet.setLivePrice(value);
					wallet.setDepositeCurrentPrice((coinRequest.getAmount().multiply(value)));
				}

			} else {
				return null;
			}
			return wallet;
		} catch (Exception e) {
			System.out.println(e);
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

	@Override
	public CryptoResponseModel withdrawAPI(CryptoRequestModel coinRequest) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		try {

			Optional<StorageDetail> walletRequiredData = storageDetailsDao.findByCoinTypeAndStorageType("POLKADOT",
					"HOT");
			if (walletRequiredData.isPresent()) {
				String apiBaseUri = apiUrl.getPOLKADOT_API_BASE() + POLKADOT_LOWER_CASE + "/withdraw";

				List<NameValuePair> formparams = new ArrayList<>();
				formparams.add(new BasicNameValuePair("senderAddress", walletRequiredData.get().getAddress()));
				formparams.add(new BasicNameValuePair("privateKey", walletRequiredData.get().getWalletFile()));
				formparams.add(new BasicNameValuePair("recieverAddress", coinRequest.getToAddress()));
				formparams.add(new BasicNameValuePair("amountToSend", String.valueOf(coinRequest.getAmount())));

				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
				String responseString = APIUtils.extractPostAPIData(apiBaseUri, POLKADOT_TIMEOUT, entity);
				Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
				if (allData.containsKey("responseResult")) {
					wallet = new CryptoResponseModel();
					Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");
					wallet.setTxnHash(String.valueOf(resultSet.get("Hash")));
					wallet.setCoinType(POLKADOT);
					wallet.setToAddress(coinRequest.getToAddress());
					wallet.setFromAddress(coinRequest.getFromAddress());
					wallet.setStorageType(coinRequest.getStorageType());
					wallet.setTxnType(WITHDRAW);
					wallet.setActualAmount(coinRequest.getAmount());
				}
			}
			return wallet;
		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

}