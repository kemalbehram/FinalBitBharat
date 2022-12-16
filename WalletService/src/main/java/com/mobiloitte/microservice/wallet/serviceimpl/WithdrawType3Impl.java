package com.mobiloitte.microservice.wallet.serviceimpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.constants.EmailConstants;
import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.cryptocoins.USDTUtil;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.CoinDepositWithdrawalDao;
import com.mobiloitte.microservice.wallet.dao.WalletDao;
import com.mobiloitte.microservice.wallet.dto.UserEmailAndNameDto;
import com.mobiloitte.microservice.wallet.dto.WithdrawRequestDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.enums.UserStatus;
import com.mobiloitte.microservice.wallet.exception.WalletNotFoundException;
import com.mobiloitte.microservice.wallet.feign.UserClient;
import com.mobiloitte.microservice.wallet.model.CryptoRequestModel;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.WithdrawType3;
import com.mobiloitte.microservice.wallet.utils.MailSender;
import com.mobiloitte.microservice.wallet.utils.RandomTagGenerator;

@Service("WithdrawType3")
public class WithdrawType3Impl implements WithdrawType3, WalletConstants, OtherConstants {

	private static final Logger LOGGER = LogManager.getLogger(WithdrawType3Impl.class);

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private CoinDao coinDao;

	@Autowired
	private CoinDepositWithdrawalDao coinDepositWithdrawalDao;

	@Autowired
	private UserClient userClient;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private USDTUtil usdtUtil;

	@Override
	@Transactional
	public Response<String> withdrawBalance(WithdrawRequestDto withdrawRequest, Long fkUserId) {
		if (!Boolean.TRUE.equals(withdrawRequest.getIsWithdraw())) {
			Response<UserEmailAndNameDto> getUserNameAndEmail = userClient.getUserByUserId(fkUserId);
			UserEmailAndNameDto userStatus = getUserNameAndEmail.getData();
			if (userStatus.getUserStatus().equals(UserStatus.BLOCK)) {
				return new Response<>(FAILURE_CODE, "you are blocked by admin. You could not transaction. ");
			}
			if (getUserNameAndEmail.getStatus() == 200) {
				UserEmailAndNameDto userEmailAndNameDto = getUserNameAndEmail.getData();
				Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(withdrawRequest.getCoinName(),
						fkUserId);
				if (getWalletDetails.isPresent()) {
					if (Boolean.TRUE.equals(isHashValid(fkUserId))) {
						if (getWalletDetails.get().getWalletBalance().compareTo(withdrawRequest.getAmount()) >= 0) {
							BigDecimal getBTCBalance = getBTCBalance(getWalletDetails.get().getWalletAddress());
							LOGGER.info("BTC balance -> {}", getBTCBalance.toPlainString());
							if (getBTCBalance.compareTo(BigDecimal.valueOf(0.0001)) >= 0) {
								BigDecimal calculatedFee = getWalletDetails.get().getCoin().getWithdrawlFee();
								BigDecimal actualBalToBeTransferred = withdrawRequest.getAmount()
										.subtract(calculatedFee);
								if (actualBalToBeTransferred.compareTo(BigDecimal.ZERO) > 0) {
									CoinDepositWithdrawal getWithdrawDetails = saveWithdrawTransactionDetails(
											withdrawRequest.getToAddress(), fkUserId, calculatedFee,
											withdrawRequest.getCoinName(), withdrawRequest.getAmount(),
											userEmailAndNameDto.getName(), userEmailAndNameDto.getEmail());
									if (getWithdrawDetails != null) {
										String generateToken = RandomTagGenerator.generateRandomToken()
												+ getWithdrawDetails.getTxnId();
										Map<String, Object> sendMailData = setEmailData(generateToken,
												withdrawRequest.getUrl(), userEmailAndNameDto.getName(),
												withdrawRequest.getAmount(), withdrawRequest.getCoinName(),
												withdrawRequest.getToAddress(), userEmailAndNameDto.getEmail());
										mailSender.sendMailToApproveWithdraw(sendMailData);
										return new Response<>(SUCCESS_CODE, WITHDRAW_REQUEST_POSTED_SUCCESSFULLY);
									} else
										return new Response<>(FAILURE_CODE,
												"Unable to post withdraw request, please try later...");
								} else
									return new Response<>(FAILURE_CODE,
											"Actual balance cannot be less than / equal to ZERO.");
							} else
								return new Response<>(FAILURE_CODE,
										"You have " + getBTCBalance.toPlainString() + " BTC in your address: "
												+ getWalletDetails.get().getWalletAddress()
												+ ". You need to add 0.0001BTC to make a successful withdrawal.");
						} else
							return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
					} else
						return new Response<>(FAILURE_CODE,
								"Your last transaction Hash is still not confirmed yet, please wait util it get"
										+ "confirmed to proceed another transaction. Check Omni explorer to know the confirmations.");
				} else
					return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
			} else
				return new Response<>(FAILURE_CODE, USER_NOT_FOUND);
		} else
			return new Response<>(FAILURE_CODE, ISWITHDRAW_CANNOT_BE_TRUE_FOR_FIRST_TIME);
	}

	private BigDecimal getBTCBalance(String address) {
		try {
			return (BigDecimal) usdtUtil.getNetworkBalance(address).get("BTC_balance");
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	private BigDecimal getUSDTNetworkBalance(String address) {
		try {
			return usdtUtil.getBalanceAPI(address);
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	private CoinDepositWithdrawal saveWithdrawTransactionDetails(String toAddress, Long fkUserId,
			BigDecimal withdrawlFee, String coinName, BigDecimal units, String userName, String userEmail) {
		try {
			CoinDepositWithdrawal coinDepositWithdrawal = new CoinDepositWithdrawal();
			coinDepositWithdrawal.setAddress(toAddress);
			coinDepositWithdrawal.setFees(withdrawlFee);
			coinDepositWithdrawal.setTxnType(DIRECT_WALLET_WITHDRAW);
			coinDepositWithdrawal.setStatus(PENDING);
			coinDepositWithdrawal.setCoinType(coinName);
			coinDepositWithdrawal.setUnits(units);
			coinDepositWithdrawal.setUserName(userName);
			coinDepositWithdrawal.setUserEmail(userEmail);
			coinDepositWithdrawal.setFkUserId(fkUserId);
			coinDepositWithdrawal = coinDepositWithdrawalDao.save(coinDepositWithdrawal);

			return coinDepositWithdrawal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	private Map<String, Object> setEmailData(String token, String url, String userName, BigDecimal amount,
			String coinType, String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.APPROVE_URL, url + "/" + token);
		sendMailData.put(EmailConstants.REJECT_URL, url + "/_#" + token);
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.WITHDRAW_REQUEST);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	private Map<String, Object> setEmailDataForWithdrawSuccess(String userName, BigDecimal amount, String coinType,
			String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.WITHDRAW_SUCCESS);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	@Override
	public Response<String> transferFunds(WithdrawRequestDto withdrawRequest, Long fkUserId,
			CoinDepositWithdrawal coinDepositWithdrawal) {
		Response<UserEmailAndNameDto> getUserNameAndEmail = userClient.getUserByUserId(fkUserId);
		if (getUserNameAndEmail.getStatus() == 200) {
			Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(withdrawRequest.getCoinName(), fkUserId);
			if (getWallet.isPresent() && getWallet.get().getWalletBalance() != null) {
				Optional<Coin> getCoinDetailsByCoinName = coinDao.findByCoinShortName(withdrawRequest.getCoinName());
				if (getCoinDetailsByCoinName.isPresent()) {
					if (getWallet.get().getWalletBalance().compareTo(withdrawRequest.getAmount()) >= 0) {
						BigDecimal getUSDTNetworkAmount = getUSDTNetworkBalance(getWallet.get().getWalletAddress());
						LOGGER.info("USDT balance -> {}", getUSDTNetworkAmount.toPlainString());
						if (getUSDTNetworkAmount.compareTo(BigDecimal.ZERO) > 0
								&& withdrawRequest.getAmount().compareTo(getUSDTNetworkAmount) <= 0) {
							BigDecimal getBTCBalance = getBTCBalance(getWallet.get().getWalletAddress());
							LOGGER.info("BTC balance -> {}", getBTCBalance.toPlainString());
							if (getBTCBalance.compareTo(BigDecimal.valueOf(0.0001)) >= 0) {
								CryptoRequestModel getStorageDetails = getHotStorageDetails(getWallet.get(),
										withdrawRequest, getCoinDetailsByCoinName.get());
								CryptoResponseModel responseModel = getWithdraw(getStorageDetails,
										withdrawRequest.getCoinName());
								if (responseModel != null && responseModel.getTxnHash() != null) {
									updateWithdrawTransactionDetails(responseModel.getTxnHash(), coinDepositWithdrawal);
									updateWalletBalance(withdrawRequest.getCoinName(), fkUserId,
											withdrawRequest.getAmount());

									Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess(
											getUserNameAndEmail.getData().getName(), withdrawRequest.getAmount(),
											withdrawRequest.getCoinName(), withdrawRequest.getToAddress(),
											getUserNameAndEmail.getData().getEmail());
									mailSender.sendMailToApproveWithdrawSuccess(sendMailData);

									return new Response<>(SUCCESS_CODE, WITHDRAW_SUCCESS);
								} else {
									return new Response<>(FAILURE_CODE,
											WITHDRAW_FAILED + ". Please try again later...");
								}
							} else
								return new Response<>(FAILURE_CODE,
										"You have " + getBTCBalance.toPlainString() + " BTC in your address: "
												+ getWallet.get().getWalletAddress()
												+ ". You need to add 0.0001BTC to make a successful withdrawal.");
						} else
							return new Response<>(FAILURE_CODE,
									"Insufficient network balance: " + getUSDTNetworkAmount.toPlainString() + " "
											+ getCoinDetailsByCoinName.get().getCoinShortName());
					} else {
						return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
					}
				} else {
					return new Response<>(FAILURE_CODE, NO_SUCH_COIN_FOUND);
				}
			} else {
				return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
			}
		} else
			return new Response<>(FAILURE_CODE, USER_NOT_FOUND);
	}

	private CryptoRequestModel getHotStorageDetails(Wallet walletDetails, WithdrawRequestDto withdrawRequest,
			Coin coinDetails) {
		if (walletDetails != null && withdrawRequest != null) {
			CryptoRequestModel cryptoRequestModel = new CryptoRequestModel();
			cryptoRequestModel.setFromAddress(walletDetails.getWalletAddress());
			cryptoRequestModel.setToAddress(withdrawRequest.getToAddress());
			BigDecimal amountToSend = withdrawRequest.getAmount().subtract(coinDetails.getWithdrawlFee());
			cryptoRequestModel.setAmount(amountToSend);
			cryptoRequestModel.setStorageType(STORAGE_HOT);
			if (walletDetails.getWalletFileName() != null) {
				cryptoRequestModel.setWalletFile(walletDetails.getWalletFileName());
			}
			return cryptoRequestModel;
		} else {
			return null;
		}
	}

	private CryptoResponseModel getWithdraw(CryptoRequestModel cryptoRequestModel, String coinName) {
		CryptoResponseModel cryptoResponseModel = null;
		switch (coinName) {
		case USDT:
			cryptoResponseModel = usdtUtil.withdrawAPI(cryptoRequestModel);
			break;
		default:
			cryptoResponseModel = null;
			break;
		}
		return cryptoResponseModel;
	}

	private CoinDepositWithdrawal updateWithdrawTransactionDetails(String txnHash,
			CoinDepositWithdrawal coinDepositWithdrawal) {
		try {
			if (coinDepositWithdrawal.getTxnHash() == null && coinDepositWithdrawal.getStatus().equals(PENDING)) {
				coinDepositWithdrawal.setTxnHash(txnHash);
				coinDepositWithdrawal.setStatus(CONFIRM);
				coinDepositWithdrawal = coinDepositWithdrawalDao.save(coinDepositWithdrawal);
			}
			return coinDepositWithdrawal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	private Wallet updateWalletBalance(String coinName, Long fkUserId, BigDecimal balance) {
		try {
			Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if (getWalletDetails.isPresent()) {
				Wallet wallet = null;
				getWalletDetails.get().setWalletBalance(getWalletDetails.get().getWalletBalance().subtract(balance));
				wallet = walletDao.save(getWalletDetails.get());
				return wallet;
			} else {
				throw new WalletNotFoundException(
						"No wallet found for coin: " + coinName + " and userId: " + fkUserId + "");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	private Boolean isHashValid(Long userId) {
		try {
			List<CoinDepositWithdrawal> getTxnRecords = coinDepositWithdrawalDao
					.findByCoinTypeAndTxnTypeAndFkUserIdOrderByTxnIdDesc(USDT, DIRECT_WALLET_WITHDRAW, userId);
			if (getTxnRecords.isEmpty())
				return true;
			if (getTxnRecords.get(0).getTxnHash() == null || getTxnRecords.get(0).getTxnHash().equals(""))
				return true;
			String lastHash = getTxnRecords.get(0).getTxnHash();
			Boolean isConfirmed = usdtUtil.isTxnHashValid(lastHash);
			return (Boolean.TRUE.equals(isConfirmed)) ? returnBoolean(true) : returnBoolean(false);
		} catch (Exception e) {
			return true;
		}
	}

	private Boolean returnBoolean(Boolean value) {
		return value;
	}
}
