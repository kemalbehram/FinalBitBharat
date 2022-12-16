package com.mobiloitte.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.AdminP2PService;

@RestController
@RequestMapping("/admin")
public class AdminP2PController {

	@Autowired
	private AdminP2PService adminP2PService;

	@GetMapping("/get-user-blockedby-list")
	public Response<Object> getUserBlockedByList(@RequestHeader Long userId, @RequestParam Long blockedUserId,
			@RequestParam Integer page, @RequestParam Integer pageSize) {
		return adminP2PService.getuserBlockedByList(userId, blockedUserId, page, pageSize);
	}

	@PostMapping("/remove-user-from-block-list")
	public Response<Object> removeUserFromBlockList(@RequestHeader Long userId, @RequestParam Long blockedId) {
		return adminP2PService.removeUserFromBlockList(userId, blockedId);
	}

}