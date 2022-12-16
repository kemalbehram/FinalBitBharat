package com.mobiloitte.usermanagement.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.usermanagement.dto.TradeCountDto;
import com.mobiloitte.usermanagement.model.Response;

@FeignClient("${exchange.application.p2p-exchange}")
public interface P2PClient {

	@GetMapping("/get-trade-count-by-userId")
	public Response<TradeCountDto> getTradeCount(@RequestParam Long userId);

}