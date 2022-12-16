package com.mobiloitte.microservice.wallet.serviceimpl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.microservice.wallet.constants.OtherConstants;
import com.mobiloitte.microservice.wallet.constants.WalletConstants;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.WalletDao;
import com.mobiloitte.microservice.wallet.dao.WalletHistoryDao;
import com.mobiloitte.microservice.wallet.dto.BlockBalanceDto;
import com.mobiloitte.microservice.wallet.dto.TransferBalanceDto;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.entities.WalletHistory;
import com.mobiloitte.microservice.wallet.exception.WalletNotFoundException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.OrderManagementService;
import com.mobiloitte.microservice.wallet.utils.APIUtils;

/**
 * The Class OrderManagementServiceImpl.
 * 
 * @author Ankush Mohapatra
 */
@Service("OrderManagementService")
public class OrderManagementServiceImpl implements OrderManagementService, WalletConstants, OtherConstants {
	public static final int POLKADOT_TIMEOUT = 180000;
	// DAO Objects
	/** The wallet dao. */
	@Autowired
	private WalletDao walletDao;

	/** The wallet history dao. */
	@Autowired
	private WalletHistoryDao walletHistoryDao;

	/** The coin dao. */
	@Autowired
	private CoinDao coinDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.OrderManagementService#
	 * updateUserWalletAndBlockBalanceOnOrderCancellation(com.mobiloitte.
	 * microservice.wallet.dto.BlockBalanceDto)
	 */
	@Override
	@Transactional
	public Response<String> updateUserWalletAndBlockBalanceOnOrderCancellation(BlockBalanceDto orderRequestDto) {
		Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(orderRequestDto.getCoin(),
				orderRequestDto.getUserId());

		if (getWalletDetails.isPresent()) {
			if (getWalletDetails.get().getBlockedBalance().compareTo(orderRequestDto.getAmountToBlock()) >= 0) {
				Wallet wallet = null;
				getWalletDetails.get().setWalletBalance(
						getWalletDetails.get().getWalletBalance().add(orderRequestDto.getAmountToBlock()));
				getWalletDetails.get().setBlockedBalance(
						getWalletDetails.get().getBlockedBalance().subtract(orderRequestDto.getAmountToBlock()));
				wallet = walletDao.save(getWalletDetails.get());
				if (wallet != null) {
					WalletHistory history = saveWalletHistoryDetails(orderRequestDto.getUserId(),
							orderRequestDto.getOrderId(), orderRequestDto.getAmountToBlock(), orderRequestDto.getCoin(),
							CANCEL);
					if (history != null) {
						return new Response<>(SUCCESS_CODE, BALANCE_UPDATED_SUCCESSFULLY,
								WALLET_HISTORY_UPDATED_SUCCESSFULLY);
					} else {
						return new Response<>(SUCCESS_CODE, BALANCE_UPDATED_SUCCESSFULLY,
								WALLET_HISTORY_UPDATION_FAILED);
					}
				} else {
					return new Response<>(FAILURE_CODE, UPDATION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, INSUFFICIENT_BLOCK_BALANCE);
			}
		} else {
			throw new WalletNotFoundException(NO_WALLET_FOUND_FOR_COIN + orderRequestDto.getCoin() + AND_USERID
					+ orderRequestDto.getUserId() + "");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.OrderManagementService#
	 * updateWalletBalanceOnOrderExecution(com.mobiloitte.microservice.wallet.dto.
	 * OrderRequestDto)
	 */
	@Override
	@Transactional
	public Response<String> updateWalletBalanceOnOrderExecution(BlockBalanceDto orderRequestDto) {
		Optional<Wallet> getWalletDetails = walletDao.findByCoinNameAndFkUserId(orderRequestDto.getCoin(),
				orderRequestDto.getUserId());
		if (getWalletDetails.isPresent()) {
			if (getWalletDetails.get().getWalletBalance().compareTo(orderRequestDto.getAmountToBlock()) >= 0) {
				Wallet wallet = null;
				getWalletDetails.get().setWalletBalance(
						getWalletDetails.get().getWalletBalance().subtract(orderRequestDto.getAmountToBlock()));
				getWalletDetails.get().setBlockedBalance(
						getWalletDetails.get().getBlockedBalance().add(orderRequestDto.getAmountToBlock()));
				wallet = walletDao.save(getWalletDetails.get());
				if (wallet != null) {
					WalletHistory history = saveWalletHistoryDetails(orderRequestDto.getUserId(),
							orderRequestDto.getOrderId(), orderRequestDto.getAmountToBlock(), orderRequestDto.getCoin(),
							BLOCK);
					if (history != null) {
						return new Response<>(SUCCESS_CODE, BALANCE_UPDATED_SUCCESSFULLY,
								WALLET_HISTORY_UPDATED_SUCCESSFULLY);
					} else {
						return new Response<>(SUCCESS_CODE, BALANCE_UPDATED_SUCCESSFULLY,
								WALLET_HISTORY_UPDATION_FAILED);
					}
				} else {
					return new Response<>(FAILURE_CODE, UPDATION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
			}
		} else {
			throw new WalletNotFoundException(NO_WALLET_FOUND_FOR_COIN + orderRequestDto.getCoin() + AND_USERID
					+ orderRequestDto.getUserId() + "");
		}
	}

	/**
	 * Save wallet history details.
	 *
	 * @param userId   the user id
	 * @param orderId  the order id
	 * @param amount   the amount
	 * @param coinName the coin name
	 * @param action   the action
	 * @return the wallet history
	 */
	private WalletHistory saveWalletHistoryDetails(Long userId, Long orderId, BigDecimal amount, String coinName,
			String action) {
		WalletHistory walletHistory = null;
		try {
			walletHistory = new WalletHistory();
			walletHistory.setUserId(userId);
			walletHistory.setOrderId(orderId);
			walletHistory.setAmount(amount);
			walletHistory.setCoinName(coinName);
			walletHistory.setAction(action);
//			walletHistory.setLiveAmount(BigDecimal.ZERO);
//			walletHistory.setDepositAmount(BigDecimal.ZERO);
			String apiBaseUri1 = "http://3.213.77.140:3015/eth/getMarketPriceById?ids="+coinName.toLowerCase();
			String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, POLKADOT_TIMEOUT);
			Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1, Map.class);
			if (allData1.containsKey("USD")) {
//				System.out.println(String.valueOf(allData1.get("USD")));
				BigDecimal value = BigDecimal.valueOf(Double.valueOf(String.valueOf(allData1.get("USD"))));
//				System.out.println(value.setScale(1, RoundingMode.UP)+"value");
				walletHistory.setLiveAmount(value.setScale(1, RoundingMode.UP));
//				System.out.println(amount.setScale(1, RoundingMode.UP)+"aMOUNT");
				walletHistory.setDepositAmount(value.setScale(1, RoundingMode.UP).multiply(amount.setScale(1, RoundingMode.UP)));
//				System.out.println(value.multiply(amount.setScale(1, RoundingMode.UP))+"Multiplyvalue");
				walletHistory = walletHistoryDao.save(walletHistory);
				return walletHistory;
			}
			walletHistory = walletHistoryDao.save(walletHistory);
			return walletHistory;
		} catch (Exception e) {
			return null;
		}
//		return walletHistory;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.microservice.wallet.service.OrderManagementService
	 * #transferBalanceOnOrderExecution(com.mobiloitte.microservice.wallet.dto.
	 * TransferBalanceDto)
	 */
	@Override
	@Transactional
	public Response<String> transferBalanceOnOrderExecution(TransferBalanceDto dto) {
		Optional<Coin> getCoinDetailsforCoin1 = coinDao.findByCoinShortName(dto.getCoin1());
		Optional<Coin> getCoinDetailsforCoin2 = coinDao.findByCoinShortName(dto.getCoin2());

		if (getCoinDetailsforCoin1.isPresent() && getCoinDetailsforCoin2.isPresent()) {
			BigDecimal makerFee = getCoinDetailsforCoin1.get().getMakerFee().multiply(dto.getAmount1())
					.divide(BigDecimal.valueOf(100));
			BigDecimal takerFee = getCoinDetailsforCoin2.get().getTakerFee().multiply(dto.getAmount2())
					.divide(BigDecimal.valueOf(100));

			String transferFromUser1ToUser2 = transferBalanceFromOneUserToOther(dto.getCoin1(), dto.getAmount1(),
					dto.getUser1(), dto.getUser2(), makerFee);
			String transferFromUser2ToUser1 = transferBalanceFromOneUserToOther(dto.getCoin2(), dto.getAmount2(),
					dto.getUser2(), dto.getUser1(), takerFee);
			if (transferFromUser1ToUser2.equals(SUCCESS) && transferFromUser2ToUser1.equals(SUCCESS)) {
				WalletHistory historyForUser1ForCoin1AndOrder1 = saveWalletHistoryDetails(dto.getUser1(),
						dto.getOrder1(), dto.getAmount1(), dto.getCoin1(), TRADE_DEDUCT);
				WalletHistory historyForUser2ForCoin1AndOrder1 = saveWalletHistoryDetails(dto.getUser2(),
						dto.getOrder1(), dto.getAmount1().subtract(makerFee), dto.getCoin1(), TRADE_CREDIT);
				WalletHistory historyForUser1ForCoin2AndOrder2 = saveWalletHistoryDetails(dto.getUser1(),
						dto.getOrder2(), dto.getAmount2().subtract(takerFee), dto.getCoin2(), TRADE_CREDIT);
				WalletHistory historyForUser2ForCoin2AndOrder2 = saveWalletHistoryDetails(dto.getUser2(),
						dto.getOrder2(), dto.getAmount2(), dto.getCoin2(), TRADE_DEDUCT);
				WalletHistory takerFeeHistory = saveWalletHistoryDetails(dto.getUser1(), dto.getOrder1(), takerFee,
						dto.getCoin2(), TAKER_FEE);
				WalletHistory makerFeeHistory = saveWalletHistoryDetails(dto.getUser2(), dto.getOrder2(), makerFee,
						dto.getCoin1(), MAKER_FEE);

				if (historyForUser1ForCoin1AndOrder1 != null && historyForUser2ForCoin1AndOrder1 != null
						&& historyForUser1ForCoin2AndOrder2 != null && historyForUser2ForCoin2AndOrder2 != null
						&& takerFeeHistory != null && makerFeeHistory != null) {
					return new Response<>(SUCCESS_CODE, BALANCE_TRANSFERRED_SUCCESSFULLY,
							WALLET_HISTORY_UPDATED_SUCCESSFULLY);
				} else {
					return new Response<>(SUCCESS_CODE, BALANCE_TRANSFERRED_SUCCESSFULLY,
							WALLET_HISTORY_UPDATION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, BALANCE_TRANSFER_FAILED);
			}
		} else {
			return new Response<>(FAILURE_CODE, NO_SUCH_COIN_FOUND);
		}
	}

	/**
	 * Transfer balance from one user to other.
	 *
	 * @param coin   the coin
	 * @param amount the amount
	 * @param user1  the user 1
	 * @param user2  the user 2
	 * @param fee    the fee
	 * @return the string
	 */
	private String transferBalanceFromOneUserToOther(String coin, BigDecimal amount, Long user1, Long user2,
			BigDecimal fee) {
		Optional<Wallet> getWalletDetailsOfUser1 = walletDao.findByCoinNameAndFkUserId(coin, user1);
		Optional<Wallet> getWalletDetailsOfUser2 = walletDao.findByCoinNameAndFkUserId(coin, user2);
		if (getWalletDetailsOfUser1.isPresent()) {
			if (getWalletDetailsOfUser2.isPresent()) {
				if (getWalletDetailsOfUser1.get().getBlockedBalance().compareTo(amount) >= 0) {
					getWalletDetailsOfUser1.get()
							.setBlockedBalance(getWalletDetailsOfUser1.get().getBlockedBalance().subtract(amount));
					getWalletDetailsOfUser2.get().setWalletBalance(
							getWalletDetailsOfUser2.get().getWalletBalance().add(amount.subtract(fee)));
					return SUCCESS;
				} else {
					return INSUFFICIENT_BLOCK_BALANCE;
				}
			} else {
				throw new WalletNotFoundException(NO_WALLET_FOUND_FOR_COIN + coin + AND_USERID + user2 + "");
			}
		} else {
			throw new WalletNotFoundException(NO_WALLET_FOUND_FOR_COIN + coin + AND_USERID + user1 + "");
		}
	}

}
