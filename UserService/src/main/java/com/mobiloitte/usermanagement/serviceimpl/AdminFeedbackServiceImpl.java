package com.mobiloitte.usermanagement.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.dao.FeedbackDao;
import com.mobiloitte.usermanagement.dao.UserDetailsDao;
import com.mobiloitte.usermanagement.dto.UserFeedbackListDto;
import com.mobiloitte.usermanagement.enums.FeedbackStatus;
import com.mobiloitte.usermanagement.model.Feedback;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.UserDetail;
import com.mobiloitte.usermanagement.service.AdminFeedbackService;

@Service
public class AdminFeedbackServiceImpl implements AdminFeedbackService {

	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private UserDetailsDao userdetailDao;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Response<Object> getUserFeedBackList(Long userId, Long feedbackUserId, FeedbackStatus feedbackStatus,
			Integer page, Integer pageSize) {
		try {
			List<UserFeedbackListDto> feedbackList = new ArrayList<>();
			if (feedbackStatus == null) {
				List<Feedback> userFeedbackList = feedbackDao.findBySubmittedByOrUserUserId(feedbackUserId,
						feedbackUserId, PageRequest.of(page, pageSize, Direction.DESC, "feedBackId"));
				if (!userFeedbackList.isEmpty()) {
					for (Feedback feedback : userFeedbackList) {
						UserFeedbackListDto listDto = new UserFeedbackListDto();
						listDto.setFeedbackId(feedback.getFeedBackId());
						listDto.setDate(feedback.getCreateTime());
						listDto.setFeedbackStatus(feedback.getFeedbackStatus());
						listDto.setFeedbackMessage(feedback.getFeedbackMessage());
						if (feedbackUserId == feedback.getSubmittedBy()) {
							Optional<UserDetail> detail = userdetailDao.findByUserUserId(feedback.getSubmittedBy());
							if (detail.isPresent()) {
								String name = detail.get().getFirstName();
								listDto.setName(name);
							}
							listDto.setType("Given");
						} else {
							Optional<UserDetail> detail = userdetailDao
									.findByUserUserId(feedback.getUser().getUserId());
							if (detail.isPresent()) {
								String name = detail.get().getFirstName();
								listDto.setName(name);
							}
							listDto.setType("Recieved");
						}
						feedbackList.add(listDto);
					}
					return new Response<>(200,
							messageSource.getMessage("user.feedback.list.fetch.successfully", new Object[0], Locale.US),
							feedbackList);
				} else {
					return new Response<>(201, messageSource.getMessage("user.feedback.list.not.fetch.successfully",
							new Object[0], Locale.US));
				}
			} else {
				List<Feedback> userFeedbackList1 = feedbackDao.findBySubmittedByAndFeedbackStatus(feedbackUserId,
						feedbackStatus, PageRequest.of(page, pageSize, Direction.DESC, "feedBackId"));
				if (!userFeedbackList1.isEmpty()) {
					for (Feedback feedback : userFeedbackList1) {
						UserFeedbackListDto listDto = new UserFeedbackListDto();
						listDto.setFeedbackId(feedback.getFeedBackId());
						listDto.setDate(feedback.getCreateTime());
						listDto.setFeedbackStatus(feedback.getFeedbackStatus());
						listDto.setFeedbackMessage(feedback.getFeedbackMessage());
						if (feedbackUserId == feedback.getSubmittedBy()) {
							Optional<UserDetail> detail = userdetailDao.findByUserUserId(feedback.getSubmittedBy());
							if (detail.isPresent()) {
								String name = detail.get().getFirstName();
								listDto.setName(name);
							}
							listDto.setType("Given");
						} else {
							Optional<UserDetail> detail = userdetailDao
									.findByUserUserId(feedback.getUser().getUserId());
							if (detail.isPresent()) {
								String name = detail.get().getFirstName();
								listDto.setName(name);
							}
							listDto.setType("Recieved");
						}
						feedbackList.add(listDto);
					}
				}
				List<Feedback> userFeedbackList2 = feedbackDao.findByUserUserIdAndFeedbackStatus(feedbackUserId,
						feedbackStatus, PageRequest.of(page, pageSize, Direction.DESC, "feedBackId"));
				if (!userFeedbackList2.isEmpty()) {
					for (Feedback feedback : userFeedbackList2) {
						UserFeedbackListDto listDto = new UserFeedbackListDto();
						listDto.setFeedbackId(feedback.getFeedBackId());
						listDto.setDate(feedback.getCreateTime());
						listDto.setFeedbackStatus(feedback.getFeedbackStatus());
						listDto.setFeedbackMessage(feedback.getFeedbackMessage());
						if (feedbackUserId == feedback.getSubmittedBy()) {
							Optional<UserDetail> detail = userdetailDao.findByUserUserId(feedback.getSubmittedBy());
							if (detail.isPresent()) {
								String name = detail.get().getFirstName();
								listDto.setName(name);
							}
							listDto.setType("Given");
						} else {
							Optional<UserDetail> detail = userdetailDao
									.findByUserUserId(feedback.getUser().getUserId());
							if (detail.isPresent()) {
								String name = detail.get().getFirstName();
								listDto.setName(name);
							}
							listDto.setType("Recieved");
						}
						feedbackList.add(listDto);
					}
				}
				if (!feedbackList.isEmpty()) {
					return new Response<>(200,
							messageSource.getMessage("user.feedback.list.fetch.successfully", new Object[0], Locale.US),
							feedbackList);
				} else {
					return new Response<>(201, messageSource.getMessage("user.feedback.list.not.fetch.successfully",
							new Object[0], Locale.US));
				}
			}
		} catch (Exception e) {
			return new Response<>(201,
					messageSource.getMessage("user.feedback.list.not.fetch.successfully", new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> getUserFeedBackDetail(Long userId, Long feedbackId) {
		try {
			Optional<Feedback> userFeedbackDetail = feedbackDao.findById(feedbackId);
			if (userFeedbackDetail.isPresent()) {
				return new Response<>(200,
						messageSource.getMessage("user.feedback.detail.fetch.successfully", new Object[0], Locale.US),
						userFeedbackDetail);
			} else {
				return new Response<>(201, messageSource.getMessage("user.feedback.detail.not.fetch.successfully",
						new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(201,
					messageSource.getMessage("user.feedback.detail.not.fetch.successfully", new Object[0], Locale.US));
		}
	}

	
}