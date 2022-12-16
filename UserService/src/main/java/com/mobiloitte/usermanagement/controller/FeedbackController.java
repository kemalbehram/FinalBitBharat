package com.mobiloitte.usermanagement.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.dto.FeedbackDto;
import com.mobiloitte.usermanagement.dto.FeedbackStaticDto;
import com.mobiloitte.usermanagement.enums.FeedbackStatus;
import com.mobiloitte.usermanagement.model.FeedbackModel;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.FeedbackService;

@RestController
public class FeedbackController {

	@Autowired
	private FeedbackService feedbackService;

	@PostMapping("/submit-user-feedback")
	public Response<Object> submitUserFeedback(@RequestHeader Long userId, @RequestBody FeedbackDto feedbackDto) {
		return feedbackService.submitUserFeedback(userId, feedbackDto);
	}

	@GetMapping("/get-user-feedback")
	public Response<Object> gedtUserFeedback(@RequestHeader Long userId, @RequestParam Long feedbackUserId,
			@RequestParam(required = false) FeedbackStatus feedbackStatus, @RequestParam Integer page,
			@RequestParam Integer pageSize) {
		return feedbackService.getUserFeedback(userId, feedbackUserId, feedbackStatus, page, pageSize);
	}

	@GetMapping("/get-user-feedback-count")
	public Response<Object> gedtUserFeedbackCount(@RequestHeader Long userId, @RequestParam Long feedbackUserId) {
		return feedbackService.getUserFeedbackCount(userId, feedbackUserId);
	}

	@GetMapping("/get-particular-feedback-detail")
	public Response<Object> getPerticularFeedbackDetail(@RequestHeader Long userId,
			@RequestParam(required = false) Long feedbackId) {
		return feedbackService.getParticularFeedbackDetail(userId, feedbackId);
	}
	@GetMapping("/get-particular-feedback-detail-User")
	public Response<Object> getPerticularFeedbackDetailUser(@RequestHeader Long userId,
			@RequestParam(required = false) Long feedbackNewId) {
		return feedbackService.getPerticularFeedbackDetailUser(userId, feedbackNewId);
	}


	@PostMapping("/update-user-feedback")
	public Response<Object> updateUserFeedback(@RequestHeader Long userId, @RequestParam Long feedbackId,
			@RequestBody FeedbackDto feedbackDto) {
		return feedbackService.updateUsersFeedback(userId, feedbackId, feedbackDto);
	}

	@PostMapping("/Feedback-for-static-content")
	public Response<Object> addFeedback(@RequestHeader Long userId, @RequestBody FeedbackStaticDto feedbackStaticDto) {
		return feedbackService.addFeedback(userId, feedbackStaticDto);
	}

	@GetMapping("/get-static-feedback-list-by-userId")
	public Response<Object> getList(@RequestHeader Long userId) {
		return feedbackService.getList(userId);
	}

	@GetMapping("/get-feedback-list")
	public Response<List<FeedbackModel>> getNewList(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		return feedbackService.getNewList(page, pageSize);
	}

}