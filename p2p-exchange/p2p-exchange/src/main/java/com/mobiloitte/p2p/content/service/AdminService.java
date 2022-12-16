package com.mobiloitte.p2p.content.service;

import java.util.Map;

import com.mobiloitte.p2p.content.dto.AdminChargeDto;
import com.mobiloitte.p2p.content.dto.P2PAdvertisementDto;
import com.mobiloitte.p2p.content.dto.SearchAndFilterDto;
import com.mobiloitte.p2p.content.enums.DisputeStatus;
import com.mobiloitte.p2p.content.enums.ExchangeStatusType;
import com.mobiloitte.p2p.content.enums.OrderType;
import com.mobiloitte.p2p.content.enums.PaymentType;
import com.mobiloitte.p2p.content.enums.TradeStatus;
import com.mobiloitte.p2p.content.model.AdminCharge;
import com.mobiloitte.p2p.content.model.Response;

public interface AdminService {

	Response<Map<String, Object>> getAdvertisementForAdmin(OrderType orderType, Integer page, Integer pageSize);

	Response<Map<String, Object>> getTradeHistory(TradeStatus tradeStatus, Integer page, Integer pageSize);

	Response<Map<String, Object>> getAdvertisementForAdminByTradingId(String tradeId);

	Response<Object> filterTradeDetails(TradeStatus tradeStatus, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize);

	Response<Map<String, Object>> getTradeHistoryFromDate(Long fromeDate, Long toDate, Integer page, Integer pageSize);

	Response<Object> getDetailsByEmail(String email);

	Response<AdminCharge> setBasicTradeFees(AdminChargeDto adminChargeDto);

	Response<Object> getAdvertisementSearch( Long userId, Long userId2, Long peerToPeerExchangeId, String userName, ExchangeStatusType orderStatus,
			String country, String currency, Long amount, PaymentType paymentType, OrderType orderType, Long fromDate, Long toDate, Integer page,
			Integer pageSize);

	Response<Object> getTradingSearch(Long userId, String search, TradeStatus tradeStatus, String country, PaymentType paymentType, OrderType type, Long fromDate,
			Long toDate, DisputeStatus disputeStatus, Integer page, Integer pageSize);

	Response<Object> deleteAdvertisement(Long userId, Long peerToPeerExchangeId);

	Response<Object> editAdvertisement(Long userId, P2PAdvertisementDto p2pBuySellDto, Long peerToPeerExchangeId);

	Response<Object> forReleaseBitcoinsByAdmin(String tradeId, Long userId);

	Response<Object> setEnableDisable(OrderType orderType, ExchangeStatusType statusType, Long userId, Long p2pId);

	Response<Object> getTotalP2pCount();

	

	Response<Object> getBasicTradeFees(String coinName);

	//Response<Object> assignStaffForDispute(Long userId, Long staffId, String disputeId);

	Response<Object> recommendation(Long userId, String disputeId, String recommendation);

	Response<Object> assignStaffForDispute(Long userId, String staffId, String disputeId);



	Response<Map<String, Object>> getDisputeHistory(Long buyerId, Long sellerId, Integer page, Integer pageSize);



	Response<Map<String, Object>> getTradeHistoryWithChat(Long buyerId, Long sellerId, Integer page, Integer pageSize);

	Response<Object> searchAndFilterAdmin(Long userId, SearchAndFilterDto searchAndFilterDto);
	
	
	
	
	
	
	
	
	
	
	
	
	

}
