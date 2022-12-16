package com.mobiloitte.microservice.wallet.serviceimpl;

import java.math.BigDecimal;
import java.util.Collections;
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
import com.mobiloitte.microservice.wallet.dao.BasicTradeHistoryDao;
import com.mobiloitte.microservice.wallet.dao.CoinDao;
import com.mobiloitte.microservice.wallet.dao.WalletDao;
import com.mobiloitte.microservice.wallet.dto.ExchangeRequestDto;
import com.mobiloitte.microservice.wallet.entities.BasicTradeHistory;
import com.mobiloitte.microservice.wallet.entities.Coin;
import com.mobiloitte.microservice.wallet.entities.Wallet;
import com.mobiloitte.microservice.wallet.enums.OrderStatus;
import com.mobiloitte.microservice.wallet.enums.OrderType;
import com.mobiloitte.microservice.wallet.exception.CoinNotFoundException;
import com.mobiloitte.microservice.wallet.exception.WalletNotFoundException;
import com.mobiloitte.microservice.wallet.model.Response;
import com.mobiloitte.microservice.wallet.service.BasicExchangeService;

@Service("BasicExchangeService")
public class BasicExchangeServiceImpl implements BasicExchangeService, WalletConstants, OtherConstants{

	@Autowired
	private WalletDao walletDao;

	@Autowired
	private CoinDao coinDao;

	@Autowired
	private BasicTradeHistoryDao basicTradeHistoryDao;

	@Override
	@Transactional
	public Response<String> placeBuyOrderFromWallet(ExchangeRequestDto buyOrderDto, Long fkUserId, String userEmail) {
		Optional<Coin> getBaseCoinDetails = coinDao.findByCoinShortName(buyOrderDto.getBaseCoin());
		if(getBaseCoinDetails.isPresent())
		{
			Optional<Coin> getExecCoinDetails = coinDao.findByCoinShortName(buyOrderDto.getExecCoin());
			if(getExecCoinDetails.isPresent())
			{
				Optional<Wallet> getBaseCoinWallet = walletDao.findByCoinNameAndFkUserId(buyOrderDto.getBaseCoin(), fkUserId);
				if(getBaseCoinWallet.isPresent())
				{
					Optional<Wallet> getExecCoinWallet = walletDao.findByCoinNameAndFkUserId(buyOrderDto.getExecCoin(), fkUserId);
					if(getExecCoinWallet.isPresent())
					{
						BigDecimal getBaseCoinAmount = getCalculatedBaseCoinAmount(getExecCoinDetails.get(), buyOrderDto.getExecCoinAmount());
						BigDecimal getFees = buyOrderDto.getExecCoinAmount().multiply(getExecCoinDetails.get().getBasicBuyFee().multiply(BigDecimal.valueOf(0.01)));
						if(getBaseCoinWallet.get().getWalletBalance().compareTo(getBaseCoinAmount) >= 0)
						{
							Wallet updatedBaseWallet = updateBaseWalletBalance(getBaseCoinWallet.get(), BUY, getBaseCoinAmount);
							Wallet updatedExecWallet = updateExecWalletBalance(getExecCoinWallet.get(), BUY, buyOrderDto.getExecCoinAmount().subtract(getFees));
							BasicTradeHistory savedOrderDetails = saveTradeDetails(userEmail, fkUserId, buyOrderDto, OrderType.BUY,
									getBaseCoinAmount, OrderStatus.APPROVED, getExecCoinDetails.get().getMarketPriceInUsd(), 
									buyOrderDto.getExecCoinAmount().subtract(getFees), getFees);
							if(updatedBaseWallet != null && updatedExecWallet != null && savedOrderDetails != null)
							{
								return new Response<>(SUCCESS_CODE, BUY_ORDER_PLACED_AND_EXECUTED_SUCCESSFULLY);
							}else {
								return new Response<>(FAILURE_CODE, ORDER_PLACE_FAILED);
							}	
						}else {
							return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
						}
					}else {
						throw new WalletNotFoundException("No wallet found for coin: " + buyOrderDto.getExecCoin() + " and user: "
								+ userEmail + "");
					}
				}else {
					throw new WalletNotFoundException("No wallet found for coin: " + buyOrderDto.getBaseCoin() + " and user: "
							+ userEmail + "");
				}
			}else {
				throw new CoinNotFoundException("No exec coins found named: "+ buyOrderDto.getExecCoin()+"");
			}
		}else {
			throw new CoinNotFoundException("No base coins found named: "+ buyOrderDto.getBaseCoin()+"");
		}
	}

	@Override
	@Transactional
	public Response<String> placeSellOrderFromWallet(ExchangeRequestDto sellOrderDto, Long fkUserId, String userEmail) {
		Optional<Coin> getBaseCoinDetails = coinDao.findByCoinShortName(sellOrderDto.getBaseCoin());
		if(getBaseCoinDetails.isPresent())
		{
			Optional<Coin> getExecCoinDetails = coinDao.findByCoinShortName(sellOrderDto.getExecCoin());
			if(getExecCoinDetails.isPresent())
			{
				Optional<Wallet> getBaseCoinWallet = walletDao.findByCoinNameAndFkUserId(sellOrderDto.getBaseCoin(), fkUserId);
				if(getBaseCoinWallet.isPresent())
				{
					Optional<Wallet> getExecCoinWallet = walletDao.findByCoinNameAndFkUserId(sellOrderDto.getExecCoin(), fkUserId);
					if(getExecCoinWallet.isPresent())
					{
						BigDecimal getBaseCoinAmount = getCalculatedBaseCoinAmount(getExecCoinDetails.get(), sellOrderDto.getExecCoinAmount());
						BigDecimal getFees = getBaseCoinAmount.multiply(getExecCoinDetails.get().getBasicSellFee().multiply(BigDecimal.valueOf(0.01)));
						if(getExecCoinWallet.get().getWalletBalance().compareTo(sellOrderDto.getExecCoinAmount()) >= 0)
						{
							Wallet updatedExecWallet = updateExecWalletBalance(getExecCoinWallet.get(), SELL, sellOrderDto.getExecCoinAmount());
							Wallet updatedBaseWallet = updateBaseWalletBalance(getBaseCoinWallet.get(), SELL, getBaseCoinAmount.subtract(getFees));
							BasicTradeHistory savedOrderDetails = saveTradeDetails(userEmail, fkUserId, sellOrderDto, OrderType.SELL,
									getBaseCoinAmount.subtract(getFees), OrderStatus.APPROVED, getExecCoinDetails.get().getMarketPriceInUsd(), 
									sellOrderDto.getExecCoinAmount(), getFees);
							if(updatedExecWallet != null && savedOrderDetails != null && updatedBaseWallet != null)
							{
								return new Response<>(SUCCESS_CODE, SELL_ORDER_PLACED_AND_EXECUTED_SUCCESSFULLY);
							}else {
								return new Response<>(FAILURE_CODE, ORDER_PLACE_FAILED);
							}	
						}else {
							return new Response<>(FAILURE_CODE, INSUFFICIENT_WALLET_BALANCE);
						}
					}else {
						throw new WalletNotFoundException("No wallet found for coin: " + sellOrderDto.getExecCoin() + " and user: "
								+ userEmail + "");
					}
				}else {
					throw new WalletNotFoundException("No wallet found for coin: " + sellOrderDto.getBaseCoin() + " and user: "
							+ userEmail + "");
				}
			}else {
				throw new CoinNotFoundException("No exec coins found named: "+ sellOrderDto.getExecCoin()+"");
			}

		}else {
			throw new CoinNotFoundException("No base coins found named: "+ sellOrderDto.getBaseCoin()+"");
		}
	}

	private BigDecimal getCalculatedBaseCoinAmount(Coin coin, BigDecimal execPrice)
	{	
		return execPrice.multiply(coin.getMarketPriceInUsd());	
	}

	private Wallet updateBaseWalletBalance(Wallet wallet, String paymentType, BigDecimal amount)
	{
		try {
			if(paymentType.equals(BUY))
			{
				wallet.setWalletBalance(wallet.getWalletBalance().subtract(amount));
			}else {
				wallet.setWalletBalance(wallet.getWalletBalance().add(amount));
			}
			wallet = walletDao.save(wallet);
			return wallet;
		} catch (Exception e) {
			return null;
		}

	}

	private Wallet updateExecWalletBalance(Wallet wallet, String paymentType, BigDecimal amount)
	{
		try {
			if(paymentType.equals(BUY))
			{
				wallet.setWalletBalance(wallet.getWalletBalance().add(amount));
			}else {
				wallet.setWalletBalance(wallet.getWalletBalance().subtract(amount));
			}
			wallet = walletDao.save(wallet);
			return wallet;
		} catch (Exception e) {
			return null;
		}

	}


	private BasicTradeHistory saveTradeDetails(String userEmail, Long fkUserId, ExchangeRequestDto buyOrderDto, 
			OrderType orderType, BigDecimal baseCoinAmount, OrderStatus status, 
			BigDecimal marketPrice, BigDecimal updatedExecAmount, BigDecimal fees)
	{
		try {
			BasicTradeHistory exchangeHistory = new BasicTradeHistory();
			exchangeHistory.setUserEmail(userEmail);
			exchangeHistory.setFkUserId(fkUserId);
			exchangeHistory.setBaseCoinName(buyOrderDto.getBaseCoin());
			exchangeHistory.setExecCoinName(buyOrderDto.getExecCoin());
			exchangeHistory.setBaseCoinMarketPrice(marketPrice);
			exchangeHistory.setPaymentMethod(buyOrderDto.getPaymentMethod());
			exchangeHistory.setBaseCoinamount(baseCoinAmount);
			exchangeHistory.setExecCoinamount(updatedExecAmount);
			exchangeHistory.setFee(fees);
			exchangeHistory.setOrderType(orderType);
			exchangeHistory.setStatus(status);
			exchangeHistory = basicTradeHistoryDao.save(exchangeHistory);
			return exchangeHistory;
		} catch (Exception e) {
			return null;
		}

	}

	@Override
	public Response<Map<String, Object>> getExchangeHistory(Long fkUserId, OrderType orderType, String exeCoin, Integer page, Integer pageSize) {
		List<BasicTradeHistory> getUserTradeHistory = basicTradeHistoryDao.findByFkUserIdAndOrderTypeAndExecCoinName(fkUserId, orderType, exeCoin,
				PageRequest.of(page, pageSize, Direction.DESC, "basicTradeHistoryId"));
		Long getTotalCount = basicTradeHistoryDao.countByFkUserIdAndOrderTypeAndExecCoinName(fkUserId, orderType, exeCoin);
		Map<String, Object> responseMap = new HashMap<>();
		if(getUserTradeHistory != null && !getUserTradeHistory.isEmpty())
		{
			responseMap.put(RESULT_LIST, getUserTradeHistory);
			responseMap.put(TOTAL_COUNT, getTotalCount);
			return new Response<>(SUCCESS_CODE, USER_ORDER_HISTORY_FETCHED_SUCCESSFULLY, responseMap);
		}else {
			responseMap.put(RESULT_LIST, Collections.emptyList());
			responseMap.put(TOTAL_COUNT, getTotalCount);
			return new Response<>(SUCCESS_CODE, NO_RECORDS_FOUND, responseMap);
		}
	}


}
