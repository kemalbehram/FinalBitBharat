package com.mobiloitte.microservice.wallet.config;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.mobiloitte.microservice.wallet.constants.ApiUrlConstants;
import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.cryptocoins.TRXUtil;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.MnemonicDao;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.Mnemonic;
import com.mobiloitte.microservice.wallet.enums.TotalCoins;
import com.mobiloitte.microservice.wallet.utils.APIUtils;

/**
 * The Class InitializeCoins.
 * 
 * @author Ankush Mohapatra
 */

@Configuration
public class InitializeCoins implements OtherConstants {

	@Autowired
	private ApiUrlConstants apiUrl;
	public static final int TRX_TIMEOUT = 10 * 10000;
	private static final Logger LOGGER = LogManager.getLogger(TRXUtil.class);

	/**
	 * Initialize coin table.
	 *
	 * @param coinDao the coin dao
	 * @return the initializing bean
	 */
	@Bean
	public InitializingBean initializeCoinTable(CoinDao coinDao) {
		return () -> {
			if (coinDao.findAll().isEmpty()) {
				TotalCoins[] totalCoins = TotalCoins.values();
				List<Coin> coinList = new ArrayList<>();
				for (int i = 0; i < totalCoins.length; i++) {
					Coin coin = new Coin();
					coin.setCoinShortName(totalCoins[i].name());
					coin.setMakerFee(BigDecimal.ZERO);
					coin.setTakerFee(BigDecimal.ZERO);
					coin.setBasicBuyFee(BigDecimal.ZERO);
					coin.setBasicSellFee(BigDecimal.ZERO);
					coin.setMarketPriceInUsd(BigDecimal.TEN);
					coin.setWithdrawalAmount(BigDecimal.TEN);
					coin.setWithdrawlFee(BigDecimal.ZERO);
					coin.setInternalTransferFee(BigDecimal.ZERO);
					coin.setCreateDate(new Date());
					coin.setIsFavourite(false);
					if (totalCoins[i].name().equalsIgnoreCase(TotalCoins.USD.name()))
						coin.setCoinType(FIAT_CURRENCY);
					else
						coin.setCoinType(CRYPTO_CURRENCY);
					if (totalCoins[i].name().equalsIgnoreCase(TotalCoins.USDT.name()))
						coin.setThresholdPrice(BigDecimal.valueOf(50));
					else
						coin.setThresholdPrice(BigDecimal.ZERO);
					coinList.add(coin);
				}
				coinDao.saveAll(coinList);

			}
		};
	}

	@SuppressWarnings("unchecked")
	@Bean
	public InitializingBean saveMnemonic(MnemonicDao mnemonicDao) {
		return () -> {

			Optional<Mnemonic> usdt = mnemonicDao.findByCoinName("ETH");
			if (!usdt.isPresent()) {
				String apiBaseUri = "http://3.213.77.140:3015/eth/generateMnemonic";
				String responseString = APIUtils.extractGetAPIData(apiBaseUri, TRX_TIMEOUT);

				Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
				if (allData.containsKey("responseResult")
						&& Integer.parseInt(String.valueOf(allData.get("responseCode"))) == 200) {
					Mnemonic Mnemonic = new Mnemonic();
					Mnemonic.setMnemonic(String.valueOf(allData.get("responseResult")));
					Mnemonic.setIsDeleted(Boolean.FALSE);
					Mnemonic.setCoinName("ETH");
					mnemonicDao.save(Mnemonic);
				}
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Bean
	public InitializingBean saveMnemonicTRX(MnemonicDao mnemonicDao) {
		return () -> {

			Optional<Mnemonic> usdt = mnemonicDao.findByCoinName("TRX");
			if (!usdt.isPresent()) {
				String apiBaseUri = "http://3.213.77.140:3017/trx/generateMnemonic";
				String responseString = APIUtils.extractGetAPIData(apiBaseUri, TRX_TIMEOUT);
				LOGGER.info(responseString);
				Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);

				if (allData.containsKey("responseResult")
						&& Integer.parseInt(String.valueOf(allData.get("responseCode"))) == 200) {
					Mnemonic Mnemonic = new Mnemonic();
					Mnemonic.setMnemonic(String.valueOf(allData.get("responseResult")));
					Mnemonic.setIsDeleted(Boolean.FALSE);
					Mnemonic.setCoinName("TRX");
					mnemonicDao.save(Mnemonic);
				}
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Bean
	public InitializingBean saveMnemonicUSDT(MnemonicDao mnemonicDao) {
		return () -> {

			Optional<Mnemonic> usdt = mnemonicDao.findByCoinName("USDT");
			if (!usdt.isPresent()) {
				String apiBaseUri = "http://3.213.77.140:3029/bep20/generateMnemonic";
				String responseString = APIUtils.extractGetAPIData(apiBaseUri, TRX_TIMEOUT);

				Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
				if (allData.containsKey("responseResult")
						&& Integer.parseInt(String.valueOf(allData.get("responseCode"))) == 200) {
					Mnemonic Mnemonic = new Mnemonic();
					Mnemonic.setMnemonic(String.valueOf(allData.get("responseResult")));
					Mnemonic.setIsDeleted(Boolean.FALSE);
					Mnemonic.setCoinName("USDT");
					mnemonicDao.save(Mnemonic);
				}
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Bean
	public InitializingBean saveMnemonicBNB(MnemonicDao mnemonicDao) {
		return () -> {

			Optional<Mnemonic> usdt = mnemonicDao.findByCoinName("BNB");
			if (!usdt.isPresent()) {
				String apiBaseUri = "http://3.213.77.140:3024/bnb/generateMnemonic";
				String responseString = APIUtils.extractGetAPIData(apiBaseUri, TRX_TIMEOUT);
				Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
				if (allData.containsKey("responseResult")
						&& Integer.parseInt(String.valueOf(allData.get("responseCode"))) == 200) {
					Mnemonic Mnemonic = new Mnemonic();
					Mnemonic.setMnemonic(String.valueOf(allData.get("responseResult")));
					Mnemonic.setIsDeleted(Boolean.FALSE);
					Mnemonic.setCoinName("BNB");
					mnemonicDao.save(Mnemonic);
				}
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Bean
	public InitializingBean saveMnemonicMATIC(MnemonicDao mnemonicDao) {
		return () -> {

			Optional<Mnemonic> usdt = mnemonicDao.findByCoinName("MATIC");
			if (!usdt.isPresent()) {
				String apiBaseUri = "http://3.213.77.140:3023/matic/generateMnemonic";
				String responseString = APIUtils.extractGetAPIData(apiBaseUri, TRX_TIMEOUT);

				Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
				if (allData.containsKey("responseResult")
						&& Integer.parseInt(String.valueOf(allData.get("responseCode"))) == 200) {
					Mnemonic Mnemonic = new Mnemonic();
					Mnemonic.setMnemonic(String.valueOf(allData.get("responseResult")));
					Mnemonic.setIsDeleted(Boolean.FALSE);
					Mnemonic.setCoinName("MATIC");
					mnemonicDao.save(Mnemonic);
				}
			}
		};
	}

	@SuppressWarnings("unchecked")
	@Bean
	public InitializingBean saveMnemonicAVAX(MnemonicDao mnemonicDao) {
		return () -> {

			Optional<Mnemonic> usdt = mnemonicDao.findByCoinName("AVAX");
			if (!usdt.isPresent()) {
				String apiBaseUri = "http://3.213.77.140:3021/avax/generateMnemonic";
				String responseString = APIUtils.extractGetAPIData(apiBaseUri, TRX_TIMEOUT);

				Map<String, Object> allData = APIUtils.getJavaObjectFromJsonString(responseString, Map.class);
				if (allData.containsKey("responseResult")
						&& Integer.parseInt(String.valueOf(allData.get("responseCode"))) == 200) {
					Mnemonic Mnemonic = new Mnemonic();
					Mnemonic.setMnemonic(String.valueOf(allData.get("responseResult")));
					Mnemonic.setIsDeleted(Boolean.FALSE);
					Mnemonic.setCoinName("AVAX");
					mnemonicDao.save(Mnemonic);
				}
			}
		};
	}

}
