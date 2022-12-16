package com.mobiloitte.microservice.wallet.serviceimpl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.constants.EmailConstants;
import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.cryptocoins.ETHUtil;
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
import com.mobiloitte.microservice.wallet.service.WithdrawType2;
import com.mobiloitte.microservice.wallet.utils.MailSender;
import com.mobiloitte.microservice.wallet.utils.RandomTagGenerator;

@Service("WithdrawType2")
public class WithdrawType2Impl implements WithdrawType2, WalletConstants, OtherConstants {

	private static final Logger LOGGER = LogManager.getLogger(WithdrawType2Impl.class);

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
	private ETHUtil ethUtil;


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
					if (getWalletDetails.get().getWalletBalance().compareTo(withdrawRequest.getAmount()) >= 0) {
						BigDecimal getETHBalance = getEtherBalance(getWalletDetails.get().getWalletAddress());
						if (getETHBalance.compareTo(BigDecimal.valueOf(0.01)) >= 0) {
							BigDecimal calculatedFee = getWalletDetails.get().getCoin().getWithdrawlFee();
							BigDecimal actualBalToBeTransferred = withdrawRequest.getAmount().subtract(calculatedFee);
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
											"Unable to post withdraw request, some error occured...");
							} else
								return new Response<>(FAILURE_CODE,
										"Actual balance cannot be less than / equal to ZERO.");
						} else
							return new Response<>(FAILURE_CODE,
									"You have " + getETHBalance.toPlainString() + " ETH in your address: "
											+ getWalletDetails.get().getWalletAddress()
											+ ". You need to add 0.01ETH to make a successful withdrawal.");
					} else
						return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
				} else
					return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
			} else
				return new Response<>(FAILURE_CODE, USER_NOT_FOUND);
		} else
			return new Response<>(FAILURE_CODE, ISWITHDRAW_CANNOT_BE_TRUE_FOR_FIRST_TIME);
	}

	private BigDecimal getEtherBalance(String address) {
		try {
			return ethUtil.getBalanceAPI(address);
		} catch (Exception e) {
			return BigDecimal.ZERO;
		}
	}

	private BigDecimal getERC20Balance(String address, String coinName) {
		try {
			if (coinName.equals(OMG))
//				return omgUtil.getBalanceAPI(address);
				return null;	
			else
				return BigDecimal.ZERO;
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
						BigDecimal getERC20Amount = getERC20Balance(getWallet.get().getWalletAddress(),
								getCoinDetailsByCoinName.get().getCoinShortName());
						if (getERC20Amount.compareTo(BigDecimal.ZERO) > 0
								&& withdrawRequest.getAmount().compareTo(getERC20Amount) <= 0) {
							BigDecimal getETHBalance = getEtherBalance(getWallet.get().getWalletAddress());
							LOGGER.info("ETH balance -> {}", getETHBalance.toPlainString());
							if (getETHBalance.compareTo(BigDecimal.valueOf(0.01)) >= 0) {
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
									return new Response<>(FAILURE_CODE, WITHDRAW_FAILED);
								}
							} else
								return new Response<>(FAILURE_CODE,
										"You have " + getETHBalance.toPlainString() + " ETH in your address: "
												+ getWallet.get().getWalletAddress()
												+ ". You need to add 0.01ETH to make a successful withdrawal.");
						} else
							return new Response<>(FAILURE_CODE,
									"Insufficient network balance: " + getERC20Amount.toPlainString() + " "
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

	private CryptoResponseModel getWithdraw(CryptoRequestModel cryptoRequestModel, String coinName) {
		CryptoResponseModel cryptoResponseModel = null;
		switch (coinName) {
		/*
		 * case USDT: cryptoResponseModel = usdtUtil.withdrawAPI(cryptoRequestModel);
		 * break;
		 */
	

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

	@Override
	@Transactional
	public Response<String> transferFundsForTaggedCoins(WithdrawRequestDto withdrawRequest, Long fkUserId,
			CoinDepositWithdrawal coinDepositWithdrawal, Coin coinDetails) {
		Optional<Wallet> fromUserWallet = walletDao.findByCoinNameAndFkUserId(withdrawRequest.getCoinName(), fkUserId);
		if (!fromUserWallet.isPresent())
			return new Response<>(FAILURE_CODE, FAILURE,
					"From user " + withdrawRequest.getCoinName() + " wallet not found.");
		Optional<Wallet> toUserWallet = walletDao.findByWalletAddressAndTag(withdrawRequest.getToAddress(),
				withdrawRequest.getTag());
		if (!toUserWallet.isPresent())
			return new Response<>(FAILURE_CODE, FAILURE,
					"No user with " + withdrawRequest.getCoinName() + " wallet having address: "
							+ withdrawRequest.getToAddress() + " and tag: " + withdrawRequest.getTag() + " not found.");
		if (fromUserWallet.get().getWalletBalance().compareTo(withdrawRequest.getAmount()) >= 0) {
			BigDecimal amountToSend = withdrawRequest.getAmount().subtract(coinDetails.getWithdrawlFee());
			String txnHash = generateUniqueTxnIds(withdrawRequest.getCoinName(), fkUserId);
			updateWithdrawTransactionDetails(txnHash, coinDepositWithdrawal);
			saveDepositsForToUser(txnHash, fromUserWallet.get().getWalletAddress(), toUserWallet.get().getFkUserId(),
					withdrawRequest.getCoinName(), amountToSend);
			updateWalletBalanceForFromUser(fromUserWallet.get(), amountToSend);
			updateWalletBalanceForToUser(toUserWallet.get(), amountToSend);
			return new Response<>(SUCCESS_CODE, WITHDRAW_SUCCESS);
		} else
			return new Response<>(FAILURE_CODE, WITHDRAW_FAILED);
	}

	private String generateUniqueTxnIds(String coinName, Long fkUserId) {
		return coinName.toLowerCase() + fkUserId + RandomStringUtils.randomAlphanumeric(10) + "-"
				+ new Date().getTime();
	}

	private Wallet updateWalletBalanceForFromUser(Wallet wallet, BigDecimal balance) {
		try {
			wallet.setWalletBalance(wallet.getWalletBalance().subtract(balance));
			wallet = walletDao.save(wallet);
			return wallet;

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	private Wallet updateWalletBalanceForToUser(Wallet wallet, BigDecimal balance) {
		try {
			wallet.setWalletBalance(wallet.getWalletBalance().add(balance));
			wallet = walletDao.save(wallet);
			return wallet;

		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	private CoinDepositWithdrawal saveDepositsForToUser(String hash, String fromAddr, Long fkUserId, String coinName,
			BigDecimal amount) {
		try {
			CoinDepositWithdrawal coinDepositWithdrawal = null;
			if (hash != null) {
				coinDepositWithdrawal = new CoinDepositWithdrawal();
				coinDepositWithdrawal.setTxnHash(hash);
				coinDepositWithdrawal.setUnits(amount);
				coinDepositWithdrawal.setAddress(fromAddr);
				coinDepositWithdrawal.setFkUserId(fkUserId);
				coinDepositWithdrawal.setStatus(CONFIRM);
				coinDepositWithdrawal.setCoinType(coinName);
				coinDepositWithdrawal.setTxnType(DEPOSIT);
				coinDepositWithdrawal = coinDepositWithdrawalDao.save(coinDepositWithdrawal);
			}
			return coinDepositWithdrawal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

}
