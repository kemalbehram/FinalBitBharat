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
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.MnemonicDao;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.Mnemonic;
import com.mobiloitte.microservice.wallet.model.CryptoRequestModel;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.service.CryptoCoinService;
import com.mobiloitte.microservice.wallet.utils.APIUtils;

/**
 * 
 * @author Priyank Mishra
 * 
 */
@Component
@SuppressWarnings("unchecked")
public class ERCUtil implements CryptoCoinService, WalletConstants, OtherConstants {

	public static final int USDT_TIMEOUT = 180000;

	@Autowired
	private ApiUrlConstants apiUrl;

	@Autowired
	private CoinDao coinDao;

	private static final Logger LOGGER = LogManager.getLogger(USDTUtil.class);
	@Autowired
	private MnemonicDao mnemonicDao;

	public BigDecimal getBalanceAPIERCBEP(String walletAddress, String getContract) {
		try {
			String apiBaseUri = "http://13.233.44.156:3030/erc20/getBalance?address=" + walletAddress + "&contract="
					+ getContract;
//			String apiBaseUri = "http://3.213.77.140:3030/erc20/getBalance?address=" + walletAddress + "&contract="
//					+ getContract;
			String responseString = APIUtils.extractGetAPIData(apiBaseUri, USDT_TIMEOUT);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);

			if (allData.containsKey("responseResult")) {
				Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");
				return new BigDecimal(String.valueOf(resultSet.get("balance")));

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
		return null;
	}

	public CryptoResponseModel internalTransferAPIBEPERC(CryptoRequestModel coinRequest, String getContract,
			String coinName) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair("senderAddress", coinRequest.getFromAddress()));
		formparams.add(new BasicNameValuePair("senderPrivateKey", coinRequest.getWalletFile()));
		formparams.add(new BasicNameValuePair("receiverAddress", coinRequest.getToAddress()));
		formparams.add(new BasicNameValuePair("token", coinRequest.getAmount().toPlainString()));
		formparams.add(new BasicNameValuePair("contract", getContract));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {

			String apiBaseUri = "http://13.233.44.156:3030/erc20/transferTokenUserToUser";
//			String apiBaseUri = "http://3.213.77.140:3030/erc20/transferTokenUserToUser";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, USDT_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);

			if (allData.containsKey("responseResult")) {
				Map<String, Object> resultSet1 = (Map<String, Object>) allData.get("responseResult");
				String a = String.valueOf(resultSet1.get("hash"));
				wallet.setTxnHash(a);
				wallet.setActualAmount(coinRequest.getAmount());
				wallet.setCoinType(coinName);
				wallet.setToAddress(coinRequest.getToAddress());
				wallet.setStorageType(coinRequest.getStorageType());
				wallet.setTxnType(DEPOSIT);
				String apiBaseUri1 = "http://3.213.77.140:3015/eth/getMarketPriceById?ids=" + coinName.toLowerCase();
				String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, USDT_TIMEOUT);
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
		}
		return wallet;
	}

	public CryptoResponseModel withdrawAPIERCBep(CryptoRequestModel coinRequest, String getContract, String coinName) {

		CryptoResponseModel wallet = new CryptoResponseModel();
		List<NameValuePair> formparams = new ArrayList<>();
		formparams.add(new BasicNameValuePair("recieverAddress", coinRequest.getToAddress()));
		formparams.add(new BasicNameValuePair("amountToSend", coinRequest.getAmount().toPlainString()));
		formparams.add(new BasicNameValuePair("privateKey", coinRequest.getWalletFile()));
		formparams.add(new BasicNameValuePair("contract", getContract));
		UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
		try {
//			String apiBaseUri = apiUrl.getUSDT_API_BASE() + "transfer";
			String apiBaseUri = "http://13.233.44.156:3030/erc20/withdraw";
			String responseString = APIUtils.extractPostAPIData(apiBaseUri, USDT_TIMEOUT, entity);
			LOGGER.info(responseString);
			Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);

			if (allData.containsKey("responseResult")) {
				Map<String, Object> resultSet = (Map<String, Object>) allData.get("responseResult");

				if (!resultSet.isEmpty()) {
					String a = String.valueOf(resultSet.get("transactionHash"));
					wallet.setTxnHash(a);
					wallet.setActualAmount(coinRequest.getAmount());
					wallet.setCoinType(coinName);
					wallet.setToAddress(coinRequest.getToAddress());
					wallet.setStorageType(coinRequest.getStorageType());
					wallet.setTxnType(WITHDRAW);
				}
			}

		} catch (Exception e) {
			LOGGER.catching(e);
		}
		return wallet;
	}

	@Override
	public CryptoResponseModel getAddressAPI(String account) {
		CryptoResponseModel wallet = new CryptoResponseModel();
		try {
			String mnemonic = mnemonic();
			String apiBaseUri = "http://13.233.44.156:3030/bep20/generateAddress?mnemonic=" + mnemonic + "&count="
					+ account;
//			String apiBaseUri = "http://3.213.77.140:3030/bep20/generateAddress?mnemonic=" + mnemonic + "&count="
//					+ account;
			apiBaseUri.replace(" ", "%20");
			String responseString = APIUtils.extractGetAPIData(apiBaseUri.replace(" ", "%20"), USDT_TIMEOUT);
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

	private String mnemonic() {
		String mnemonic = null;
		Optional<Mnemonic> trx =

				mnemonicDao.findByCoinName("USDT");
		if (!trx.isPresent()) {
			mnemonic = "";
		} else {

			mnemonic = trx.get().getMnemonic();
		}
		return mnemonic;
	}

	@Override
	public BigDecimal getBalanceAPI(String address) {

		return null;
	}

	public Map<String, Object> getNetworkBalance(String address) {

		return null;
	}

	public Boolean isTxnHashValid(String lastHash) {

		return null;
	}

	@Override
	public CryptoResponseModel withdrawAPI(CryptoRequestModel coinRequest) {

		return null;
	}

}
