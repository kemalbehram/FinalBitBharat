package com.mobiloitte.microservice.wallet.serviceimpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.boot.json.JsonParseException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.mobiloitte.microservice.wallet.constants.EmailConstants;
import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.cryptocoins.AVAXUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.BEPUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.BNBUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.BTCUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.ERCUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.ETHUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.MATICUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.POLKADOTUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.SOLANAUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.TRXUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.USDTUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.XRPUtil;
import com.mobiloitte.microservice.wallet.dao.AmountDao;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.CoinDepositWithdrawalDao;
import com.mobiloitte.microservice.wallet.dao.FiatDao;
import com.mobiloitte.microservice.wallet.dao.LimitDataDao;
import com.mobiloitte.microservice.wallet.dao.MarketPriceDao;
import com.mobiloitte.microservice.wallet.dao.SmallAssetDao;
import com.mobiloitte.microservice.wallet.dao.StorageDetailsDao;
import com.mobiloitte.microservice.wallet.dao.WalletDao;
import com.mobiloitte.microservice.wallet.dto.AdminAddressDto;
import com.mobiloitte.microservice.wallet.dto.ApproveWithdrawRequestDto;
import com.mobiloitte.microservice.wallet.dto.EmailDto;
import com.mobiloitte.microservice.wallet.dto.FiatWithdrawRequestDto;
import com.mobiloitte.microservice.wallet.dto.HotToColdTransferDto;
import com.mobiloitte.microservice.wallet.dto.SearchAndFilterCoinWithdrawlDepositDto;
import com.mobiloitte.microservice.wallet.dto.TransactionListDto;
import com.mobiloitte.microservice.wallet.dto.UserEmailAndNameDto;
import com.mobiloitte.microservice.wallet.dto.WithdrawRequestDto;
import com.mobiloitte.microservice.wallet.dto.WithdrawRequestDtoUpdated;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.entities.DepositInrAmount;
import com.mobiloitte.microservice.wallet.entities.LimitData;
import com.mobiloitte.microservice.wallet.entities.MarketPrice;
import com.mobiloitte.microservice.wallet.entities.SmallAssetHistory;
import com.mobiloitte.microservice.wallet.entities.StorageDetail;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.enums.Network;
import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.exception.StorageDetailsNotFoundException;
import com.mobiloitte.microservice.wallet.exception.WalletNotFoundException;
import com.mobiloitte.microservice.wallet.feign.NotificationClient;
import com.mobiloitte.microservice.wallet.feign.UserClient;
import com.mobiloitte.microservice.wallet.model.CryptoRequestModel;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.HotColdManagementService;
import com.mobiloitte.microservice.wallet.service.WithdrawType2;
import com.mobiloitte.microservice.wallet.service.WithdrawType3;
import com.mobiloitte.microservice.wallet.utils.APIUtils;
import com.mobiloitte.microservice.wallet.utils.MailSender;
import com.mobiloitte.microservice.wallet.utils.RandomTagGenerator;

/**
 * The Class HotColdManagementServiceImpl.
 * 
 * @author Ankush Mohapatra
 */
@Service("HotColdManagementService")
public class HotColdManagementServiceImpl implements HotColdManagementService, WalletConstants, OtherConstants {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(HotColdManagementServiceImpl.class);
	/** The wallet dao. */
	@Autowired
	private WalletDao walletDao;

	@Autowired
	private EntityManager em;

	/** The coin dao. */
	@Autowired
	private CoinDao coinDao;

	/** The storage details dao. */
	@Autowired
	private StorageDetailsDao storageDetailsDao;
	public static final int TIMEOUT = 10 * 10000;
	/** The coin deposit withdrawal dao. */
	@Autowired
	private CoinDepositWithdrawalDao coinDepositWithdrawalDao;

	@Autowired
	private WithdrawType2 withdrawType2;

	@Autowired
	private WithdrawType3 withdrawType3;

	/** The user client. */
	@Autowired
	private UserClient userClient;
	@Autowired
	private NotificationClient notificationClient;

	@Value("${isNotification}")
	private Boolean isNotification;

	/** The mail sender. */
	@Autowired
	private MailSender mailSender;

	@Autowired
	private LimitDataDao limitDataDao;

	@Autowired
	private SmallAssetDao smallAssetDao;

	@Autowired
	private MarketPriceDao marketPriceDao;

	@Autowired
	private FiatDao fiatDao;
	/** The btc util. */
	@Autowired
	private BTCUtil btcUtil;

	/** The eth util. */
	@Autowired
	private ETHUtil ethUtil;

	/** The xrp util. */
	@Autowired
	private XRPUtil xrpUtil;

	@Autowired
	private USDTUtil usdtUtil;
	@Autowired
	private AmountDao amountdao;

	@Autowired
	private SOLANAUtil solanaUtil;

	@Autowired
	private TRXUtil trxUtil;

	@Autowired
	private BNBUtil bnbUtil;

	@Autowired
	private MATICUtil maticUtil;

	@Autowired
	private POLKADOTUtil polkadotUtil;

	@Autowired
	private AVAXUtil avaxUtil;

	@Autowired
	private BEPUtil bepUtil;

	@Autowired
	private ERCUtil ercUtil;

	@Override
	@Transactional
	public Response<Map<String, Object>> deposit(String coinName, Long fkUserId, Integer page, Integer pageSize,
			String email) {
		Map<String, Object> responseMap = new HashMap<>();
		Response<UserEmailAndNameDto> adminEmail = userClient.getUserByUserId(fkUserId);
		if (adminEmail.getStatus() == 200 && adminEmail.getData() != null) {
			Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if (getWallet.isPresent() && getWallet.get().getWalletAddress() != null) {
				BigDecimal getLiveBalance = getLiveBalance(coinName, getWallet.get());
				if (getLiveBalance.compareTo(BigDecimal.ZERO) > 0) {
					CryptoRequestModel cryptoRequestModel = getStorageDetailsByLimit(coinName);
					if (cryptoRequestModel != null) {
						cryptoRequestModel.setFromAddress(getWallet.get().getWalletAddress());

						if (getWallet.get().getWalletFileName() != null) {
							cryptoRequestModel.setWalletFile(getWallet.get().getWalletFileName());
						}
						if (getWallet.get().getEosAccountName() != null) {
							cryptoRequestModel.setFromEosAccount(getWallet.get().getEosAccountName());
						}
						if (getWallet.get().getHexAddress() != null) {
							cryptoRequestModel.setFromHexAddress(getWallet.get().getHexAddress());
						}
						if (getWallet.get().getWif() != null) {
							cryptoRequestModel.setWif(getWallet.get().getWif());
						}

						cryptoRequestModel.setAmount(getLiveBalance);
						CryptoResponseModel cryptoResponseModel = getInternalTransfer(cryptoRequestModel, coinName);

						cryptoResponseModel.setActualAmount(getLiveBalance);

						if (cryptoResponseModel != null && cryptoResponseModel.getTxnHash() != null) {
							saveTransactionDetails(cryptoResponseModel, fkUserId, null, adminEmail.getData());
							updateWalletBalance(coinName, fkUserId, cryptoResponseModel.getActualAmount(), DEPOSIT);
							updateStorageBalance(coinName, cryptoRequestModel.getStorageType(),
									cryptoResponseModel.getActualAmount(), DEPOSIT);

							List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao
									.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
											PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
							Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName,
									DEPOSIT, fkUserId);
							responseMap.put(RESULT_LIST, getTransactionList);
							responseMap.put(TOTAL_COUNT, getTotalCount);

							// send mail of deposit success
							mailSender.sendMailForDepositConfirmation(
									setDepositConfirmationMailObj(adminEmail.getData().getEmail(), coinName,
											cryptoResponseModel.getActualAmount().toPlainString()));
							if (isNotification.equals(Boolean.TRUE)) {
								Map<String, String> setData = new HashMap<>();
								setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
								setData.put(EmailConstants.COIN_TOKEN, coinName);
								setData.put(EmailConstants.AMOUNT_TOKEN,
										String.valueOf(cryptoResponseModel.getActualAmount()));
								EmailDto emailDto = new EmailDto(fkUserId, "deposit", "en", email, setData);
								notificationClient.sendNotification(emailDto);
							}
							return new Response<>(SUCCESS_CODE, DEPOSIT_SUCCESS, responseMap);
						} else {
							List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao
									.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
											PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
							Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName,
									DEPOSIT, fkUserId);
							responseMap.put(RESULT_LIST, getTransactionList);
							responseMap.put(TOTAL_COUNT, getTotalCount);
							return new Response<>(FAILURE_CODE, DEPOSIT_FAILED, responseMap);
						}
					} else {
						List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao
								.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
										PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
						Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName,
								DEPOSIT, fkUserId);
						responseMap.put(RESULT_LIST, getTransactionList);
						responseMap.put(TOTAL_COUNT, getTotalCount);
						return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND, responseMap);

					}
				} else {
					List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao
							.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
									PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
					Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName,
							DEPOSIT, fkUserId);
					responseMap.put(RESULT_LIST, getTransactionList);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, NO_NEW_DEPOSITS_FOUND, responseMap);
				}
			} else {
				return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
			}
		} else
			return new Response<>(FAILURE_CODE, USER_NOT_FOUND);
	}

	/**
	 * Gets the storage details by limit.
	 *
	 * @param coinName the coin name
	 * @return the storage details by limit
	 */
	private CryptoRequestModel getStorageDetailsByLimit(String coinName) {
		Optional<StorageDetail> getHotStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(coinName,
				STORAGE_HOT);
		CryptoRequestModel cryptoRequestModel = null;
		if (getHotStorageDetails.isPresent()) {
			if (getHotStorageDetails.get().getHotWalletBalance()
					.compareTo(getHotStorageDetails.get().getCoinLimit()) > 0) {
				Optional<StorageDetail> getColdStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(coinName,
						STORAGE_COLD);
				if (getColdStorageDetails.isPresent()) {
					cryptoRequestModel = new CryptoRequestModel();
					cryptoRequestModel.setToAddress(getColdStorageDetails.get().getAddress());
					if (getColdStorageDetails.get().getWalletFile() != null) {
						cryptoRequestModel.setWalletFile(getColdStorageDetails.get().getWalletFile());
					}
					if (getColdStorageDetails.get().getWalletPassword() != null) {
						cryptoRequestModel.setWalletPassword(getColdStorageDetails.get().getWalletPassword());
					}
					if (getColdStorageDetails.get().getHexAddress() != null) {
						cryptoRequestModel.setToHexAddress(getColdStorageDetails.get().getHexAddress());
					}
					cryptoRequestModel.setStorageType(STORAGE_COLD);
				}
			} else {
				cryptoRequestModel = new CryptoRequestModel();
				cryptoRequestModel.setToAddress(getHotStorageDetails.get().getAddress());
				if (getHotStorageDetails.get().getWalletFile() != null) {
					cryptoRequestModel.setWalletFile(getHotStorageDetails.get().getWalletFile());
				}
				if (getHotStorageDetails.get().getWalletPassword() != null) {
					cryptoRequestModel.setWalletPassword(getHotStorageDetails.get().getWalletPassword());
				}
				if (getHotStorageDetails.get().getHexAddress() != null) {
					cryptoRequestModel.setToHexAddress(getHotStorageDetails.get().getHexAddress());
				}
				if (getHotStorageDetails.get().getWif() != null) {
					cryptoRequestModel.setWif(getHotStorageDetails.get().getWif());
				}

				cryptoRequestModel.setStorageType(STORAGE_HOT);
			}
		} else {
			return null;
		}
		return cryptoRequestModel;
	}

	/**
	 * Save transaction details.
	 *
	 * @param responseModel the response model
	 * @param fkUserId      the fk user id
	 * @param withdrawlFee  the withdrawl fee
	 * @return the coin deposit withdrawal
	 */
	private CoinDepositWithdrawal saveTransactionDetails(CryptoResponseModel responseModel, Long fkUserId,
			BigDecimal withdrawlFee, UserEmailAndNameDto userEmailAndNameDto) {
		try {
			CoinDepositWithdrawal coinDepositWithdrawal = null;
			if (responseModel != null && responseModel.getTxnHash() != null) {
				coinDepositWithdrawal = new CoinDepositWithdrawal();
				if (responseModel.getToAddress() != null) {
					coinDepositWithdrawal.setAddress(responseModel.getToAddress());
				}
				coinDepositWithdrawal.setTxnHash(responseModel.getTxnHash());
				if (withdrawlFee != null) {
					coinDepositWithdrawal.setFees(withdrawlFee);
				}
				coinDepositWithdrawal.setTxnType(responseModel.getTxnType());
				coinDepositWithdrawal.setStatus(CONFIRM);
				coinDepositWithdrawal.setCoinType(responseModel.getCoinType());
				coinDepositWithdrawal.setUnits(responseModel.getActualAmount());
				coinDepositWithdrawal.setFkUserId(fkUserId);
				coinDepositWithdrawal.setUserName(userEmailAndNameDto.getName());
				coinDepositWithdrawal.setUserEmail(userEmailAndNameDto.getEmail());
				coinDepositWithdrawal.setLivePrice(responseModel.getLivePrice());
				coinDepositWithdrawal.setDepositeCurrentPrice(responseModel.getDepositeCurrentPrice());
				coinDepositWithdrawal = coinDepositWithdrawalDao.save(coinDepositWithdrawal);
			}
			return coinDepositWithdrawal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	/**
	 * Update wallet balance.
	 *
	 * @param coinName    the coin name
	 * @param fkUserId    the fk user id
	 * @param balance     the balance
	 * @param paymentType the payment type
	 * @return the wallet
	 */
	private Wallet updateWalletBalance(String coinName, Long fkUserId, BigDecimal balance, String paymentType) {
		try {

			Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if (getWalletDetails.isPresent()) {
				Wallet wallet = null;
				if (paymentType.equalsIgnoreCase(DEPOSIT)) {
					getWalletDetails.get().setWalletBalance(getWalletDetails.get().getWalletBalance().add(balance));
					getWalletDetails.get().setInvestAmount(getWalletDetails.get().getInvestAmount().add(balance));

				} else if (paymentType.equalsIgnoreCase(WITHDRAW)) {
					getWalletDetails.get()
							.setWalletBalance(getWalletDetails.get().getWalletBalance().subtract(balance));
				}
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

	/**
	 * Update storage balance.
	 *
	 * @param coinName    the coin name
	 * @param storageType the storage type
	 * @param balance     the balance
	 * @param paymentType the payment type
	 * @return the storage detail
	 */
	private StorageDetail updateStorageBalance(String coinName, String storageType, BigDecimal balance,
			String paymentType) {
		try {
			Optional<StorageDetail> getStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(coinName,
					storageType);
			if (getStorageDetails.isPresent()) {
				StorageDetail storageDetail = null;
				if (paymentType.equalsIgnoreCase(DEPOSIT)) {
					getStorageDetails.get()
							.setHotWalletBalance(getStorageDetails.get().getHotWalletBalance().add(balance));
				} else if (paymentType.equalsIgnoreCase(WITHDRAW)) {
					getStorageDetails.get()
							.setHotWalletBalance(getStorageDetails.get().getHotWalletBalance().subtract(balance));
				}
				storageDetail = storageDetailsDao.save(getStorageDetails.get());
				return storageDetail;
			} else {
				throw new StorageDetailsNotFoundException(
						"No Storage found for the coin: " + coinName + "and storage type: " + storageType + "");
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	/**
	 * Gets the live balance.
	 *
	 * @param coinName  the coin name
	 * @param getWallet the get wallet
	 * @return the live balance
	 */
	private BigDecimal getLiveBalance(String coinName, Wallet getWallet) {
		BigDecimal getLiveBalanceByCoin = null;
		switch (coinName) {
		case BTC:
			getLiveBalanceByCoin = btcUtil.getBalanceAPI(getWallet.getWalletAddress());
			break;

		case SOLANA:
			getLiveBalanceByCoin = solanaUtil.getBalanceAPI(getWallet.getWalletAddress());
			break;

		case ETH:
			getLiveBalanceByCoin = ethUtil.getBalanceAPI(getWallet.getWalletAddress());
			break;

		case USDT:
			getLiveBalanceByCoin = usdtUtil.getBalanceAPI(getWallet.getWalletAddress());
			break;
		case TRX:
			getLiveBalanceByCoin = trxUtil.getBalanceAPI(getWallet.getWalletAddress());
			break;

		case BNB:
			getLiveBalanceByCoin = bnbUtil.getBalanceAPI(getWallet.getWalletAddress());
			break;
		case MATIC:
			getLiveBalanceByCoin = maticUtil.getBalanceAPI(getWallet.getWalletAddress());
			break;
		case POLKADOT:
			getLiveBalanceByCoin = polkadotUtil.getBalanceAPI(getWallet.getWalletAddress());
			break;
		case AVAX:
			getLiveBalanceByCoin = avaxUtil.getBalanceAPI(getWallet.getWalletAddress());
			break;
		default:
			getLiveBalanceByCoin = DEFAULT_BALANCE;
			break;
		}
		return getLiveBalanceByCoin;
	}

	/**
	 * Gets the internal transfer.
	 *
	 * @param cryptoRequestModel the crypto request model
	 * @param coinName           the coin name
	 * @return the internal transfer
	 */
	private CryptoResponseModel getInternalTransfer(CryptoRequestModel cryptoRequestModel, String coinName) {
		CryptoResponseModel cryptoResponseModel = null;
		switch (coinName) {
		case BTC:
			cryptoResponseModel = btcUtil.internalTransferAPI(cryptoRequestModel);
			break;

		case SOLANA:
			cryptoResponseModel = solanaUtil.internalTransferAPI(cryptoRequestModel);
			break;

		case ETH:
			cryptoResponseModel = ethUtil.internalTransferAPI(cryptoRequestModel);
			break;

		case USDT:
			cryptoResponseModel = usdtUtil.internalTransferAPI(cryptoRequestModel);
			break;
		case TRX:
			cryptoResponseModel = trxUtil.internalTransferAPI(cryptoRequestModel);
			break;

		case BNB:
			cryptoResponseModel = bnbUtil.internalTransferAPI(cryptoRequestModel);
			break;
		case MATIC:
			cryptoResponseModel = maticUtil.internalTransferAPI(cryptoRequestModel);
			break;
		case POLKADOT:
			cryptoResponseModel = polkadotUtil.internalTransferAPI(cryptoRequestModel);
			break;
		case AVAX:
			cryptoResponseModel = avaxUtil.internalTransferAPI(cryptoRequestModel);
			break;

		// to do ...
		default:
			cryptoResponseModel = null;
			break;
		}
		return cryptoResponseModel;
	}

	@Override
	@Transactional
	public Response<String> withdrawBalance(WithdrawRequestDto withdrawRequest, Long fkUserId) {
		if (!withdrawRequest.getIsWithdraw()) {

			Response<UserEmailAndNameDto> getUserNameAndEmail = userClient.getUserByUserId(fkUserId);
			if (getUserNameAndEmail.getStatus() == 200) {
				UserEmailAndNameDto userEmailAndNameDto = getUserNameAndEmail.getData();
				Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(withdrawRequest.getCoinName(),
						fkUserId);
				if (getWallet.isPresent() && getWallet.get().getWalletBalance() != null) {
					Optional<Coin> getCoinDetailsByCoinName = coinDao
							.findByCoinShortName(withdrawRequest.getCoinName());
					if (getCoinDetailsByCoinName.isPresent()) {
						if (getWallet.get().getWalletBalance().compareTo(withdrawRequest.getAmount()) >= 0) {
							Optional<StorageDetail> getHotStorageDetails = storageDetailsDao
									.findByCoinTypeAndStorageType(withdrawRequest.getCoinName(), STORAGE_HOT);
							if (getHotStorageDetails.isPresent()) {
								if (getHotStorageDetails.get().getHotWalletBalance()
										.compareTo(withdrawRequest.getAmount()) > 0) {
									BigDecimal calculatedFee = getCoinDetailsByCoinName.get().getWithdrawlFee();

									CoinDepositWithdrawal getWithdrawDetails = saveWithdrawTransactionDetails(
											withdrawRequest.getToAddress(), fkUserId, calculatedFee,
											withdrawRequest.getCoinName(), withdrawRequest.getAmount(),
											withdrawRequest.getTag(), userEmailAndNameDto.getName(),
											userEmailAndNameDto.getEmail(), withdrawRequest.getIsExternal());

									if (getWithdrawDetails != null) {
										String generateToken = RandomTagGenerator.generateRandomToken()
												+ getWithdrawDetails.getTxnId();
										Map<String, Object> sendMailData = setEmailData(generateToken,
												withdrawRequest.getUrl(), userEmailAndNameDto.getName(),
												withdrawRequest.getAmount(), withdrawRequest.getCoinName(),
												withdrawRequest.getToAddress(), userEmailAndNameDto.getEmail());
										mailSender.sendMailToApproveWithdraw(sendMailData);
										if (isNotification) {
											Map<String, String> setData = new HashMap<>();
											setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
											setData.put(EmailConstants.COIN_TOKEN, withdrawRequest.getCoinName());
											setData.put(EmailConstants.AMOUNT_TOKEN,
													String.valueOf(withdrawRequest.getAmount()));
											setData.put(EmailConstants.COIN_ADDRESS_TOKEN,
													withdrawRequest.getToAddress());
											EmailDto emailDto = new EmailDto(fkUserId, "withdraw_confirmation_request",
													"en", "email", setData);
											notificationClient.sendNotification(emailDto);
										}
										return new Response<>(SUCCESS_CODE, WITHDRAW_REQUEST_POSTED_SUCCESSFULLY);
									}
									return new Response<>(SUCCESS_CODE, WITHDRAW_REQUEST_POSTED_SUCCESSFULLY);
								} else {
									return new Response<>(WITHDRAW_FAILURE_CODE, INSUFFICIENT_STORAGE_BALANCE);
								}
							} else {
								return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
							}
						} else {
							return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
						}
					} else {
						return new Response<>(FAILURE_CODE, NO_SUCH_COIN_FOUND);
					}
				} else {
					return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
				}
			} else {
				return new Response<>(FAILURE_CODE, USER_NOT_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, ISWITHDRAW_CANNOT_BE_TRUE_FOR_FIRST_TIME);
		}

	}

	/**
	 * Transfer funds.
	 *
	 * @param withdrawRequest       the withdraw request
	 * @param fkUserId              the fk user id
	 * @param coinDepositWithdrawal the coin deposit withdrawal
	 * @return the response
	 */

	private Response<String> transferFunds(WithdrawRequestDto withdrawRequest, Long fkUserId,
			CoinDepositWithdrawal coinDepositWithdrawal) {
		Response<UserEmailAndNameDto> adminEmail = userClient.getUserByUserId(fkUserId);
		if (adminEmail.getStatus() == 200 && adminEmail.getData() != null) {
			Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(withdrawRequest.getCoinName(), fkUserId);
			if (getWallet.isPresent() && getWallet.get().getWalletBalance() != null) {
				Optional<Coin> getCoinDetailsByCoinName = coinDao.findByCoinShortName(withdrawRequest.getCoinName());
				if (getCoinDetailsByCoinName.isPresent()) {
					if (getWallet.get().getWalletBalance().compareTo(withdrawRequest.getAmount()) >= 0) {
						Optional<StorageDetail> getHotStorageDetails = storageDetailsDao
								.findByCoinTypeAndStorageType(withdrawRequest.getCoinName(), STORAGE_HOT);
						if (getHotStorageDetails.isPresent()) {
							if (getHotStorageDetails.get().getHotWalletBalance()
									.compareTo(withdrawRequest.getAmount()) > 0) {
								CryptoRequestModel getStorageDetails = getHotStorageDetails(getHotStorageDetails.get(),
										withdrawRequest, getCoinDetailsByCoinName.get());
								if (getStorageDetails != null) {
									CryptoResponseModel responseModel = getWithdraw(getStorageDetails,
											withdrawRequest.getCoinName());
									if (responseModel != null && responseModel.getTxnHash() != null) {
										updateWithdrawTransactionDetails(responseModel.getTxnHash(),
												coinDepositWithdrawal);
										updateWalletBalance(withdrawRequest.getCoinName(), fkUserId,
												withdrawRequest.getAmount(), WITHDRAW);

										updateStorageBalance(withdrawRequest.getCoinName(),
												responseModel.getStorageType(), responseModel.getActualAmount(),
												WITHDRAW);

										Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess(
												adminEmail.getData().getName(), withdrawRequest.getAmount(),
												withdrawRequest.getCoinName(), withdrawRequest.getToAddress(),
												adminEmail.getData().getEmail());
										mailSender.sendMailToApproveWithdrawSuccess(sendMailData);
										if (isNotification) {
											Map<String, String> setData = new HashMap<>();
											setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
											setData.put(EmailConstants.COIN_TOKEN, withdrawRequest.getCoinName());
											setData.put(EmailConstants.AMOUNT_TOKEN,
													String.valueOf(withdrawRequest.getAmount()));
											setData.put(EmailConstants.COIN_ADDRESS_TOKEN,
													withdrawRequest.getToAddress());
											EmailDto emailDto = new EmailDto(fkUserId, "withdraw_request_success", "en",
													"email", setData);
											notificationClient.sendNotification(emailDto);
										}
										return new Response<>(SUCCESS_CODE, WITHDRAW_SUCCESS);
									} else {
										return new Response<>(FAILURE_CODE, WITHDRAW_FAILED);
									}
								} else {
									return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
								}
							} else {
								mailSender.sendMailForLowStorageBalanceAlert(setDepositConfirmationMailObj(
										adminEmail.getData().getEmail(), withdrawRequest.getCoinName(),
										getHotStorageDetails.get().getHotWalletBalance().toPlainString()));
								return new Response<>(WITHDRAW_FAILURE_CODE, INSUFFICIENT_STORAGE_BALANCE);
							}
						} else {
							return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
						}
					} else {
						return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
					}
				}

			} else {
				return new Response<>(FAILURE_CODE, NO_SUCH_COIN_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
		}
		return null;
	}

	/**
	 * Save withdraw transaction details.
	 *
	 * @param toAddress    the to address
	 * @param fkUserId     the fk user id
	 * @param withdrawlFee the withdrawl fee
	 * @param coinName     the coin name
	 * @param units        the units
	 * @param toTag        the to tag
	 * @param boolean1
	 * @return the coin deposit withdrawal
	 */
	private CoinDepositWithdrawal saveWithdrawTransactionDetails(String toAddress, Long fkUserId,
			BigDecimal withdrawlFee, String coinName, BigDecimal units, String toTag, String userName, String userEmail,
			Boolean boolean1) {
		try {
			CoinDepositWithdrawal coinDepositWithdrawal = new CoinDepositWithdrawal();
			coinDepositWithdrawal.setAddress(toAddress);
			coinDepositWithdrawal.setFees(withdrawlFee);
			coinDepositWithdrawal.setTxnType(WITHDRAW);
			coinDepositWithdrawal.setStatus(PENDING);
			coinDepositWithdrawal.setCoinType(coinName);
			coinDepositWithdrawal.setUnits(units);
			coinDepositWithdrawal.setIsExternal(boolean1);
			coinDepositWithdrawal.setUserName(userName);
			coinDepositWithdrawal.setUserEmail(userEmail);
			coinDepositWithdrawal.setFkUserId(fkUserId);
			if (toTag != null) {
				coinDepositWithdrawal.setToTag(toTag);
			}
			coinDepositWithdrawal = coinDepositWithdrawalDao.save(coinDepositWithdrawal);

			return coinDepositWithdrawal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
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

	/**
	 * Update withdraw transaction details.
	 *
	 * @param txnHash               the txn hash
	 * @param coinDepositWithdrawal the coin deposit withdrawal
	 * @return the coin deposit withdrawal
	 */
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

	/**
	 * Sets the withdraw request.
	 *
	 * @param coinDepositWithdrawal the coin deposit withdrawal
	 * @return the withdraw request dto
	 */
	private WithdrawRequestDto setWithdrawRequest(CoinDepositWithdrawal coinDepositWithdrawal) {
		WithdrawRequestDto requestDto = new WithdrawRequestDto();
		requestDto.setAmount(coinDepositWithdrawal.getUnits());
		requestDto.setCoinName(coinDepositWithdrawal.getCoinType());
		requestDto.setIsWithdraw(true);
		if (coinDepositWithdrawal.getToTag() != null) {
			requestDto.setTag(coinDepositWithdrawal.getToTag());
		}
		requestDto.setToAddress(coinDepositWithdrawal.getAddress());
		return requestDto;
	}

	/**
	 * Sets the email data.
	 *
	 * @param token     the token
	 * @param url       the url
	 * @param userName  the user name
	 * @param amount    the amount
	 * @param coinType  the coin type
	 * @param toAddress the to address
	 * @param toEmail   the to email
	 * @return the map
	 */
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

	/**
	 * Gets the hot storage details.
	 *
	 * @param storageDetail   the storage detail
	 * @param withdrawRequest the withdraw request
	 * @param coinDetails     the coin details
	 * @return the hot storage details
	 */
	private CryptoRequestModel getHotStorageDetails(StorageDetail storageDetail, WithdrawRequestDto withdrawRequest,
			Coin coinDetails) {
		if (storageDetail != null && withdrawRequest != null) {
			CryptoRequestModel cryptoRequestModel = new CryptoRequestModel();
			cryptoRequestModel.setFromAddress(storageDetail.getAddress());
			cryptoRequestModel.setToAddress(withdrawRequest.getToAddress());
			BigDecimal amountToSend = withdrawRequest.getAmount().subtract(coinDetails.getWithdrawlFee());
			cryptoRequestModel.setAmount(amountToSend);
			cryptoRequestModel.setStorageType(STORAGE_HOT);
			if (storageDetail.getWalletFile() != null) {
				cryptoRequestModel.setWalletFile(storageDetail.getWalletFile());
			}
			if (storageDetail.getHexAddress() != null) {
				cryptoRequestModel.setFromHexAddress(storageDetail.getHexAddress());
			}
			if (storageDetail.getSecretKey() != null) {
				cryptoRequestModel.setSecretKey(storageDetail.getSecretKey());
			}
			if (storageDetail.getWalletPassword() != null) {
				cryptoRequestModel.setWalletPassword(storageDetail.getWalletPassword());
			}
			if (storageDetail.getWif() != null) {
				cryptoRequestModel.setWif(storageDetail.getWif());
			}
			if (withdrawRequest.getTag() != null) {
				cryptoRequestModel.setTag(withdrawRequest.getTag());
			}
			return cryptoRequestModel;
		} else {
			return null;
		}
	}

	/**
	 * Gets the withdraw.
	 *
	 * @param cryptoRequestModel the crypto request model
	 * @param coinName           the coin name
	 * @return the withdraw
	 */
	private CryptoResponseModel getWithdraw(CryptoRequestModel cryptoRequestModel, String coinName) {
		CryptoResponseModel cryptoResponseModel = null;
		switch (coinName) {
		case BTC:
			cryptoResponseModel = btcUtil.withdrawAPI(cryptoRequestModel);
			break;

		case SOLANA:
			cryptoResponseModel = solanaUtil.withdrawAPI(cryptoRequestModel);
			break;

		case ETH:
			cryptoResponseModel = ethUtil.withdrawAPI(cryptoRequestModel);
			break;

		case XRP:
			cryptoResponseModel = xrpUtil.withdrawAPI(cryptoRequestModel);
			break;
		case USDT:
			cryptoResponseModel = usdtUtil.withdrawAPI(cryptoRequestModel);
			break;

		case TRX:
			cryptoResponseModel = trxUtil.withdrawAPI(cryptoRequestModel);
			break;

		case BNB:
			cryptoResponseModel = bnbUtil.withdrawAPI(cryptoRequestModel);
			break;
		case MATIC:
			cryptoResponseModel = maticUtil.withdrawAPI(cryptoRequestModel);
			break;
		case POLKADOT:
			cryptoResponseModel = polkadotUtil.withdrawAPI(cryptoRequestModel);
			break;
		case AVAX:
			cryptoResponseModel = avaxUtil.withdrawAPI(cryptoRequestModel);
			break;
		default:
			cryptoResponseModel = null;
			break;
		}
		return cryptoResponseModel;
	}

	@Override
	public Response<Map<String, Object>> withdrawList(String coinName, Long fkUserId, Integer page, Integer pageSize) {
		Map<String, Object> responseMap = new HashMap<>();
		List<CoinDepositWithdrawal> getWithdrawList = coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(
				coinName, WITHDRAW, fkUserId, PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
		Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName, WITHDRAW,
				fkUserId);

		if (getWithdrawList != null && !getWithdrawList.isEmpty()) {
			responseMap.put(RESULT_LIST, getWithdrawList);
			responseMap.put(TOTAL_COUNT, getTotalCount);

			return new Response<>(SUCCESS_CODE, WITHDRAW_LIST_FETCHED, responseMap);
		} else {
			responseMap.put(RESULT_LIST, Collections.emptyList());
			responseMap.put(TOTAL_COUNT, getTotalCount);
			return new Response<>(SUCCESS_CODE, NO_WITHDRAW_FOUND, responseMap);
		}
	}

	@Override
	public Response<Map<String, Object>> withdrawListFilter(Long userId,
			SearchAndFilterCoinWithdrawlDepositDto serAndFiltertDto) {
		Map<String, Object> responseMap = new HashMap<>();
		List<CoinDepositWithdrawal> getWithdrawList = coinDepositWithdrawalDao
				.findByCoinTypeAndTxnTypeAndFkUserIdAndTxnTimeBetween(serAndFiltertDto.getCoinName(), WITHDRAW, userId,
						new Date(Long.parseLong(serAndFiltertDto.getFromDate())),
						new Date(Long.parseLong(serAndFiltertDto.getToDate())), PageRequest.of(
								serAndFiltertDto.getPage(), serAndFiltertDto.getPageSize(), Direction.DESC, "txnId"));
		Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserIdAndTxnTimeBetween(
				serAndFiltertDto.getCoinName(), WITHDRAW, serAndFiltertDto.getUserId(),
				new Date(Long.parseLong(serAndFiltertDto.getFromDate())),
				new Date(Long.parseLong(serAndFiltertDto.getToDate())));

		if (getWithdrawList != null && !getWithdrawList.isEmpty()) {
			responseMap.put(RESULT_LIST, getWithdrawList);
			responseMap.put(TOTAL_COUNT, getTotalCount);
			return new Response<>(SUCCESS_CODE, WITHDRAW_LIST_FETCHED, responseMap);
		} else {
			responseMap.put(RESULT_LIST, Collections.emptyList());
			responseMap.put(TOTAL_COUNT, getTotalCount);
			return new Response<>(SUCCESS_CODE, NO_WITHDRAW_FOUND, responseMap);
		}
	}

	@Override
	@Transactional
	public Response<String> approveWithdraw(ApproveWithdrawRequestDto approveWithdrawRequestDto) {
		if (approveWithdrawRequestDto.getIsWithdraw()) {
			Long getDecodedToken = Long.parseLong(approveWithdrawRequestDto.getWithdrawToken());
			Optional<CoinDepositWithdrawal> getTxnDetails = coinDepositWithdrawalDao.findById(getDecodedToken);
			if (getTxnDetails.isPresent()) {
				if (getTxnDetails.get().getStatus().equals(PENDING)) {
					WithdrawRequestDto setWithdrawRequest = setWithdrawRequest(getTxnDetails.get());
					if (getTxnDetails.get().getCoinType().equals(OMG) || getTxnDetails.get().getCoinType().equals(USDT))
						return transferFundsomgusdT(setWithdrawRequest, getTxnDetails.get().getFkUserId(),
								getTxnDetails.get());

					else
						return transferFunds(setWithdrawRequest, getTxnDetails.get().getFkUserId(),
								getTxnDetails.get());
				} else {
					return new Response<>(FAILURE_CODE, WITHDRAW_TOKEN_EXPIRED);
				}
			} else {
				return new Response<>(FAILURE_CODE, WITHDRAW_TOKEN_EXPIRED);
			}
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	private Response<String> transferFundsomgusdT(WithdrawRequestDto withdrawRequest, Long fkUserId,
			CoinDepositWithdrawal coinDepositWithdrawal) {
		Response<UserEmailAndNameDto> adminEmail = userClient.getUserByUserId(fkUserId);
		if (adminEmail.getStatus() == 200 && adminEmail.getData() != null) {
			Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(withdrawRequest.getCoinName(), fkUserId);
			if (getWallet.isPresent() && getWallet.get().getWalletBalance() != null) {
				Optional<Coin> getCoinDetailsByCoinName = coinDao.findByCoinShortName(withdrawRequest.getCoinName());
				if (getCoinDetailsByCoinName.isPresent()) {

					if (getWallet.get().getWalletBalance().compareTo(withdrawRequest.getAmount()) >= 0) {
						Optional<StorageDetail> getHotStorageDetails = storageDetailsDao
								.findByCoinTypeAndStorageType(withdrawRequest.getCoinName(), STORAGE_HOT);
						if (getHotStorageDetails.isPresent()) {
							if (getHotStorageDetails.get().getHotWalletBalance()
									.compareTo(withdrawRequest.getAmount()) > 0) {
								CryptoRequestModel getStorageDetails = getHotStorageDetails(getHotStorageDetails.get(),
										withdrawRequest, getCoinDetailsByCoinName.get());
								if (getStorageDetails != null) {
									CryptoResponseModel responseModel = getWithdraw(getStorageDetails,
											withdrawRequest.getCoinName());
									if (responseModel != null && responseModel.getTxnHash() != null) {
										updateWithdrawTransactionDetails(responseModel.getTxnHash(),
												coinDepositWithdrawal);
										updateWalletBalance(withdrawRequest.getCoinName(), fkUserId,
												withdrawRequest.getAmount(), WITHDRAW);
										updateStorageBalance(withdrawRequest.getCoinName(),
												responseModel.getStorageType(), responseModel.getActualAmount(),
												WITHDRAW);

										Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess(
												adminEmail.getData().getName(), withdrawRequest.getAmount(),
												withdrawRequest.getCoinName(), withdrawRequest.getToAddress(),
												adminEmail.getData().getEmail());
										mailSender.sendMailToApproveWithdrawSuccess(sendMailData);

										return new Response<>(SUCCESS_CODE, WITHDRAW_SUCCESS);
									} else {
										return new Response<>(FAILURE_CODE, WITHDRAW_FAILED);
									}
								} else {
									return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
								}
							} else {
								mailSender.sendMailForLowStorageBalanceAlert(setDepositConfirmationMailObj(
										adminEmail.getData().getEmail(), withdrawRequest.getCoinName(),
										getHotStorageDetails.get().getHotWalletBalance().toPlainString()));
								return new Response<>(WITHDRAW_FAILURE_CODE, INSUFFICIENT_STORAGE_BALANCE);
							}
						} else {
							return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
						}
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

	@Override
	@Transactional
	public Response<String> rejectWithdraw(String withdrawToken) {
		try {
			Long getDecodedToken = Long.parseLong(withdrawToken);
			Optional<CoinDepositWithdrawal> getTxnDetails = coinDepositWithdrawalDao.findById(getDecodedToken);
			if (getTxnDetails.isPresent()) {
				if (getTxnDetails.get().getStatus().equals(PENDING)) {
					CoinDepositWithdrawal rejectWithdrawRequest = null;
					getTxnDetails.get().setStatus(REJECTED);
					rejectWithdrawRequest = coinDepositWithdrawalDao.save(getTxnDetails.get());
					if (rejectWithdrawRequest != null && rejectWithdrawRequest.getStatus().equals(REJECTED)) {
						return new Response<>(SUCCESS_CODE, WITHDRAW_REQUEST_CANCELLED);
					} else {
						return new Response<>(FAILURE_CODE, UPDATION_FAILED);
					}
				} else {
					return new Response<>(FAILURE_CODE, WITHDRAW_TOKEN_EXPIRED);
				}
			} else {
				return new Response<>(FAILURE_CODE, WITHDRAW_TOKEN_EXPIRED);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	@Override
	@Transactional
	public Response<String> withdrawFiatBalance(FiatWithdrawRequestDto fiatWithdrawRequest, Long fkUserId) {
		Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(fiatWithdrawRequest.getCoinName(), fkUserId);
		if (getWallet.isPresent() && getWallet.get().getWalletBalance() != null) {
			Optional<Coin> getCoinDetailsByCoinName = coinDao
					.findByCoinShortNameAndCoinType(fiatWithdrawRequest.getCoinName(), FIAT_CURRENCY);
			if (getCoinDetailsByCoinName.isPresent()) {
				if (getWallet.get().getWalletBalance().compareTo(fiatWithdrawRequest.getAmount()) > 0) {
					BigDecimal calculatedFee = getCoinDetailsByCoinName.get().getWithdrawlFee();
					BigDecimal amountToSend = fiatWithdrawRequest.getAmount()
							.subtract(calculatedFee.multiply(fiatWithdrawRequest.getAmount()));
					saveFiatWithdrawTransactionDetails(fkUserId, calculatedFee, fiatWithdrawRequest.getCoinName(),
							amountToSend);
					updateWalletBalance(fiatWithdrawRequest.getCoinName(), fkUserId, fiatWithdrawRequest.getAmount(),
							WITHDRAW);
					return new Response<>(SUCCESS_CODE, WITHDRAW_REQUEST_POSTED_SUCCESSFULLY);
				} else {
					return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
				}
			} else {
				return new Response<>(FAILURE_CODE, NO_SUCH_COIN_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
		}
	}

	/**
	 * Save fiat withdraw transaction details.
	 *
	 * @param fkUserId     the fk user id
	 * @param withdrawlFee the withdrawl fee
	 * @param coinName     the coin name
	 * @param units        the units
	 * @return the coin deposit withdrawal
	 */
	private CoinDepositWithdrawal saveFiatWithdrawTransactionDetails(Long fkUserId, BigDecimal withdrawlFee,
			String coinName, BigDecimal units) {
		try {
			CoinDepositWithdrawal coinDepositWithdrawal = new CoinDepositWithdrawal();
			coinDepositWithdrawal.setFees(withdrawlFee);
			coinDepositWithdrawal.setTxnType(WITHDRAW);
			coinDepositWithdrawal.setStatus(PENDING);
			coinDepositWithdrawal.setCoinType(coinName);
			coinDepositWithdrawal.setUnits(units);
			coinDepositWithdrawal.setFkUserId(fkUserId);
			coinDepositWithdrawal = coinDepositWithdrawalDao.save(coinDepositWithdrawal);
			return coinDepositWithdrawal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}

	public Map<String, String> setDepositConfirmationMailObj(String email, String coinname, String balance) {
		Map<String, String> mailObj = new HashMap<>();
		mailObj.put("email", email);
		mailObj.put("coinname", coinname);
		mailObj.put("amount", balance);
		mailObj.put(EmailConstants.EMAIL_TO, email);
		mailObj.put(EmailConstants.SUBJECT_OF, "Deposition Confirmed");
		return mailObj;
	}

	public Map<String, String> setLowHotWalletBalanceAlert(String email, String coinname, String balance) {
		Map<String, String> mailObj = new HashMap<>();
		mailObj.put("email", email);
		mailObj.put("coinname", coinname);
		mailObj.put("amount", balance);
		mailObj.put(EmailConstants.EMAIL_TO, email);
		mailObj.put(EmailConstants.SUBJECT_OF, "LOW HOT BALANCE ALERT");
		return mailObj;
	}

	private BigDecimal getLimitAmount(Boolean isKycApproved) {
		if (!isKycApproved)
			return getLimitData("NO_KYC");
		else
			return getLimitData("YES_KYC");
	}

	private BigDecimal getLimitData(String limitType) {
		Optional<LimitData> limitData = limitDataDao.findByLimitName(limitType);
		if (limitData.isPresent())
			return limitData.get().getLimitPrice();
		else
			throw new RuntimeException("No such limit type found / Something wrong occured !!!");
	}

	private BigDecimal convertValueToBtc(BigDecimal amount, String coinName) {
		try {
			Optional<MarketPrice> getMarketPrice = marketPriceDao.findByCoinName(coinName);
			if (getMarketPrice.isPresent())
				return amount.multiply(getMarketPrice.get().getPriceInBtc());
			else
				throw new RuntimeException("No price found for coin / Something wrong occured !!!");
		} catch (Exception e) {
			throw new RuntimeException("No price found for coin / Something wrong occured !!!");
		}
	}

	private BigDecimal get24HoursTransactionsAmount(Long userId, String coinName) {
		List<CoinDepositWithdrawal> getTxnData = coinDepositWithdrawalDao
				.findByFkUserIdAndTxnTypeAndStatusAndCoinTypeAndTxnTimeBetween(userId, "WITHDRAW", "CONFIRM", coinName,
						DateTime.now().minusHours(24).toDate(), new Date());

		if (getTxnData.isEmpty())
			return BigDecimal.ZERO;
		else {
			List<BigDecimal> get24hrWithdrawAmount = new LinkedList<>();
			for (CoinDepositWithdrawal coinDepositWithdrawal : getTxnData) {
				get24hrWithdrawAmount.add(coinDepositWithdrawal.getUnits());
			}

			return calculateAmount(get24hrWithdrawAmount);
		}
	}

	private BigDecimal calculateAmount(List<BigDecimal> resultSet) {
		return resultSet.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
	}

	@Override
	@Transactional
	public Response<String> transferFromHotToCold(HotToColdTransferDto hotToColdTransferDto, Long fkUserId) {

		Optional<StorageDetail> getStorageDetails = storageDetailsDao
				.findByCoinTypeAndStorageType(hotToColdTransferDto.getCoinName(), STORAGE_HOT);

		Optional<StorageDetail> getColdStorageDetails = storageDetailsDao
				.findByaddress(hotToColdTransferDto.getToAddress());
		if (!getColdStorageDetails.isPresent()) {

			StorageDetail storageDetails = new StorageDetail();
			storageDetails.setCoinType(hotToColdTransferDto.getCoinName());
			storageDetails.setStorageType(STORAGE_COLD);
			storageDetails.setAddress(hotToColdTransferDto.getToAddress());

			storageDetailsDao.save(storageDetails);

		}

		if (!getStorageDetails.isPresent())
			return new Response<>(FAILURE_CODE,
					"No hot storage found for the coin: " + hotToColdTransferDto.getCoinName());
		if (hotToColdTransferDto.getAmount().compareTo(getStorageDetails.get().getHotWalletBalance()) >= 0)
			return new Response<>(FAILURE_CODE,
					"Insufficient amount / Not able to transfer all amount from HOT Wallet");

		CryptoRequestModel setTxnParams = setTransactionParams(getStorageDetails.get(), hotToColdTransferDto);
		CryptoResponseModel responseModel = getWithdraw(setTxnParams, hotToColdTransferDto.getCoinName());
		if (responseModel != null && responseModel.getTxnHash() != null) {
			saveHotToColdTxnDetails(responseModel.getTxnHash(), fkUserId, hotToColdTransferDto.getCoinName(),
					hotToColdTransferDto.getTag(), hotToColdTransferDto.getAmount(),
					hotToColdTransferDto.getToAddress());
			updateStorageBalance(getStorageDetails.get(), hotToColdTransferDto.getAmount());
			return new Response<>(SUCCESS_CODE, "HOT to COLD transfer successful");
		} else
			return new Response<>(FAILURE_CODE, "Transfer Failed");
	}

	private CryptoRequestModel setTransactionParams(StorageDetail storageDetail,
			HotToColdTransferDto hotToColdTransferDto) {
		if (storageDetail != null && hotToColdTransferDto != null) {
			CryptoRequestModel cryptoRequestModel = new CryptoRequestModel();
			cryptoRequestModel.setFromAddress(storageDetail.getAddress());
			cryptoRequestModel.setToAddress(hotToColdTransferDto.getToAddress());
			cryptoRequestModel.setAmount(hotToColdTransferDto.getAmount());
			cryptoRequestModel.setStorageType(STORAGE_HOT);
			if (storageDetail.getWalletFile() != null) {
				cryptoRequestModel.setWalletFile(storageDetail.getWalletFile());
			}
			if (storageDetail.getHexAddress() != null) {
				cryptoRequestModel.setFromHexAddress(storageDetail.getHexAddress());
			}
			if (storageDetail.getSecretKey() != null) {
				cryptoRequestModel.setSecretKey(storageDetail.getSecretKey());
			}
			if (storageDetail.getWalletPassword() != null) {
				cryptoRequestModel.setWalletPassword(storageDetail.getWalletPassword());
			}
			if (hotToColdTransferDto.getTag() != null) {
				cryptoRequestModel.setTag(hotToColdTransferDto.getTag());
			}
			return cryptoRequestModel;
		} else {
			return null;
		}
	}

	private CoinDepositWithdrawal saveHotToColdTxnDetails(String hash, Long fkUserId, String coinName, String toTag,
			BigDecimal amount, String toAddress) {
		try {
			CoinDepositWithdrawal coinDepositWithdrawal = null;
			if (hash != null) {
				coinDepositWithdrawal = new CoinDepositWithdrawal();
				coinDepositWithdrawal.setTxnHash(hash);
				coinDepositWithdrawal.setUnits(amount);
				coinDepositWithdrawal.setAddress(toAddress);
				coinDepositWithdrawal.setFkUserId(fkUserId);
				coinDepositWithdrawal.setStatus(CONFIRM);
				coinDepositWithdrawal.setCoinType(coinName);
				coinDepositWithdrawal.setTxnType("HOT_TO_COLD_TRANSFER");
				coinDepositWithdrawal.setToTag(toTag);

				coinDepositWithdrawal = coinDepositWithdrawalDao.save(coinDepositWithdrawal);
			}
			return coinDepositWithdrawal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	private StorageDetail updateStorageBalance(StorageDetail storageDetails, BigDecimal balance) {
		try {
			storageDetails.setHotWalletBalance(storageDetails.getHotWalletBalance().subtract(balance));
			return storageDetailsDao.save(storageDetails);
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
	}

	@Override
	public Response<Map<String, Object>> getDepositeFilter(Long userId,
			SearchAndFilterCoinWithdrawlDepositDto serAndFiltertDto) {

		Map<String, Object> responseMap = new HashMap<>();
		List<CoinDepositWithdrawal> getWithdrawList = coinDepositWithdrawalDao
				.findByCoinTypeAndTxnTypeAndFkUserIdAndTxnTimeBetween(serAndFiltertDto.getCoinName(), DEPOSIT, userId,
						new Date(Long.parseLong(serAndFiltertDto.getFromDate())),
						new Date(Long.parseLong(serAndFiltertDto.getToDate())), PageRequest.of(
								serAndFiltertDto.getPage(), serAndFiltertDto.getPageSize(), Direction.DESC, "txnId"));
		Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserIdAndTxnTimeBetween(
				serAndFiltertDto.getCoinName(), DEPOSIT, serAndFiltertDto.getUserId(),
				new Date(Long.parseLong(serAndFiltertDto.getFromDate())),
				new Date(Long.parseLong(serAndFiltertDto.getToDate())));

		if (getWithdrawList != null && !getWithdrawList.isEmpty()) {
			responseMap.put(RESULT_LIST, getWithdrawList);
			responseMap.put(TOTAL_COUNT, getTotalCount);
			return new Response<>(SUCCESS_CODE, DEPOSIT, responseMap);
		} else {
			responseMap.put(RESULT_LIST, Collections.emptyList());
			responseMap.put(TOTAL_COUNT, getTotalCount);
			return new Response<>(SUCCESS_CODE, "No deposit found.", responseMap);
		}
	}

	@Override
	public BigDecimal getPrice(String coinName)
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		if (coinName.equals("BTC")) {
			String txHashUrl = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=inr";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(txHashUrl, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			System.out.println(map);
			Object object = map.get("bitcoin");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("inr");
			System.out.println(javaUid);
			BigDecimal bigDecimal = new BigDecimal(javaUid);
			return bigDecimal;
		} else if (coinName.equals("ETH")) {
			String txHashUrl1 = "https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=inr";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(txHashUrl1, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("ethereum");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("inr");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		} else if (coinName.equals("TRX"))

		{
			String txHashUrl1 = "https://api.coingecko.com/api/v3/simple/price?ids=tron&vs_currencies=inr";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(txHashUrl1, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("tron");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("inr");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		} else if (coinName.equals("XRP")) {
			String txHashUrl1 = "https://api.coingecko.com/api/v3/simple/price?ids=ripple&vs_currencies=inr";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(txHashUrl1, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("ripple");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("inr");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);
			return bigDecimal;
		}

		else {
			String txHashUrl1 = "https://min-api.cryptocompare.com/data/price?fsym=INR&tsyms=BNB";
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(txHashUrl1, String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("BNB");
			String string = object.toString();
			BigDecimal bigDecimal = new BigDecimal(string);
			return bigDecimal;
		}
	}

	@Override
	public BigDecimal getPriceUsd(String coinName)
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		if (coinName.equals("BTC")) {

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			System.out.println(map);
			Object object = map.get("bitcoin");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);
			BigDecimal bigDecimal = new BigDecimal(javaUid);
			return bigDecimal;
		} else if (coinName.equals("ETH")) {

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("ethereum");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		} else if (coinName.equals("MATIC"))

		{

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("matic-network");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		} else if (coinName.equals("AVAX")) {

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("avalanche-2");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		} else if (coinName.equals("XRP")) {

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("ripple");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		} else if (coinName.equals("USDT"))

		{

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("tether");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		} else if (coinName.equals("POLKADOT"))

		{

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("polkadot");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		} else if (coinName.equals("SOLANA"))

		{

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("solana");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		} else if (coinName.equals("TRX"))

		{

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});

			System.out.println(map);
			Object object = map.get("tron");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);

			return bigDecimal;
		}

		else {

			RestTemplate restTemplate = new RestTemplate();
			restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

			ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs(coinName), String.class);

			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> map = objectMapper.readValue(responses.getBody(),
					new TypeReference<Map<String, Object>>() {
					});
			System.out.println(map);
			Object object = map.get("binancecoin");

			String json = new Gson().toJson(object);
			JSONObject jsonObj = new JSONObject(json);

			String javaUid = jsonObj.getString("usd");
			System.out.println(javaUid);

			BigDecimal bigDecimal = new BigDecimal(javaUid);
			return bigDecimal;
		}
	}

	@Override
	public Response<Object> getAdminAddress(String coinType) {
		Optional<StorageDetail> data = storageDetailsDao.findByCoinType(coinType);
		AdminAddressDto dto = new AdminAddressDto();
		dto.setCoinName(data.get().getCoinType());
		dto.setAddress(data.get().getAddress());
		return new Response<>(200, "Address fetch successfully", dto);

	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Map<String, Object>> getTransactionHistoryNew(String coinName, String txnType, Long fromDate,
			Long toDate, Long userId, Integer page, Integer pageSize, String txnHash, String status, BigDecimal amount,
			String userName, String userEmail) {
		StringBuilder query = new StringBuilder(
				"select c.txnId, c.coinType, c.status, c.txnHash, c.units, c.txnTime, c.txnType, c.userName, c.userEmail,c.address from CoinDepositWithdrawal c");

		List<String> conditions = new ArrayList<>();

		if (userId != null && !userId.equals(BLANK)) {
			conditions.add(" c.fkUserId=:fkUserId ");
		}
		if (coinName != null && !coinName.equals(BLANK)) {
			conditions.add(" c.coinType=:cointype ");
		}
		if (txnType != null && !txnType.equals(BLANK)) {
			conditions.add(" c.txnType=:txntype ");
		}
		if (fromDate != null && !String.valueOf(fromDate).equals(BLANK)) {
			conditions.add(" c.txnTime >= :fromDate ");
		}
		if (toDate != null && !String.valueOf(toDate).equals(BLANK)) {
			conditions.add(" c.txnTime <= :toDate ");
		}
		if (txnHash != null && !txnHash.equals(BLANK)) {
			conditions.add("c.txnHash=:txnHash");
		}
		if (status != null && !status.equals(BLANK)) {
			conditions.add("c.status=:status");
		}
		if (amount != null && !amount.equals(BLANK)) {
			conditions.add("c.units=:units");
		}
		if (userName != null && !userName.equals(BLANK)) {
			conditions.add("c.userName=:userName");
		}
		if (userEmail != null && !userEmail.equals(BLANK)) {
			conditions.add("c.userEmail=:userEmail");
		}
		if (!conditions.isEmpty()) {
			query.append(" where ");
			query.append(String.join(" and ", conditions.toArray(new String[0])));
		}
		query.append(" order by c.txnId desc");
		Query createQuery = em.createQuery(query.toString());
		if (userId != null && !userId.equals(BLANK))
			createQuery.setParameter("fkUserId", userId);

		if (coinName != null && !coinName.equals(BLANK))
			createQuery.setParameter("cointype", coinName);
		if (txnType != null && !txnType.equals(BLANK))
			createQuery.setParameter("txntype", txnType);
		if (fromDate != null && !String.valueOf(fromDate).equals(BLANK))
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null && !String.valueOf(toDate).equals(BLANK))
			createQuery.setParameter("toDate", new Date(toDate));
		if (txnHash != null && !txnHash.equals(BLANK)) {
			createQuery.setParameter("txnHash", txnHash);
		}
		if (status != null && !status.equals(BLANK)) {
			createQuery.setParameter("status", status);
		}
		if (amount != null && !amount.equals(BLANK)) {
			createQuery.setParameter("units", amount);
		}
		if (userName != null && !userName.equals(BLANK)) {
			createQuery.setParameter("userName", userName);
		}
		if (userEmail != null && !userEmail.equals(BLANK)) {
			createQuery.setParameter("userEmail", userEmail);
		}
		int filteredResultCount = createQuery.getResultList().size();
		createQuery.setFirstResult(page * pageSize);
		createQuery.setMaxResults(pageSize);
		List<Object[]> list = createQuery.getResultList();
		List<TransactionListDto> response = list.parallelStream().map(o -> {
			TransactionListDto dto = new TransactionListDto();
			dto.setTxnId((Long) o[0]);
			dto.setCoinType((String) o[1]);
			dto.setStatus((String) o[2]);
			dto.setTxnHash((String) o[3]);
			dto.setAmount((BigDecimal) o[4]);
			dto.setTxnTime((Date) o[5]);
			dto.setTxnType((String) o[6]);
			dto.setUserName((String) o[7]);
			dto.setUserEmail((String) o[8]);
			dto.setAddress((String) o[9]);
			return dto;
		}).collect(Collectors.toList());
		Map<String, Object> data = new HashMap<>();
		data.put(RESULT_LIST, response);
		data.put(TOTAL_COUNT, filteredResultCount);

		return new Response<>(SUCCESS_CODE, SUCCESS, data);
	}

	/**
	 * 
	 * @author Priyank Mishra
	 */
	private String getContract(Network network, String coinName) {
		Optional<Coin> isCoinAvailabe1 = coinDao.findByCoinShortNameAndNetwork(coinName, network);
		String contract = null;

		switch (coinName) {
		case USDT:
			if (network.equals(Network.ERC20)) {
				contract = "0xdAC17F958D2ee523a2206206994597C13D831ec7";
			} else if (network.equals(Network.BEP20)) {
				contract = "0x55d398326f99059fF775485246999027B3197955";
			}
			break;

		case XINDIA:
			if (network.equals(Network.ERC20)) {
				contract = "0x60fa9cbDA5DCa3A18f579EFedD0a828CEea2623C";
			} else if (network.equals(Network.BEP20)) {
				contract = "0x4d8c829c02585aa62d2bbaf562099bf749637580";
			}
			break;
//		case USDT:
//			if (network.equals(Network.ERC20)) {
//				contract = "0x773f343890E4bc5f0d57377FaA71A718B1FA820D";
//			} else if (network.equals(Network.BEP20)) {
//				contract = "0x4f197b1b01200FAc52Cb46d2A4968CeBf7F2a13c";
//			}
//			break;
//
//		case XINDIA:
//			if (network.equals(Network.ERC20)) {
//				contract = "0x60fa9cbDA5DCa3A18f579EFedD0a828CEea2623C";
//			} else if (network.equals(Network.BEP20)) {
//				contract = "0x79C21620e4B476cBE533B51baCb54aCafC4C4cC4";
//			}
//			break;
		default:
			if (isCoinAvailabe1.isPresent()) {

				if (isCoinAvailabe1.get().getNetwork().equals(Network.BEP20)) {
					contract = isCoinAvailabe1.get().getContractAddress();
				} else if (isCoinAvailabe1.get().getNetwork().equals(Network.ERC20)) {
					contract = isCoinAvailabe1.get().getContractAddress();
				}
			}
			break;
		}

		return contract;
	}

	/**
	 * Gets the internal transfer.
	 * 
	 * @author Priyank Mishra
	 * @param cryptoRequestModel the crypto request model
	 * @param coinName           the coin name
	 * @return the internal transfer
	 */
	private CryptoResponseModel getInternalTransferERCBEP(CryptoRequestModel cryptoRequestModel, String coinName,
			Network network, String getContract) {
		CryptoResponseModel cryptoResponseModel = null;
		Optional<Coin> isCoinAvailabe4 = coinDao.findByCoinShortName(coinName);
		switch (coinName) {
		case USDT:
			if (network.equals(Network.BEP20)) {
				cryptoResponseModel = bepUtil.internalTransferAPIBEPERC(cryptoRequestModel, getContract, coinName);
			} else if (network.equals(Network.ERC20)) {
				cryptoResponseModel = ercUtil.internalTransferAPIBEPERC(cryptoRequestModel, getContract, coinName);
			}
			break;

		case XINDIA:
			if (network.equals(Network.BEP20)) {
				cryptoResponseModel = bepUtil.internalTransferAPIBEPERC(cryptoRequestModel, getContract, coinName);
			} else if (network.equals(Network.ERC20)) {
				cryptoResponseModel = ercUtil.internalTransferAPIBEPERC(cryptoRequestModel, getContract, coinName);
			}
			break;
		// to do ...
		default:

			if (isCoinAvailabe4.isPresent()) {

				if (isCoinAvailabe4.get().getNetwork().equals(Network.BEP20)) {
					cryptoResponseModel = bepUtil.internalTransferAPIBEPERC(cryptoRequestModel, getContract, coinName);
				} else if (isCoinAvailabe4.get().getNetwork().equals(Network.ERC20)) {
					cryptoResponseModel = ercUtil.internalTransferAPIBEPERC(cryptoRequestModel, getContract, coinName);
				}
			}
			break;
		}
		return cryptoResponseModel;
	}

	/**
	 * 
	 * @author Priyank Mishra
	 */

	private BigDecimal getLiveBalanceERCBEP(String coinName, Wallet getWallet, Network network, String getContract) {
		BigDecimal getLiveBalanceByCoin = null;
		Optional<Coin> isCoinAvailabe2 = coinDao.findByCoinShortName(coinName);
		switch (coinName) {

		case USDT:
			if (network.equals(Network.BEP20)) {
				getLiveBalanceByCoin = bepUtil.getBalanceAPIERCBEP(getWallet.getWalletAddress(), getContract);
			} else if (network.equals(Network.ERC20)) {
				getLiveBalanceByCoin = ercUtil.getBalanceAPIERCBEP(getWallet.getWalletAddress(), getContract);
			}
			break;
		case XINDIA:
			if (network.equals(Network.BEP20)) {
				getLiveBalanceByCoin = bepUtil.getBalanceAPIERCBEP(getWallet.getWalletAddress(), getContract);
			} else if (network.equals(Network.ERC20)) {
				getLiveBalanceByCoin = ercUtil.getBalanceAPIERCBEP(getWallet.getWalletAddress(), getContract);
			}
			break;
		default:

			if (isCoinAvailabe2.isPresent()) {

				if (isCoinAvailabe2.get().getNetwork().equals(Network.BEP20)) {
					getLiveBalanceByCoin = bepUtil.getBalanceAPIERCBEP(getWallet.getWalletAddress(), getContract);
				} else if (isCoinAvailabe2.get().getNetwork().equals(Network.ERC20)) {
					getLiveBalanceByCoin = ercUtil.getBalanceAPIERCBEP(getWallet.getWalletAddress(), getContract);
				}
			}
			break;
		}
		return getLiveBalanceByCoin;
	}

	@Override
	public Response<Map<String, Object>> getDepositsErcBep(String coinName, Long fkUserId, Integer page,
			Integer pageSize, String username, Network network, String email) {
		Map<String, Object> responseMap = new HashMap<>();
		Response<UserEmailAndNameDto> adminEmail = userClient.getUserByUserId(fkUserId);
		if (adminEmail.getStatus() == 200 && adminEmail.getData() != null) {
			Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if (getWallet.isPresent() && getWallet.get().getWalletAddress() != null) {
				BigDecimal getLiveBalance = getLiveBalanceERCBEP(coinName, getWallet.get(), network,
						getContract(network, coinName));
				if (getLiveBalance.compareTo(BigDecimal.ZERO) > 0) {
					CryptoRequestModel cryptoRequestModel = getStorageDetailsByLimit(coinName);
					if (cryptoRequestModel != null) {
						cryptoRequestModel.setFromAddress(getWallet.get().getWalletAddress());

						if (getWallet.get().getWalletFileName() != null) {
							cryptoRequestModel.setWalletFile(getWallet.get().getWalletFileName());
						}
						if (getWallet.get().getEosAccountName() != null) {
							cryptoRequestModel.setFromEosAccount(getWallet.get().getEosAccountName());
						}
						if (getWallet.get().getHexAddress() != null) {
							cryptoRequestModel.setFromHexAddress(getWallet.get().getHexAddress());
						}
						if (getWallet.get().getWif() != null) {
							cryptoRequestModel.setWif(getWallet.get().getWif());
						}

						cryptoRequestModel.setAmount(getLiveBalance);
						CryptoResponseModel cryptoResponseModel = getInternalTransferERCBEP(cryptoRequestModel,
								coinName, network, getContract(network, coinName));

						cryptoResponseModel.setActualAmount(getLiveBalance);

						if (cryptoResponseModel != null && cryptoResponseModel.getTxnHash() != null) {
							saveTransactionDetails(cryptoResponseModel, fkUserId, null, adminEmail.getData());
							updateWalletBalance(coinName, fkUserId, cryptoResponseModel.getActualAmount(), DEPOSIT);
							updateStorageBalance(coinName, cryptoRequestModel.getStorageType(),
									cryptoResponseModel.getActualAmount(), DEPOSIT);

							List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao
									.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
											PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
							Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName,
									DEPOSIT, fkUserId);
							responseMap.put(RESULT_LIST, getTransactionList);
							responseMap.put(TOTAL_COUNT, getTotalCount);

							// send mail of deposit success
							mailSender.sendMailForDepositConfirmation(
									setDepositConfirmationMailObj(adminEmail.getData().getEmail(), coinName,
											cryptoResponseModel.getActualAmount().toPlainString()));
							if (isNotification.equals(Boolean.TRUE)) {
								Map<String, String> setData = new HashMap<>();
								setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
								setData.put(EmailConstants.COIN_TOKEN, coinName);
								setData.put(EmailConstants.AMOUNT_TOKEN,
										String.valueOf(cryptoResponseModel.getActualAmount()));
								EmailDto emailDto = new EmailDto(fkUserId, "deposit", "en", email, setData);
								notificationClient.sendNotification(emailDto);
							}
							return new Response<>(SUCCESS_CODE, DEPOSIT_SUCCESS, responseMap);
						} else {
							List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao
									.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
											PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
							Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName,
									DEPOSIT, fkUserId);
							responseMap.put(RESULT_LIST, getTransactionList);
							responseMap.put(TOTAL_COUNT, getTotalCount);
							return new Response<>(FAILURE_CODE, DEPOSIT_FAILED, responseMap);
						}
					} else {
						List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao
								.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
										PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
						Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName,
								DEPOSIT, fkUserId);
						responseMap.put(RESULT_LIST, getTransactionList);
						responseMap.put(TOTAL_COUNT, getTotalCount);
						return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND, responseMap);

					}
				} else {
					List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao
							.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
									PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
					Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName,
							DEPOSIT, fkUserId);
					responseMap.put(RESULT_LIST, getTransactionList);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, NO_NEW_DEPOSITS_FOUND, responseMap);
				}
			} else {
				return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
			}
		} else
			return new Response<>(FAILURE_CODE, USER_NOT_FOUND);
	}

	private CryptoResponseModel getWithdrawERCBEP(CryptoRequestModel cryptoRequestModel, String coinName,
			String getContract, Network network) {
		Optional<Coin> isCoinAvailabe7 = coinDao.findByCoinShortName(coinName);
		CryptoResponseModel cryptoResponseModel = null;
		switch (coinName) {

		case USDT:
			if (network.equals(Network.BEP20)) {
				cryptoResponseModel = bepUtil.withdrawAPIERCBep(cryptoRequestModel, getContract, coinName);
			} else if (network.equals(Network.ERC20)) {
				cryptoResponseModel = ercUtil.withdrawAPIERCBep(cryptoRequestModel, getContract, coinName);
			}
			break;
		case XINDIA:
			if (network.equals(Network.BEP20)) {
				cryptoResponseModel = bepUtil.withdrawAPIERCBep(cryptoRequestModel, getContract, coinName);
			} else if (network.equals(Network.ERC20)) {
				cryptoResponseModel = ercUtil.withdrawAPIERCBep(cryptoRequestModel, getContract, coinName);
			}
			break;
		default:

			if (isCoinAvailabe7.isPresent()) {
				if (isCoinAvailabe7.get().getNetwork().equals(Network.BEP20)) {
					cryptoResponseModel = bepUtil.withdrawAPIERCBep(cryptoRequestModel, getContract, coinName);
				} else if (isCoinAvailabe7.get().getNetwork().equals(Network.ERC20)) {
					cryptoResponseModel = ercUtil.withdrawAPIERCBep(cryptoRequestModel, getContract, coinName);
				}

			}
			break;
		}
		return cryptoResponseModel;
	}

	private Response<String> transferFundsERCBEP(WithdrawRequestDto withdrawRequest, Long fkUserId,
			CoinDepositWithdrawal coinDepositWithdrawal, Network network, String coinName) {
		Response<UserEmailAndNameDto> adminEmail = userClient.getUserByUserId(fkUserId);
		if (adminEmail.getStatus() == 200 && adminEmail.getData() != null) {
			Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(withdrawRequest.getCoinName(), fkUserId);
			if (getWallet.isPresent() && getWallet.get().getWalletBalance() != null) {
				Optional<Coin> getCoinDetailsByCoinName = coinDao.findByCoinShortName(withdrawRequest.getCoinName());
				if (getCoinDetailsByCoinName.isPresent()) {
					if (getWallet.get().getWalletBalance().compareTo(withdrawRequest.getAmount()) >= 0) {
						Optional<StorageDetail> getHotStorageDetails = storageDetailsDao
								.findByCoinTypeAndStorageType(withdrawRequest.getCoinName(), STORAGE_HOT);
						if (getHotStorageDetails.isPresent()) {
							if (getHotStorageDetails.get().getHotWalletBalance()
									.compareTo(withdrawRequest.getAmount()) > 0) {
								CryptoRequestModel getStorageDetails = getHotStorageDetails(getHotStorageDetails.get(),
										withdrawRequest, getCoinDetailsByCoinName.get());
								if (getStorageDetails != null) {
									CryptoResponseModel responseModel = getWithdrawERCBEP(getStorageDetails,
											withdrawRequest.getCoinName(), getContract(network, coinName), network);
									if (responseModel != null && responseModel.getTxnHash() != null) {
										updateWithdrawTransactionDetails(responseModel.getTxnHash(),
												coinDepositWithdrawal);
										updateWalletBalance(withdrawRequest.getCoinName(), fkUserId,
												withdrawRequest.getAmount(), WITHDRAW);

										updateStorageBalance(withdrawRequest.getCoinName(),
												responseModel.getStorageType(), responseModel.getActualAmount(),
												WITHDRAW);

										Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess(
												adminEmail.getData().getName(), withdrawRequest.getAmount(),
												withdrawRequest.getCoinName(), withdrawRequest.getToAddress(),
												adminEmail.getData().getEmail());
										mailSender.sendMailToApproveWithdrawSuccess(sendMailData);
										if (isNotification) {
											Map<String, String> setData = new HashMap<>();
											setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
											setData.put(EmailConstants.COIN_TOKEN, withdrawRequest.getCoinName());
											setData.put(EmailConstants.AMOUNT_TOKEN,
													String.valueOf(withdrawRequest.getAmount()));
											setData.put(EmailConstants.COIN_ADDRESS_TOKEN,
													withdrawRequest.getToAddress());
											EmailDto emailDto = new EmailDto(fkUserId, "withdraw_request_success", "en",
													"email", setData);
											notificationClient.sendNotification(emailDto);
										}
										return new Response<>(SUCCESS_CODE, WITHDRAW_SUCCESS);
									} else {
										return new Response<>(FAILURE_CODE, WITHDRAW_FAILED);
									}
								} else {
									return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
								}
							} else {
								mailSender.sendMailForLowStorageBalanceAlert(setDepositConfirmationMailObj(
										adminEmail.getData().getEmail(), withdrawRequest.getCoinName(),
										getHotStorageDetails.get().getHotWalletBalance().toPlainString()));
								return new Response<>(WITHDRAW_FAILURE_CODE, INSUFFICIENT_STORAGE_BALANCE);
							}
						} else {
							return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
						}
					} else {
						return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
					}
				}

			} else {
				return new Response<>(FAILURE_CODE, NO_SUCH_COIN_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
		}
		return null;
	}

	@Override
	@Transactional
	public Response<String> withdrawBalanceErcBep(WithdrawRequestDtoUpdated withdrawRequest, Long fkUserId) {
		if (!withdrawRequest.getIsWithdraw()) {

			Response<UserEmailAndNameDto> getUserNameAndEmail = userClient.getUserByUserId(fkUserId);
			if (getUserNameAndEmail.getStatus() == 200) {
				UserEmailAndNameDto userEmailAndNameDto = getUserNameAndEmail.getData();
				Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(withdrawRequest.getCoinName(),
						fkUserId);
				if (getWallet.isPresent() && getWallet.get().getWalletBalance() != null) {
					Optional<Coin> getCoinDetailsByCoinName = coinDao
							.findByCoinShortName(withdrawRequest.getCoinName());
					if (getCoinDetailsByCoinName.isPresent()) {
						if (getWallet.get().getWalletBalance().compareTo(withdrawRequest.getAmount()) >= 0) {
							Optional<StorageDetail> getHotStorageDetails = storageDetailsDao
									.findByCoinTypeAndStorageType(withdrawRequest.getCoinName(), STORAGE_HOT);
							if (getHotStorageDetails.isPresent()) {
								if (getHotStorageDetails.get().getHotWalletBalance()
										.compareTo(withdrawRequest.getAmount()) > 0) {
									BigDecimal calculatedFee = getCoinDetailsByCoinName.get().getWithdrawlFee();

									CoinDepositWithdrawal getWithdrawDetails = saveWithdrawTransactionDetails(
											withdrawRequest.getToAddress(), fkUserId, calculatedFee,
											withdrawRequest.getCoinName(), withdrawRequest.getAmount(),
											withdrawRequest.getTag(), userEmailAndNameDto.getName(),
											userEmailAndNameDto.getEmail(), withdrawRequest.getIsExternal());

									if (getWithdrawDetails != null) {
										String generateToken = RandomTagGenerator.generateRandomToken()
												+ getWithdrawDetails.getTxnId() + withdrawRequest.getNetwork();
										Map<String, Object> sendMailData = setEmailData(generateToken,
												withdrawRequest.getUrl(), userEmailAndNameDto.getName(),
												withdrawRequest.getAmount(), withdrawRequest.getCoinName(),
												withdrawRequest.getToAddress(), userEmailAndNameDto.getEmail());
										mailSender.sendMailToApproveWithdraw(sendMailData);
										if (isNotification) {
											Map<String, String> setData = new HashMap<>();
											setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
											setData.put(EmailConstants.COIN_TOKEN, withdrawRequest.getCoinName());
											setData.put(EmailConstants.AMOUNT_TOKEN,
													String.valueOf(withdrawRequest.getAmount()));
											setData.put(EmailConstants.COIN_ADDRESS_TOKEN,
													withdrawRequest.getToAddress());
											EmailDto emailDto = new EmailDto(fkUserId, "withdraw_confirmation_request",
													"en", "email", setData);
											notificationClient.sendNotification(emailDto);
										}
										return new Response<>(SUCCESS_CODE, WITHDRAW_REQUEST_POSTED_SUCCESSFULLY);
									}
									return new Response<>(SUCCESS_CODE, WITHDRAW_REQUEST_POSTED_SUCCESSFULLY);
								} else {
									return new Response<>(WITHDRAW_FAILURE_CODE, INSUFFICIENT_STORAGE_BALANCE);
								}
							} else {
								return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
							}
						} else {
							return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
						}
					}

					else {
						return new Response<>(FAILURE_CODE, NO_SUCH_COIN_FOUND);
					}
				} else {
					return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
				}
			} else {
				return new Response<>(FAILURE_CODE, USER_NOT_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, ISWITHDRAW_CANNOT_BE_TRUE_FOR_FIRST_TIME);
		}

	}

	@Override
	@Transactional
	public Response<String> getApproveWithdrawBepErc(ApproveWithdrawRequestDto approveWithdrawRequestDto,
			Network network) {
		if (approveWithdrawRequestDto.getIsWithdraw()) {
			Long getDecodedToken = Long.parseLong(approveWithdrawRequestDto.getWithdrawToken());
			Optional<CoinDepositWithdrawal> getTxnDetails = coinDepositWithdrawalDao.findById(getDecodedToken);
			if (getTxnDetails.isPresent()) {
				if (getTxnDetails.get().getStatus().equals(PENDING)) {
					WithdrawRequestDto setWithdrawRequest = setWithdrawRequest(getTxnDetails.get());
					if (getTxnDetails.get().getCoinType().equals(OMG) || getTxnDetails.get().getCoinType().equals(USDT))
						return transferFundsERCBEP(setWithdrawRequest, getTxnDetails.get().getFkUserId(),
								getTxnDetails.get(), network, getTxnDetails.get().getCoinType());
					else
						return transferFundsERCBEP(setWithdrawRequest, getTxnDetails.get().getFkUserId(),
								getTxnDetails.get(), network, getTxnDetails.get().getCoinType());
				} else {
					return new Response<>(FAILURE_CODE, WITHDRAW_TOKEN_EXPIRED);
				}
			} else {
				return new Response<>(FAILURE_CODE, WITHDRAW_TOKEN_EXPIRED);
			}
		} else {
			throw new BadRequestException(BAD_REQUEST_EXCEPTION);
		}
	}

	@Override
	public Response<Map<String, Object>> referalAmountDeductNew(String coinName, Long fkUserId, BigDecimal amount) {
		Response<UserEmailAndNameDto> adminEmail = userClient.getUserByUserId(fkUserId);
		if (adminEmail.getStatus() == 200 && adminEmail.getData() != null) {
			Optional<Wallet> isWalletExists = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			Optional<StorageDetail> isDetaOptional = storageDetailsDao.findByCoinTypeAndStorageType(coinName, "HOT");
			BigDecimal b = new BigDecimal(10).subtract(isDetaOptional.get().getHotWalletBalance());
			if (isWalletExists.isPresent()) {
				isWalletExists.get().setWalletBalance(b);
			}
		}
		return new Response<>(200, "Found");
	}

	public String trxHashs(String coinName) {
		String txHashUrls = null;
		switch (coinName) {
		case ETH:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=ethereum&vs_currencies=usd";
			break;
		case BTC:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd";
			break;
		case BNB:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=BinanceCoin&vs_currencies=usd";
			break;
		case USDT:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=Tether&vs_currencies=usd";
			break;
		case XRP:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=Ripple&vs_currencies=usd";
			break;
		case TRX:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=Tron&vs_currencies=usd";
			break;
		case POLKADOT:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=Polkadot&vs_currencies=usd";
			break;
		case SOLANA:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=Solana&vs_currencies=usd";
			break;
		case MATIC:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=matic-network&vs_currencies=usd";
			break;
		case AVAX:
			txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=avalanche-2&vs_currencies=usd";
			break;
		default:
			break;
		}
		return txHashUrls;
	}

	@Override
	public Response<Object> usdAmountConvert(Long userId)
			throws JsonParseException, JsonMappingException, IOException, JSONException {

		List<Wallet> getWallet = walletDao.findByFkUserId(userId);
		List<Wallet> getUsdAmount = walletDao.findByFkUserIdAndUsdAmountLessThanEqual(userId, new BigDecimal(10));

		if (!getWallet.isEmpty()) {
			for (Wallet getCoin : getWallet) {
				if (getCoin.getCoinName().equals("XINDIA")) {
					String xindia = getCoin.getCoinName().replace("XINDIA", "india");
					System.out.print(xindia);
					System.out.print(xindia);
					BigDecimal ciel = balance(xindia);
					System.out.print(BigDecimal.valueOf(Math.ceil(Double.valueOf(ciel.toString()))));
					BigDecimal liveBalace = BigDecimal.valueOf(Math.ceil(Double.valueOf(ciel.toString())));
					BigDecimal walletBalance = getCoin.getWalletBalance().multiply(liveBalace);
					getCoin.setUsdAmount(walletBalance);
					walletDao.save(getCoin);

				}
				if (!getCoin.getCoinName().equals("XINDIA")) {
					BigDecimal liveBalace = balance(getCoin.getCoinName());
					BigDecimal walletBalance = getCoin.getWalletBalance().multiply(liveBalace);
					getCoin.setUsdAmount(walletBalance);
					walletDao.save(getCoin);
					if (!getUsdAmount.isEmpty()) {
					}
				}

			}
			return new Response<>(200, "ConvertedAmount", getUsdAmount);

		}

		return null;
	}

	@Override
	public Response<Object> usdAmountList() {
		List<Wallet> getUsdAmount = walletDao.findByUsdAmountLessThanEqual(new BigDecimal(10));
		if (!getUsdAmount.isEmpty()) {
			return new Response<>(200, "UsdAmount", getUsdAmount);
		}
		return null;

	}

	@Override
	public Response<Object> usdAmountConvertParticularUser(List<String> coinName, Long userId) {
		Map<String, Object> map = new HashMap<>();

		for (String coin : coinName) {
			Optional<Wallet> getUsdAmount = walletDao.findByCoinNameAndFkUserIdAndUsdAmountLessThanEqual(coin, userId,
					new BigDecimal(10));
			Optional<Wallet> getXindia = walletDao.findByCoinNameAndFkUserId("XINDIA", userId);

			BigDecimal usdAmountTotal = BigDecimal.ZERO;
			if (getUsdAmount.isPresent()) {

				usdAmountTotal = usdAmountTotal.add(getUsdAmount.get().getUsdAmount());
				SmallAssetHistory assetHistory = new SmallAssetHistory();
				assetHistory.setCoinName(coin);
				assetHistory.setTotalUsdAmount(usdAmountTotal);
				assetHistory.setUsdAmount(getUsdAmount.get().getUsdAmount());
				assetHistory.setUserId(userId);
				assetHistory.setCreateDate(new Date());
				smallAssetDao.save(assetHistory);
			}
			BigDecimal xindiabalance = getXindia.get().getWalletBalance();
			BigDecimal TotalXinidia = xindiabalance.add(usdAmountTotal);
			getXindia.get().setWalletBalance(TotalXinidia);
			getUsdAmount.get().setWalletBalance(new BigDecimal(0));
			getUsdAmount.get().setUsdAmount(new BigDecimal(0));

			map.put("Xindia", getXindia.get());
			map.put("coin", getUsdAmount.get());
			walletDao.save(getXindia.get());
			walletDao.save(getUsdAmount.get());
		}
		return new Response<>(200, "Total Balance", map);
	}

	@SuppressWarnings("unchecked")
	public BigDecimal balance(String coiname) {

		try {
			String apiBaseUri1 = "http://3.213.77.140:3015/eth/getMarketPriceById?ids=" + coiname;
			String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, TIMEOUT);
//			LOGGER.info(responseString1);
			Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1, Map.class);
			return BigDecimal.valueOf(Double.valueOf(String.valueOf(allData1.get("USD"))));
		} catch (Exception e) {
//			LOGGER.catching(e);
			return DEFAULT_BALANCE;
		}
	}

	@Override
	public Response<Object> inrAmountConvert(Long userId)
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		List<Wallet> getWallet = walletDao.findByFkUserId(userId);
		List<Coin> coinList = coinDao.findAll();
		BigDecimal liveBalace = null;
		Map<String, Object> map = new HashMap<>();
		BigDecimal total = BigDecimal.ZERO;
		BigDecimal totalBlocked = BigDecimal.ZERO;
		BigDecimal availableFund = BigDecimal.ZERO;
		if (!getWallet.isEmpty()) {
			for (Coin getcoinlist : coinList) {
				for (Wallet getCoin : getWallet) {
					if (getcoinlist.getCoinShortName().equals(getCoin.getCoinName())) {
						if (getCoin.getCoinName().equals("XINDIA")) {
							String xindia = getcoinlist.getCoinShortName().replace("XINDIA", "india");
							BigDecimal ciel = balance(xindia);
							liveBalace = BigDecimal.valueOf(Math.ceil(Double.valueOf(ciel.toString())));
						}
						if (!getCoin.getCoinName().equals("XINDIA")) {
							liveBalace = balance(getcoinlist.getCoinShortName().toLowerCase());
						}
						BigDecimal walletBalance = getCoin.getWalletBalance().multiply(liveBalace);
						BigDecimal blocked = getCoin.getBlockedBalance().multiply(liveBalace);
						getCoin.setInrBlockedAmount(blocked);
						getCoin.setInrAmount(walletBalance);
						walletDao.save(getCoin);
						BigDecimal fund = getCoin.getInrAmount().subtract(getCoin.getInrBlockedAmount());
						getCoin.setFundData(fund);
						walletDao.save(getCoin);
						total = total.add(getCoin.getInrAmount());
						totalBlocked = totalBlocked.add(getCoin.getInrBlockedAmount());
						availableFund = availableFund.add(getCoin.getFundData());
						map.put("Total_Inr_Amount", total);
						map.put("Total_Inr_Blocked", totalBlocked);
						map.put("Available_Fund", availableFund);
					}
				}
			}

			return new Response<>(200, "ConvertedAmount", map);

		}

		return new Response<>(205, "No Data Present");
	}

	@Override
	public BigDecimal getPriceUSD(String coinName)
			throws JsonParseException, JsonMappingException, IOException, JSONException {
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new FormHttpMessageConverter());

		ResponseEntity<String> responses = restTemplate.getForEntity(trxHashs1(coinName), String.class);
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> map = objectMapper.readValue(responses.getBody(), new TypeReference<Map<String, Object>>() {
		});

		System.out.println(map);
		System.out.println(map);
		Object object = map.get(coinName);

		String json = new Gson().toJson(object);
		JSONObject jsonObj = new JSONObject(json);

		String javaUid = jsonObj.getString("usd");
		System.out.println(javaUid);
		BigDecimal bigDecimal = new BigDecimal(javaUid);
		return bigDecimal;
	}

	public String trxHashs1(String coinName) {
		String txHashUrls = "https://api.coingecko.com/api/v3/simple/price?ids=" + coinName + "&vs_currencies=usd";
		return txHashUrls;
	}

	@Override
	public Response<Object> fullData(Long userId) {
		List<DepositInrAmount> list = amountdao.findAll();
		Map<String, Object> map = new HashMap<>();
		for (DepositInrAmount data : list) {

			map.put("DepositFee", data.getDepositFee());
			map.put("WithdrawFee", data.getWithdrawFee());
			map.put("minimunDeposite", data.getMinimumdeposite());
			map.put("minimumWithdarw", data.getMinimunwithdraw());
		}
		return new Response<>(200, "Found Successfully", map);
	}

	@Override
	public Response<Object> getLivePrice(String coinName) {
		BigDecimal liveBalace = balance(coinName.toLowerCase());
		return new Response<>(200, "LiveBalance", liveBalace);
	}

}
