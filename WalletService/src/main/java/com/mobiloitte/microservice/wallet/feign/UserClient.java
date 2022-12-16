package com.mobiloitte.microservice.wallet.feign;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.microservice.wallet.dto.UserEmailAndNameDto;
import com.mobiloitte.microservice.wallet.entities.ReferalComission;
import com.mobiloitte.microservice.wallet.model.Response;

@FeignClient("user-service")
public interface UserClient {

	@GetMapping(value = "/get-name-email")
	public Response<UserEmailAndNameDto> getUserByUserId(@RequestParam Long userId);

	@PostMapping(value = "/send-payment-reject-email")
	public Response<Object> sendPaymentRejectedEmail(@RequestParam Long userId, @RequestParam Double amount);

	@GetMapping(value = "/get-admin-email")
	public Response<String> getAdminEmailId();

	@GetMapping("/registered-user")
	public Response<Object> registredUser();

	@GetMapping("/full-user-list")
	public Response<UserEmailAndNameDto> fullUser();
	
	@GetMapping("/refer-comission-List")
	public Response<List<ReferalComission>> referCommisionList(@RequestParam Long userId,@RequestParam String coinName);
}
