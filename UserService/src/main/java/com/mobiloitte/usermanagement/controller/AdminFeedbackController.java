package com.mobiloitte.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.enums.FeedbackStatus;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.AdminFeedbackService;

@RestController
public class AdminFeedbackController {

	@Autowired
	private AdminFeedbackService adminFeedbackService;

	@GetMapping("/admin/get-user-feedback-list")
	public Response<Object> getUserFeedbackList(@RequestHeader Long userId,
			@RequestParam(required = false) Long feedbackUserId,
			@RequestParam(required = false) FeedbackStatus feedbackStatus, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		return adminFeedbackService.getUserFeedBackList(userId, feedbackUserId, feedbackStatus, page, pageSize);
	}

	@GetMapping("/admin/get-user-feedback-Detail")
	public Response<Object> getUserFeedbackList(@RequestHeader Long userId,
			@RequestParam(required = false) Long feedbackId) {
		return adminFeedbackService.getUserFeedBackDetail(userId, feedbackId);
	}

}