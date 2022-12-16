package com.mobiloitte.microservice.wallet.serviceimpl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.constants.EmailConstants;
import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.cryptocoins.AVAXUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.BEPUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.BNBUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.BTCUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.ETHUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.MATICUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.POLKADOTUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.SOLANAUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.TRXUtil;
import com.mobiloitte.microservice.wallet.dao.AdminBankDao;
import com.mobiloitte.microservice.wallet.dao.AdminUpidao;
import com.mobiloitte.microservice.wallet.dao.AmountDao;
import com.mobiloitte.microservice.wallet.dao.BankDao;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.CoinDepositWithdrawalDao;
import com.mobiloitte.microservice.wallet.dao.FiatDao;
import com.mobiloitte.microservice.wallet.dao.InternalTransferAmountDao;
import com.mobiloitte.microservice.wallet.dao.MarketPriceDao;
import com.mobiloitte.microservice.wallet.dao.P2pHistoryDao;
import com.mobiloitte.microservice.wallet.dao.StorageDetailsDao;
import com.mobiloitte.microservice.wallet.dao.UpiUserDao;
import com.mobiloitte.microservice.wallet.dao.UserToAdminTransferDao;
import com.mobiloitte.microservice.wallet.dao.UserTotalAmountDao;
import com.mobiloitte.microservice.wallet.dao.WalletDao;
import com.mobiloitte.microservice.wallet.dao.WalletHistoryDao;
import com.mobiloitte.microservice.wallet.dto.AdminBankDto;
import com.mobiloitte.microservice.wallet.dto.AdminUpiDto;
import com.mobiloitte.microservice.wallet.dto.DepositInrDto;
import com.mobiloitte.microservice.wallet.dto.EmailDto;
import com.mobiloitte.microservice.wallet.dto.FiatDto;
import com.mobiloitte.microservice.wallet.dto.FundUserDto;
import com.mobiloitte.microservice.wallet.dto.GetBalanceResponseDto;
import com.mobiloitte.microservice.wallet.dto.InternalTransferDto;
import com.mobiloitte.microservice.wallet.dto.P2pHistoryDto;
import com.mobiloitte.microservice.wallet.dto.SmallAssetListDto;
import com.mobiloitte.microservice.wallet.dto.TransferListDto;
import com.mobiloitte.microservice.wallet.dto.UpiUserDto;
import com.mobiloitte.microservice.wallet.dto.UserEmailAndNameDto;
import com.mobiloitte.microservice.wallet.dto.UserToAdminTransferDto;
import com.mobiloitte.microservice.wallet.dto.WithdrawDtoInr;
import com.mobiloitte.microservice.wallet.entities.AdminBank;
import com.mobiloitte.microservice.wallet.entities.AdminUpi;
import com.mobiloitte.microservice.wallet.entities.BankDetails;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.entities.DepositInrAmount;
import com.mobiloitte.microservice.wallet.entities.Fiat;
import com.mobiloitte.microservice.wallet.entities.InternalTransferAmount;
import com.mobiloitte.microservice.wallet.entities.MarketPrice;
import com.mobiloitte.microservice.wallet.entities.P2pHistory;
import com.mobiloitte.microservice.wallet.entities.ReferalComission;
import com.mobiloitte.microservice.wallet.entities.StorageDetail;
import com.mobiloitte.microservice.wallet.entities.UpiUser;
import com.mobiloitte.microservice.wallet.entities.UserToAdminTransfer;
import com.mobiloitte.microservice.wallet.entities.UserTotalAmount;
//import com.mobiloitte.microservice.wallet.entities.UserToAdminTransfer;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.entities.WalletHistory;
import com.mobiloitte.microservice.wallet.enums.FiatStatus;
import com.mobiloitte.microservice.wallet.enums.TransactionType;
import com.mobiloitte.microservice.wallet.exception.CoinNotFoundException;
import com.mobiloitte.microservice.wallet.exception.WalletNotFoundException;
import com.mobiloitte.microservice.wallet.feign.NotificationClient;
import com.mobiloitte.microservice.wallet.feign.UserClient;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.WalletManagementService;
import com.mobiloitte.microservice.wallet.utils.APIUtils;
import com.mobiloitte.microservice.wallet.utils.MailSender;
import com.mobiloitte.microservice.wallet.utils.RandomTagGenerator;

/**
 * The Class WalletManagementServiceImpl.
 * 
 * @author Ankush Mohapatra
 */
@Service("WalletManagementService")
public class WalletManagementServiceImpl implements WalletManagementService, OtherConstants, WalletConstants {
	public static final int USDT_TIMEOUT = 180000;
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(WalletManagementServiceImpl.class);

	// DAO Objects
	/** The coin dao. */
	@Autowired
	private CoinDao coinDao;
	@Autowired
	private NotificationClient notificationClient;

	@Value("${isNotification}")
	private Boolean isNotification;
	@Autowired
	MarketPriceDao marketPriceDao;

	@Autowired
	private FiatDao fiatDao;

	@Autowired
	private AdminBankDao adminBankDao;

	@Autowired
	private AmountDao amountdao;

	@Autowired
	private UpiUserDao upiUserDao;

	@Autowired
	private UserTotalAmountDao userTotalAmountDao;

	@Autowired
	private UserClient userClient;

	@Autowired
	private BankDao bankDao;
	@Autowired
	private UserToAdminTransferDao userToAdminTransferDao;
	/** The wallet dao. */
	@Autowired
	private CoinDepositWithdrawalDao coinDepositWithdrawalDao;
	@Autowired
	private WalletDao walletDao;

	@Autowired
	private AdminUpidao adminUpidao;
	/*
	 * @Autowired private AdminToUserTransfer adminToUserTransfer;
	 */
	@Autowired
	private InternalTransferAmountDao internalTransferAmountDao;
	ModelMapper modelMapper = new ModelMapper();
	// Crypto Coin Objects

	/** The btc util. */
	@Autowired
	private BTCUtil btcUtil;

	/** The eth util. */
	@Autowired
	private ETHUtil ethUtil;

	@Autowired
	private MailSender mailSender;

	@Autowired
	private SOLANAUtil solanaUtil;

	@Autowired
	private TRXUtil trxUtil;
	@Autowired
	private EntityManager em;

	@Autowired
	private BNBUtil bnbUtil;

	@Autowired
	private MATICUtil maticUtil;

	@Autowired
	private POLKADOTUtil polkadotUtil;

	@Autowired
	private AVAXUtil avaxUtil;

	@Autowired
	private WalletHistoryDao walletHistoryDao;
	@Autowired
	private BEPUtil bepUtil;

	@Autowired
	private StorageDetailsDao storageDetailsDao;

	@Autowired
	private CoinDepositWithdrawalDao coinDepositeWithrawlDao;

	@Autowired
	private P2pHistoryDao p2pHistoryDao;

	@Override
	@Transactional
	public Response<String> createWallet(Long userId, String randomId) {
		try {
			List<Coin> getCoinDetails = coinDao.findAll();
			for (int i = 0; i < getCoinDetails.size(); i++) {
				if (!isWalletFound(getCoinDetails.get(i).getCoinShortName(), userId)) {
					Wallet wallet = new Wallet();
					wallet.setCoin(getCoinDetails.get(i));
					wallet.setFkUserId(userId);
					wallet.setRandomId(randomId);
					wallet.setCoinName(getCoinDetails.get(i).getCoinShortName());
					wallet.setBlockedBalance(BigDecimal.ZERO);
					wallet.setWalletBalance(BigDecimal.ZERO);
					wallet.setCoinType(getCoinDetails.get(i).getCoinType());
					walletDao.save(wallet);
				} else {
					Optional<Wallet> getWalletDetails = walletDao
							.findByCoinNameAndFkUserId(getCoinDetails.get(i).getCoinShortName(), userId);
					if (getWalletDetails.isPresent()) {
						Wallet wallet = getWalletDetails.get();
						wallet.setRandomId(randomId);
						walletDao.save(wallet);

					}
				}
			}
			return new Response<>(SUCCESS_CODE, WALLET_CREATED_SUCCESSFULLY);
		} catch (Exception e) {
			LOGGER.catching(e);
			throw new CoinNotFoundException("No coins found to create wallet" + "");
		}
	}

	@Override
	public Response<Object> deleteUserBankAccount(Long userId, Long upiId) {
		try {
			Optional<UpiUser> accountExist = upiUserDao.findByUserUpiIdAndUserId(upiId, userId);
			if (accountExist.isPresent()) {
				upiUserDao.deleteById(upiId);
				return new Response<>(200, "User Upi Account Deleted Successfully");
			} else {
				return new Response<>(201, "User Upi Account Does Not Exist");
			}
		} catch (EmptyResultDataAccessException e) {
			return new Response<>(203, "User Upi Account Not Deleted");
		}
	}

	/**
	 * Checks if is wallet found.
	 *
	 * @param coinName the coin name
	 * @param userId   the user id
	 * @return true, if is wallet found
	 */
	private boolean isWalletFound(String coinName, Long userId) {
		Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, userId);
		if (getWalletDetails.isPresent()) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Response<Object> updateId(Long userId, String oldRandomId, String newRandomId) {
		Optional<Wallet> data = walletDao.findByRandomId(oldRandomId);
		if (data.isPresent()) {
			data.get().setRandomId(newRandomId);
			walletDao.save(data.get());
			return new Response<>(200, "randomId updated SuccessFully.");

		} else
			return new Response<>(205, "No randomId found, create one!!!");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.WalletManagementService
	 * #getWalletAddress(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public Response<Wallet> getWalletAddress(String coinName, Long fkUserId) {
		Wallet wallet = null;
		wallet = getWalletDetailsByCoinName(coinName, fkUserId);
		if (wallet != null && wallet.getWalletAddress() != null) {
			return new Response<>(SUCCESS_CODE, SUCCESS, wallet);
		} else {
			CryptoResponseModel cryptoResponseModel = getAddressFromLiveCoinApi(coinName, String.valueOf(fkUserId));
			if (cryptoResponseModel != null && cryptoResponseModel.getAddress() != null) {
				wallet = saveAddressInDB(cryptoResponseModel, coinName, String.valueOf(fkUserId));
				if (wallet != null) {
					return new Response<>(SUCCESS_CODE, SUCCESS, wallet);
				} else {
					return new Response<>(SERVER_ERROR_CODE, SERVER_ERROR);
				}
			} else {
				return new Response<>(FAILURE_CODE, FAILED_TO_FETCH_ADDRESS);
			}
		}
	}

	private String checkUniqueEosAccount() {
		String randomEosAccName = RandomTagGenerator.generateRandEosAcc();
		if (!Boolean.TRUE.equals(walletDao.existsByEosAccountName(randomEosAccName))) {
			return randomEosAccName;
		} else
			return checkUniqueEosAccount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.WalletManagementService
	 * #getBalance(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional
	public Response<GetBalanceResponseDto> getBalance(String coinName, Long fkUserId) {
		try {
			Wallet wallet = getWalletDetailsByCoinName(coinName, fkUserId);
			if (wallet != null) {
				GetBalanceResponseDto responseDto = new GetBalanceResponseDto();
				responseDto.setWalletBalance(wallet.getWalletBalance());
				responseDto.setBlockedBalance(wallet.getBlockedBalance());
				return new Response<>(SUCCESS_CODE, SUCCESS, responseDto);
			} else {
				return new Response<>(FAILURE_CODE, BALANCE_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new WalletNotFoundException(
					"No wallet found for coin: " + coinName + " and userId: " + fkUserId + "");
		}
	}

	/**
	 * Gets the wallet details by coin name.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the wallet details by coin name
	 */
	private Wallet getWalletDetailsByCoinName(String coinName, Long fkUserId) {
		Optional<Wallet> optionalWallet = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
		if (optionalWallet.isPresent()) {
			return optionalWallet.get();
		} else {
			return null;
		}
	}

	/**
	 * Save address in DB.
	 *
	 * @param cryptoResponseModel the crypto response model
	 * @param coinName            the coin name
	 * @param fkUserId            the fk user id
	 * @return the wallet
	 */
	private Wallet saveAddressInDB(CryptoResponseModel cryptoResponseModel, String coinName, String fkUserId) {
		try {
			Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(coinName, Long.parseLong(fkUserId));
			if (getWallet.isPresent() && cryptoResponseModel.getAddress() != null) {
				Wallet wallet = getWallet.get();
				wallet.setWalletId(wallet.getWalletId());
				return updateExistingWallet(cryptoResponseModel, wallet);
			} else {
				return saveNewWallet(cryptoResponseModel, coinName, fkUserId);
			}

		} catch (Exception e) {
			LOGGER.catching(e);
			return null;
		}
	}

	/**
	 * Gets the address from live coin api.
	 *
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the address from live coin api
	 */
	private CryptoResponseModel getAddressFromLiveCoinApi(String coinName, String fkUserId) {
		CryptoResponseModel cryptoResponseModel = null;
		Optional<Coin> isCoinAvailabe = coinDao.findByCoinShortName(coinName);

		switch (coinName) {
		case BTC:
			cryptoResponseModel = btcUtil.getAddressAPI(fkUserId);
			break;

		case SOLANA:
			cryptoResponseModel = solanaUtil.getAddressAPI(fkUserId);
			break;
		case ETH:
			cryptoResponseModel = ethUtil.getAddressAPI(fkUserId);
			break;

		case USDT:
			cryptoResponseModel = bepUtil.getAddressAPI(fkUserId);
			break;
		case TRX:
			cryptoResponseModel = trxUtil.getAddressAPI(fkUserId);
			break;

		case BNB:
			cryptoResponseModel = bnbUtil.getAddressAPI(fkUserId);
			break;
		case MATIC:
			cryptoResponseModel = maticUtil.getAddressAPI(fkUserId);
			break;
		case POLKADOT:
			cryptoResponseModel = polkadotUtil.getAddressAPI(fkUserId);
			break;
		case AVAX:
			cryptoResponseModel = avaxUtil.getAddressAPI(fkUserId);
			break;

		case XINDIA:
			cryptoResponseModel = bepUtil.getAddressAPI(fkUserId);
			break;
		default:
			if (isCoinAvailabe.isPresent()) {

				cryptoResponseModel = bepUtil.getAddressAPI(fkUserId);

			}
			break;
		}

		return cryptoResponseModel;
	}

	/**
	 * Save new wallet.
	 *
	 * @param cryptoResponseModel the crypto response model
	 * @param coinName            the coin name
	 * @param fkUserId            the fk user id
	 * @return the wallet
	 */
	private Wallet saveNewWallet(CryptoResponseModel cryptoResponseModel, String coinName, String fkUserId) {
		Optional<Coin> getCoinDetails = coinDao.findByCoinShortName(coinName);
		if (getCoinDetails.isPresent()) {
			Wallet newWallet = new Wallet();
			newWallet.setCoin(getCoinDetails.get());
			newWallet.setFkUserId(Long.parseLong(fkUserId));
			newWallet.setCoinName(getCoinDetails.get().getCoinShortName());
			newWallet.setBlockedBalance(BigDecimal.ZERO);
			newWallet.setWalletBalance(BigDecimal.ZERO);
			newWallet.setCoinType(getCoinDetails.get().getCoinType());
			if (cryptoResponseModel.getAddress() != null) {
				newWallet.setWalletAddress(cryptoResponseModel.getAddress());
			}
			if (cryptoResponseModel.getHexAddress() != null) {
				newWallet.setHexAddress(cryptoResponseModel.getHexAddress());
			}
			if (cryptoResponseModel.getPrivateKey() != null) {
				newWallet.setWalletFileName(cryptoResponseModel.getPrivateKey());
			}
			if (cryptoResponseModel.getEosAccName() != null) {
				newWallet.setEosAccountName(cryptoResponseModel.getEosAccName());
			}
			if (cryptoResponseModel.getWif() != null) {
				newWallet.setWif(cryptoResponseModel.getWif());
			}
			newWallet = walletDao.save(newWallet);
			return newWallet;
		} else {
			throw new CoinNotFoundException("No such coin found: " + coinName);
		}
	}

	/**
	 * Update existing wallet.
	 *
	 * @param cryptoResponseModel the crypto response model
	 * @param wallet              the wallet
	 * @return the wallet
	 */
	private Wallet updateExistingWallet(CryptoResponseModel cryptoResponseModel, Wallet wallet) {
		wallet.setWalletId(wallet.getWalletId());
		if (cryptoResponseModel.getAddress() != null) {
			wallet.setWalletAddress(cryptoResponseModel.getAddress());
		}
		if (cryptoResponseModel.getHexAddress() != null) {
			wallet.setHexAddress(cryptoResponseModel.getHexAddress());
			;
		}
		if (cryptoResponseModel.getPrivateKey() != null) {
			wallet.setWalletFileName(cryptoResponseModel.getPrivateKey());
		}
		if (cryptoResponseModel.getEosAccName() != null) {
			wallet.setEosAccountName(cryptoResponseModel.getEosAccName());
		}
		if (cryptoResponseModel.getWif() != null) {
			wallet.setWif(cryptoResponseModel.getWif());
		}
		wallet = walletDao.save(wallet);
		return wallet;
	}

	@Override
	@Transactional
	public Response<Wallet> getWalletDetailsByCoinNameAndUser(String coinName, Long fkUserId) {

		try {
			Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if (getWalletDetails.isPresent()) {
				return new Response<>(SUCCESS_CODE, WALLET_DETAILS_FOUND_SUCCESSFULLY, getWalletDetails.get());
			} else {
				return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
			}
		} catch (Exception e) {
			throw new WalletNotFoundException(
					"The following wallet for userId: " + fkUserId + "and coinName: " + coinName + "is not found");
		}
	}

	@Override
	@Transactional
	public Response<Map<String, Object>> getUserAllBalanceWithCoinList(Long userId) {
		List<Coin> getAllCoins = coinDao.findAllByIsVisibleTrue();
		if (!getAllCoins.isEmpty()) {

			List<Wallet> getAllUserWallets = walletDao
					.findByFkUserIdAndCoinIsVisibleTrueOrderByWalletBalanceDesc(userId);
			if (getAllUserWallets != null && !getAllUserWallets.isEmpty()) {
				List<Map<String, Object>> getAllCoinsWalletBalance = new ArrayList<>();
				for (Wallet wallet : getAllUserWallets) {
					List<CoinDepositWithdrawal> data1 = coinDepositWithdrawalDao
							.findByCoinTypeAndTxnTypeAndFkUserIdAndStatus(wallet.getCoinName(), "DEPOSIT", userId,
									"CONFIRM");
					List<P2pHistory> data2 = p2pHistoryDao.findByUserIdAndCoinName(userId, wallet.getCoinName());

					List<WalletHistory> walletHistories = walletHistoryDao
							.findByActionAndCoinNameAndUserId("TRADE_CREDIT", wallet.getCoinName(), userId);
					List<InternalTransferAmount> transferAmount = internalTransferAmountDao
							.findByToUserIdAndCoinType(userId, wallet.getCoinName());
					Response<List<ReferalComission>> response = userClient.referCommisionList(userId,
							wallet.getCoinName());
					BigDecimal totalCoinAmount = BigDecimal.ZERO;
					BigDecimal totalCoinValue = BigDecimal.ZERO;
					BigDecimal averageAmount = BigDecimal.ZERO;

					BigDecimal totalP2PCoinAmount = BigDecimal.ZERO;
					BigDecimal totalP2PCoinValue = BigDecimal.ZERO;
					BigDecimal averageP2PAmount = BigDecimal.ZERO;

					BigDecimal totalOrderCoinAmount = BigDecimal.ZERO;
					BigDecimal totalOrderCoinValue = BigDecimal.ZERO;
					BigDecimal averageOrderAmount = BigDecimal.ZERO;

					BigDecimal totalTransferCoinAmount = BigDecimal.ZERO;
					BigDecimal totalTransferCoinValue = BigDecimal.ZERO;
					BigDecimal averageTransferAmount = BigDecimal.ZERO;

					BigDecimal totalReferalCoinAmount = BigDecimal.ZERO;
					BigDecimal totalReferalCoinValue = BigDecimal.ZERO;
					BigDecimal averageReferalAmount = BigDecimal.ZERO;

					if (response.getStatus() == 200) {
						for (ReferalComission history : response.getData()) {
							totalReferalCoinAmount = totalReferalCoinAmount.add(history.getDepositLiveAmount());
							totalReferalCoinValue = totalReferalCoinValue.add(history.getAmount());
						}
						averageReferalAmount = totalReferalCoinAmount.divide(totalReferalCoinValue, 2,
								RoundingMode.HALF_UP);
					}
					if (!transferAmount.isEmpty()) {
						for (InternalTransferAmount history : transferAmount) {
							totalTransferCoinAmount = totalTransferCoinAmount.add(history.getDepositLiveAmount());
							totalTransferCoinValue = totalTransferCoinValue.add(history.getUnits());
						}
						averageTransferAmount = totalTransferCoinAmount.divide(totalTransferCoinValue, 2,
								RoundingMode.HALF_UP);
					}
					if (!walletHistories.isEmpty()) {
						for (WalletHistory history : walletHistories) {
							totalOrderCoinAmount = totalOrderCoinAmount.add(history.getDepositAmount());
							totalOrderCoinValue = totalOrderCoinValue.add(history.getAmount());
						}
						averageOrderAmount = totalOrderCoinAmount.divide(totalOrderCoinValue, 2, RoundingMode.HALF_UP);
					}
					if (!data2.isEmpty()) {
						for (P2pHistory history : data2) {
							totalP2PCoinAmount = totalP2PCoinAmount.add(history.getDepositLiveAmount());
							totalP2PCoinValue = totalP2PCoinValue.add(history.getCoinAmount());
						}
						averageP2PAmount = totalP2PCoinAmount.divide(totalP2PCoinValue, 2, RoundingMode.HALF_UP);
					}
					if (!data1.isEmpty()) {
						for (CoinDepositWithdrawal coinDepositWithdrawal : data1) {
							totalCoinAmount = totalCoinAmount.add(coinDepositWithdrawal.getDepositeCurrentPrice());
							totalCoinValue = totalCoinValue.add(coinDepositWithdrawal.getUnits());
						}
						averageAmount = totalCoinAmount.divide(totalCoinValue, 2, RoundingMode.HALF_UP);

					}

					Map<String, Object> getBalanceAndCoinName = new HashMap<>();
					getBalanceAndCoinName.put("instrument", wallet.getCoinName());
					BigDecimal liveBalace = balance(wallet.getCoinName());
					getBalanceAndCoinName.put("coinName", wallet.getCoin().getCoinFullName());
					getBalanceAndCoinName.put("availableBalance", wallet.getWalletBalance());
					getBalanceAndCoinName.put("investedAmount", wallet.getInvestAmount());
					getBalanceAndCoinName.put("AverageAmount", averageAmount.add(averageP2PAmount)
							.add(averageOrderAmount).add(averageTransferAmount).add(averageReferalAmount));
					getBalanceAndCoinName.put("currentValue", liveBalace.multiply(wallet.getWalletBalance()));
					BigDecimal currentValue = liveBalace.multiply(wallet.getWalletBalance());
					BigDecimal averageValue = (averageAmount.add(averageP2PAmount).add(averageOrderAmount)
							.add(averageTransferAmount).add(averageReferalAmount)).multiply(wallet.getWalletBalance());
					getBalanceAndCoinName.put("totalAverageValue", averageValue);
					System.out.println(averageValue.setScale(2, RoundingMode.HALF_UP));

					if (!averageValue.setScale(2, RoundingMode.HALF_UP).toString().equals("0.00")) {
						BigDecimal data = currentValue.divide(averageValue, MathContext.DECIMAL128);
						BigDecimal dataNew = averageValue.divide(averageValue);
						BigDecimal subTract = data.subtract(dataNew);
						System.out.println(subTract.multiply(new BigDecimal(100)));
						getBalanceAndCoinName.put("profitOrLoss", subTract.multiply(new BigDecimal(100)));

					} else if (averageValue.setScale(2, RoundingMode.HALF_UP).toString().equals("0.00")) {
						getBalanceAndCoinName.put("profitOrLoss", BigDecimal.ZERO);
					}
					getBalanceAndCoinName.put("inOrderBalance", wallet.getBlockedBalance());

					getBalanceAndCoinName.put("totalBalance",
							wallet.getWalletBalance().add(wallet.getBlockedBalance()));
					getBalanceAndCoinName.put("livePrice", totalCoinAmount.add(totalP2PCoinAmount)
							.add(totalOrderCoinAmount).add(totalTransferCoinAmount).add(totalReferalCoinAmount));
					getAllCoinsWalletBalance.add(getBalanceAndCoinName);

				}
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("coinList", getAllCoins);
				responseMap.put("userBalance", getAllCoinsWalletBalance);
				return new Response<>(SUCCESS_CODE, USER_BALANCE_AND_COINLIST_FETCHED_SUCCESSFULLY, responseMap);
			} else {
				return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	public BigDecimal balance(String coiname) {

		try {
			String apiBaseUri1 = "http://3.213.77.140:3015/eth/getMarketPriceById?ids=" + coiname;
			String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, USDT_TIMEOUT);
//			LOGGER.info(responseString1);
			Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1, Map.class);
			return BigDecimal.valueOf(Double.valueOf(String.valueOf(allData1.get("USD"))));
		} catch (Exception e) {
//			LOGGER.catching(e);
			return DEFAULT_BALANCE;
		}
	}

	@Override
	@Transactional
	public Response<List<Map<String, Object>>> getAllUserAddress(Long userId) {
		List<Wallet> getAllWalletList = walletDao.findByFkUserId(userId);
		if (getAllWalletList != null && !getAllWalletList.isEmpty()) {
			List<Map<String, Object>> responseList = new ArrayList<>();
			for (Wallet wallet : getAllWalletList) {
				if (wallet.getWalletAddress() != null) {
					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put("cryptoAddress", wallet.getWalletAddress());
					responseMap.put("coinName", wallet.getCoinName());
					responseList.add(responseMap);
				}
			}
			return new Response<>(SUCCESS_CODE, ALL_USER_ADDRESS_FETCHED_SUCCESSFULLY, responseList);
		} else {
			throw new CoinNotFoundException("No coins address found wallet" + "");
		}
	}

	@Override
	@Transactional
	public Response<String> updateWallet(BigDecimal walletBalance, Long userId, String coinName) {

		try {
			Optional<Wallet> data = walletDao.findByCoinNameAndFkUserId(coinName, userId);
			if (data.isPresent()) {
				data.get().setWalletBalance(walletBalance);
				walletDao.save(data.get());
				System.out.println("Hello");
				return new Response<>(SUCCESS_CODE, "WALLET_updated_SUCCESSFULLY");
			} else
				return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);

		} catch (Exception e) {
			LOGGER.catching(e);
			throw new CoinNotFoundException("No coins found to update wallet" + "");

		}

	}

	@Override
	@Transactional
	public Response<String> updateWalletNew(BigDecimal walletBalance, Long userId, String coinName) {

		try {
			Optional<Wallet> data = walletDao.findByCoinNameAndFkUserId(coinName, userId);
			if (data.isPresent()) {
				data.get().setWalletBalance(data.get().getWalletBalance().add(walletBalance));
				walletDao.save(data.get());
				return new Response<>(SUCCESS_CODE, "WALLET_updated_SUCCESSFULLY");
			} else
				return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);

		} catch (Exception e) {
			LOGGER.catching(e);
			throw new CoinNotFoundException("No coins found to update wallet" + "");

		}

	}

	@Override
	@Transactional
	public Response<String> getAddressforERC20Tokens(String coinName, Long fkUserId) {
		Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
		if (getWalletDetails.isPresent()) {
			if (getWalletDetails.get().getWalletAddress() != null)
				return new Response<>(SUCCESS_CODE, SUCCESS, getWalletDetails.get().getWalletAddress());
			else {
				Optional<Wallet> getEthWallet = walletDao.findByCoinNameAndFkUserId(ETH, fkUserId);
				if (getEthWallet.isPresent() && getEthWallet.get().getWalletAddress() != null) {
					getWalletDetails.get().setWalletAddress(getEthWallet.get().getWalletAddress());
					getWalletDetails.get().setWalletFileName(getEthWallet.get().getWalletFileName());
					walletDao.save(getWalletDetails.get());
					return new Response<>(SUCCESS_CODE, SUCCESS, getWalletDetails.get().getWalletAddress());
				} else
					return new Response<>(FAILURE_CODE,
							"Please generate the ETH wallet 1st to generate " + coinName + " wallet.");
			}
		} else
			return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
	}

	@Override
	@Transactional
	public Response<Map<String, Object>> getAllUserBalanceAndCoinListWithName(Long userId) {
		List<Coin> getAllCoins = coinDao.findAll();
		if (!getAllCoins.isEmpty()) {
			// getUserByUserId
			Response<UserEmailAndNameDto> userData = userClient.getUserByUserId(userId);

			UserEmailAndNameDto userDetail = userData.getData();
			List<Wallet> getAllUserWallets = walletDao.findByFkUserId(userId);
			if (getAllUserWallets != null && !getAllUserWallets.isEmpty()) {
				List<Map<String, Object>> getAllCoinsWalletBalance = new ArrayList<>();
				for (Wallet wallet : getAllUserWallets) {
					Map<String, Object> getBalanceAndCoinName = new HashMap<>();
					getBalanceAndCoinName.put("instrument", wallet.getCoinName());
					getBalanceAndCoinName.put("coinName", wallet.getCoin().getCoinFullName());
					getBalanceAndCoinName.put("availableBalance", wallet.getWalletBalance());
					getBalanceAndCoinName.put("inOrderBalance", wallet.getBlockedBalance());
					getBalanceAndCoinName.put("totalBalance",
							wallet.getWalletBalance().add(wallet.getBlockedBalance()));
					getAllCoinsWalletBalance.add(getBalanceAndCoinName);
				}
				Map<String, Object> responseMap = new HashMap<>();
				responseMap.put("userDetails", userDetail);
				responseMap.put("userBalance", getAllCoinsWalletBalance);
				return new Response<>(SUCCESS_CODE, USER_BALANCE_AND_COINLIST_FETCHED_SUCCESSFULLY, responseMap);
			} else {
				return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, COIN_NOT_FOUND);
		}
	}

	@Transactional
	@Override
	public Response<Map<String, Object>> saveInternalAmountTransfer(Long userId, String randomId, BigDecimal amount,
			String coinName) {
		Optional<StorageDetail> adminStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(coinName,
				STORAGE_HOT);
		Response<UserEmailAndNameDto> adminEmail = userClient.getUserByUserId(userId);
		Optional<Coin> coinData = coinDao.findByCoinShortName("XINDIA");
		if (adminStorageDetails.isPresent()) {
			Optional<Wallet> toUserWalletCheck = null;
			if (adminStorageDetails.get().getHotWalletBalance().compareTo(amount) > 0) {
				toUserWalletCheck = walletDao.findByCoinNameAndRandomId(coinName, randomId);
				Response<UserEmailAndNameDto> adminEmail2 = userClient
						.getUserByUserId(toUserWalletCheck.get().getFkUserId());
				if (toUserWalletCheck.isPresent()) {

					Optional<Wallet> fromUserWalletCheck = walletDao.findByCoinNameAndFkUserId(coinName, userId);
					if (fromUserWalletCheck.isPresent()) {
						if (fromUserWalletCheck.get().getWalletBalance().compareTo(amount) > 0 && fromUserWalletCheck
								.get().getWalletBalance().compareTo(coinData.get().getInternalTransferFee()) == 1) {
							BigDecimal actualAmountToDeducted = fromUserWalletCheck.get().getWalletBalance()
									.subtract(amount);
							BigDecimal actualAmountToCredited = toUserWalletCheck.get().getWalletBalance().add(amount);
							fromUserWalletCheck.get().setWalletBalance(actualAmountToDeducted);
							toUserWalletCheck.get().setWalletBalance(actualAmountToCredited);

							Optional<Wallet> walletData = walletDao.findByCoinNameAndFkUserId("XINDIA", userId);
							Optional<StorageDetail> storageData = storageDetailsDao.findByCoinType("XINDIA");
							if (coinData.isPresent()) {
								if (walletData.isPresent()) {
									if (storageData.isPresent()) {
										walletData.get().setWalletBalance(walletData.get().getWalletBalance()
												.subtract(coinData.get().getInternalTransferFee()));
										storageData.get().setHotWalletBalance(storageData.get().getHotWalletBalance()
												.add(coinData.get().getInternalTransferFee()));
										storageDetailsDao.save(storageData.get());
										walletDao.save(walletData.get());
									}
								}
							}
							InternalTransferDto internalTransferAmounts = new InternalTransferDto();
							InternalTransferAmount internalTransferAmount = modelMapper.map(internalTransferAmounts,
									InternalTransferAmount.class);
							try {
								String apiBaseUri1 = "http://3.213.77.140:3015/eth/getMarketPriceById?ids="
										+ coinName.toLowerCase();
								String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, USDT_TIMEOUT);
								LOGGER.info(responseString1);
								Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1,
										Map.class);
								if (allData1.containsKey("USD")) {
									LOGGER.info(allData1.get("USD"));
									BigDecimal value = BigDecimal
											.valueOf(Double.valueOf(String.valueOf(allData1.get("USD"))));
									LOGGER.info(value);
									internalTransferAmount.setLiveAmount(value);
									internalTransferAmount.setDepositLiveAmount((amount.multiply(value)));
								}
							} catch (Exception e) {
								throw new RuntimeException(e);
							}
							walletDao.save(fromUserWalletCheck.get());
							walletDao.save(toUserWalletCheck.get());
							internalTransferAmount.setTransferFee(coinData.get().getInternalTransferFee());
							internalTransferAmount.setToaddress(toUserWalletCheck.get().getWalletAddress());
							internalTransferAmount.setFromaddress(fromUserWalletCheck.get().getWalletAddress());
							internalTransferAmount.setCoinType(coinName);
							internalTransferAmount.setUnits(amount);
							internalTransferAmount.setTxnTime(new Date());
							internalTransferAmount.setRandomId(randomId);
							internalTransferAmount.setTxnType("INTERNAL_TRANSFER");
							internalTransferAmount.setStatus(COMPLETE);
							internalTransferAmount.setFromUserId(fromUserWalletCheck.get().getFkUserId());
							internalTransferAmount.setToUserId(toUserWalletCheck.get().getFkUserId());
							Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess1(
									adminEmail.getData().getName(), amount, coinName,
									toUserWalletCheck.get().getWalletAddress(), adminEmail.getData().getEmail());
							mailSender.sendMailToApproveWithdrawSuccess1(sendMailData);
							if (isNotification) {
								Map<String, String> setData = new HashMap<>();
								setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
								setData.put(EmailConstants.COIN_TOKEN, coinName);
								setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(amount));
								setData.put(EmailConstants.COIN_ADDRESS_TOKEN,
										toUserWalletCheck.get().getWalletAddress());
								EmailDto emailDto = new EmailDto(userId, "internal_transfer", "en", "email", setData);
								notificationClient.sendNotification(emailDto);
							}
							Map<String, Object> sendMailData2 = setEmailDataForWithdrawSuccess2(
									adminEmail2.getData().getName(), amount, coinName,
									fromUserWalletCheck.get().getWalletAddress(), adminEmail2.getData().getEmail());
							mailSender.sendMailToApproveWithdrawSuccess2(sendMailData2);
							if (isNotification) {
								Map<String, String> setData = new HashMap<>();
								setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
								setData.put(EmailConstants.COIN_TOKEN, coinName);
								setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(amount));
								setData.put(EmailConstants.COIN_ADDRESS_TOKEN,
										fromUserWalletCheck.get().getWalletAddress());
								EmailDto emailDto = new EmailDto(toUserWalletCheck.get().getFkUserId(),
										"internal_transfer_receiver", "en", "email", setData);
								notificationClient.sendNotification(emailDto);
							}
							internalTransferAmountDao.save(internalTransferAmount);
							return new Response<>(SUCCESS_CODE, AMOUNT_TRANSFERED_SUCCESSFULLY);
						} else {
							return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
						}
					} else {
						return new Response<>(FAILURE_CODE, FROM_USER_ID_WALLET_NOT_FOUND);
					}
				} else {
					return new Response<>(FAILURE_CODE, TO_USER_ID_WALLET_NOT_FOUND);
				}
			} else {
				return new Response<>(FAILURE_CODE, INSUFFICIENT_STORAGE_BALANCE);
			}
		} else {
			return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
		}
	}

	private Map<String, Object> setEmailDataForWithdrawSuccess1(String userName, BigDecimal amount, String coinType,
			String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.INERNAL_TRANSFER);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	private Map<String, Object> setEmailDataForWithdrawSuccess2(String userName, BigDecimal amount, String coinType,
			String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.INERNAL_TRANSFER);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	@Transactional
	@Override
	public Response<Object> UserToAdminTransfer(Long userId, UserToAdminTransferDto userToAdminTransferDto) {
		Optional<StorageDetail> adminStorageDetails = storageDetailsDao
				.findByCoinTypeAndStorageType(userToAdminTransferDto.getBaseCoin(), STORAGE_HOT);
		if (adminStorageDetails.isPresent()) {
			Optional<Wallet> fromUserWalletCheck = walletDao
					.findByCoinNameAndFkUserId(userToAdminTransferDto.getBaseCoin(), userId);
			Optional<Wallet> toAddress = null;

			toAddress = walletDao.findByCoinNameAndRandomId(userToAdminTransferDto.getBaseCoin(),
					userToAdminTransferDto.getToRandomId());
			if (userId.equals(toAddress.get().getFkUserId()))
				return new Response<>(FAILURE_CODE, "You Can't Send Self Request.");

			if (!toAddress.isPresent())
				return new Response<>(FAILURE_CODE, "Receiver address not found.");

			if (fromUserWalletCheck.isPresent()) {
				UserToAdminTransfer userToAdminTransfer = new UserToAdminTransfer();
				userToAdminTransfer.setUser1Id(userId);
				userToAdminTransfer.setUser2Id(toAddress.get().getFkUserId());
				userToAdminTransfer.setAmount(userToAdminTransferDto.getAmount());
				userToAdminTransfer.setBaseCoin(userToAdminTransferDto.getBaseCoin());
				userToAdminTransfer.setToAddress(userToAdminTransferDto.getToAddress());
				userToAdminTransfer.setFromAddress(fromUserWalletCheck.get().getWalletAddress());

				userToAdminTransfer.setToTagId(userToAdminTransferDto.getTagId());
				userToAdminTransfer.setUser1Status("WAITING");
				userToAdminTransfer.setUser2Status("PENDING");

				userToAdminTransfer.setTxnType("ESCROW_TRANSFER");
				userToAdminTransfer.setToRandomId(userToAdminTransferDto.getToRandomId());
				userToAdminTransfer.setCreationTime(new Date());

				userToAdminTransferDao.save(userToAdminTransfer);

				adminStorageDetails.get().setHotWalletBalance(
						adminStorageDetails.get().getHotWalletBalance().add(userToAdminTransferDto.getAmount()));
				fromUserWalletCheck.get().setWalletBalance(
						fromUserWalletCheck.get().getWalletBalance().subtract(userToAdminTransferDto.getAmount()));
				walletDao.save(fromUserWalletCheck.get());
				storageDetailsDao.save(adminStorageDetails.get());

				return new Response<>(SUCCESS_CODE, AMOUNT_TRANSFERED_SUCCESSFULLY_TO_ADMIN);
			} else {
				return new Response<>(FAILURE_CODE, FROM_USER_ID_WALLET_NOT_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
		}
	}

	@Override
	public Response<List<UserToAdminTransfer>> getUserToAdminSendAmountDetails(Long page, Long p) {
		List<UserToAdminTransfer> findData = userToAdminTransferDao.findAll();
		return new Response<>(SUCCESS_CODE, "find all user1 request", findData);
	}

	@Transactional
	@Override
	public Response<Object> adminToUser2Transfer(Long referenceId) {
		Optional<UserToAdminTransfer> findAmountToSend = userToAdminTransferDao
				.findByUserToAdminTransferId(referenceId);

		if (findAmountToSend.isPresent() && findAmountToSend.get().getUser1Status().equals("PAID")
				&& findAmountToSend.get().getUser2Status().equals("WAITING_FOR_RELEASE")) {
			Optional<StorageDetail> adminStorageDetails = storageDetailsDao
					.findByCoinTypeAndStorageType(findAmountToSend.get().getBaseCoin(), STORAGE_HOT);
			if (adminStorageDetails.isPresent()) {

				Optional<Wallet> toUserWalletCheck = walletDao.findByCoinNameAndFkUserId(
						findAmountToSend.get().getBaseCoin(), findAmountToSend.get().getUser2Id());
				if (toUserWalletCheck.isPresent()) {

					toUserWalletCheck.get().setWalletBalance(
							toUserWalletCheck.get().getWalletBalance().add(findAmountToSend.get().getAmount()));
					walletDao.save(toUserWalletCheck.get());
					adminStorageDetails.get().setHotWalletBalance(adminStorageDetails.get().getHotWalletBalance()
							.subtract(findAmountToSend.get().getAmount()));
					storageDetailsDao.save(adminStorageDetails.get());
					findAmountToSend.get().setUser1Status("COMPLETED");
					findAmountToSend.get().setUser2Status("COMPLETED");

					userToAdminTransferDao.save(findAmountToSend.get());
					return new Response<>(SUCCESS_CODE, "Amount Send Successfully");
				} else {
					return new Response<>(FAILURE_CODE, TO_USER_ID_WALLET_NOT_FOUND);
				}
			} else {
				return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, "Request already denied");

		}

	}

	@Transactional
	@Override
	public Response<Object> adminToUser2TransferByAdmin(Long referenceId) {
		Optional<UserToAdminTransfer> findAmountToSend = userToAdminTransferDao
				.findByUserToAdminTransferId(referenceId);

		if (findAmountToSend.isPresent() && findAmountToSend.get().getUser1Status().equals("DISPUTED")
				&& findAmountToSend.get().getUser2Status().equals("DISPUTED")) {
			Optional<StorageDetail> adminStorageDetails = storageDetailsDao
					.findByCoinTypeAndStorageType(findAmountToSend.get().getBaseCoin(), STORAGE_HOT);
			if (adminStorageDetails.isPresent()) {

				Optional<Wallet> toUserWalletCheck = walletDao.findByCoinNameAndFkUserId(
						findAmountToSend.get().getBaseCoin(), findAmountToSend.get().getUser2Id());
				if (toUserWalletCheck.isPresent()) {

					toUserWalletCheck.get().setWalletBalance(
							toUserWalletCheck.get().getWalletBalance().add(findAmountToSend.get().getAmount()));
					walletDao.save(toUserWalletCheck.get());
					adminStorageDetails.get().setHotWalletBalance(adminStorageDetails.get().getHotWalletBalance()
							.subtract(findAmountToSend.get().getAmount()));
					storageDetailsDao.save(adminStorageDetails.get());
					findAmountToSend.get().setUser1Status("COMPLETED");
					findAmountToSend.get().setUser2Status("COMPLETED");

					userToAdminTransferDao.save(findAmountToSend.get());
					return new Response<>(SUCCESS_CODE, "Amount Send Successfully");
				} else {
					return new Response<>(FAILURE_CODE, TO_USER_ID_WALLET_NOT_FOUND);
				}
			} else {
				return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
			}
		} else {
			return new Response<>(FAILURE_CODE, "Request already denied");

		}

	}

	@Override
	public Response<Date> getLastTransactionOfUser(Long userId) {
		Optional<CoinDepositWithdrawal> transactionData = coinDepositeWithrawlDao.findTopByFkUserId(userId);
		if (transactionData.isPresent()) {
			Date lastTransactionDate = transactionData.get().getTxnTime();
			return new Response<>(200, "User Last Transaction Detail Fetched", lastTransactionDate);
		} else {
			return new Response<>(205, "User Last Transaction Detail Not Fetched");
		}

	}

	@Override
	public Response<Object> historyOfTransferAmountPerticularUser(Long userId, BigDecimal amount, String coinName,
			Integer page, Integer pageSize, Long fromdate, Long toDate) {

		StringBuilder query = new StringBuilder(
				"select c.txnId, c.coinType, c.status,c.units, c.txnTime, c.txnType,c.fromUserId,c.fromaddress,c.toaddress,c.randomId from InternalTransferAmount c");
		List<String> conditions = new ArrayList<>();

		if (userId != null && !userId.equals(BLANK)) {
			conditions.add(" c.fromUserId=:userId ");
		}
		if (coinName != null && !coinName.equals(BLANK)) {
			conditions.add(" c.coinType=:coinName ");
		}

		if (fromdate != null && !String.valueOf(fromdate).equals(BLANK)) {
			conditions.add(" c.txnTime >= :fromdate ");
		}
		if (toDate != null && !String.valueOf(toDate).equals(BLANK)) {
			conditions.add(" c.txnTime <= :toDate ");
		}
		if (amount != null && !amount.equals(BLANK)) {
			conditions.add("c.units=:amount");
		}
		if (!conditions.isEmpty()) {
			query.append(" where ");
			query.append(String.join(" and ", conditions.toArray(new String[0])));
		}
		query.append(" order by c.txnId desc");
		Query createQuery = em.createQuery(query.toString());
		if (userId != null && !userId.equals(BLANK))
			createQuery.setParameter("userId", userId);

		if (coinName != null && !coinName.equals(BLANK))
			createQuery.setParameter("coinName", coinName);

		if (fromdate != null && !String.valueOf(fromdate).equals(BLANK))
			createQuery.setParameter("fromdate", new Date(fromdate));
		if (toDate != null && !String.valueOf(toDate).equals(BLANK))
			createQuery.setParameter("toDate", new Date(toDate));
		if (amount != null && !amount.equals(BLANK)) {
			createQuery.setParameter("amount", amount);
		}
		int filteredResultCount = createQuery.getResultList().size();
		createQuery.setFirstResult(page * pageSize);
		createQuery.setMaxResults(pageSize);
		List<Object[]> list = createQuery.getResultList();
		List<TransferListDto> response = list.parallelStream().map(o -> {
			TransferListDto dto = new TransferListDto();
			dto.setTxnId((Long) o[0]);
			dto.setCoinType((String) o[1]);
			dto.setStatus((String) o[2]);
			dto.setAmount((BigDecimal) o[3]);
			dto.setTxnTime((Date) o[4]);
			dto.setTxnType((String) o[5]);
			dto.setFromUserId((Long) o[6]);
			dto.setFromAddress((String) o[7]);
			dto.setToAddress((String) o[8]);
			dto.setRandomId((String) o[9]);
			return dto;
		}).collect(Collectors.toList());
		Map<String, Object> data = new HashMap<>();
		data.put(RESULT_LIST, response);
		data.put(TOTAL_COUNT, filteredResultCount);

		return new Response<>(SUCCESS_CODE, SUCCESS, data);
	}

	@Override
	public Response<List<UserToAdminTransfer>> escrowTransferAmount(Long userId, String status,
			Long userToAdminTransferId) {

		if (status != null && status.equalsIgnoreCase("SEND")) {
			List<UserToAdminTransfer> data = userToAdminTransferDao.findByUser1Id(userId);

			if (userToAdminTransferId == null) {
				data = userToAdminTransferDao.findByUser1Id(userId);
				return new Response<>(SUCCESS_CODE, "Your transfer details found successfully", data);
			} else {
				return new Response<>(SUCCESS_CODE, "Your transfer details found successfully",
						data.parallelStream().filter(a -> userToAdminTransferId.equals(a.getUserToAdminTransferId()))
								.collect(Collectors.toList()));
			}
		} else if (status != null && status.equalsIgnoreCase("RECEIVE")) {
			List<UserToAdminTransfer> data = userToAdminTransferDao.findByUser2Id(userId);
			if (userToAdminTransferId == null) {
				data = userToAdminTransferDao.findByUser2Id(userId);
				return new Response<>(SUCCESS_CODE, "Your transfer details found successfully", data);
			} else {

				return new Response<>(SUCCESS_CODE, "Your transfer details found successfully",
						data.parallelStream().filter(a -> userToAdminTransferId.equals(a.getUserToAdminTransferId()))
								.collect(Collectors.toList()));
			}
		} else {
			return new Response<>(FAILURE_CODE, "Your transfer details not found ");
		}
	}

	@Override
	public Response<Map<String, Object>> allEscrowTransferAmount(Long userId) {
		try {
			Map<String, Object> TransferData = new HashMap<>();
			List<UserToAdminTransfer> data = userToAdminTransferDao.findByUser1Id(userId);
			List<UserToAdminTransfer> data1 = userToAdminTransferDao.findByUser2Id(userId);

			TransferData.put("RECEIVE", data1);
			TransferData.put("SEND", data);
			return new Response<>(SUCCESS_CODE, "Your transfer details found successfully", TransferData);
		} catch (Exception e) {
			return new Response<>(FAILURE_CODE, "transaction not found.Something went wrong.");
		}

	}

	@Override
	public Response<Object> requestSendToUser1FromUser2(Long userId, Long userToAdminTransferId) {
		if (userToAdminTransferId != null) {
			Optional<UserToAdminTransfer> data = userToAdminTransferDao.findByUser2IdAndUserToAdminTransferId(userId,
					userToAdminTransferId);
			if (!data.isPresent())
				return new Response<>(FAILURE_CODE, "transaction not found");
			else {
				if (data.get().getUser1Status().equals("CANCELLED") || data.get().getUser2Status().equals("CANCELLED"))
					return new Response<>(FAILURE_CODE, "transaction already cancel.");
				data.get().setUser1Status("PAID");
				data.get().setUser2Status("WAITING_FOR_RELEASE");

				userToAdminTransferDao.save(data.get());
				return new Response<>(SUCCESS_CODE, "request accepted for release coins");
			}
		} else
			return new Response<>(FAILURE_CODE, "transaction not found");

	}

	@Override
	public Response<Object> getInternaltransferHistory() {
		List<InternalTransferAmount> userData = internalTransferAmountDao.findAll();
		if (userData.isEmpty()) {
			return new Response<>(205, "No records Found");

		}
		return new Response<>(200, "User Details Fetched", userData);
	}

	@Override
	@Transactional
	public Response<Map<String, BigDecimal>> getBalanceInUsd(String coinName, String converterCoin, Long fkUserId) {
		Map<String, BigDecimal> balance = new HashMap<>();
		BigDecimal walletBalanceInBTC = new BigDecimal(0);
		BigDecimal balanceInUsd = new BigDecimal(0);

		try {

			List<Coin> s = coinDao.findAll();
			for (Coin a : s) {

				a.getCoinShortName();
				Wallet walletBalance = getWalletDetailsByCoinName(a.getCoinShortName(), fkUserId);
				if (walletBalance == null) {
					return new Response<>(FAILURE_CODE, "Wallet not found");
				}
				Optional<MarketPrice> MarketPrice = marketPriceDao.findByCoinName(a.getCoinShortName());

				if (MarketPrice.isPresent()) {
					MarketPrice priceInBTC1 = MarketPrice.get();

					walletBalanceInBTC = walletBalanceInBTC
							.add(walletBalance.getWalletBalance().multiply(priceInBTC1.getPriceInBtc()));

				}
			}
			balance.put("balanceInBtc", walletBalanceInBTC);
			Optional<MarketPrice> MarketPrice = marketPriceDao.findByCoinName("BTC");
			balanceInUsd = MarketPrice.get().getPriceInUsd().multiply(walletBalanceInBTC);
			balance.put("balanceInUSD", balanceInUsd);

			return new Response<>(SUCCESS_CODE, SUCCESS, balance);

		} catch (Exception e) {
			throw new WalletNotFoundException(
					"No wallet found for coin: " + coinName + " and userId: " + fkUserId + "");
		}
	}

	@Override
	@Transactional
	public Response<Object> requestCancel(Long userId, Long userToAdminTransferId) {
		Optional<UserToAdminTransfer> cancelData = userToAdminTransferDao
				.findByUserToAdminTransferId(userToAdminTransferId);
		if (!cancelData.isPresent())
			return new Response<>(FAILURE_CODE, "Data not found for this id");

		Optional<UserToAdminTransfer> findAmountToSend = userToAdminTransferDao
				.findByUserToAdminTransferId(userToAdminTransferId);

		if (findAmountToSend.isPresent() && findAmountToSend.get().getUser1Status().equals("WAITING")
				|| findAmountToSend.get().getUser2Status().equals("PENDING")) {
			Optional<StorageDetail> adminStorageDetails = storageDetailsDao
					.findByCoinTypeAndStorageType(findAmountToSend.get().getBaseCoin(), "HOT");
			if (adminStorageDetails.isPresent()) {

				Optional<Wallet> UserUserWalletCheck = walletDao.findByCoinNameAndFkUserId(
						findAmountToSend.get().getBaseCoin(), findAmountToSend.get().getUser1Id());
				if (UserUserWalletCheck.isPresent()) {
					cancelData.get().setUser1Status("CANCELLED");
					cancelData.get().setUser2Status("CANCELLED");

					userToAdminTransferDao.save(cancelData.get());
					UserUserWalletCheck.get().setWalletBalance(
							UserUserWalletCheck.get().getWalletBalance().add(findAmountToSend.get().getAmount()));
					walletDao.save(UserUserWalletCheck.get());
					adminStorageDetails.get().setHotWalletBalance(adminStorageDetails.get().getHotWalletBalance()
							.subtract(findAmountToSend.get().getAmount()));
					storageDetailsDao.save(adminStorageDetails.get());
					userToAdminTransferDao.save(findAmountToSend.get());
					return new Response<>(200, "amount return successfully");
				} else {
					return new Response<>(201, "TO_USER_ID_WALLET_NOT_FOUND");
				}
			} else {
				return new Response<>(201, "NO_STORAGE_DETAILS_FOUND");
			}
		} else {
			return new Response<>(201, "REFERENCEID_DETAILS_FOUND");

		}

	}

	@Override
	public Response<Object> disputeTrade(Long userId, Long referenceId) {
		Optional<UserToAdminTransfer> optData = userToAdminTransferDao.findByUserToAdminTransferId(referenceId);
		if (optData.isPresent()) {
			if (userId == optData.get().getUser1Id())
				return new Response<>(205, "You Can't Raise Dispute");

			if (optData.get().getUser1Status().equals("PAID")
					&& optData.get().getUser2Status().equals("WAITING_FOR_RELEASE")) {
				optData.get().setUser1Status("DISPUTED");
				optData.get().setUser2Status("DISPUTED");

				optData.get().setUpdationTime(new Date());
				userToAdminTransferDao.save(optData.get());
				return new Response<>(SUCCESS_CODE, "Trade Disputed successfully");

			} else if (optData.get().getUser1Status() == "DISPUTE")
				return new Response<>(205, "Trade Already Disputed");

		}
		return new Response<>(205, "No records Found");
	}

	@Override
	public Response<Object> getEscrowList(Integer page, Integer pageSize) {
		Page<UserToAdminTransfer> list = null;
		list = userToAdminTransferDao.findAll(PageRequest.of(page, pageSize, Direction.DESC, "userToAdminTransferId"));
		Long count = userToAdminTransferDao.count();
		Map<String, Object> map = new HashMap<>();
		map.put("list", list);
		map.put("count", count);
		if (list.hasContent())
			return new Response<>(SUCCESS_CODE, "Trade Disputed successfully", map);
		else
			return new Response<>(205, "No records Found");

	}

	@Override
	public Response<Object> totalbalance(Long fkUserId) {
		List<Wallet> historyDetails = walletDao.findByFkUserId(fkUserId);
		if (!historyDetails.isEmpty()) {
			List<Map<String, Object>> d = new ArrayList<>();
			BigDecimal count1 = BigDecimal.ZERO;
			for (Wallet buyOrder : historyDetails) {
				count1 = (count1.add(buyOrder.getWalletBalance()));
			}

			return new Response<>(200, "Balance Details Fetched Successfully", count1);
		} else {
			return new Response<>(205, "Details Not Found");
		}
	}

	@Override
	public Response<Object> totalblockedbalance(Long fkUserId) {
		List<Wallet> historyDetails = walletDao.findByFkUserId(fkUserId);
		if (!historyDetails.isEmpty()) {
			List<Map<String, Object>> d = new ArrayList<>();
			BigDecimal count1 = BigDecimal.ZERO;
			for (Wallet buyOrder : historyDetails) {
				count1 = (count1.add(buyOrder.getBlockedBalance()));
			}

			return new Response<>(200, "Blocked Balance Details Fetched Successfully", count1);
		} else {
			return new Response<>(205, "Details Not Found");
		}
	}

	@Override
	public Response<Object> totalbalanceSubtractedFromtotalblockedbalance(Long fkUserId) {
		List<Wallet> historyDetails = walletDao.findByFkUserId(fkUserId);
		if (!historyDetails.isEmpty()) {
			List<Map<String, Object>> d = new ArrayList<>();
			BigDecimal count1 = BigDecimal.ZERO;
			BigDecimal count2 = BigDecimal.ZERO;
			BigDecimal count3 = BigDecimal.ZERO;
			for (Wallet buyOrder : historyDetails) {
				count1 = (count1.add(buyOrder.getWalletBalance()));
				count2 = (count2.add(buyOrder.getBlockedBalance()));
				count3 = (count1.subtract(count2));
			}

			return new Response<>(200, "Balance Subtracted Successfully", count3);
		} else {
			return new Response<>(205, "Details Not Found");

		}

	}

	@Override
	public Response<Object> percentage(Long userId) {
		List<Wallet> historyDetails = walletDao.findByFkUserId(userId);
		if (!historyDetails.isEmpty()) {
			BigDecimal count1 = BigDecimal.ZERO;
			BigDecimal count2 = BigDecimal.ZERO;
			BigDecimal count3 = BigDecimal.ZERO;
			BigDecimal count4 = BigDecimal.ZERO;

			BigDecimal _a = new BigDecimal("100");
			BigDecimal _b = new BigDecimal("0");
			for (Wallet buyOrder : historyDetails) {
				count1 = (count1.add(buyOrder.getWalletBalance()));

				count2 = (count2.add(buyOrder.getBlockedBalance()));

				if (count1.compareTo(new BigDecimal("0.00")) == 0 && count2.compareTo(new BigDecimal("0.00")) == 0) {

					return new Response<>(200, "Percentage Fetched Successfully", _b);

				}

				else if (count2.compareTo(BigDecimal.ZERO) > count1.compareTo(BigDecimal.ZERO)) {

					return new Response<>(BAD_REQUEST_CODE, "Your Balance is Low");

				} else {

					count3 = (count1.subtract(count2));

					count4 = ((count3.divide(count1, 2, RoundingMode.HALF_UP)).multiply(_a));

				}

			}
			return new Response<>(200, "Percentage Fetched Successfully", count4.intValue());
		} else {
			return new Response<>(205, "Details Not Found");

		}

	}

	@Override
	public Response<Object> getReferalTier1(String myReferralCode, Long userId) {
		Response<UserEmailAndNameDto> userData = userClient.getUserByUserId(userId);
		if (userData.getStatus() == 200) {

		}
		return null;
	}

	@Override
	public Response<Object> depositInr(Long userId, FiatDto fiatDto) {
		Response<UserEmailAndNameDto> userData = userClient.getUserByUserId(userId);

		UserEmailAndNameDto userDetail = userData.getData();
		Fiat fiat = new Fiat();

		fiat.setUserId(userId);
		fiat.setCreateTime(fiatDto.getCreateTime());
		fiat.setAmount(fiatDto.getAmount());
		fiat.setUtrNo(fiatDto.getUtrNo());
		fiat.setEmail(userDetail.getEmail());
		fiat.setName(userDetail.getName());
		fiat.setFiatStatus(FiatStatus.PENDING);
		fiat.setImage(fiatDto.getImage());
		fiat.setTransactionType(TransactionType.DEPOSIT);

		Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess3(userData.getData().getName(),
				fiatDto.getAmount(), "INR", fiatDto.getUtrNo(), userData.getData().getEmail());
		mailSender.sendMailToApproveWithdrawSuccess3(sendMailData);
		if (isNotification) {
			Map<String, String> setData = new HashMap<>();
			setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
			setData.put(EmailConstants.COIN_TOKEN, "INR");
			setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(fiatDto.getAmount()));
			setData.put(EmailConstants.COIN_ADDRESS_TOKEN, fiatDto.getUtrNo());
			EmailDto emailDto = new EmailDto(userId, "deposit_inr", "en", "email", setData);
			notificationClient.sendNotification(emailDto);
		}
		fiatDao.save(fiat);
		return new Response<>(200, "Deposit Successfully");

	}

	private Map<String, Object> setEmailDataForWithdrawSuccess3(String userName, BigDecimal amount, String coinType,
			String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.DEPOSIT_REQUEST);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	private Map<String, Object> setEmailDataForWithdrawSuccess5(String userName, BigDecimal amount, String coinType,
			String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.DEPOSIT_SUCCESS);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	private Map<String, Object> setEmailDataForWithdrawSuccess6(String userName, BigDecimal amount, String coinType,
			String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.DEPOSIT_REJECTED);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	private Map<String, Object> setEmailDataForWithdrawSuccess4(String userName, BigDecimal amount, String coinType,
			String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.WITHDRAW_REQUEST_INR);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	private Map<String, Object> setEmailDataForWithdrawSuccess7(String userName, BigDecimal amount, String coinType,
			String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.WITHDRAW_SUCCESS_INR);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	private Map<String, Object> setEmailDataForWithdrawSuccess8(String userName, BigDecimal amount, String coinType,
			String toAddress, String toEmail) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, toEmail);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.WITHDRAW_REJECT_INR);
		sendMailData.put(AMOUNT, amount);
		sendMailData.put(EmailConstants.COIN_TYPE, coinType);
		sendMailData.put(EmailConstants.TO_ADDRESS, toAddress);
		sendMailData.put(EmailConstants.USER_NAME, userName);
		return sendMailData;
	}

	@Override
	public Response<Object> getListDeposit(Long userId, Long fromDate, Long toDate, String email, String name,
			String utrNo, Integer page, Integer pageSize, BigDecimal amount) {
		if ((page != null) && (pageSize != null)) {
			if ((fromDate == null && (toDate == null) && (email == null) && (name == null)) && (utrNo == null)
					&& (amount == null)) {

				List<Fiat> isListExists = fiatDao.findByUserIdAndTransactionType(userId, TransactionType.DEPOSIT,
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
			if ((email != null) && (page != null) && (pageSize != null)) {
				List<Fiat> isListExists = fiatDao.findByEmailAndTransactionType(email, TransactionType.DEPOSIT,
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
			if (name != null) {
				List<Fiat> isListExists = fiatDao.findByNameAndTransactionType(name, TransactionType.DEPOSIT,
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
			if (amount != null) {
				List<Fiat> isListExists = fiatDao.findByDepositAndTransactionType(amount, TransactionType.DEPOSIT,
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
			if (utrNo != null) {
				List<Fiat> isListExists = fiatDao.findByUtrNoAndTransactionType(utrNo, TransactionType.DEPOSIT,
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
			if ((fromDate != null && (toDate != null) && (page != null) && (pageSize != null))) {
				List<Fiat> isListExists = fiatDao.findByUserIdAndCreateTimeBetweenAndTransactionType(userId,
						new Date(fromDate), new Date(toDate), TransactionType.DEPOSIT,
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
		}

		return new Response<>(205, "Data Not Found");
	}

	@Override
	public Response<Object> getListDepositAdmin(String utrNo, Integer page, Integer pageSize, FiatStatus fiatStatus) {
		if ((page != null) && (pageSize != null)) {
			if ((utrNo != null) && (fiatStatus == null)) {
				List<Fiat> isDataNew = fiatDao.findByTransactionTypeAndUtrNo(
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"), TransactionType.DEPOSIT, utrNo);
				return new Response<>(200, "List Fetched", isDataNew);
			} else if ((fiatStatus != null) && (utrNo == null)) {
				List<Fiat> isDataNew = fiatDao.findByTransactionTypeAndFiatStatus(
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"), TransactionType.DEPOSIT, fiatStatus);
				return new Response<>(200, "List Fetched", isDataNew);
			} else if ((fiatStatus != null) && (utrNo != null)) {
				List<Fiat> isDataNew = fiatDao.findByTransactionTypeAndFiatStatusAndUtrNo(
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"), TransactionType.DEPOSIT, fiatStatus,
						utrNo);
				return new Response<>(200, "List Fetched", isDataNew);
			}
			List<Fiat> isDataNew = fiatDao.findByTransactionType(
					PageRequest.of(page, pageSize, Direction.DESC, "fiatId"), TransactionType.DEPOSIT);
			return new Response<>(200, "List Fetched", isDataNew);
		}
		return new Response<>(205, "List Not Fetched");
	}

	@Override
	public Response<Object> addDeposit(DepositInrDto depositInrDto, Long userId) {
		DepositInrAmount depositInrAmount = new DepositInrAmount();
		Optional<DepositInrAmount> isData = amountdao.findByUserId(userId);
		if (isData.isPresent()) {
			isData.get().setAmount(depositInrDto.getAmount());

			amountdao.save(isData.get());
			return new Response<>(200, "Amount Added", isData.get());
		}
		if (!isData.isPresent()) {
			depositInrAmount.setUserId(userId);
			depositInrAmount.setAmount(depositInrDto.getAmount());

			amountdao.save(depositInrAmount);
			return new Response<>(200, "Amount Added", depositInrAmount.getAmount());
		}
		return new Response<>(200, "Data Not Present");

	}

	@Override
	public Response<Object> addbank(AdminBankDto adminBankDto) {
		AdminBank adminBank = new AdminBank();
		adminBank.setAccountNumber(adminBankDto.getAccountNumber());
		adminBank.setBankName(adminBankDto.getBankName());
		adminBank.setIfscCode(adminBankDto.getIfscCode());
		adminBank.setIsVisible(false);
		adminBank.setAccountHolderName(adminBankDto.getAccountHolderName());
		adminBankDao.save(adminBank);
		return new Response<>(200, "Admin Bank Added Successfully");
	}

	@Override
	public Response<Object> changestatus(FiatStatus fiatStatus, String utrNo, Long userId) {
		Optional<Fiat> userIdNew = fiatDao.findByUtrNo(utrNo);
		Optional<UserTotalAmount> isdataExists = userTotalAmountDao.findByUserId(userIdNew.get().getUserId());
		Response<UserEmailAndNameDto> userData = userClient.getUserByUserId(userId);
		if (!isdataExists.isPresent()) {
			UserTotalAmount userTotalAmount = new UserTotalAmount();
			userTotalAmount.setTotalAmount(BigDecimal.ZERO);
			userTotalAmount.setDepositAmount(BigDecimal.ZERO);
			userTotalAmount.setWithdrawAmount(BigDecimal.ZERO);
			userTotalAmount.setUserId(userIdNew.get().getUserId());
			userTotalAmount.setLockedAmount(BigDecimal.ZERO);
			userTotalAmountDao.save(userTotalAmount);
		}
		if (fiatStatus.equals(FiatStatus.COMPLETED) && userIdNew.get().getFiatStatus().equals(FiatStatus.PENDING)) {

			Optional<DepositInrAmount> isInrPresent = amountdao.findByUserId(userId);
			BigDecimal amount = isInrPresent.get().getAmount().subtract(userIdNew.get().getAmount());
			isInrPresent.get().setAmount(amount);
			BigDecimal amount1 = userIdNew.get().getAmount().subtract(isInrPresent.get().getDepositFee());
			userIdNew.get().setDeposit(amount1);
			BigDecimal AMOUNT3 = isdataExists.get().getDepositAmount().add(amount1);
			isdataExists.get().setDepositAmount(AMOUNT3);
			isdataExists.get().setTotalAmount(isdataExists.get().getTotalAmount().add(userIdNew.get().getAmount()));
			isInrPresent.get().setDepositCommision(
					isInrPresent.get().getDepositCommision().add(isInrPresent.get().getDepositFee()));
			userIdNew.get().setFiatStatus(fiatStatus);
			Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess5(userData.getData().getName(),
					userIdNew.get().getAmount(), "INR", utrNo, userData.getData().getEmail());
			mailSender.sendMailToApproveWithdrawSuccess5(sendMailData);
			if (isNotification) {
				Map<String, String> setData = new HashMap<>();
				setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
				setData.put(EmailConstants.COIN_TOKEN, "INR");
				setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(userIdNew.get().getAmount()));
				setData.put(EmailConstants.COIN_ADDRESS_TOKEN, utrNo);
				EmailDto emailDto = new EmailDto(userIdNew.get().getUserId(), "deposit_inr_success", "en", "email",
						setData);
				notificationClient.sendNotification(emailDto);
			}
			userTotalAmountDao.save(isdataExists.get());
			fiatDao.save(userIdNew.get());
			amountdao.save(isInrPresent.get());
			return new Response<>(200, "Amount Send To User Successfully");

		}
		if (fiatStatus.equals(FiatStatus.REJECTED) && userIdNew.get().getFiatStatus().equals(FiatStatus.PENDING)) {
			userIdNew.get().setFiatStatus(fiatStatus);
			Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess6(userData.getData().getName(),
					userIdNew.get().getAmount(), "INR", utrNo, userData.getData().getEmail());
			mailSender.sendMailToApproveWithdrawSuccess6(sendMailData);
			if (isNotification) {
				Map<String, String> setData = new HashMap<>();
				setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
				setData.put(EmailConstants.COIN_TOKEN, "INR");
				setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(userIdNew.get().getAmount()));
				setData.put(EmailConstants.COIN_ADDRESS_TOKEN, utrNo);
				EmailDto emailDto = new EmailDto(userIdNew.get().getUserId(), "deposit_inr_rejected", "en", "email",
						setData);
				notificationClient.sendNotification(emailDto);
			}
			fiatDao.save(userIdNew.get());
			return new Response<>(200, "User request Rejected Successfully");
		}
		return new Response<>(205, "UTR No Not Exists");

	}

	@Override
	public Response<Object> addUpi(AdminUpiDto adminUpiDto) {
		AdminUpi adminBank = new AdminUpi();

		adminBank.setUpiId(adminUpiDto.getUpiId());
		adminBank.setName(adminUpiDto.getName());
		adminBank.setIsVisible(false);
		adminUpidao.save(adminBank);
		return new Response<>(200, "Admin Upi Added Successfully");

	}

	@Override
	public Response<Object> getListWithdraw(Long userId, WithdrawDtoInr withdrawDtoInr) {
		Response<UserEmailAndNameDto> userData = userClient.getUserByUserId(userId);

		UserEmailAndNameDto userDetail = userData.getData();
		Optional<UserTotalAmount> data = userTotalAmountDao.findByUserId(userId);
		Integer withdraw = withdrawDtoInr.getAmount().intValue();
		Integer dataGet = data.get().getTotalAmount().intValue();
		if (withdraw < dataGet) {
			Fiat fiat = new Fiat();
			fiat.setUserId(userId);
			fiat.setCreateTime(withdrawDtoInr.getCreateTime());
			fiat.setAmount(withdrawDtoInr.getAmount());
			fiat.setEmail(userDetail.getEmail());
			fiat.setName(userDetail.getName());
			fiat.setFiatStatus(FiatStatus.PENDING);
			fiat.setUpiId(withdrawDtoInr.getUpiId());
			fiat.setUpiName(withdrawDtoInr.getName());
			fiat.setBank(withdrawDtoInr.getBank());
			fiat.setTransactionType(TransactionType.WITHDRAW);
			if (!withdrawDtoInr.getBank().isEmpty()) {
				Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess4(userData.getData().getName(),
						withdrawDtoInr.getAmount(), "INR", withdrawDtoInr.getBank(), userData.getData().getEmail());
				mailSender.sendMailToApproveWithdrawSuccess4(sendMailData);
				if (isNotification) {
					Map<String, String> setData = new HashMap<>();
					setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
					setData.put(EmailConstants.COIN_TOKEN, "INR");
					setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(withdrawDtoInr.getAmount()));
					setData.put(EmailConstants.COIN_ADDRESS_TOKEN, withdrawDtoInr.getBank());
					EmailDto emailDto = new EmailDto(userId, "withdraw_inr", "en", "email", setData);
					notificationClient.sendNotification(emailDto);
				}
			}
			if (!withdrawDtoInr.getUpiId().isEmpty()) {
				Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess4(userData.getData().getName(),
						withdrawDtoInr.getAmount(), "INR", withdrawDtoInr.getUpiId(), userData.getData().getEmail());
				mailSender.sendMailToApproveWithdrawSuccess4(sendMailData);
				if (isNotification) {
					Map<String, String> setData = new HashMap<>();
					setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
					setData.put(EmailConstants.COIN_TOKEN, "INR");
					setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(withdrawDtoInr.getAmount()));
					setData.put(EmailConstants.COIN_ADDRESS_TOKEN, withdrawDtoInr.getUpiId());
					EmailDto emailDto = new EmailDto(userId, "withdraw_inr", "en", "email", setData);
					notificationClient.sendNotification(emailDto);
				}
			}
			fiatDao.save(fiat);
			return new Response<>(200, "Withdraw Successfully");
		}
		return new Response<>(205, "Withdarw amount should be less than available balance");
	}

	@Override
	public Response<Object> getListWithdrawInr(Long userId, Long fromDate, Long toDate, String email, String name,
			Integer page, Integer pageSize, BigDecimal amount) {

		if ((page != null) && (pageSize != null)) {
			if ((fromDate == null && (toDate == null) && (email == null) && (name == null) && (amount == null))) {

				List<Fiat> isListExists = fiatDao.findByUserIdAndTransactionTypeOrderByCreateTimeDesc(userId,
						TransactionType.WITHDRAW, PageRequest.of(page, pageSize));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
			if (email != null) {
				List<Fiat> isListExists = fiatDao.findByEmailAndTransactionType(email, TransactionType.WITHDRAW,
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
			if (name != null) {
				List<Fiat> isListExists = fiatDao.findByNameAndTransactionType(name, TransactionType.WITHDRAW,
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
			if (amount != null) {
				List<Fiat> isListExists = fiatDao.findByWithdrawAndTransactionType(amount, TransactionType.WITHDRAW,
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
			if ((fromDate != null && (toDate != null))) {
				List<Fiat> isListExists = fiatDao
						.findByUserIdAndCreateTimeBetweenAndTransactionTypeOrderByCreateTimeDesc(userId,
								new Date(fromDate), new Date(toDate), TransactionType.WITHDRAW,
								PageRequest.of(page, pageSize));
				if (!isListExists.isEmpty()) {
					Map<String, Object> map = new HashMap<>();
					map.put("Data", isListExists);
					map.put("count", isListExists.size());
					return new Response<>(200, "List fetched Successfully", map);
				}
			}
		}

		return new Response<>(205, "Data Not Found");
	}

	@Override
	public Response<Object> getListwithdrawAdmin(Integer page, Integer pageSize, FiatStatus fiatStatus) {
		if ((page != null) && (pageSize != null)) {

			if (fiatStatus != null) {
				List<Fiat> isDataNew = fiatDao.findByTransactionTypeAndFiatStatus(
						PageRequest.of(page, pageSize, Direction.DESC, "fiatId"), TransactionType.WITHDRAW, fiatStatus);
				return new Response<>(200, "List Fetched", isDataNew);
			}
			List<Fiat> isDataNew = fiatDao.findByTransactionType(
					PageRequest.of(page, pageSize, Direction.DESC, "fiatId"), TransactionType.WITHDRAW);
			return new Response<>(200, "List Fetched", isDataNew);
		}
		List<Fiat> isListPresent = fiatDao.findByFiatStatusAndTransactionType(fiatStatus, TransactionType.WITHDRAW);
		if (!isListPresent.isEmpty())
			return new Response<>(200, "List Fetched", isListPresent);
		return new Response<>(205, "List Not Fetched");
	}

	@Override
	public Response<Object> changestatuswithdraw(FiatStatus fiatStatus, Long userId, Long userId1, BigDecimal amount) {
		Optional<DepositInrAmount> isInrPresent = amountdao.findByUserId(userId);
		Response<UserEmailAndNameDto> userData = userClient.getUserByUserId(userId1);
		Optional<UserTotalAmount> isdataExists = userTotalAmountDao.findByUserId(userId1);
		Optional<Fiat> isDataexists = fiatDao.findByUserIdAndFiatStatusAndTransactionTypeAndAmount(userId1,
				FiatStatus.PENDING, TransactionType.WITHDRAW, amount);
		if (isdataExists.isPresent()) {

			if (fiatStatus.equals(FiatStatus.COMPLETED)
					&& isDataexists.get().getFiatStatus().equals(FiatStatus.PENDING)) {
				BigDecimal amount1 = isdataExists.get().getTotalAmount().subtract(isDataexists.get().getAmount());
				isdataExists.get()
//						.set (isdataExists.get().getTotalAmount().subtract(isDataexists.get().getAmount()));
						.setDepositAmount(
								isdataExists.get().getDepositAmount().subtract(isDataexists.get().getAmount()));
				userTotalAmountDao.save(isdataExists.get());
				BigDecimal amount2 = isDataexists.get().getAmount().subtract(isInrPresent.get().getWithdrawFee());
				System.out.println(amount2);
				isDataexists.get().setWithdraw(amount2);
				isdataExists.get().setWithdrawAmount(amount2);
				userTotalAmountDao.save(isdataExists.get());
				isDataexists.get().setFiatStatus(fiatStatus);
				isInrPresent.get().setWithdrawCommision(
						isInrPresent.get().getWithdrawCommision().add(isInrPresent.get().getWithdrawFee()));
				if (!isDataexists.get().getBank().equals(null)) {
					Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess7(userData.getData().getName(),
							amount, "INR", isDataexists.get().getBank(), userData.getData().getEmail());
					mailSender.sendMailToApproveWithdrawSuccess7(sendMailData);
					if (isNotification) {
						Map<String, String> setData = new HashMap<>();
						setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
						setData.put(EmailConstants.COIN_TOKEN, "INR");
						setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(amount));
						setData.put(EmailConstants.COIN_ADDRESS_TOKEN, isDataexists.get().getBank());
						EmailDto emailDto = new EmailDto(userId1, "withdraw_inr_success", "en", "email", setData);
						notificationClient.sendNotification(emailDto);
					}
				}
				if (!isDataexists.get().getUpiId().equals(null)) {
					Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess7(userData.getData().getName(),
							amount, "INR", isDataexists.get().getUpiId(), userData.getData().getEmail());
					mailSender.sendMailToApproveWithdrawSuccess7(sendMailData);
					if (isNotification) {
						Map<String, String> setData = new HashMap<>();
						setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
						setData.put(EmailConstants.COIN_TOKEN, "INR");
						setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(amount));
						setData.put(EmailConstants.COIN_ADDRESS_TOKEN, isDataexists.get().getUpiId());
						EmailDto emailDto = new EmailDto(userId1, "withdraw_inr_success", "en", "email", setData);
						notificationClient.sendNotification(emailDto);
					}
				}
				amountdao.save(isInrPresent.get());
				fiatDao.save(isDataexists.get());
				return new Response<>(200, "Amount Send To User Successfully");
			}
			if (fiatStatus.equals(FiatStatus.REJECTED)
					&& isDataexists.get().getFiatStatus().equals(FiatStatus.PENDING)) {
				isDataexists.get().setFiatStatus(fiatStatus);
				if (!isDataexists.get().getBank().equals(null)) {
					Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess8(userData.getData().getName(),
							amount, "INR", isDataexists.get().getBank(), userData.getData().getEmail());
					mailSender.sendMailToApproveWithdrawSuccess8(sendMailData);
					if (isNotification) {
						Map<String, String> setData = new HashMap<>();
						setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
						setData.put(EmailConstants.COIN_TOKEN, "INR");
						setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(amount));
						setData.put(EmailConstants.COIN_ADDRESS_TOKEN, isDataexists.get().getBank());
						EmailDto emailDto = new EmailDto(userId1, "withdraw_inr_rejected", "en", "email", setData);
						notificationClient.sendNotification(emailDto);
					}
				}
				if (!isDataexists.get().getUpiId().equals(null)) {
					Map<String, Object> sendMailData = setEmailDataForWithdrawSuccess8(userData.getData().getName(),
							amount, "INR", isDataexists.get().getUpiId(), userData.getData().getEmail());
					mailSender.sendMailToApproveWithdrawSuccess8(sendMailData);
					if (isNotification) {
						Map<String, String> setData = new HashMap<>();
						setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
						setData.put(EmailConstants.COIN_TOKEN, "INR");
						setData.put(EmailConstants.AMOUNT_TOKEN, String.valueOf(amount));
						setData.put(EmailConstants.COIN_ADDRESS_TOKEN, isDataexists.get().getUpiId());
						EmailDto emailDto = new EmailDto(userId1, "withdraw_inr_rejected", "en", "email", setData);
						notificationClient.sendNotification(emailDto);
					}
				}
				fiatDao.save(isDataexists.get());
				return new Response<>(200, "User request Rejected Successfully");
			}

		}
		return new Response<>(205, "Fund Not Present");
	}

	@Override
	public Response<Object> addUpiUser(UpiUserDto upiUserDto, Long userId) {
		UpiUser upiUser = new UpiUser();
		upiUser.setUpiId(upiUserDto.getUpiId());
		upiUser.setName(upiUserDto.getName());
		upiUser.setUserId(userId);
		upiUserDao.save(upiUser);
		return new Response<>(200, "User Upi Data Added Successfully");
	}

	@Override
	public Response<Object> userListUpi(Long userId) {
		List<UpiUser> listNew = upiUserDao.findByUserId(userId);
		if (!listNew.isEmpty()) {
			return new Response<>(200, "List Fetched", listNew);
		}
		return new Response<>(205, "List Not Fetched");
	}

	@Override
	public Response<Object> fundUser(Long userId) {
		Map<String, Object> map = new HashMap<>();
		Optional<UserTotalAmount> data = userTotalAmountDao.findByUserId(userId);
		if (data.isPresent()) {
			map.put("AVailable_fund", data.get().getDepositAmount());
			map.put("deposit_fund", data.get().getTotalAmount());
			map.put("withdraw_fund", data.get().getWithdrawAmount());
			map.put("locked_amount", data.get().getLockedAmount());
			return new Response<>(200, "Fund Details Fetched", map);
		}
		return new Response<>(205, "No Data Pesent");
	}

	@Override
	public Response<FundUserDto> fundUserParam(Long userId) {
		Optional<UserTotalAmount> data = userTotalAmountDao.findByUserId(userId);
		if (data.isPresent()) {
			FundUserDto dto = new FundUserDto();
			dto.setAvailableFund(data.get().getDepositAmount());
			dto.setLockedAmount(data.get().getLockedAmount());
			return new Response<>(200, "Fund Details Fetched", dto);
		}
		return new Response<>(205, "No Data Pesent");
	}

	@Override
	public Response<Object> listUpi() {
		List<AdminUpi> listAll = adminUpidao.findAll();
		if (!listAll.isEmpty()) {
			return new Response<>(200, "List Fetched", listAll);
		}
		return new Response<>(205, "No Data Present");
	}

	@Override
	public Response<Object> bankList() {
		List<AdminBank> listFull = adminBankDao.findAll();
		if (!listFull.isEmpty()) {
			return new Response<>(200, "List Fetched", listFull);
		}
		return new Response<>(205, "No Data Present");
	}

	@Override
	public Response<Object> getAmount() {
		List<DepositInrAmount> listFull = amountdao.findAll();
		if (!listFull.isEmpty()) {
			return new Response<>(200, "List Fetched", listFull);
		}
		return new Response<>(205, "No Data Present");
	}

	@Override
	public Response<Object> addBankStatus(Boolean isVisible, Long bankId) {
		Optional<AdminBank> staus = adminBankDao.findByBankId(bankId);
		if (staus.isPresent()) {
			staus.get().setIsVisible(isVisible);
			adminBankDao.save(staus.get());
			return new Response<>(200, "Status Changed Successfully");
		}
		return new Response<>(205, "Invalid input");
	}

	@Override
	public Response<Object> addUpiStatus(Boolean isVisible, String upiId) {
		Optional<AdminUpi> staus = adminUpidao.findByUpiId(upiId);
		if (staus.isPresent()) {
			staus.get().setIsVisible(isVisible);
			adminUpidao.save(staus.get());
			return new Response<>(200, "Status Changed Successfully");
		}
		return new Response<>(205, "Invalid input");
	}

	@Override
	public Response<Object> bankListUser() {
		List<AdminBank> listFull = adminBankDao.findByIsVisibleTrue();
		if (!listFull.isEmpty()) {
			return new Response<>(200, "List Fetched", listFull);
		}
		return new Response<>(205, "No Data Present");
	}

	@Override
	public Response<Object> listUpiUser() {
		List<AdminUpi> listAll = adminUpidao.findByIsVisibleTrue();
		if (!listAll.isEmpty()) {
			return new Response<>(200, "List Fetched", listAll);
		}
		return new Response<>(205, "No Data Present");
	}

	@Override
	public Response<Object> bankListuser() {
		List<BankDetails> isdata = bankDao.findAll();
		if (!isdata.isEmpty()) {
			return new Response<>(200, "Upi User", isdata);
		}
		return new Response<>(205, "No Data Present");
	}

	@Override
	public Response<Object> listUpiuser() {
		List<UpiUser> isdata = upiUserDao.findAll();
		if (!isdata.isEmpty()) {
			return new Response<>(200, "Upi User", isdata);
		}
		return new Response<>(205, "No Data Present");
	}

	@Override
	public Response<Object> getView(Long userId, String upiId) {
		Optional<UpiUser> userData = upiUserDao.findByUpiIdAndUserId(upiId, userId);
		if (userData.isPresent()) {
			return new Response<>(200, "Bank_data", userData.get());
		}
		return new Response<>(205, "Not Bank Account Present");
	}

	@Override
	public Response<Object> getFullList(Integer page, Integer pageSize, String name, String upiId) {
		List<UpiUser> fullData = upiUserDao.findAll();
		if ((name != null) || (upiId != null)) {
			List<UpiUser> fullList = upiUserDao
					.findByNameOrUpiId(PageRequest.of(page, pageSize, Direction.DESC, "userUpiId"), name, upiId);
			if (!fullList.isEmpty()) {
				Map<String, Object> map = new HashMap<>();
				map.put("List", fullList);
				map.put("Count", fullData.size());
				return new Response<>(200, "List Fetched", map);
			}
			return new Response<>(205, "No Data Present");
		}
		if (name == null && upiId == null) {
			Page<UpiUser> fullList = upiUserDao.findAll(PageRequest.of(page, pageSize, Direction.DESC, "userUpiId"));
			if (!fullList.isEmpty()) {
				Map<String, Object> map = new HashMap<>();
				map.put("List", fullList);
				map.put("Count", fullData.size());
				return new Response<>(200, "List Fetched", map);
			}
			return new Response<>(205, "No Data Present");
		}
		return new Response<>(205, "No Data Present");

	}

	@Override
	public Response<Wallet> getWalletAddressNew() {
		List<Coin> fullList = coinDao.findAll();
		{
			for (Coin coin : fullList) {
				Response<UserEmailAndNameDto> userData = userClient.fullUser();
				List<UserEmailAndNameDto> list = (List<UserEmailAndNameDto>) userData.getData();
				if (!list.isEmpty()) {
					for (UserEmailAndNameDto user2 : list) {
						getWalletAddress(coin.getCoinShortName(), user2.getUserId());
					}
				}
			}
		}
		return null;
	}

	@Override
	public Response<Object> addDepositFee(BigDecimal deposite, BigDecimal withdraw, Long userId) {
		Optional<DepositInrAmount> data = amountdao.findByUserId(userId);
		if (data.isPresent() && deposite != null && withdraw == null) {
			data.get().setDepositFee(deposite);
			amountdao.save(data.get());
			return new Response<>(200, "Deposite Fee changed Successfully");
		} else if (data.isPresent() && withdraw != null && deposite == null) {
			data.get().setWithdrawFee(withdraw);
			amountdao.save(data.get());
			return new Response<>(200, "Withdraw Fee Changed Successfully");
		} else if (data.isPresent() && withdraw != null && deposite != null) {
			data.get().setWithdrawFee(withdraw);
			data.get().setDepositFee(deposite);
			amountdao.save(data.get());
			return new Response<>(200, " Fee Changed Successfully");
		}
		return new Response<>(205, "No data Found");
	}

	@Override
	public Response<Object> addMinimumDeposit(BigDecimal minimumdeposite, BigDecimal minimumwithdraw, Long userId) {
		Optional<DepositInrAmount> data = amountdao.findByUserId(userId);
		if (data.isPresent() && minimumdeposite != null && minimumwithdraw == null) {
			data.get().setMinimumdeposite(minimumdeposite);
			amountdao.save(data.get());
			return new Response<>(200, "Deposite Fee changed Successfully");
		} else if (data.isPresent() && minimumwithdraw != null && minimumdeposite == null) {
			data.get().setMinimunwithdraw(minimumwithdraw);
			amountdao.save(data.get());
			return new Response<>(200, "Withdraw Fee Changed Successfully");
		} else if (data.isPresent() && minimumwithdraw != null && minimumdeposite != null) {
			data.get().setMinimunwithdraw(minimumwithdraw);
			data.get().setMinimumdeposite(minimumdeposite);
			amountdao.save(data.get());
			return new Response<>(200, " Fee Changed Successfully");
		}
		return new Response<>(205, "No data Found");
	}

	@Override
	public Response<Object> getdepositview(Long fiatid) {
		Optional<Fiat> datafetched = fiatDao.findByFiatId(fiatid);
		if (datafetched.isPresent()) {
			return new Response<>(200, "Data Fetched", datafetched.get());
		}
		return new Response<>(205, "no Data Fetched");
	}

	@Override
	public Response<Object> getwithdarwList(Long fiatid) {
		Optional<Fiat> datafetched = fiatDao.findByFiatId(fiatid);
		if (datafetched.isPresent()) {
			return new Response<>(200, "Data Fetched", datafetched.get());
		}
		return new Response<>(205, "no Data Fetched");
	}

	@Override
	public Response<Object> updateINR(Long userId) {
		Optional<UserTotalAmount> data = userTotalAmountDao.findByUserId(userId);
		if (data.isPresent()) {
			Optional<Wallet> data1 = walletDao.findByCoinNameAndFkUserId("INR", userId);
			if (data1.isPresent()) {
				updateWallet(data.get().getDepositAmount(), userId, "INR");
				return new Response<>(200, "Balance Updated");
			}
			return new Response<>(205, "Wallet Not Present");
		}
		return new Response<>(205, "user Not Present");
	}

	@Override
	public Response<Object> updateFund(Long userId, BigDecimal walletBalance) {
		Optional<UserTotalAmount> dataFetched = userTotalAmountDao.findByUserId(userId);
		if (dataFetched.isPresent()) {
			dataFetched.get().setDepositAmount(walletBalance);
			userTotalAmountDao.save(dataFetched.get());
			return new Response<>(200, "Balance Updated");
		}
		return new Response<>(205, "Wallet Not Present");
	}

	@Override
	public Response<Object> getAveragePrice(Long userId) {

		List<Coin> getAllCoins = coinDao.findAllByIsVisibleTrue();
		List<Map<String, Object>> list2 = new ArrayList<>();

		for (Coin coin : getAllCoins) {
			Map<String, Object> map = new HashMap<>();
			List<Map<String, Object>> list = new ArrayList<>();
			List<CoinDepositWithdrawal> data1 = coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserIdAndStatus(
					coin.getCoinShortName(), "DEPOSIT", userId, "CONFIRM");
			BigDecimal totalCoinAmount = BigDecimal.ZERO;
			BigDecimal totalCoinValue = BigDecimal.ZERO;
			BigDecimal averageAmount = BigDecimal.ZERO;
			if (!data1.isEmpty()) {
				for (CoinDepositWithdrawal coinDepositWithdrawal : data1) {
					BigDecimal depositprice = coinDepositWithdrawal.getDepositeCurrentPrice();
					System.out.println(depositprice);
					totalCoinAmount = totalCoinAmount.add(depositprice);
					totalCoinValue = totalCoinValue.add(coinDepositWithdrawal.getUnits());
				}
				averageAmount = totalCoinAmount.divide(totalCoinValue);

			}
			map.put("averageAmount", averageAmount);
			map.put("CoinName", coin.getCoinShortName());
			list.add(map);
			list2.addAll(list);
		}
		return new Response<>(200, "averagePrice", list2);
	}

	@Override
	public Response<Object> nomineeComision(BigDecimal walletBalance) {
		List<DepositInrAmount> dataFetch = amountdao.findAll();
		if (!dataFetch.isEmpty()) {
			BigDecimal addBalance = dataFetch.get(0).getKycCommision().add(walletBalance);
			dataFetch.get(0).setKycCommision(addBalance);
			dataFetch.get(0).setAmount(dataFetch.get(0).getAmount().add(addBalance));
			amountdao.save(dataFetch.get(0));
			return new Response<>(200, "Success");
		}
		return new Response<>(205, "no data fetch");
	}

	@Override
	public Response<String> updateBlocked(BigDecimal blockedBalance, Long userId, String coinName) {
		try {
			Optional<Wallet> data = walletDao.findByCoinNameAndFkUserId(coinName, userId);
			if (data.isPresent()) {
				data.get().setBlockedBalance(blockedBalance);
				walletDao.save(data.get());
				System.out.println("Hello");
				return new Response<>(SUCCESS_CODE, "WALLET_updated_SUCCESSFULLY");
			} else
				return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);

		} catch (Exception e) {
			LOGGER.catching(e);
			throw new CoinNotFoundException("No coins found to update wallet" + "");

		}

	}

	@Override
	public Response<Object> getAdminCommisssionAmount() {
		List<Coin> coinList = coinDao.findAllByIsVisibleTrue();
		List<Map<String, Object>> list2 = new ArrayList<>();
		if (!coinList.isEmpty()) {
			for (Coin coin : coinList) {
				Map<String, Object> map = new HashMap<>();
				List<Map<String, Object>> list = new ArrayList<>();
				List<CoinDepositWithdrawal> history = coinDepositeWithrawlDao
						.findByCoinTypeAndStatus(coin.getCoinShortName(), "CONFIRM");

				List<InternalTransferAmount> history1 = internalTransferAmountDao.findByStatus(COMPLETE);
				BigDecimal totalCoinAmount = BigDecimal.ZERO;
				if (!history.isEmpty()) {

					for (CoinDepositWithdrawal data : history) {
						if (!data.getTxnType().equals("DEPOSIT")) {
							BigDecimal depositprice = data.getFees();
							totalCoinAmount = totalCoinAmount.add(depositprice);
							System.out.println(totalCoinAmount);
						}

					}

				}
				if (coin.getCoinShortName().equals("XINDIA")) {
					if (!history1.isEmpty()) {
						for (InternalTransferAmount internalTransferAmount2 : history1) {
							totalCoinAmount = totalCoinAmount.add(internalTransferAmount2.getTransferFee());
							System.out.println(totalCoinAmount);
						}
					}
				}
				map.put("feeComission", totalCoinAmount);
				map.put("CoinName", coin.getCoinShortName());
				list.add(map);
				list2.addAll(list);

			}
			return new Response<>(200, "fees", list2);
		}
		return new Response<>(205, "No data found");
	}

	@Override
	public Response<String> updateP2pData(P2pHistoryDto p2pHistoryDto, Long userId) {
		P2pHistory p2pHistory = new P2pHistory();

		p2pHistory.setCoinAmount(p2pHistoryDto.getAmount());
		p2pHistory.setDepositLiveAmount(p2pHistoryDto.getDepositLiveAmount());
		p2pHistory.setLiveAmount(p2pHistoryDto.getLiveAmount());
		p2pHistory.setP2pId(p2pHistoryDto.getP2pId());
		p2pHistory.setTransactionStatus(p2pHistoryDto.getTransactionStatus());
		p2pHistory.setType(p2pHistoryDto.getType());
		p2pHistory.setTradeId(p2pHistoryDto.getTradeId());
		p2pHistory.setUserId(userId);

		p2pHistory.setTradeStatus(p2pHistoryDto.getTradeStatus());
		p2pHistoryDao.save(p2pHistory);
		return new Response<>(200, "Success");
	}

	@Override
	public Response<Object> updateLocked(Long userId, BigDecimal walletBalance) {
		Optional<UserTotalAmount> dataFetched = userTotalAmountDao.findByUserId(userId);
		if (dataFetched.isPresent()) {
			dataFetched.get().setLockedAmount(walletBalance);
			userTotalAmountDao.save(dataFetched.get());
			return new Response<>(200, "Balance Updated");
		}
		return new Response<>(205, "Wallet Not Present");

	}

	@Override
	public Response<Object> getSmallAssetHistory(String coinName, Long userId, Integer page, Integer pageSize,
			Long fromDate, Long toDate, BigDecimal amount) {
		StringBuilder query = new StringBuilder(
				"select c.smallAssetId, c.coinName, c.usdAmount, c.totalUsdAmount, c.userId, c.createDate from SmallAssetHistory c");

		List<String> conditions = new ArrayList<>();

		if (userId != null && !userId.equals(BLANK)) {
			conditions.add(" c.userId=:userId ");
		}
		if (coinName != null && !coinName.equals(BLANK)) {
			conditions.add(" c.coinName=:coinName ");
		}

		if (fromDate != null && !String.valueOf(fromDate).equals(BLANK)) {
			conditions.add(" c.createDate >= :fromDate ");
		}
		if (toDate != null && !String.valueOf(toDate).equals(BLANK)) {
			conditions.add(" c.createDate <= :toDate ");
		}

		if (amount != null && !amount.equals(BLANK)) {
			conditions.add("c.totalUsdAmount=:totalUsdAmount");
		}

		if (!conditions.isEmpty()) {
			query.append(" where ");
			query.append(String.join(" and ", conditions.toArray(new String[0])));
		}
		query.append(" order by c.createDate desc");
		Query createQuery = em.createQuery(query.toString());
		if (userId != null && !userId.equals(BLANK))
			createQuery.setParameter("userId", userId);

		if (coinName != null && !coinName.equals(BLANK))
			createQuery.setParameter("coinName", coinName);

		if (fromDate != null && !String.valueOf(fromDate).equals(BLANK))
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null && !String.valueOf(toDate).equals(BLANK))
			createQuery.setParameter("toDate", new Date(toDate));

		if (amount != null && !amount.equals(BLANK)) {
			createQuery.setParameter("totalUsdAmount", amount);
		}

		int filteredResultCount = createQuery.getResultList().size();
		createQuery.setFirstResult(page * pageSize);
		createQuery.setMaxResults(pageSize);
		List<Object[]> list = createQuery.getResultList();
		List<SmallAssetListDto> response = list.parallelStream().map(o -> {
			SmallAssetListDto dto = new SmallAssetListDto();
			dto.setSmallAssetId((Long) o[0]);
			dto.setCoinName((String) o[1]);
			dto.setUsdAmount((BigDecimal) o[2]);
			dto.setTotalUsdAmount((BigDecimal) o[3]);
			dto.setUserId((Long) o[4]);
			dto.setCreateDate((Date) o[5]);
			return dto;
		}).collect(Collectors.toList());
		Map<String, Object> data = new HashMap<>();
		data.put(RESULT_LIST, response);
		data.put(TOTAL_COUNT, filteredResultCount);

		return new Response<>(SUCCESS_CODE, SUCCESS, data);
	}
}
