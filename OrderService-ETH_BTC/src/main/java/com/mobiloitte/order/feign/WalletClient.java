package com.mobiloitte.order.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

import com.mobiloitte.order.dto.BlockBalanceDto;
import com.mobiloitte.order.dto.TransferBalanceDto;
import com.mobiloitte.order.model.Response;

@FeignClient("${exchange.application.wallet-service}")
public interface WalletClient {
	@PostMapping("/orders/block-balance")
	Response<Object> verifyAndBlockBalance(BlockBalanceDto dto);

	@PostMapping("/orders/transfer-balance")
	Response<Object> transferBalance(TransferBalanceDto dto);

	@PostMapping("/orders/return-block-balance")
	Response<Object> returnBlockBalance(BlockBalanceDto blockBalanceDto);
}
