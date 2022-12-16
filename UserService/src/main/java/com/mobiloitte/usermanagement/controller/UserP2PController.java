package com.mobiloitte.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.UserP2PService;

@RestController
public class UserP2PController {

	@Autowired
	private UserP2PService userP2PService;

	@PostMapping("/block-p2p-user")
	public Response<Object> blockP2PUser(@RequestHeader Long userId, @RequestParam Long blockUserId) {
		return userP2PService.blockP2PUser(userId, blockUserId);
	}

	@GetMapping("/get-p2p-user-profile")
	public Response<Object> getP2PUserProfile(@RequestHeader Long userId, @RequestParam Long p2pUserId) {
		return userP2PService.getP2PUserProfile(userId, p2pUserId);
	}

}