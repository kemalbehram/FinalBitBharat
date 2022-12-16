package com.mobiloitte.microservice.wallet.service;

import java.util.Map;

import com.mobiloitte.microservice.wallet.dto.ExchangeRequestDto;
import com.mobiloitte.microservice.wallet.enums.OrderType;
import com.mobiloitte.microservice.wallet.model.Response;

public interface BasicExchangeService {

	Response<String> placeBuyOrderFromWallet(ExchangeRequestDto buyOrderDto, Long fkUserId, String userEmail);
	
	Response<String> placeSellOrderFromWallet(ExchangeRequestDto sellOrderDto, Long fkUserId, String userEmail);
	
	Response<Map<String, Object>> getExchangeHistory(Long fkUserId, OrderType orderType, String exeCoin, Integer page, Integer pageSize);
}
