package com.mobiloitte.usermanagement.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.constants.EmailConstants;
import com.mobiloitte.usermanagement.dao.FeedbackDao;
import com.mobiloitte.usermanagement.dao.Feedbackstaticdao;
import com.mobiloitte.usermanagement.dto.FeedbackDto;
import com.mobiloitte.usermanagement.dto.FeedbackListDto;
import com.mobiloitte.usermanagement.dto.FeedbackStaticDto;
import com.mobiloitte.usermanagement.enums.FeedbackEnum;
import com.mobiloitte.usermanagement.enums.FeedbackStatus;
import com.mobiloitte.usermanagement.model.Feedback;
import com.mobiloitte.usermanagement.model.FeedbackModel;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.service.FeedbackService;
import com.mobiloitte.usermanagement.util.MailSender;

@Service
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackDao feedbackDao;

	@Autowired
	private EntityManager em;

	@Autowired
	private Feedbackstaticdao feedbackstaticdao;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private MailSender mailSender;

	@Override
	public Response<Object> submitUserFeedback(Long userId, FeedbackDto feedbackDto) {
		try {
			Feedback feedback = new Feedback();
			feedback.setFeedbackStatus(feedbackDto.getFeedbackStatus());
			feedback.setFeedbackMessage(feedbackDto.getFeedbackMessage());
			feedback.setSubmittedBy(userId);
			User user = new User();
			user.setUserId(feedbackDto.getUserId());
			feedback.setUser(user);
			feedbackDao.save(feedback);
			return new Response<>(200, "User Feedback Submitted Successfully");
		} catch (Exception e) {
			return new Response<>(201, "User Feedback Not Submitted");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> getUserFeedback(Long userId, Long feedbackUserId, FeedbackStatus feedbackStatus,
			Integer page, Integer pageSize) {
		StringBuilder query = new StringBuilder(
				"select f.feedBackId, f.feedbackStatus, f.feedbackMessage, f.submittedBy, f.createTime from Feedback f where f.user.userId=");
		List<String> conditions = new ArrayList<>();
		if (feedbackStatus != null) {
			conditions.add("((f.feedbackStatus like :feedbackStatus))");
		}
		query.append(feedbackUserId);
		if (!conditions.isEmpty()) {
			query.append(" and ");
			query.append(String.join(" and ", conditions.toArray(new String[0])));
		}
		query.append(" order by f.feedBackId desc ");
		Query createQuery = em.createQuery(query.toString());
		if (feedbackStatus != null) {
			createQuery.setParameter("feedbackStatus", feedbackStatus);
		}
		int filteredResultCount = createQuery.getResultList().size();
		if (page != null && pageSize != null) {
			createQuery.setFirstResult(page * pageSize);
			createQuery.setMaxResults(pageSize);
		}
		List<Object[]> list = createQuery.getResultList();
		List<FeedbackListDto> response = list.parallelStream().map(o -> {
			FeedbackListDto dto = new FeedbackListDto();
			dto.setFeedbackId((Long) o[0]);
			dto.setFeedbackStatus((FeedbackStatus) o[1]);
			dto.setFeedbackMessage((String) o[2]);
			dto.setSubmittedBy((Long) o[3]);
			dto.setCreateTime((Date) o[4]);
			return dto;
		}).collect(Collectors.toList());
		Long positiveFeedbackCount = feedbackDao.countByFeedbackStatusAndUserUserId(FeedbackStatus.POSITIVE, userId);
		Long negativeFeedbackCount = feedbackDao.countByFeedbackStatusAndUserUserId(FeedbackStatus.NEGATIVE, userId);
		Map<String, Object> map = new HashMap<>();
		if (!response.isEmpty()) {
			map.put("size", filteredResultCount);
			map.put("positiveFeedbackCount", positiveFeedbackCount);
			map.put("negativeFeedbackCount", negativeFeedbackCount);
			map.put("list", response);
			return new Response<>(200, "Feedback List Of User Fetched Successfully", map);
		} else {
			return new Response<>(201, "Feedback List Of User Not Found");
		}
	}

	@Override
	public Response<Object> getUserFeedbackCount(Long userId, Long feedbackUserId) {
		try {
			List<Feedback> userFeedback = feedbackDao.findByUserUserId(feedbackUserId);
			if (!userFeedback.isEmpty()) {
				Long positiveFeedbackCount = feedbackDao.countByFeedbackStatusAndUserUserId(FeedbackStatus.POSITIVE,
						feedbackUserId);
				Long negativeFeedbackCount = feedbackDao.countByFeedbackStatusAndUserUserId(FeedbackStatus.NEGATIVE,
						feedbackUserId);
				Long nutralFeedbackCount = feedbackDao.countByFeedbackStatusAndUserUserId(FeedbackStatus.NEUTRAL,
						feedbackUserId);
				Map<String, Object> response = new HashMap<>();
				response.put("totalFeedback", userFeedback.size());
				response.put("positiveFeedback", positiveFeedbackCount);
				response.put("negativeFeedback", negativeFeedbackCount);
				response.put("nutralFeedback", nutralFeedbackCount);
				return new Response<>(200, "User Feedback Count Get Successfully", response);
			} else {
				return new Response<>(201, "User Feedback Count Not Found");
			}
		} catch (Exception e) {
			return new Response<>(201, "User Feedback Count Not Found");
		}
	}

	@Override
	public Response<Object> getParticularFeedbackDetail(Long userId, Long feedbackId) {
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

	@Override
	public Response<Object> updateUsersFeedback(Long userId, Long feedbackId, FeedbackDto feedbackDto) {
		try {
			Optional<Feedback> feedbackExist = feedbackDao.findById(feedbackId);
			if (feedbackExist.isPresent()) {
				Feedback feedback = feedbackExist.get();
				feedback.setFeedbackStatus(feedbackDto.getFeedbackStatus());
				feedback.setFeedbackMessage(feedbackDto.getFeedbackMessage());
				feedback.setSubmittedBy(userId);
				User user = new User();
				user.setUserId(feedbackDto.getUserId());
				feedback.setUser(user);
				feedback.setTradeId(feedbackDto.getTradeId());
				feedbackDao.save(feedback);
				return new Response<>(200,
						messageSource.getMessage("user.feedback.updated.successfully", new Object[0], Locale.US));
			} else {
				return new Response<>(201, messageSource.getMessage("user.feedback.detail.not.fetch.successfully",
						new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(201,
					messageSource.getMessage("user.feedback.detail.not.fetch.successfully", new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> addFeedback(Long userId, FeedbackStaticDto feedbackStaticDto) {
		FeedbackModel feedbackModel = new FeedbackModel();
		feedbackModel.setUserId(userId);
		feedbackModel.setEmail(feedbackStaticDto.getEmail());
		feedbackModel.setPhoneNo(feedbackStaticDto.getPhoneNo());
		feedbackModel.setMessage(feedbackStaticDto.getMessage());
		feedbackModel.setFeedbackEnum(FeedbackEnum.ACTIVE);
		feedbackstaticdao.save(feedbackModel);
		Map<String, Object> sendMailData = setEmailDataForFeedbackSubmitSuccess(feedbackStaticDto.getEmail());
		mailSender.sendMailToFeedbackSubmissionSuccess(sendMailData, "en");
		return new Response<>(200, "Feedback Submitted Successfully...");
	}

	@Override
	public Response<Object> getList(Long userId) {
		List<FeedbackModel> getData = feedbackstaticdao.findByUserId(userId);
		if (!getData.isEmpty()) {
			return new Response<>(200, "Feedback List Fetched Successsfully...", getData);
		} else {
			return new Response<>(205, "No feedback List Found");
		}
	}

	@Override
	public Response<List<FeedbackModel>> getNewList(Integer page, Integer pageSize) {
		List<FeedbackModel> data = feedbackstaticdao.findByFeedbackEnum(PageRequest.of(page, pageSize),
				FeedbackEnum.ACTIVE);
		if (!data.isEmpty()) {
			return new Response<>(200, "List Fetched Successfully", data);
		} else {
			return new Response<>(205, "No Data Found");
		}
	}

	private Map<String, Object> setEmailDataForFeedbackSubmitSuccess(String email) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, email);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.FEEDBACK_SUBMITTED_SUCCESSFULLY);
		return sendMailData;
	}

	@Override
	public Response<Object> getPerticularFeedbackDetailUser(Long userId, Long feedbackNewId) {
		try {
			Optional<FeedbackModel> userFeedbackDetail = feedbackstaticdao.findById(feedbackNewId);
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