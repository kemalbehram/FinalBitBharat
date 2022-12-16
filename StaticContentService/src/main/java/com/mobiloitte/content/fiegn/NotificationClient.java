package com.mobiloitte.content.fiegn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mobiloitte.content.dto.EmailDto;

@FeignClient("${exchange.application.notification-service}")
public interface NotificationClient {
	@PostMapping("send-notification")
	public Boolean sendNotification(@RequestBody EmailDto emailDto);

}
