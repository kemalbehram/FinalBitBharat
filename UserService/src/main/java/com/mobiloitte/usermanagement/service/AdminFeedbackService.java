package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.enums.FeedbackStatus;
import com.mobiloitte.usermanagement.model.Response;

public interface AdminFeedbackService {

	Response<Object> getUserFeedBackList(Long userId, Long feedbackUserId, FeedbackStatus feedbackStatus, Integer page,
			Integer pageSize);

	Response<Object> getUserFeedBackDetail(Long userId, Long feedbackId);

}