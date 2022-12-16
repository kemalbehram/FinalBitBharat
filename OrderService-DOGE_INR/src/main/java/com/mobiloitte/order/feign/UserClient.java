package com.mobiloitte.order.feign;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.order.dto.UserEmailAndNameDto;
import com.mobiloitte.order.model.Response;

@FeignClient("${exchange.application.user-service}")
public interface UserClient {

	@GetMapping(value = "/get-user-information-by-id")
	public Response<Map<String, String>> getEmailNameAndPhoneNo(@RequestParam Long userId);

	@PostMapping("/getNotificationPreference-order")
	public Response<Object> getNotificationPreferenceOrder(@RequestHeader Long userId);

	@GetMapping("/get-name-email")
	public Response<UserEmailAndNameDto> getEmailAndName(@RequestParam Long userId);
}
