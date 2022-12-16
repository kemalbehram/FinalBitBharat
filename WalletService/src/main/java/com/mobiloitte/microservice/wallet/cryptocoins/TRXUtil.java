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
public class TRXUtil implements CryptoCoinService, WalletConstants, OtherConstants {
	public static final int TRX_TIMEOUT = 10 * 10000;
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
	public CryptoResponseModel getAddressAPI(String account) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		try {
			String mnemonic = mnemonic();
//			String apiBaseUri = apiUrl.getAVAX_API_BASE() + AVAX_LOWER_CASE + "/generateAvaxWallet?count=" + account
//					+ "&mnemonic=" + mnemonic;
			String apiBaseUri = apiUrl.getTRX_API_BASE() + TRX_LOWER_CASE + "/generateTRXWallet?mnemonic=" + mnemonic
					+ "&count=" + account;
			apiBaseUri.replace(" ", "%20");
			String responseString = APIUtils.extractGetAPIData(apiBaseUri.replace(" ", "%20"), TRX_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			if (allData.containsKey("responseResult")) {
				Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");
				if (resultSet.containsKey("totalwallet")) {
					List<Map<String, Object>> resultSet1 = (List<Map<String, Object>>) resultSet.get("totalwallet");
					Map<String, Object> data = resultSet1.get(0);
					wallet.setAddress(String.valueOf(data.get("address")));
					wallet.setPrivateKey(String.valueOf(data.get("privateKey")));
				}
			}

		} catch (Exception e) {
			LOGGER.catching(e);
			wallet = null;
		}
		return wallet;
	}

	private String mnemonic() {
		String mnemonic = null;
		Optional<Mnemonic> trx =

				mnemonicDao.findByCoinName("AVAX");
		if (!trx.isPresent()) {
			mnemonic = "";
		} else {

			mnemonic = trx.get().getMnemonic();
		}
		return mnemonic;
	}

	@Override
	public BigDecimal getBalanceAPI(String address) {
		try {
			String apiBaseUri = apiUrl.getTRX_API_BASE() + TRX_LOWER_CASE + "/accountTRXBalance?address=" + address;
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, TRX_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);

			if (allData.containsKey("responseResult")) {
				return BigDecimal.valueOf(Double.valueOf(String.valueOf(allData.get("responseResult"))));
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

//			String apiBaseUri = apiUrl.getAVAX_API_BASE() + AVAX_LOWER_CASE + "/transferAVAX";
			String apiBaseUri = apiUrl.getTRX_API_BASE() + TRX_LOWER_CASE + "/trxTransfer";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, TRX_TIMEOUT, entity);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
			System.out.println("allDataallDataallData " + allData);
			if (allData.containsKey("responseResult")) {
				Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");
				wallet.setTxnHash(String.valueOf(resultSet.get("txid")));

				BigDecimal b = coinRequest.getAmount();
				wallet.setActualAmount(b);
				wallet.setCoinType(TRX);

				wallet.setToAddress(coinRequest.getToAddress());
				wallet.setFromAddress(coinRequest.getFromAddress());
				wallet.setStorageType(coinRequest.getStorageType());
				wallet.setTxnType(DEPOSIT);
				String apiBaseUri1 = "http://13.233.44.156:3015/eth/getMarketPriceById?ids=TRX";
				String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, TRX_TIMEOUT);
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

			Optional<StorageDetail> walletRequiredData = storageDetailsDao.findByCoinTypeAndStorageType("TRX", "HOT");
			if (walletRequiredData.isPresent()) {
//				String apiBaseUri = apiUrl.getAVAX_API_BASE() + AVAX_LOWER_CASE + "/withdrawAVAX";
				String apiBaseUri = apiUrl.getTRX_API_BASE() + TRX_LOWER_CASE + "/withdraw";

				List<NameValuePair> formparams = new ArrayList<>();
				formparams.add(new BasicNameValuePair("senderAddress", walletRequiredData.get().getAddress()));
				formparams.add(new BasicNameValuePair("privateKey", walletRequiredData.get().getWalletFile()));
				formparams.add(new BasicNameValuePair("recieverAddress", coinRequest.getToAddress()));
				formparams.add(new BasicNameValuePair("amount", String.valueOf(coinRequest.getAmount())));

				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
				String responseString = APIUtils.extractPostAPIData(apiBaseUri, TRX_TIMEOUT, entity);
				Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
				if (allData.containsKey("responseResult")) {
					Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");
					wallet = new CryptoResponseModel();
					wallet.setTxnHash(String.valueOf(resultSet.get("txid")));
					BigDecimal toHotWallet = coinRequest.getAmount();
					wallet.setActualAmount(toHotWallet);
					wallet.setCoinType(TRX);
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