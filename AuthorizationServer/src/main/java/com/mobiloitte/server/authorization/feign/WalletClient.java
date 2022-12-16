package com.mobiloitte.server.authorization.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.mobiloitte.server.authorization.model.Response;

@FeignClient(value = "${exchange.application.wallet-service}")
public interface WalletClient {

	@GetMapping(value = "/wallet/create-wallet")
	public Response<String> createWallet(@RequestParam("id") Long userId,@RequestParam String randomId);
}
