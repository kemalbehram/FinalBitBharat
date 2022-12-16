package com.mobiloitte.p2p.content.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.mobiloitte.p2p.content.dto.MarketPriceDto;
import com.mobiloitte.p2p.content.dto.P2PAdvertisementDto;
import com.mobiloitte.p2p.content.dto.PaymentMethodDto;
import com.mobiloitte.p2p.content.dto.PaymentMethodUpdateDto;
import com.mobiloitte.p2p.content.dto.TradeCountDto;
import com.mobiloitte.p2p.content.enums.DisputeStatus;
import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.model.P2PAdvertisement;
import com.mobiloitte.p2p.content.model.Response;

public interface CreateAdvertisementService {

	Response<Object> createAdvertisement(P2PAdvertisementDto p2pBuySellDto, Long userId);

	Response<Map<String, Object>> getBuySEllAdvertisementDetails(OrderType orderType, Long userId, Integer page, Integer pageSize);

	Response<Object> editAdvertisement(Long userId, P2PAdvertisementDto p2pBuySellDto, Long peerToPeerExchangeId);

	Response<List<P2PAdvertisement>> getAllBuyAdvertismentForAdmin(OrderType orderType, Integer page,
			Integer pageSize);

	Response<Object> getDetailsOfBuyByfkId(Long userId);

	Response<Object> getDetailsOfSellByfkId(Long userId);

	Response<Object> getBitCoinInUSD(BigDecimal amount, Long userId, BigDecimal noOfCoins);
	

	Response<Object> sendMessageAfterPressCancelButton(Long userId, String coinName, String cancelReason);

	Response<Object> sendMessageAfterPressPaidButton(String tradeId, Long userId);

	Response<Object> setMarketPrice(MarketPriceDto marketpricedto);

	Response<Object> forReleaseBitcoins(String tradeId, Long userId);
	

	Response<Map<String, Object>> getAllTradeDetailsList(TradeStatus tradeStatus, Long userId, Integer page, Integer pageSize);
	

	Response<Object> sendTradeRequest(Long userId, Long peerToPeerExchangeId,BigDecimal amountInRange, BigDecimal restrictAmount);

	Response<Map<String, Object>> getAddvertisementListThroughAmount(BigDecimal amount, PaymentType paymentType, String currency, String country, Integer page, Integer pageSize);

	Response<Object> forStatusChecking(Long peerToPeerExchangeId, String tradeId);

	Response<Object> forStatusDisputed(Long userId, String tradeId, DisputeStatus disputeStatus);

	Response<Object> getTradeStatus(String tradeId);

//	Response<Map<String, Object>> getOpenTradeDetails(Long userId, Integer page, Date fromDate, String coin, OrderType orderType, Integer pageSize, Date toDate);

	Response<Object> sendTradeRequestUsingRestrictAmount(Long userId, Long peerToPeerExchangeId,
			BigDecimal restrictAmount);

//	Response<Object> setEnableDisable(OrderType orderType, ExchangeStatusType statusType, Long userId);

	Response<Object> getAdvertisementStataus(OrderType orderType, Long userId);

	Response<Map<String, Object>> getBuySEllAdvertisementList(OrderType orderType, Integer page, Integer pageSize, String cryptoCoin);

	Response<TradeCountDto> getTradeCount(Long userId);

	
	Response<Object> getTradingSearch(Long userId, String search, TradeStatus tradeStatus, String country,
			PaymentType paymentType, OrderType type, Long fromDate, Long toDate, DisputeStatus disputeStatus, Integer page, Integer pageSize, Long userId2);

	Response<Object> getAdvertisementSearch(Long userId, Long userId2, Long peerToPeerExchangeId, String userName,
			ExchangeStatusType orderStatus, String country, String currency, Long amount, PaymentType paymentType,
			OrderType orderType, Long fromDate, Long toDate, Integer page, Integer pageSize, String cryptoCoin, BigDecimal margin, Integer paymentWindow);

	Response<Object> getAllTradeDetailsListForPaymentWindow(Long userId);

	Response<Map<String, Object>> getPostTradeListForApp(OrderType orderType, Long userId);

	BigDecimal getBlockBalance(Long userId, String coinName);

	Response<Map<String, Object>> getCompletedTradeDetails(Long userId, Integer page, Integer pageSize);

	Response<Object> setEnableDisable(OrderType orderType, ExchangeStatusType statusType, Long userId);

	Response<Object> setEnableDisableParticular(Long userId,
			Long peerToPeerExchangeId, ExchangeStatusType exchangeStatusType, OrderType orderType);

	Response<Object> deleteAdvertisement(Long userId, Long peerToPeerExchangeId);

	Response<Object> addPaymentMethod(Long userId, PaymentMethodDto paymentMethodDto, String paymentType);

	Response<Object> editPaymentMethod(Long userId, PaymentMethodUpdateDto paymentMethodUpdateDto,
			String paymentType);

	Response<Object> deletePaymentMethod(Long userId, Long paymentDetailId);

	Response<Object> getpaymentList(Long userId);

	Response<Object> getpaymentdetails(Long userId, Long paymentDetailId);

	Response<Object> getuserdetails(Long userId);

	Response<Map<String, Object>> getOpenTradeDetails(Long userId, Integer page, Long fromDate, String coin,
			OrderType orderType, Integer pageSize, Long toDate);

	Response<Object> getBankDetails(Long userId, Long p2pId);

	Response<Object> getp2pDetails(Long p2pId);

	Response<Object> gettradedetail(Long p2pId, Integer page, Integer pageSize);

	Response<Map<String, Object>> getBuySEllAdvertisementDetailsNew(OrderType orderType, Long userId, Integer page,
			Integer pageSize);

	Response<Object> cancelDispute(Long userId, String tradeId);

	


	
	


	
	


	
	
	
	
	
	


	
	
	
	
	


	

}
