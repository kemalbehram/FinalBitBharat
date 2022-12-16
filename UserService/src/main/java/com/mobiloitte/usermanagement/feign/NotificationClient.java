package com.mobiloitte.usermanagement.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.usermanagement.dto.EmailDto;

@FeignClient("${exchange.application.notification-service}")
public interface NotificationClient {
	@PostMapping("send-notification")
	public Boolean sendNotification(@RequestBody EmailDto emailDto);

	@PostMapping("/delete-notification-data-on-delete-customer")
	public Boolean deleteNotificationDataOnDeleteCustomer(@RequestParam Long userId);

	@PostMapping("/send-custom-notification")
	public Boolean sendNotificationToParticularAll(@RequestParam(required = false) String userId,
			@RequestParam String roleList, @RequestParam String message);

}