package com.mobiloitte.microservice.wallet.serviceimpl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.CoinDepositWithdrawalDao;
import com.mobiloitte.microservice.wallet.dao.WalletHistoryDao;
import com.mobiloitte.microservice.wallet.dto.CalculateFeesRequestDto;
import com.mobiloitte.microservice.wallet.dto.MarketPriceDto;
import com.mobiloitte.microservice.wallet.dto.TakerMakerFeesRequestDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.CoinDepositWithdrawal;
import com.mobiloitte.microservice.wallet.entities.WalletHistory;
import com.mobiloitte.microservice.wallet.exception.CoinNotFoundException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.FeesAndAmountManagementService;

/**
 * The Class FeesAndAmountManagementServiceImpl.
 * 
 * @author Ankush Mohapatra
 */
@Service("FeesAndAmountManagementService")
public class FeesAndAmountManagementServiceImpl
		implements FeesAndAmountManagementService, WalletConstants, OtherConstants {

	// DAO Objects
	/** The coin dao. */
	@Autowired
	private CoinDao coinDao;

	/** The wallet history dao. */
	@Autowired
	private WalletHistoryDao walletHistoryDao;

	/** The coin deposit withdrawal dao. */
	@Autowired
	private CoinDepositWithdrawalDao coinDepositWithdrawalDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobiloitte.microservice.wallet.service.FeesAndAmountManagementService
	 * #setWithdrawlFee(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	@Transactional
	public Response<String> setWithdrawlFee(String coinShortName, BigDecimal withdrawFee) {
		Optional<Coin> getCoin = coinDao.findByCoinShortName(coinShortName);
		if (getCoin.isPresent()) {
			Coin coin = null;
			getCoin.get().setWithdrawlFee(withdrawFee);
			coin = coinDao.save(getCoin.get());
			if (coin != null) {
				return new Response<>(SUCCESS_CODE, WITHDRAW_FEE_UPDATE_SUCCESSFULLY);
			} else {
				return new Response<>(FAILURE_CODE, WITHDRAW_FEE_UPDATION_FAILED);
			}
		} else {
			throw new CoinNotFoundException("No such coin found: " + coinShortName);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.mobiloitte.microservice.wallet.service.FeesAndAmountManagementService
	 * #setMinimumCoinWithdrawlAmount(java.lang.String, java.math.BigDecimal)
	 */
	@Override
	@Transactional
	public Response<String> setMinimumCoinWithdrawlAmount(String coinShortName, BigDecimal minWithdrawlAmount,
			BigDecimal withdrawalAmountMax) {
		Optional<Coin> getCoin = coinDao.findByCoinShortName(coinShortName);
		if (getCoin.isPresent()) {
			Coin coin = null;
			getCoin.get().setWithdrawalAmount(minWithdrawlAmount);
			getCoin.get().setWithdrawalAmountMax(withdrawalAmountMax);
			coin = coinDao.save(getCoin.get());
			if (coin != null) {
				return new Response<>(SUCCESS_CODE, MINUMUM_WITHDRAW_AMOUNT_UPDATED_SUCCESSFULLY);
			} else {
				return new Response<>(FAILURE_CODE, UPDATION_FAILED);
			}
		} else {
			throw new CoinNotFoundException("No such coin found: " + coinShortName);
		}
	}

	@Override
	@Transactional
	public Response<String> getUpdateMinimumdepositeAmount(String coinName, BigDecimal depositeAmount) {
		Optional<Coin> getCoin = coinDao.findByCoinShortName(coinName);
		if (getCoin.isPresent()) {
			Coin coin = null;
			getCoin.get().setMinimumdepositeAmount(depositeAmount);

			coin = coinDao.save(getCoin.get());
			if (coin != null) {
				return new Response<>(SUCCESS_CODE, MINUMUM_WITHDRAW_AMOUNT_UPDATED_SUCCESSFULLY);
			} else {
				return new Response<>(FAILURE_CODE, UPDATION_FAILED);
			}
		} else {
			throw new CoinNotFoundException("No such coin found: " + coinName);
		}
	}
	@Override
	@Transactional
	public Response<String> setTakerMakeeFee(TakerMakerFeesRequestDto takerMakerFeesRequestDto) {
		Optional<Coin> getCoin = coinDao.findByCoinShortName(takerMakerFeesRequestDto.getCoinName());
		if (getCoin.isPresent()) {
			Coin coin = null;
			getCoin.get().setTakerFee(takerMakerFeesRequestDto.getTakerFee());
			getCoin.get().setMakerFee(takerMakerFeesRequestDto.getMakerFee());
			coin = coinDao.save(getCoin.get());
			if (coin != null) {
				return new Response<>(SUCCESS_CODE, TAKER_AND_MAKER_FEE_UPDATED_SUCCESSFULLY);
			} else {
				return new Response<>(FAILURE_CODE, UPDATION_FAILED);
			}
		} else {
			throw new CoinNotFoundException("No such coin found: " + takerMakerFeesRequestDto.getCoinName());
		}
	}
	/**
	 * Count fees from wallet history.
	 *
	 * @param historyList the history list
	 * @return the big decimal
	 */
	private BigDecimal countFeesFromWalletHistory(List<WalletHistory> historyList) {
		BigDecimal countTotalFee = BigDecimal.ZERO;
		for (int i = 0; i < historyList.size(); i++) {
			countTotalFee = countTotalFee.add(historyList.get(i).getAmount());
		}
		return countTotalFee;
	}

	@Override
	public Response<MarketPriceDto> setUpdateTradeFee(String coinName, BigDecimal tradeFee) {
		Optional<Coin> getCoin = coinDao.findByCoinShortName(coinName);
		if (getCoin.isPresent()) {
			Coin coin = null;
			getCoin.get().setTradeFee(tradeFee);
			coin = coinDao.save(getCoin.get());
			if (coin != null) {
				return new Response<>(SUCCESS_CODE, "UPDATION SUCCESSFULLY");
			} else {
				return new Response<>(FAILURE_CODE, "UPDATION FAILED");
			}
		} else {
			throw new CoinNotFoundException("No such coin found: " + coinName);
		}

	}

	/**
	 * Count withdrawal fees from crypto history.
	 *
	 * @param historyList the history list
	 * @return the big decimal
	 */
	private BigDecimal countWithdrawalFeesFromCryptoHistory(List<CoinDepositWithdrawal> historyList) {
		BigDecimal countTotalFee = BigDecimal.ZERO;
		for (int i = 0; i < historyList.size(); i++) {
			countTotalFee = countTotalFee.add(historyList.get(i).getFees());
		}
		return countTotalFee;
	}

	@Override
	public Response<Map<String, Object>> getProfitFees(CalculateFeesRequestDto calculateFeesRequestDto) {
		if (calculateFeesRequestDto != null) {

			List<WalletHistory> getWalletHistoryList = null;
			List<WalletHistory> getWalletHistoryList1 = null;
			List<CoinDepositWithdrawal> getCryptoHistoryList = null;
			Map<String, Object> responseMap = null;

			getCryptoHistoryList = coinDepositWithdrawalDao.findByTxnTypeAndCoinType(WITHDRAW,
					calculateFeesRequestDto.getCoinName());
			getWalletHistoryList = walletHistoryDao.findByActionAndCoinName(TAKER_FEE,
					calculateFeesRequestDto.getCoinName());
			getWalletHistoryList1 = walletHistoryDao.findByActionAndCoinName(MAKER_FEE,
					calculateFeesRequestDto.getCoinName());

			responseMap = new HashMap<>();
			BigDecimal countTotalMakerFee = countFeesFromWalletHistory(getWalletHistoryList1);
			BigDecimal countTotalFee = countFeesFromWalletHistory(getWalletHistoryList);
			BigDecimal calculatedFee = countWithdrawalFeesFromCryptoHistory(getCryptoHistoryList);
			responseMap.put("totalWithdrawFee", calculatedFee);
			responseMap.put("withdrawFeeCount", getCryptoHistoryList.size());
			responseMap.put("totalMakerfee", countTotalMakerFee);
			responseMap.put("makerFeeCount", getWalletHistoryList1.size());
			responseMap.put("totalTakerFee", countTotalFee);
			responseMap.put("TakerFeeCount", getWalletHistoryList.size());

			return new Response<>(SUCCESS_CODE, FEES_PROFIT_CALUCALTED_SUCCESSFULLY, responseMap);
		} else {
			Map<String, Object> responseMap = null;
			return new Response<Map<String, Object>>(205, "Coin name should not be empty.", responseMap);
		}
	}

	@SuppressWarnings("unused")
	@Override
	public Response<Map<String, BigDecimal>> getTakerMakerFee(String coinName) {
		Optional<Coin> getCoin = coinDao.findByCoinShortName(coinName);
		Map<String, BigDecimal> map = new HashMap<>();
		if (getCoin.isPresent()) {

			map.put("TakerFee", getCoin.get().getTakerFee());
			map.put("MakerFee", getCoin.get().getMakerFee());
			if (getCoin != null) {
				return new Response<>(SUCCESS_CODE, "Sucess", map);
			} else {
				return new Response<>(FAILURE_CODE, "Problem in getting Taker-Maker Fee");
			}
		} else {
			throw new CoinNotFoundException("No such coin found: " + coinName);
		}
	}

	@Override
	public Response<String> settransferFee(BigDecimal transferFee, BigDecimal minimumFee) {
		Optional<Coin> getCoin = coinDao.findByCoinShortName("XINDIA");
		if (getCoin.isPresent()) {
			Coin coin = null;
			getCoin.get().setMinimunInternalTransfer(transferFee);
			getCoin.get().setInternalTransferFee(minimumFee);
			coin = coinDao.save(getCoin.get());
			if (coin != null) {
				return new Response<>(SUCCESS_CODE, WITHDRAW_FEE_UPDATE_SUCCESSFULLY);
			} else {
				return new Response<>(FAILURE_CODE, WITHDRAW_FEE_UPDATION_FAILED);
			}
		} else {
			throw new CoinNotFoundException("No such coin found: " + "XINDIA");
		}

	}
}
