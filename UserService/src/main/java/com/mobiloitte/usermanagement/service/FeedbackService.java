package com.mobiloitte.usermanagement.service;

import java.util.List;

import com.mobiloitte.usermanagement.dto.FeedbackDto;
import com.mobiloitte.usermanagement.dto.FeedbackStaticDto;
import com.mobiloitte.usermanagement.enums.FeedbackStatus;
import com.mobiloitte.usermanagement.model.FeedbackModel;
import com.mobiloitte.usermanagement.model.Response;

public interface FeedbackService {

	Response<Object> submitUserFeedback(Long userId, FeedbackDto feedbackDto);

	Response<Object> getUserFeedback(Long userId, Long feedbackUserId, FeedbackStatus feedbackStatus, Integer page,
			Integer pageSize);

	Response<Object> getUserFeedbackCount(Long userId, Long feedbackUserId);

	Response<Object> getParticularFeedbackDetail(Long userId, Long feedbackId);

	Response<Object> updateUsersFeedback(Long userId, Long feedbackId, FeedbackDto feedbackDto);

	Response<Object> addFeedback(Long userId, FeedbackStaticDto feedbackStaticDto);

	Response<Object> getList(Long userId);

	Response<List<FeedbackModel>> getNewList(Integer page, Integer pageSize);

	Response<Object> getPerticularFeedbackDetailUser(Long userId, Long feedbackNewId);
}