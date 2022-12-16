package com.mobiloitte.microservice.wallet.serviceimpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
import com.mobiloitte.microservice.wallet.cryptocoins.USDTUtil;
import com.mobiloitte.microservice.wallet.cryptocoins.XRPUtil;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.CoinDepositWithdrawalDao;
import com.mobiloitte.microservice.wallet.dao.StorageDetailsDao;
import com.mobiloitte.microservice.wallet.dto.ColdStorageRequestDto;
import com.mobiloitte.microservice.wallet.dto.CreateStorageWalletRequestDto;
import com.mobiloitte.microservice.wallet.dto.StorageAddressRequestDto;
import com.mobiloitte.microservice.wallet.dto.StorageLimitDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.entities.StorageDetail;
import com.mobiloitte.microservice.wallet.model.CryptoResponseModel;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.CryptoStorageService;

/**
 * The Class CryptoStorageServiceImpl.
 * 
 * @author Ankush Mohapatra
 */
@Service("CryptoStorageService")
public class CryptoStorageServiceImpl implements CryptoStorageService, WalletConstants, OtherConstants {

	// DAO Objects
	/** The storage details dao. */
	@Autowired
	private StorageDetailsDao storageDetailsDao;

	@Autowired
	private CoinDepositWithdrawalDao coinDepositWithdrawalDao;

	/** The coin dao. */
	@Autowired
	private CoinDao coinDao;

	// Crypto Coin Objects
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
	private SOLANAUtil solanaUtil;



	@Autowired
	private TRXUtil trxUtil;

	@Autowired
	private BEPUtil bepUtil;



	@Autowired
	private BNBUtil bnbUtil;

	@Autowired
	private MATICUtil maticUtil;

	@Autowired
	private POLKADOTUtil polkadotUtil;

	@Autowired
	private AVAXUtil avaxUtil;

	@Override
	public Response<List<StorageDetail>> getStorageDataByStorageType(String storageType) {
		List<StorageDetail> getStorageDetails = storageDetailsDao.findByStorageType(storageType);
		if (getStorageDetails != null && !getStorageDetails.isEmpty()) {
			return new Response<>(SUCCESS_CODE, STORAGE_DETAILS_FETCHED_SUCCESSFULLY, getStorageDetails);
		} else {
			return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
		}
	}

	
	@Override
	@Transactional
	public Response<String> generateAddressForStorage(StorageAddressRequestDto storageAddressRequestDto) {
		Optional<StorageDetail> getStorageDetailsByCoinType = storageDetailsDao.findByCoinTypeAndStorageType(
				storageAddressRequestDto.getCoinName(), storageAddressRequestDto.getStorageType());
		if (getStorageDetailsByCoinType.isPresent()) {
			if (getStorageDetailsByCoinType.get().getAddress() != null) {
				return new Response<>(FAILURE_CODE, STORAGE_ADDRESS_IS_ALREADY_PRESENT);
			} else {
				CryptoResponseModel cryptoResponseModel = getAddressFromLiveCoinApi(
						storageAddressRequestDto.getCoinName(), storageAddressRequestDto.getAccName());
				if (cryptoResponseModel != null && cryptoResponseModel.getAddress() != null) {
					StorageDetail storageDetail = null;
					getStorageDetailsByCoinType.get().setAddress(cryptoResponseModel.getAddress());
					getStorageDetailsByCoinType.get().setWalletPassword(storageAddressRequestDto.getAccName());
					if (cryptoResponseModel.getPrivateKey() != null) {
						getStorageDetailsByCoinType.get().setWalletFile(cryptoResponseModel.getPrivateKey());
					}
					if (cryptoResponseModel.getHexAddress() != null) {
						getStorageDetailsByCoinType.get().setHexAddress(cryptoResponseModel.getHexAddress());
					}
					if (cryptoResponseModel.getWif() != null) {
						getStorageDetailsByCoinType.get().setWif(cryptoResponseModel.getWif());
					}
					storageDetail = storageDetailsDao.save(getStorageDetailsByCoinType.get());
					if (storageDetail != null && storageDetail.getAddress() != null) {
						return new Response<>(SUCCESS_CODE,
								STORAGE_ADDRESS_GENERATED_FOR_COIN + storageAddressRequestDto.getCoinName());
					} else {
						return new Response<>(FAILURE_CODE, UPDATION_FAILED);
					}
				} else {
					return new Response<>(FAILURE_CODE, FAILED_TO_FETCH_ADDRESS);
				}
			}
		} else {
			return new Response<>(FAILURE_CODE, NO_SUCH_STORAGE_FOUND_PLEASE_CREATE_STORAGE_FIRST);
		}
	}

	/**
	 * Gets the address from live coin api.
	 *
	 * @param coinName the coin name
	 * @param accName  the acc name
	 * @return the address from live coin api
	 */
	private CryptoResponseModel getAddressFromLiveCoinApi(String coinName, String accName) {
		CryptoResponseModel cryptoResponseModel = null;
		Optional<Coin> isCoinAvailabe = coinDao.findByCoinShortName(coinName);
		switch (coinName) {
		case BTC:
			cryptoResponseModel = btcUtil.getAddressAPI(accName);
			break;
		case SOLANA:
			cryptoResponseModel = solanaUtil.getAddressAPI(accName);
			break;
		case ETH:
			cryptoResponseModel = ethUtil.getAddressAPI(accName);
			break;
		case XRP:
			cryptoResponseModel = xrpUtil.getAddressAPI(accName);
			break;
		case USDT:
			cryptoResponseModel = bepUtil.getAddressAPI(accName);
			break;
		case XINDIA:
			cryptoResponseModel = bepUtil.getAddressAPI(accName);
			break;
		case TRX:
			cryptoResponseModel = trxUtil.getAddressAPI(accName);
			break;

		case BNB:
			cryptoResponseModel = bnbUtil.getAddressAPI(accName);
			break;

		case MATIC:
			cryptoResponseModel = maticUtil.getAddressAPI(accName);
			break;
		case POLKADOT:
			cryptoResponseModel = polkadotUtil.getAddressAPI(accName);
			break;
		case AVAX:
			cryptoResponseModel = avaxUtil.getAddressAPI(accName);
			break;
		default:
			if (isCoinAvailabe.isPresent()) {

				cryptoResponseModel = bepUtil.getAddressAPI(accName);

			}
			break;
		}

		return cryptoResponseModel;
	}
	@Override
	@Transactional
	public Response<String> createStorageWallet(CreateStorageWalletRequestDto createStorageWalletRequestDto) {
		Optional<StorageDetail> getStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(
				createStorageWalletRequestDto.getCoinName(), createStorageWalletRequestDto.getStorageType());
		if (getStorageDetails.isPresent()) {
			return new Response<>(FAILURE_CODE, STORAGE_WALLET_IS_ALREADY_PRESENT);
		} else {
			Optional<Coin> getCoin = coinDao.findByCoinShortName(createStorageWalletRequestDto.getCoinName());
			if (getCoin.isPresent()) {
				StorageDetail storageDetail = new StorageDetail();
				storageDetail.setCoin(getCoin.get());
				storageDetail.setCoinLimit(BigDecimal.valueOf(100.0));
				storageDetail.setHotWalletBalance(BigDecimal.ZERO);
				storageDetail.setSupportThreshold(BigDecimal.valueOf(0.01));
				storageDetail.setCoinThreshold(BigDecimal.ZERO);
				storageDetail.setStorageType(createStorageWalletRequestDto.getStorageType());
				storageDetail.setCoinType(createStorageWalletRequestDto.getCoinName());
				storageDetail = storageDetailsDao.save(storageDetail);
				if (storageDetail != null) {
					return new Response<>(SUCCESS_CODE,
							STORAGE_WALLET_CREATED_SUCCESSFULLY + createStorageWalletRequestDto.getCoinName());
				} else {
					return new Response<>(FAILURE_CODE, INSERTION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, NO_SUCH_COIN_FOUND);
			}
		}
	}

	@Override
	@Transactional
	public Response<String> updateStorageBalance(CreateStorageWalletRequestDto createStorageWalletRequestDto) {
		Optional<StorageDetail> getStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(
				createStorageWalletRequestDto.getCoinName(), createStorageWalletRequestDto.getStorageType());
		if (getStorageDetails.isPresent() && getStorageDetails.get().getAddress() != null) {
			BigDecimal getLiveBalance = getLiveBalance(createStorageWalletRequestDto.getCoinName(),
					getStorageDetails.get().getAddress(), getStorageDetails.get().getWalletPassword());
			if (getLiveBalance.compareTo(DEFAULT_BALANCE) != 0) {
				StorageDetail storageDetail = null;
				getStorageDetails.get().setHotWalletBalance(getLiveBalance);
				storageDetail = storageDetailsDao.save(getStorageDetails.get());
				if (storageDetail != null) {
					return new Response<>(SUCCESS_CODE, BALANCE_UPDATED_SUCCESSFULLY);
				} else {
					return new Response<>(FAILURE_CODE, BALANCE_UPDATION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, FAILED_TO_FETCH_BALANCE);
			}
		} else {
			return new Response<>(FAILURE_CODE, NO_SUCH_STORAGE_WALLET_FOUND_OR_STORAGE_ADDRESS_FOUND
					+ createStorageWalletRequestDto.getCoinName());
		}
	}

	/**
	 * Gets the live balance.
	 *
	 * @param coinName      the coin name
	 * @param walletAddress the wallet address
	 * @return the live balance
	 */
	private BigDecimal getLiveBalance(String coinName, String walletAddress, String walletPassword) {
		BigDecimal getLiveBalanceByCoin = null;
		switch (coinName) {
		case BTC:
			getLiveBalanceByCoin = btcUtil.getBalanceAPI(walletAddress);
			break;
		
		case SOLANA:
			getLiveBalanceByCoin = solanaUtil.getBalanceAPI(walletAddress);
			break;
		case ETH:
			getLiveBalanceByCoin = ethUtil.getBalanceAPI(walletAddress);
			break;
	
		case XRP:
			getLiveBalanceByCoin = xrpUtil.getBalanceAPI(walletAddress);
			break;
		case USDT:
			getLiveBalanceByCoin = usdtUtil.getBalanceAPI(walletAddress);
			break;
		
		case TRX:
			getLiveBalanceByCoin = trxUtil.getBalanceAPI(walletAddress);
			break;
		
		case BNB:
			getLiveBalanceByCoin = bnbUtil.getBalanceAPI(walletAddress);
			break;
		case MATIC:
			getLiveBalanceByCoin = maticUtil.getBalanceAPI(walletAddress);
			break;
		case POLKADOT:
			getLiveBalanceByCoin = polkadotUtil.getBalanceAPI(walletAddress);
			break;
		case AVAX:
			getLiveBalanceByCoin = avaxUtil.getBalanceAPI(walletAddress);
			break;
		default:
			getLiveBalanceByCoin = DEFAULT_BALANCE;
			break;
		}
		return getLiveBalanceByCoin;
	}

	@Override
	public Response<String> updateColdStorageAddress(ColdStorageRequestDto coldStorageRequestDto) {
		Optional<StorageDetail> getStorageDetails = storageDetailsDao
				.findByCoinTypeAndStorageType(coldStorageRequestDto.getCoinName(), STORAGE_COLD);
		if (getStorageDetails.isPresent()) {
			StorageDetail storageDetail = null;
			getStorageDetails.get().setAddress(coldStorageRequestDto.getColdAddress());
			storageDetail = storageDetailsDao.save(getStorageDetails.get());
			if (storageDetail != null && storageDetail.getAddress() != null) {
				return new Response<>(SUCCESS_CODE,
						String.format(COLD_STORAGE_ADDRESS_UPDATED_SUCCESSFULLY, coldStorageRequestDto.getCoinName()));
			} else {
				return new Response<>(FAILURE_CODE, UPDATION_FAILED);
			}
		} else {
			return new Response<>(FAILURE_CODE, NO_SUCH_STORAGE_FOUND_PLEASE_CREATE_STORAGE_FIRST);
		}
	}

	@Override
	public Response<Boolean> updateStorageLimit(StorageLimitDto storageLimitDto) {
		try {
			Optional<StorageDetail> getStorageDetails = storageDetailsDao
					.findByCoinTypeAndStorageType(storageLimitDto.getCoinName(), STORAGE_HOT);
			if (getStorageDetails.isPresent()) {
				getStorageDetails.get().setCoinLimit(storageLimitDto.getLimit());
				storageDetailsDao.save(getStorageDetails.get());
				return new Response<>(SUCCESS_CODE, "storage limit updated succcessfully", true);
			} else
				return new Response<>(FAILURE_CODE, NO_SUCH_STORAGE_FOUND_PLEASE_CREATE_STORAGE_FIRST);
		} catch (Exception e) {
			throw new RuntimeException("Something went wrong internally");
		}

	}

	@Override
	public Response<Object> getStorageDetailsCoinHot(String coin, String storageType) {
		Optional<StorageDetail> getStorageDetails = storageDetailsDao.findByCoinTypeAndStorageType(coin, storageType);
		if (getStorageDetails != null && getStorageDetails.isPresent()) {
			return new Response<>(SUCCESS_CODE, STORAGE_DETAILS_FETCHED_SUCCESSFULLY, getStorageDetails);
		} else {
			return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
		}
	}

	@Override
	public Response<Map<String, Object>> getStorageDetailsWithLatestTIme(String coinName) {
		List<CoinDepositWithdrawal> getStorageDetails = coinDepositWithdrawalDao.findBycoinTypeAndTxnType(coinName,
				"HOT_TO_COLD_TRANSFER", PageRequest.of(0, 1, Direction.DESC, "txnTime"));
		Optional<StorageDetail> getStorageDetailsByCoinType = storageDetailsDao.findByCoinTypeAndStorageType(coinName,
				STORAGE_HOT);
		if (getStorageDetailsByCoinType.isPresent()) {

			Map<String, Object> lastWithdrawTime = new HashMap<>();
			lastWithdrawTime.put("HotWalletData", getStorageDetailsByCoinType.get());
			if (getStorageDetails.isEmpty()) {
				lastWithdrawTime.put("lastWithdrawTime", "");
			} else {
				lastWithdrawTime.put("lastWithdrawTime", getStorageDetails.get(0).getTxnTime());
			}
			lastWithdrawTime.put("CoinName", getStorageDetailsByCoinType.get().getCoinType());
			return new Response<>(SUCCESS_CODE, STORAGE_DETAILS_FETCHED_SUCCESSFULLY, lastWithdrawTime);

		} else {
			return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
		}
	}

	@Override
	public Response<List<StorageDetail>> findColdWalletByCoin(String coinName) {
		List<StorageDetail> getStorageDetails = storageDetailsDao.findByStorageTypeAndCoinType(STORAGE_COLD, coinName);
		if (getStorageDetails != null) {
			return new Response<List<StorageDetail>>(SUCCESS_CODE, STORAGE_DETAILS_FETCHED_SUCCESSFULLY,
					getStorageDetails);
		} else {
			return new Response<>(FAILURE_CODE, NO_STORAGE_DETAILS_FOUND);
		}
	}

	@Override
	public Response<Object> getStorageList() {
		List<StorageDetail> isDetail = storageDetailsDao.findAll();
		if (!isDetail.isEmpty()) {
			return new Response<>(200, "List Fetched Successfully", isDetail);
		}
		return new Response<>(205, "Data Not Found");
	}
	
	@Override
	public Response<Object> updateBalance(BigDecimal storageBalance, String coinName) {
		Optional<StorageDetail> dataFound=storageDetailsDao.findByCoinType(coinName);
		if(dataFound.isPresent()) {
			dataFound.get().setHotWalletBalance(storageBalance);
			storageDetailsDao.save(dataFound.get());
			return new Response<>(200,"Updated Balance");
		}
		return new Response<>(205,"No Coin Found");
	}
}
