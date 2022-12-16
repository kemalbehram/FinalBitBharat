package com.mobiloitte.microservice.wallet.utils;

import java.math.BigDecimal;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.cryptocoins.BTCUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.ETHUtil;
import com.mobiloitte.microservice.wallet.dao.StorageDetailsDao;
import com.mobiloitte.microservice.wallet.dao.ThresholdTransferHistoryDao;
import com.mobiloitte.microservice.wallet.entities.StorageDetail;
import com.mobiloitte.microservice.wallet.entities.ThresholdTransferHistory;
import com.mobiloitte.microservice.wallet.exception.StorageDetailsNotFoundException;
import com.mobiloitte.microservice.wallet.feign.UserClient;
import com.mobiloitte.microservice.wallet.model.CryptoRequestModel;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.model.ThresholdMechanismOutput;

@Component
public class SupportThresholdMechanism {

	private SupportThresholdMechanism() {
	}

	private static final Logger LOGGER = LogManager.getLogger(SupportThresholdMechanism.class);

	@Autowired
	private StorageDetailsDao storageDetailDao;

	@Autowired
	private ThresholdTransferHistoryDao thresholdTransferHistoryDao;

	@Autowired
	private StorageDetailsDao storageDetailsDao;

	@Autowired
	private UserClient userClient;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private BTCUtil btcUtil;

	@Autowired
	private ETHUtil ethUtil;

	public Response<ThresholdMechanismOutput> handleThresholdMechanism(String coinName) {
		Optional<StorageDetail> getStorageDetail = storageDetailDao.findByCoinTypeAndStorageType(coinName,
				OtherConstants.STORAGE_HOT);
		if (getStorageDetail.isPresent()) {
			String supportPlatform = coinSupportPlatformMatcher(coinName);
			if (supportPlatform != null) {
				Optional<StorageDetail> getSupportStorage = storageDetailDao
						.findByCoinTypeAndStorageType(supportPlatform, OtherConstants.STORAGE_HOT);
				if (getSupportStorage.isPresent()) {
					BigDecimal supportBalance = checkSupportCoinBalance(coinName, getStorageDetail.get().getAddress());
					if (supportBalance.compareTo(getStorageDetail.get().getSupportThreshold()) < 0) {
						BigDecimal finalAmmountToTransfer = getStorageDetail.get().getSupportThreshold()
								.subtract(supportBalance);
						if (finalAmmountToTransfer.compareTo(getSupportStorage.get().getHotWalletBalance()) >= 0) {
							Boolean isTransferred = transferSupportBalance(finalAmmountToTransfer,
									getStorageDetail.get().getAddress(), getSupportStorage.get().getAddress(),
									getSupportStorage.get().getWalletFile(), coinName);
							if (Boolean.TRUE.equals(isTransferred))
								return new Response<>(
										new ThresholdMechanismOutput(false, "Please try it after 30 minutes...", true));
							else
								return new Response<>(
										new ThresholdMechanismOutput(false, "Please try it again...", false));
						} else {
							// send Notification mail
							String message = "Please top up your " + supportPlatform
									+ " hot wallet address to avoid failure of user withdrawal.";
							mailSender.sendMailForStorageIssueAlert(message, getEmailOfAdmin());
							return new Response<>(
									new ThresholdMechanismOutput(false, "Please try it after 6 hours...", true));
						}

					} else
						return new Response<>(new ThresholdMechanismOutput(true, "allow to withdraw...", false));
				} else {
					// send Notification mail
					String message = "The support wallet " + supportPlatform + " is not been created for " + coinName
							+ " withdrawal";
					mailSender.sendMailForStorageIssueAlert(message, getEmailOfAdmin());
					return new Response<>(new ThresholdMechanismOutput(false, "No support storage found", false));
				}
			} else
				return new Response<>(new ThresholdMechanismOutput(false, "invalid support coin...", false));
		} else {
			// send Notification mail
			String message = "The main wallet " + coinName + " is not been created, please create";
			mailSender.sendMailForStorageIssueAlert(message, getEmailOfAdmin());
			return new Response<>(new ThresholdMechanismOutput(false, "No main storage found", false));
		}
	}

	private String coinSupportPlatformMatcher(String coinName) {
		if (coinName.equalsIgnoreCase(WalletConstants.OMG))
			return WalletConstants.ETH;
		else if (coinName.equalsIgnoreCase(WalletConstants.USDT))
			return WalletConstants.BTC;
		return null;
	}

	private String reverseCoinSupportPlatformMatcher(String coinName) {
		if (coinName.equalsIgnoreCase(WalletConstants.ETH))
			return WalletConstants.OMG;
		else if (coinName.equalsIgnoreCase(WalletConstants.BTC))
			return WalletConstants.USDT;
		return null;
	}

	private BigDecimal checkSupportCoinBalance(String supportCoin, String walletAddress) {
		BigDecimal getLiveBalanceByCoin = null;
		switch (supportCoin) {
		case WalletConstants.USDT:
			getLiveBalanceByCoin = btcUtil.getNetworkBalance(walletAddress);
			break;
		case WalletConstants.OMG:
			getLiveBalanceByCoin = ethUtil.getBalanceAPI(walletAddress);
			break;
		default:
			getLiveBalanceByCoin = BigDecimal.valueOf(-1L);
			break;
		}
		return getLiveBalanceByCoin;
	}

	private Boolean transferSupportBalance(BigDecimal amount, String toAddr, String fromAddr, String secret,
			String coinName) {
		try {
			CryptoRequestModel cryptoRequestModel = readyTransferObject(amount, toAddr, fromAddr, secret);
			CryptoResponseModel cryptoResponseModel = getInternalTransfer(cryptoRequestModel, coinName);
			if (cryptoResponseModel != null && cryptoResponseModel.getTxnHash() != null) {
				saveTransactionDetails(cryptoResponseModel);
				updateStorageBalance(coinName, cryptoResponseModel.getActualAmount());
				return true;
			} else
				return false;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}

	private CryptoRequestModel readyTransferObject(BigDecimal amount, String toAddr, String fromAddr, String secret) {
		CryptoRequestModel cryptoRequestModel = new CryptoRequestModel();
		cryptoRequestModel.setFromAddress(fromAddr);
		cryptoRequestModel.setToAddress(toAddr);
		cryptoRequestModel.setWalletFile(secret);
		cryptoRequestModel.setAmount(amount);
		return cryptoRequestModel;
	}

	private CryptoResponseModel getInternalTransfer(CryptoRequestModel cryptoRequestModel, String coinName) {
		CryptoResponseModel cryptoResponseModel = null;
		switch (coinName) {
		case WalletConstants.BTC:
			cryptoResponseModel = btcUtil.withdrawAPI(cryptoRequestModel);
			break;
		case WalletConstants.ETH:
			cryptoResponseModel = ethUtil.withdrawAPI(cryptoRequestModel);
			break;
		default:
			cryptoResponseModel = null;
			break;
		}
		return cryptoResponseModel;
	}

	private ThresholdTransferHistory saveTransactionDetails(CryptoResponseModel responseModel) {
		try {
			ThresholdTransferHistory coinDepositWithdrawal = null;
			if (responseModel != null && responseModel.getTxnHash() != null) {
				coinDepositWithdrawal = new ThresholdTransferHistory();
				if (responseModel.getToAddress() != null) {
					coinDepositWithdrawal.setToCoinAddress(responseModel.getToAddress());
				}
				if (responseModel.getFromAddress() != null) {
					coinDepositWithdrawal.setFromCoinAddress(responseModel.getFromAddress());
				}
				coinDepositWithdrawal.setTxnHash(responseModel.getTxnHash());
				coinDepositWithdrawal.setFromCoin(responseModel.getCoinType());
				coinDepositWithdrawal.setToCoin(reverseCoinSupportPlatformMatcher(responseModel.getCoinType()));
				coinDepositWithdrawal.setUnits(responseModel.getActualAmount());
				coinDepositWithdrawal = thresholdTransferHistoryDao.save(coinDepositWithdrawal);
			}
			return coinDepositWithdrawal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	private StorageDetail updateStorageBalance(String coinName, BigDecimal balance) {
		try {
			Optional<StorageDetail> getStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(coinName,
					OtherConstants.STORAGE_HOT);
			if (getStorageDetails.isPresent()) {
				StorageDetail storageDetail = null;
				getStorageDetails.get()
						.setHotWalletBalance(getStorageDetails.get().getHotWalletBalance().subtract(balance));
				storageDetail = storageDetailsDao.save(getStorageDetails.get());
				return storageDetail;
			} else {
				throw new StorageDetailsNotFoundException("No Storage found for the coin: " + coinName
						+ "and storage type: " + OtherConstants.STORAGE_HOT + "");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	private String getEmailOfAdmin() {
		try {
			Response<String> adminEmail = userClient.getAdminEmailId();
			if (adminEmail.getStatus() == 200 && adminEmail.getData() != null)
				return adminEmail.getData();
			else
				return "cryptoadmin@mailinator.com";
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return "cryptoadmin@mailinator.com";
		}
	}
}
