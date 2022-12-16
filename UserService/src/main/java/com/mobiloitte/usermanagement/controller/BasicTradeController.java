package com.mobiloitte.usermanagement.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.dto.UserProfileDto;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.BasicTradeService;
import com.mobiloitte.usermanagement.service.UserService;

@RestController
public class BasicTradeController {
	@Autowired
	UserService userService;

	@Autowired
	BasicTradeService basicTradeService;

	@PostMapping(value = "/send-payment-reject-email")
	public Response<Object> sendPaymentRejectedEmail(@RequestParam Long userId, @RequestParam Double amount) {
		System.err.println(amount);
		Response<UserProfileDto> userData = userService.getUserByUserId(userId);
		String email = userData.getData().getEmail();
		return basicTradeService.sendPaymentRejectEmail(email,amount);
	}
}
