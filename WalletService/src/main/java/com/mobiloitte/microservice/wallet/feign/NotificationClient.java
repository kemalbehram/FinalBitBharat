package com.mobiloitte.microservice.wallet.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.microservice.wallet.dto.EmailDto;

@FeignClient("${exchange.application.notification-service}")
public interface NotificationClient {
	@PostMapping("send-notification")
	public Boolean sendNotification(@RequestBody EmailDto emailDto);

	@PostMapping("/send-custom-notification")
	public Boolean sendNotificationToParticularAll(@RequestParam(required = false) String userId,
			@RequestParam String roleList, @RequestParam String message);

}