package com.mobiloitte.microservice.wallet.serviceimpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.constants.EmailConstants;
import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.cryptocoins.ETHUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.USDTUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.XRPUtil;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.CoinDepositWithdrawalDao;
import com.mobiloitte.microservice.wallet.dao.StorageDetailsDao;
import com.mobiloitte.microservice.wallet.dao.WalletDao;
import com.mobiloitte.microservice.wallet.dto.UserEmailAndNameDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.entities.StorageDetail;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.exception.CoinNotFoundException;
import com.mobiloitte.microservice.wallet.exception.StorageDetailsNotFoundException;
import com.mobiloitte.microservice.wallet.exception.WalletNotFoundException;
import com.mobiloitte.microservice.wallet.feign.UserClient;
import com.mobiloitte.microservice.wallet.model.CryptoRequestModel;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.WalletManagementForCoinType2Service;
import com.mobiloitte.microservice.wallet.utils.MailSender;
import com.mobiloitte.microservice.wallet.utils.RandomTagGenerator;

/**
 * The Class WalletManagementForCoinType2ServiceImpl.
 * @author Ankush Mohapatra
 */
@Service("WalletManagementForCoinType2Service")
public class WalletManagementForCoinType2ServiceImpl implements WalletManagementForCoinType2Service, OtherConstants, WalletConstants{

	/** The Constant LOGGER. */
 	private static final Logger LOGGER = LogManager.getLogger(WalletManagementForCoinType2ServiceImpl.class);
 	
	// DAO Objects
	/** The wallet dao. */
	@Autowired
	private WalletDao walletDao;

	/** The coin dao. */
	@Autowired
	private CoinDao coinDao;

	/** The storage details dao. */
	@Autowired
	private StorageDetailsDao storageDetailsDao;

	/** The coin deposit withdrawal dao. */
	@Autowired
	private CoinDepositWithdrawalDao coinDepositWithdrawalDao;
	
	@Autowired
	private MailSender mailSender;
	
	@Autowired
	private UserClient userClient;

	// Crypto Coin Objects
	/** The xrp util. */
	@Autowired
	private XRPUtil xrpUtil;
	
	
	
	@Autowired
	private ETHUtil ethUtil;
	
	@Autowired
	private USDTUtil usdtUtil;
	

		

	/* (non-Javadoc)
	 * @see com.mobiloitte.microservice.wallet.service.WalletManagementForCoinType2Service
	 * #getWalletAddress(java.lang.String, java.lang.Long)
	 */
	@Override
	@Transactional
	public Response<Wallet> getWalletAddress(String coinName, Long fkUserId) {
		Wallet wallet=null;
		wallet = getWalletDetailsByCoinName(coinName, fkUserId);
		if (wallet != null && wallet.getWalletAddress() != null) {
			return new Response<>(SUCCESS_CODE, SUCCESS, wallet);
		} else {
			CryptoResponseModel cryptoResponseModel = getAddressForSingleAddressCoin(coinName);
			if(cryptoResponseModel != null && cryptoResponseModel.getAddress() != null)
			{
				wallet = saveAddressInDB(cryptoResponseModel, coinName, fkUserId);
				if (wallet != null) {
					return new Response<>(SUCCESS_CODE, SUCCESS, wallet);
				} else {
					return new Response<>(SERVER_ERROR_CODE, SERVER_ERROR);
				}
			}
			else {
				return new Response<>(FAILURE_CODE, String.format(NO_STORAGE_ADDRESS_FOUND, coinName));
			}
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
	 * Gets the address for single address coin.
	 *
	 * @param coinName the coin name
	 * @return the address for single address coin
	 */
	private CryptoResponseModel getAddressForSingleAddressCoin(String coinName)
	{
		CryptoResponseModel wallet = null;
		Optional<StorageDetail> getStorageDetails=storageDetailsDao.findByCoinTypeAndStorageType(coinName, STORAGE_HOT);
		if(getStorageDetails.isPresent() && getStorageDetails.get().getAddress()!=null)
		{
			wallet= new CryptoResponseModel();
			wallet.setAddress(getStorageDetails.get().getAddress());
		}
		return wallet;	
	}
	
	/**
	 * Save address in DB.
	 *
	 * @param cryptoResponseModel the crypto response model
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the wallet
	 */
	private Wallet saveAddressInDB(CryptoResponseModel cryptoResponseModel, String coinName, Long fkUserId) {
		try {
			Optional<Wallet> getWallet = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if (getWallet.isPresent() && cryptoResponseModel.getAddress()!=null) {
				Wallet wallet = getWallet.get();
				wallet.setWalletId(wallet.getWalletId());
				return updateExistingWallet(cryptoResponseModel, wallet, fkUserId);
			} else {
				return saveNewWallet(cryptoResponseModel, coinName, fkUserId);
			}

		} catch (Exception e) {
			LOGGER.catching(e);
			return null;
		}
	}
	
	/**
	 * Update existing wallet.
	 *
	 * @param cryptoResponseModel the crypto response model
	 * @param wallet the wallet
	 * @param userId the user id
	 * @return the wallet
	 */
	private Wallet updateExistingWallet(CryptoResponseModel cryptoResponseModel, Wallet wallet, Long userId)
	{
		try {
			wallet.setWalletId(wallet.getWalletId());
			if (cryptoResponseModel.getAddress() != null) {
				wallet.setWalletAddress(cryptoResponseModel.getAddress());
			}
			if (cryptoResponseModel.getPrivateKey() != null) {
				wallet.setWalletFileName(cryptoResponseModel.getPrivateKey());
			}
			if (wallet.getTag() == null) {
				wallet.setTag(RandomTagGenerator.generateTag());
			}
			wallet=walletDao.save(wallet);
			return wallet;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}

	}
	
	/**
	 * Save new wallet.
	 *
	 * @param cryptoResponseModel the crypto response model
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @return the wallet
	 */
	private Wallet saveNewWallet(CryptoResponseModel cryptoResponseModel, String coinName, Long fkUserId)
	{
		try {
			Optional<Coin> getCoinDetails = coinDao.findByCoinShortName(coinName);
			if(getCoinDetails.isPresent())
			{
				Wallet newWallet = new Wallet();
				newWallet.setCoin(getCoinDetails.get());
				newWallet.setFkUserId(fkUserId);
				newWallet.setCoinName(getCoinDetails.get().getCoinShortName());
				newWallet.setBlockedBalance(BigDecimal.ZERO);
				newWallet.setWalletBalance(BigDecimal.ZERO);
				newWallet.setCoinType(getCoinDetails.get().getCoinType());
				if (cryptoResponseModel.getAddress() != null) {
					newWallet.setWalletAddress(cryptoResponseModel.getAddress());
				}
				if (cryptoResponseModel.getPrivateKey() != null) {
					newWallet.setWalletFileName(cryptoResponseModel.getPrivateKey());
				}
				if(newWallet.getTag()==null)
				{	newWallet.setTag(RandomTagGenerator.generateTag());
				}
				newWallet=walletDao.save(newWallet);
				return newWallet;
			}
			else {
				throw new CoinNotFoundException("No such coin found: "+coinName);
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}
		
	}

	/* (non-Javadoc)
	 * @see com.mobiloitte.microservice.wallet.service.WalletManagementForCoinType2Service
	 * #depositListForXRP(java.lang.String, java.lang.Long)
	 */
	@Override
	@Transactional
	public Response<Map<String, Object>> depositListForXRPXLM(String coinName, Long fkUserId, Integer page, Integer pageSize, String email) {
		Map<String, Object> responseMap = new HashMap<>();
		Response<UserEmailAndNameDto> userData = userClient.getUserByUserId(fkUserId);
		if (userData.getStatus() == 200 && userData.getData() != null)
		{
			Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if(getWalletDetails.isPresent())
			{
				CryptoRequestModel setDepositsArguments=setArgumentsToGetDeposits(getWalletDetails.get());
				CryptoResponseModel getDepositsListFromApi = getDepositListFromAPI(coinName, setDepositsArguments);
				if(getDepositsListFromApi != null && !getDepositsListFromApi.getDepositList().isEmpty())
				{
					BigDecimal amount=BigDecimal.ZERO;
					List<CoinDepositWithdrawal> getDepositsFromDB=coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId);
					if(getDepositsFromDB !=null && !getDepositsFromDB.isEmpty())
					{
						List<Map<String, Object>> filter = getFilteredList(getDepositsListFromApi.getDepositList(), getDepositsFromDB);
						for(int i=0;i<filter.size();i++)
						{
							if(coinName.equals(XRP)) {
								amount = amount.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(filter.get(i).get(BALANCE)))));
								saveDeposits(filter.get(i), fkUserId, coinName, BigDecimal.valueOf(Double.valueOf(String.valueOf(filter.get(i).get(BALANCE)))),
										userData.getData());
							}else {
								amount = amount.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(filter.get(i).get(AMOUNT)))));
								saveDeposits(filter.get(i), fkUserId, coinName, BigDecimal.valueOf(Double.valueOf(String.valueOf(filter.get(i).get(AMOUNT)))),
										userData.getData());
							}
							
						}
					}else {
						for(int i=0;i<getDepositsListFromApi.getDepositList().size();i++)
						{
							if(coinName.equals(XRP)) {
								amount=amount.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(getDepositsListFromApi.getDepositList().get(i).get(BALANCE)))));
								saveDeposits(getDepositsListFromApi.getDepositList().get(i), fkUserId, coinName, BigDecimal.valueOf(Double.valueOf(String.valueOf(getDepositsListFromApi.getDepositList().get(i).get(BALANCE)))),
										userData.getData());
							}else {
								amount=amount.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(getDepositsListFromApi.getDepositList().get(i).get(AMOUNT)))));
								saveDeposits(getDepositsListFromApi.getDepositList().get(i), fkUserId, coinName, BigDecimal.valueOf(Double.valueOf(String.valueOf(getDepositsListFromApi.getDepositList().get(i).get(AMOUNT)))),
										userData.getData());
							}
							
						}
					}
					updateWalletBalance(coinName, fkUserId, amount);
					updateStorageBalance(coinName, STORAGE_HOT, amount);
					List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
							PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
					Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId);
					responseMap.put(RESULT_LIST, getTransactionList);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					//send mail of deposit success
					if(amount.compareTo(BigDecimal.ZERO)>0)
						mailSender.sendMailForDepositConfirmation(setDepositConfirmationMailObj(email, coinName, 
								amount.toPlainString()));
					return new Response<>(SUCCESS_CODE, DEPOSIT_SUCCESS, responseMap);
				}else {
					List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
							PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
					Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId);
					responseMap.put(RESULT_LIST, getTransactionList);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, NO_NEW_DEPOSITS_FOUND, responseMap);
				}
			}else {
				throw new WalletNotFoundException(
						"No wallet found for coin: " + coinName + " and userId: " + fkUserId + "");
			}
		}
		else
			return new Response<>(FAILURE_CODE, USER_NOT_FOUND);

	}
	
	/* (non-Javadoc)
	 * @see com.mobiloitte.microservice.wallet.service.WalletManagementForCoinType2Service
	 * #depositListForERC20Tokens(java.lang.String, java.lang.Long)
	 */
	@Override
	@Transactional
	public Response<Map<String, Object>> depositListForERC20Tokens(String coinName, Long fkUserId, Integer page, Integer pageSize, String email) {
		Map<String, Object> responseMap = new HashMap<>();
		Response<UserEmailAndNameDto> userData = userClient.getUserByUserId(fkUserId);
		if (userData.getStatus() == 200 && userData.getData() != null)
		{
			Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if(getWalletDetails.isPresent())
			{
				
				CryptoRequestModel setDepositsArguments=setArgumentsToGetDeposits(getWalletDetails.get());
				CryptoResponseModel getDepositsListFromApi = getDepositListFromAPI(coinName, setDepositsArguments);	
				if(getDepositsListFromApi != null && !getDepositsListFromApi.getDepositListType2().isEmpty())
				{
					BigDecimal amount=BigDecimal.ZERO;
					List<CoinDepositWithdrawal> getDepositsFromDB=coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId);
					if(getDepositsFromDB !=null && !getDepositsFromDB.isEmpty())
					{
						List<Map<String, Object>> filter = getFilteredListType2(getDepositsListFromApi.getDepositListType2(), getDepositsFromDB);
						for(int i=0;i<filter.size();i++)
						{
							amount = amount.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(filter.get(i).get(VALUE))) / Math.pow(10, 18)));
							saveDepositsType2(filter.get(i), fkUserId, coinName, BigDecimal.valueOf(Double.valueOf(String.valueOf(filter.get(i).get(VALUE))) / Math.pow(10, 18)),
									userData.getData());
						}
					}else {
						for(int i=0;i<getDepositsListFromApi.getDepositListType2().size();i++)
						{
							amount = amount.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(getDepositsListFromApi.getDepositListType2().get(i).get(VALUE))) / Math.pow(10, 18)));
							saveDepositsType2(getDepositsListFromApi.getDepositListType2().get(i), fkUserId, coinName,
									BigDecimal.valueOf(Double.valueOf(String.valueOf(getDepositsListFromApi.getDepositListType2().get(i).get(VALUE))) / Math.pow(10, 18)),
									userData.getData());
						}
					}

					updateWalletBalance(coinName, fkUserId, amount);
					updateStorageBalance(coinName, STORAGE_HOT, amount);
					List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
							PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
					Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId);
					responseMap.put(RESULT_LIST, getTransactionList);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					//send mail of deposit success
					if(amount.compareTo(BigDecimal.ZERO)>0)
						mailSender.sendMailForDepositConfirmation(setDepositConfirmationMailObj(email, coinName, 
								amount.toPlainString()));
					return new Response<>(SUCCESS_CODE, DEPOSIT_SUCCESS, responseMap);
				}else {
					List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
							PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
					Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId);
					responseMap.put(RESULT_LIST, getTransactionList);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, NO_NEW_DEPOSITS_FOUND, responseMap);
				}
			}else {
				throw new WalletNotFoundException(
						"No wallet found for coin: " + coinName + " and userId: " + fkUserId + "");
			}
		}
		else
			return new Response<>(FAILURE_CODE, USER_NOT_FOUND);
	}
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
				cryptoRequestModel.setStorageType(STORAGE_HOT);
			}
		} else {
			return null;
		}
		return cryptoRequestModel;
	}

	/**
	 * Gets the deposit list from API.
	 *
	 * @param coinName the coin name
	 * @param coinRequest the coin request
	 * @return the deposit list from API
	 */
	private CryptoResponseModel getDepositListFromAPI(String coinName, CryptoRequestModel coinRequest) {
		
		CryptoResponseModel depositResponseModel = null;
		switch (coinName) {
		case XRP:
			depositResponseModel=xrpUtil.internalTransferAPI(coinRequest);
			break;
		
		case ETH:
			depositResponseModel = ethUtil.internalTransferAPI(coinRequest);
			break;
		case USDT:
			depositResponseModel = usdtUtil.internalTransferAPI(coinRequest);
			break;
		
		default:
			break;
		}
		return depositResponseModel;
	}
	
	/**
	 * Sets the arguments to get deposits.
	 *
	 * @param getWallet the get wallet
	 * @return the crypto request model
	 */
	private CryptoRequestModel setArgumentsToGetDeposits(Wallet getWallet)
	{
		if(getWallet !=null) {
			CryptoRequestModel cryptoRequestModel = new CryptoRequestModel();
			cryptoRequestModel.setFromAddress(getWallet.getWalletAddress());
			cryptoRequestModel.setToAddress(getWallet.getWalletAddress());
			cryptoRequestModel.setWalletFile(getWallet.getWalletFileName());
			if(getWallet.getTag() !=null) {
				cryptoRequestModel.setTag(getWallet.getTag());	
			}			
			return cryptoRequestModel;
		}
		else {
			return null;
		}
	}
	
	/**
	 * Gets the filtered list.
	 *
	 * @param depositsListFromApi the deposits list from api
	 * @param depositListFromDB the deposit list from DB
	 * @return the filtered list
	 */
	private List<Map<String, Object>> getFilteredList(List<Map<String, Object>> depositsListFromApi, List<CoinDepositWithdrawal> depositListFromDB)
	{
		return depositsListFromApi.parallelStream()
				.filter(m -> depositListFromDB.parallelStream()
						.noneMatch(c -> c.getTxnHash().equals(String.valueOf(m.get(XRP_TXN_HASH)))))
				.collect(Collectors.toList());
	}
	
	/**
	 * Gets the filtered list type 2.
	 *
	 * @param depositsListFromApi the deposits list from api
	 * @param depositListFromDB the deposit list from DB
	 * @return the filtered list type 2
	 */
	private List<Map<String, Object>> getFilteredListType2(List<Map<String, Object>> depositsListFromApi, List<CoinDepositWithdrawal> depositListFromDB)
	{
		return depositsListFromApi.parallelStream()
				.filter(m -> depositListFromDB.parallelStream()
						.noneMatch(c -> c.getTxnHash().equals(String.valueOf(m.get(HASH)))))
				.collect(Collectors.toList());
	}
	
	
	/**
	 * Save deposits.
	 *
	 * @param filteredList the filtered list
	 * @param fkUserId the fk user id
	 * @param coinName the coin name
	 * @param amount the amount
	 * @return the coin deposit withdrawal
	 */
	private CoinDepositWithdrawal saveDeposits(Map<String, Object> filteredList, Long fkUserId, String coinName, BigDecimal amount
			,UserEmailAndNameDto emailAndNameDto)
	{
		try {
			CoinDepositWithdrawal coinDepositWithdrawal = null;
			if(filteredList != null)
			{
				coinDepositWithdrawal = new CoinDepositWithdrawal();
				coinDepositWithdrawal.setTxnHash(String.valueOf(filteredList.get(XRP_TXN_HASH)));
				coinDepositWithdrawal.setUnits(amount);
				coinDepositWithdrawal.setAddress(String.valueOf(filteredList.get(FROM)));
				coinDepositWithdrawal.setFkUserId(fkUserId);
				coinDepositWithdrawal.setStatus(CONFIRM);
				coinDepositWithdrawal.setCoinType(coinName);
				coinDepositWithdrawal.setTxnType(DEPOSIT);
				coinDepositWithdrawal.setUserEmail(emailAndNameDto.getEmail());
				coinDepositWithdrawal.setUserName(emailAndNameDto.getName());
				coinDepositWithdrawal = coinDepositWithdrawalDao.save(coinDepositWithdrawal);
			}
			return coinDepositWithdrawal;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return null;
		}	
	}
	
	/**
	 * Save deposits type 2.
	 *
	 * @param filteredList the filtered list
	 * @param fkUserId the fk user id
	 * @param coinName the coin name
	 * @param amount the amount
	 * @return the coin deposit withdrawal
	 */
	private CoinDepositWithdrawal saveDepositsType2(Map<String, Object> filteredList, Long fkUserId, String coinName, BigDecimal amount,
			UserEmailAndNameDto emailAndNameDto)
	{
		try {
			CoinDepositWithdrawal coinDepositWithdrawal = null;
			if(filteredList != null)
			{
				coinDepositWithdrawal = new CoinDepositWithdrawal();
				coinDepositWithdrawal.setTxnHash(String.valueOf(filteredList.get(HASH)));
				coinDepositWithdrawal.setUnits(amount);
				coinDepositWithdrawal.setAddress(String.valueOf(filteredList.get(FROM)));
				coinDepositWithdrawal.setFkUserId(fkUserId);
				coinDepositWithdrawal.setStatus(CONFIRM);
				coinDepositWithdrawal.setCoinType(coinName);
				coinDepositWithdrawal.setTxnType(DEPOSIT);
				coinDepositWithdrawal.setUserEmail(emailAndNameDto.getEmail());
				coinDepositWithdrawal.setUserName(emailAndNameDto.getName());
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
	 * @param coinName the coin name
	 * @param fkUserId the fk user id
	 * @param balance the balance
	 * @return the wallet
	 */
	private Wallet updateWalletBalance(String coinName, Long fkUserId, BigDecimal balance) {
		try {
			Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if (getWalletDetails.isPresent()) {
				Wallet wallet = null;
				getWalletDetails.get().setWalletBalance(getWalletDetails.get().getWalletBalance().add(balance));
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
	 * @param coinName the coin name
	 * @param storageType the storage type
	 * @param balance the balance
	 * @return the storage detail
	 */
	private StorageDetail updateStorageBalance(String coinName, String storageType, BigDecimal balance) {
		try {
			Optional<StorageDetail> getStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(coinName, storageType);
			if (getStorageDetails.isPresent()) {
				StorageDetail storageDetail = null;
				getStorageDetails.get().setHotWalletBalance(getStorageDetails.get().getHotWalletBalance().add(balance));
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


	/* (non-Javadoc)
	 * @see com.mobiloitte.microservice.wallet.service.WalletManagementForCoinType2Service
	 * #asyncTransfer(java.lang.String, java.lang.Long)
	 */
	@Override
	@Transactional
	public Response<String> asyncTransfer(String coinName, Long fkUserId) {
		Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
		if(getWalletDetails.isPresent())
		{
			Optional<StorageDetail> getStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(coinName, STORAGE_HOT);
			if(getStorageDetails.isPresent())
			{
				String transferResp = callAsyncTransferApi(coinName, getStorageDetails.get().getAddress(),
						getWalletDetails.get().getWalletAddress(), getWalletDetails.get().getWalletFileName());
				if(transferResp != null)
				{
					return new Response<>(SUCCESS_CODE, DEPOSIT_SUCCESS, transferResp);
				}else {
					return new Response<>(FAILURE_CODE, DEPOSIT_FAILED);
				}
			}else {
				return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
			}
		}else {
			return new Response<>(FAILURE_CODE, WALLET_NOT_FOUND);
		}
	}
	
	/**
	 * Call async transfer api.
	 *
	 * @param coinName the coin name
	 * @param toAddress the to address
	 * @param fromAddress the from address
	 * @param privateKey the private key
	 * @return the string
	 */
	private String callAsyncTransferApi(String coinName, String toAddress, String fromAddress, String privateKey)
	{
		String transferResponse = null;
		if(coinName.equals(ETH)) {
			transferResponse = ethUtil.getTransferFunds(toAddress, fromAddress, privateKey);
		}
		return transferResponse;
	}


	public Map<String, String> setDepositConfirmationMailObj(String email, String coinname, String balance)
	{
		Map<String, String> mailObj = new HashMap<>();
		mailObj.put("email", email);
		mailObj.put("coinname", coinname);
		mailObj.put("amount", balance);
		mailObj.put(EmailConstants.EMAIL_TO, email);
		mailObj.put(EmailConstants.SUBJECT_OF, "Deposition Confirmed");
		return mailObj;
	}


	@Override
	@Transactional
	public Response<Map<String, Object>> depositListForUSDT(String coinName, Long fkUserId, Integer page,
			Integer pageSize, String email) {
		Map<String, Object> responseMap = new HashMap<>();
		Response<UserEmailAndNameDto> userData = userClient.getUserByUserId(fkUserId);
		if (userData.getStatus() == 200 && userData.getData() != null)
		{
			Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(coinName, fkUserId);
			if(getWalletDetails.isPresent())
			{
				CryptoRequestModel setDepositsArguments=setArgumentsToGetDeposits(getWalletDetails.get());
				CryptoResponseModel getDepositsListFromApi = getDepositListFromAPI(coinName, setDepositsArguments);	
				if(getDepositsListFromApi != null && !getDepositsListFromApi.getDepositListType2().isEmpty())
				{
					BigDecimal amount=BigDecimal.ZERO;
					List<CoinDepositWithdrawal> getDepositsFromDB=coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId);
					if(getDepositsFromDB !=null && !getDepositsFromDB.isEmpty())
					{
						List<Map<String, Object>> filter = getFilteredListType2(getDepositsListFromApi.getDepositListType2(), getDepositsFromDB);
						for(int i=0;i<filter.size();i++)
						{
							amount = amount.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(filter.get(i).get(AMOUNT)))));
							saveDepositsType2(filter.get(i), fkUserId, coinName, BigDecimal.valueOf(Double.valueOf(String.valueOf(filter.get(i).get(AMOUNT)))),
									userData.getData());
						}
					}else {
						for(int i=0;i<getDepositsListFromApi.getDepositListType2().size();i++)
						{
							amount = amount.add(BigDecimal.valueOf(Double.valueOf(String.valueOf(getDepositsListFromApi.getDepositListType2().get(i).get(AMOUNT)))));
							saveDepositsType2(getDepositsListFromApi.getDepositListType2().get(i), fkUserId, coinName,
									BigDecimal.valueOf(Double.valueOf(String.valueOf(getDepositsListFromApi.getDepositListType2().get(i).get(AMOUNT)))),
									userData.getData());
						}
					}

					updateWalletBalance(coinName, fkUserId, amount);
					updateStorageBalance(coinName, STORAGE_HOT, amount);
					List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
							PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
					Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId);
					responseMap.put(RESULT_LIST, getTransactionList);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					//send mail of deposit success
					if(amount.compareTo(BigDecimal.ZERO)>0)
						mailSender.sendMailForDepositConfirmation(setDepositConfirmationMailObj(email, coinName, 
								amount.toPlainString()));
					return new Response<>(SUCCESS_CODE, DEPOSIT_SUCCESS, responseMap);
				}else {
					List<CoinDepositWithdrawal> getTransactionList = coinDepositWithdrawalDao.findByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId,
							PageRequest.of(page, pageSize, Direction.DESC, "txnId"));
					Long getTotalCount = coinDepositWithdrawalDao.countByCoinTypeAndTxnTypeAndFkUserId(coinName, DEPOSIT, fkUserId);
					responseMap.put(RESULT_LIST, getTransactionList);
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, NO_NEW_DEPOSITS_FOUND, responseMap);
				}
			}else {
				throw new WalletNotFoundException(
						"No wallet found for coin: " + coinName + " and userId: " + fkUserId + "");
			}
		}
		else
			return new Response<>(FAILURE_CODE, USER_NOT_FOUND);

	}
	
}
