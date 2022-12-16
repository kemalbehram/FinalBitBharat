package com.mobiloitte.notification.serviceimpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiloitte.notification.constants.MessageConstant;
import com.mobiloitte.notification.dao.ChatDao;
import com.mobiloitte.notification.dao.ChatDetailsDao;
import com.mobiloitte.notification.dao.NotificationDataDao;
import com.mobiloitte.notification.dao.NotificationTemplateContentDao;
import com.mobiloitte.notification.dao.P2pExchangeNotificationDao;
import com.mobiloitte.notification.dto.EmailDto;
import com.mobiloitte.notification.dto.ExchangeBellNotiticationDto;
import com.mobiloitte.notification.dto.RoleListDto;
import com.mobiloitte.notification.dto.UserEmailAndNameDto;
import com.mobiloitte.notification.enums.NotificationSubType;
import com.mobiloitte.notification.enums.NotificationType;
import com.mobiloitte.notification.exception.MailNotSendException;
import com.mobiloitte.notification.exception.RunTimeException;
import com.mobiloitte.notification.feign.UserClient;
import com.mobiloitte.notification.handler.CustomWebSocketChatHandler;
import com.mobiloitte.notification.handler.CustomWebSocketHandler;
import com.mobiloitte.notification.model.ChatData;
import com.mobiloitte.notification.model.ChatDetails;
import com.mobiloitte.notification.model.NotificationData;
import com.mobiloitte.notification.model.NotificationTemplateContent;
import com.mobiloitte.notification.model.P2pExchangeNotification;
import com.mobiloitte.notification.model.Response;
import com.mobiloitte.notification.service.NotificationService;
import com.mobiloitte.notification.util.MailSender;

@Service
public class NotificationServiceImpl extends MessageConstant implements NotificationService {
	private static final Logger LOGGER = LogManager.getLogger(NotificationServiceImpl.class);

	@Autowired
	private MailSender mailSender;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private UserClient userClient;

	@Autowired
	private NotificationTemplateContentDao notificationTemplateContentDao;

	@Autowired
	private NotificationDataDao notificationDataDao;

	@Autowired
	private ChatDao chatDao;

	@Autowired
	private P2pExchangeNotificationDao p2pExchangeNotificationDao;

	@Autowired
	private ChatDetailsDao chatDetailsDao;

	@Autowired
	private CustomWebSocketHandler webSocketHandler;
	@Autowired
	private CustomWebSocketChatHandler webSocketChatHandler;

	@Override
	@SuppressWarnings("unchecked")
	public Boolean sendNotification(EmailDto emailDto) throws IOException {
		try {
			Response<Object> emailByKey = getNotificationByKey(emailDto.getKey());
			List<NotificationTemplateContent> notificationData = (List<NotificationTemplateContent>) emailByKey
					.getData();
			if (!notificationData.isEmpty()) {
				List<String> email = Arrays.asList(emailDto.getEmail());
				notificationData.parallelStream().forEachOrdered(notificationTemplateContent -> {
					if (notificationTemplateContent.getNotificationType().equals(NotificationType.EMAIL)) {
						try {
							checkSendEmail(emailDto, email, notificationTemplateContent);
						} catch (IOException e) {
							LOGGER.info(e);
						}
					}
					if (notificationTemplateContent.getNotificationType().equals(NotificationType.NOTIFICATION)) {

						checkSendBellNotificationToUser(emailDto, notificationTemplateContent);
					}
				});
			}
		} catch (Exception e) {
			throw new RunTimeException(SOMTHING_WENT_WRONG);
		}
		return true;
	}

	private Boolean checkSendBellNotificationToUser(EmailDto emailDto,
			NotificationTemplateContent notificationTemplateContent) {

		try {
			List<RoleListDto> roleListDto = new ArrayList<>();
			RoleListDto roleList = new RoleListDto();
			if (notificationTemplateContent.getNotificationType().equals(NotificationType.NOTIFICATION)) {
				if (notificationTemplateContent.getNotificationSubType().equals(NotificationSubType.BELL)
						&& notificationTemplateContent.getRoleType().equals(CUSTOMER)) {
					roleList.setUserId(Arrays.asList(emailDto.getUserId()));
					roleList.setRoleID(Long.valueOf(notificationTemplateContent.getRoleId()));
					roleListDto.add(roleList);
					sendBellNotification(notificationTemplateContent, roleListDto, emailDto);

				} else {
					List<Long> rolesId = Arrays.asList(notificationTemplateContent.getRoleId().split(","))
							.parallelStream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
					Response<List<RoleListDto>> response = userClient.getRolesDataForNotification(rolesId);
					if (!response.getData().isEmpty()) {

						sendBellNotification(notificationTemplateContent, response.getData(), emailDto);
					}
				}
			}
			return Boolean.TRUE;

		} catch (Exception e) {
			throw new RunTimeException(SOMTHING_WENT_WRONG);
		}

	}

	public Boolean sendBellNotification(NotificationTemplateContent messageContent, List<RoleListDto> roleList,
			EmailDto emailDto) {
		String replaceMessage = "";
		try {

			replaceMessage = setReplacementToken(messageContent.getMessage(),
					emailDto.getReplacementToken().entrySet());

			List<NotificationData> listNotificationData = notificationDataDao.findAll();
			if (messageContent.getRoleType().equals(CUSTOMER)) {

				sendNotificatoinToCustomer(roleList, emailDto, replaceMessage, listNotificationData,
						emailDto.getReplacementToken());
			} else {

				sendNotificatoinToOthers(roleList, emailDto, replaceMessage, emailDto.getReplacementToken());
			}
			return Boolean.TRUE;

		} catch (Exception e) {
			throw new RunTimeException(SOMTHING_WENT_WRONG);
		}

	}

	private Boolean sendNotificatoinToOthers(List<RoleListDto> roleList, EmailDto emailDto, String replaceMessage,
			Map<String, String> replacementTokens) {

		try {
			List<NotificationData> collectNotificationDatas = new LinkedList<>();
			List<Long> s = new ArrayList<>();
			if (!roleList.isEmpty()) {
				roleList.parallelStream().forEachOrdered(a -> a.getUserId().parallelStream().forEachOrdered(b -> {
					NotificationData notificationData = new NotificationData();
					notificationData.setFkUserId(b);
					notificationData.setRoleId(a.getRoleID());
					notificationData.setIsSeen(Boolean.FALSE);
					notificationData.setIsEnabled(emailDto.getIsEnabled());
					notificationData.setMessage(replaceMessage);
					notificationData.setData(replacementTokens.get("data"));
					notificationData.setCreatedAt(new Date());
					notificationData.setNotificationType(emailDto.getNotificationType());
					notificationData.setReturnUrl(replacementTokens.get("returnUrl"));

					Boolean check = notificationDataDao.existsByFkUserIdAndMessageAndCreatedAt(
							notificationData.getFkUserId(), notificationData.getMessage(),
							notificationData.getCreatedAt());

					if (check.equals(Boolean.FALSE)) {
						notificationDataDao.save(notificationData);
						List<NotificationData> notificationCustomerData = notificationDataDao.findByFkUserId(b);
						webSocketHandler.sendDataToUser(notificationCustomerData);

					}
					collectNotificationDatas.add(notificationData);
				}));
				collectNotificationDatas.parallelStream().forEachOrdered(b -> {
					Boolean check = notificationDataDao.existsByFkUserIdAndMessage(b.getFkUserId(), b.getMessage());
					if (check.equals(Boolean.FALSE)) {
						notificationDataDao.save(b);
						List<NotificationData> notificationCustomerData = notificationDataDao
								.findByFkUserId(s.get(s.size() - 1));
						webSocketHandler.sendDataToUser(notificationCustomerData);

					}
				});
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}

		} catch (Exception e) {
			LOGGER.info(e);
		}
		return Boolean.FALSE;

	}

	private Boolean sendNotificatoinToCustomer(List<RoleListDto> roleList, EmailDto emailDto, String replaceMessage,
			List<NotificationData> listNotificationData, Map<String, String> replacementTokens) {

		try {

			List<Long> s = new ArrayList<>();
			List<NotificationData> collectNotificationData = new LinkedList<>();
			if (!roleList.isEmpty()) {
				roleList.parallelStream().forEachOrdered(a -> a.getUserId().parallelStream().forEachOrdered(b -> {
					NotificationData notificationDatas = new NotificationData();
					s.add(b);

					notificationDatas.setFkUserId(b);
					notificationDatas.setRoleId(a.getRoleID());
					notificationDatas.setIsSeen(Boolean.FALSE);
					notificationDatas.setNotiType(emailDto.getNotiType());
					notificationDatas.setMessage(replaceMessage);
					notificationDatas.setIsEnabled(emailDto.getIsEnabled());
					notificationDatas.setNotificationType(emailDto.getNotificationType());
					notificationDatas.setData(replacementTokens.get("data"));
					notificationDatas.setCreatedAt(new Date());
					notificationDatas.setReturnUrl(replacementTokens.get("returnUrl"));
					collectNotificationData.add(notificationDatas);

				}));
				if (!listNotificationData.isEmpty()) {
					collectNotificationData.parallelStream().forEachOrdered(b -> {
						Boolean check12 = notificationDataDao.existsByFkUserIdAndMessageAndCreatedAt(b.getFkUserId(),
								b.getMessage(), b.getCreatedAt());
						if (check12.equals(Boolean.FALSE)) {
							notificationDataDao.saveAll(collectNotificationData);
							List<NotificationData> notificationCustomerData = notificationDataDao
									.findByFkUserId(s.get(s.size() - 1));
							webSocketHandler.sendDataToUser(notificationCustomerData);
						}
					});
				} else {
					collectNotificationData.parallelStream().forEachOrdered(b -> {
						Boolean check = notificationDataDao.existsByFkUserIdAndMessage(b.getFkUserId(), b.getMessage());
						if (check.equals(Boolean.FALSE)) {
							notificationDataDao.saveAll(collectNotificationData);
							List<NotificationData> notificationDatas = new ArrayList<>();
							List<NotificationData> notificationData = notificationDataDao
									.findByFkUserId(b.getFkUserId());
							notificationDatas.addAll(notificationData);
							webSocketHandler.sendDataToUser(notificationDatas);
						}
					});
				}
				return Boolean.TRUE;
			} else {
				return Boolean.FALSE;
			}

		} catch (Exception e) {
			LOGGER.info(e);
		}
		return Boolean.FALSE;

	}

	public String setReplacementToken(String messages, Set<Entry<String, String>> repacementToken) {
		for (Map.Entry<String, String> token : repacementToken) {
			messages = messages.replace(token.getKey(), token.getValue());
		}
		return messages;
	}

	private Boolean checkSendEmail(EmailDto emailDto, List<String> email,
			NotificationTemplateContent notificationTemplateContent) throws IOException {
		try {
			if (notificationTemplateContent.getNotificationType().equals(NotificationType.EMAIL)
					&& notificationTemplateContent.getRoleType().equals(CUSTOMER)) {

				Boolean isbln = sendEmailNotification(notificationTemplateContent, email, emailDto);

				if (isbln.equals(Boolean.TRUE))
					return Boolean.TRUE;
			} else {

				List<Long> rolesId = Arrays.asList(notificationTemplateContent.getRoleId().split(",")).parallelStream()
						.map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
				List<String> emailList = userClient.getNotificationPreference(rolesId);
				Boolean isbln = sendEmailNotification(notificationTemplateContent, emailList, emailDto);

				if (isbln.equals(Boolean.TRUE))
					return Boolean.TRUE;

			}
			return Boolean.FALSE;
		} catch (Exception e) {

			throw new RunTimeException(SOMTHING_WENT_WRONG);
		}

	}

	public Boolean sendEmailNotification(NotificationTemplateContent messageContent, List<String> email,
			EmailDto emailDto) throws IOException {
		for (String object : email) {
			emailDto.getReplacementToken().replace(EMAIL, object);
			if (messageContent.getMessage() != null || messageContent.getSubject() != null) {

				String getEmailTemplate = getEmailTemplate(messageContent, emailDto);
				String subjectMessage = messageContent.getSubject();
				String subjectMessageAfterReplaced = setReplacementToken(subjectMessage,
						emailDto.getReplacementToken().entrySet());

				if (messageContent.getSubject().contains(SUBJECTNAMEFIELD)) {

					return Boolean.TRUE == mailSender.sendEmail(getEmailTemplate, email, messageContent.getSubject()
							.replace(SUBJECTNAMEFIELD, emailDto.getReplacementToken().get(SUBJECTNAMEFIELD)));
				} else {

					return Boolean.TRUE == mailSender.sendEmail(getEmailTemplate, email, subjectMessageAfterReplaced);
				}
			} else {

			}

		}

		return Boolean.TRUE;
	}

	private String getEmailTemplate(NotificationTemplateContent messageContent, EmailDto emailDto) throws IOException {
		String messages = "";
		String replaceMessage = "";
		messages = mailSender.sendEmailTemplate(messageContent.getMessage(), messageContent.getSubject());
		replaceMessage = setReplacementToken(messages, emailDto.getReplacementToken().entrySet());
		return replaceMessage;
	}

	public Map<String, Object> checkNotificationEnableOption(EmailDto emailDto) {

		try {

			Boolean isEmail = Boolean.TRUE;
			Boolean isWeb = Boolean.TRUE;
			Map<String, Object> mod = new HashMap<>();

			if ((emailDto.getReplacementToken().containsKey(ISEMAIL))
					&& emailDto.getReplacementToken().get(ISWEB) != null
					&& emailDto.getReplacementToken().get(ISEMAIL) != null
					&& (emailDto.getReplacementToken().containsKey(ISWEB))) {
				isEmail = Boolean.valueOf(emailDto.getReplacementToken().get(ISEMAIL)).equals(Boolean.TRUE)
						? Boolean.TRUE
						: Boolean.FALSE;
				isWeb = Boolean.valueOf(emailDto.getReplacementToken().get(ISWEB)).equals(Boolean.TRUE) ? Boolean.TRUE
						: Boolean.FALSE;

				mod.put(ISEMAIL, isEmail);
				mod.put(ISWEB, isWeb);
			} else {
				mod.put(ISEMAIL, isEmail);
				mod.put(ISWEB, isWeb);

			}

			return mod;
		} catch (Exception e) {
			throw new RunTimeException(SOMTHING_WENT_WRONG);
		}

	}

	@Override
	public Response<Object> getNotificationByKey(String key) throws IOException {
		try {
			List<NotificationTemplateContent> notificationContent = notificationTemplateContentDao
					.findByActivityType(key);

			if (!notificationContent.isEmpty()) {
				return new Response<>(1609,
						messageSource.getMessage(NOTIFICATION_CONTENT_SUCCESS, new Object[0], Locale.US),
						notificationContent);
			}

			return new Response<>(1611, messageSource.getMessage(RESPONSE_FAILED, new Object[0], Locale.US));
		} catch (Exception e) {

			throw new RunTimeException(SOMTHING_WENT_WRONG);
		}

	}

	@Override
	public Response<Object> notificationReadByUser(Long userId) {
		List<NotificationData> notificationData = notificationDataDao.findByFkUserIdAndIsSeenFalse(userId);
		if (!notificationData.isEmpty()) {
			notificationData.parallelStream().forEachOrdered(a -> a.setIsSeen(Boolean.TRUE));
			notificationDataDao.saveAll(notificationData);
			return new Response<>(1616, messageSource.getMessage(NOTIFICATION_READ_SUCCESS, new Object[0], Locale.US));
		} else {
			return new Response<>(1617,
					messageSource.getMessage(LATEST_NOTIFICATION_NOT_PRESENT, new Object[0], Locale.US));
		}
	}

	@Override
	public Response<List<NotificationData>> getNotificationData(String role, Long userId) {
		Response<Long> response = userClient.getRoleIdByName(role);
		List<NotificationData> data = notificationDataDao.findByRoleIdAndFkUserIdOrderByCreatedAtDesc(response.getData(), userId);
		List<NotificationData> datas = new LinkedList<>();
		for (NotificationData obj : data) {
			obj.setNotificationId(obj.getNotificationId());
			obj.setFkUserId(obj.getFkUserId());
			obj.setIsSeen(obj.getIsSeen());
			obj.setMessage(obj.getMessage());
			obj.setNotificationType(obj.getNotificationType());
			obj.setRoleId(obj.getRoleId());
			obj.setCreatedAt1(obj.getCreatedAt().getTime());
			datas.add(obj);
		}
		if (!datas.isEmpty()) {
			return new Response<>(1618, messageSource.getMessage(RESPONSE_SUCCESS, new Object[0], Locale.US), datas);
		} else {
			return new Response<>(1619, messageSource.getMessage(RESPONSE_NO_DATA, new Object[0], Locale.US));
		}

	}

	@Override
	public Response<List<ChatDetails>> getChatData(Long userId) {
		List<ChatDetails> data = chatDao.findByFromUserId(userId);
		List<ChatDetails> datas = new LinkedList<>();
		for (ChatDetails obj : data) {
			obj.setMessageId(obj.getMessageId());
			obj.setCreationTime(obj.getCreationTime());
			obj.setFileUrl(obj.getFileUrl());
			obj.setFromUserId(obj.getFromUserId());
			obj.setMessage(obj.getMessage());
			obj.setRoleId(obj.getRoleId());
			datas.add(obj);
		}
		if (!datas.isEmpty()) {
			return new Response<>(1620, messageSource.getMessage(RESPONSE_SUCCESS, new Object[0], Locale.US), datas);
		} else {
			return new Response<>(1621, messageSource.getMessage(RESPONSE_NO_DATA, new Object[0], Locale.US));
		}
	}

	@Override
	public Response<List<ChatDetails>> getChatDataFordmin(String tradeId) {
		List<ChatDetails> data = chatDao.findByTradeId(tradeId);
		List<ChatDetails> datas = new LinkedList<>();
		for (ChatDetails obj : data) {
			Response<UserEmailAndNameDto> dataFetched = userClient.getEmailAndName(obj.getFromUserId());
			Response<UserEmailAndNameDto> dataFetched1 = userClient.getEmailAndName(obj.getToUserId());
			obj.setMessageId(obj.getMessageId());
			obj.setProfileImageOfBuyer(dataFetched.getData().getImageUrl());
			obj.setProfileImageOfSeller(dataFetched1.getData().getImageUrl());
			obj.setCreationTime(obj.getCreationTime());
			obj.setFileUrl(obj.getFileUrl());
			obj.setFromUserId(obj.getFromUserId());
			obj.setMessage(obj.getMessage());
			obj.setRoleId(obj.getRoleId());
			obj.setProfileImageOfBuyer(tradeId);
			datas.add(obj);
		}
		if (!datas.isEmpty()) {
			return new Response<>(1620, messageSource.getMessage(RESPONSE_SUCCESS, new Object[0], Locale.US), datas);
		} else {
			return new Response<>(1621, messageSource.getMessage(RESPONSE_NO_DATA, new Object[0], Locale.US));
		}
	}

	@Override
	public Response<List<ChatData>> getSupportChatData(String username, String toUsername) {
		List<String> usernames = new ArrayList<>();

		usernames.add(username);
		usernames.add(toUsername);

		if (toUsername != null) {
			List<ChatData> data = chatDetailsDao
					.findByFromEmailInAndToEmailInAndIsSeenTrueOrderByMessageIdAsc(usernames, usernames);
			List<ChatData> datas = new LinkedList<>();
			for (ChatData obj : data) {
				obj.setMessageId(obj.getMessageId());
				obj.setCreatedAt(obj.getCreatedAt());
				obj.setFileUrl(obj.getFileUrl());
				obj.setFkRoomId(obj.getFkRoomId());
				obj.setIsSeen(obj.getIsSeen());
				obj.setMessage(obj.getMessage());
				obj.setToEmail(obj.getToEmail());
				obj.setTopic(obj.getTopic());
				datas.add(obj);
			}
			if (!datas.isEmpty()) {
				return new Response<>(1620, messageSource.getMessage(RESPONSE_SUCCESS, new Object[0], Locale.US),
						datas);
			} else {
				return new Response<>(1621, messageSource.getMessage(RESPONSE_NO_DATA, new Object[0], Locale.US));
			}
		} else {
			List<ChatData> data = chatDetailsDao.findByFromEmailOrToEmailAndIsSeenTrue(username, username);
			List<ChatData> datas = new LinkedList<>();
			for (ChatData obj : data) {
				obj.setMessageId(obj.getMessageId());
				obj.setCreatedAt(obj.getCreatedAt());
				obj.setFileUrl(obj.getFileUrl());
				obj.setFkRoomId(obj.getFkRoomId());
				obj.setIsSeen(obj.getIsSeen());
				obj.setMessage(obj.getMessage());
				obj.setToEmail(obj.getToEmail());
				obj.setTopic(obj.getTopic());
				datas.add(obj);
			}
			if (!datas.isEmpty()) {
				return new Response<>(1620, messageSource.getMessage(RESPONSE_SUCCESS, new Object[0], Locale.US),
						datas);
			} else {
				return new Response<>(1621, messageSource.getMessage(RESPONSE_NO_DATA, new Object[0], Locale.US));
			}
		}

	}

	@Override
	public Boolean sendNotificationForExchange(Map<String, Object> emailMap) {
		List<ExchangeBellNotiticationDto> notification = new LinkedList<>();
		ExchangeBellNotiticationDto dto = new ObjectMapper().convertValue(emailMap, ExchangeBellNotiticationDto.class);
		notification.add(dto);
		try {
			System.err.println(emailMap.toString());
			sendExchangeBellNotification(notification);
			return Boolean.TRUE;
		} catch (Exception e) {
			throw new MailNotSendException(EMAIL_IS_NOT_SEND_DUE_TO_ERROR);
		}
	}

	private boolean sendExchangeBellNotification(List<ExchangeBellNotiticationDto> notification) {
		P2pExchangeNotification notfi = new P2pExchangeNotification();
		notfi.setNotificationId(notification.get(0).getNotificationId());
		notfi.setCreatedAt(notification.get(0).getTimeStamp());
		notfi.setFromRequestAcceptRejectId(notification.get(0).getFromRequestAcceptRejectId());
		notfi.setFromUserId(notification.get(0).getFromUserId());
		notfi.setIsSeen(notification.get(0).getIsSeen());
		notfi.setNoOfCoins(notification.get(0).getNoOfCoins());
		notfi.setNotificationStatus(notification.get(0).getNotificationStatus());
		notfi.setOrderType(notification.get(0).getOrderType());
		notfi.setPaymentWindow(notification.get(0).getPaymentWindow());
		notfi.setPeerToPeerExchangeId(notification.get(0).getPeerToPeerExchangeId());
		notfi.setRequestMessage(notification.get(0).getRequestMessage());
		notfi.setTimeStamp(notification.get(0).getTimeStamp());
		notfi.setToRequestAcceptRejectId(notification.get(0).getFromRequestAcceptRejectId());
		notfi.setToUserId(notification.get(0).getToUserId());
		notfi.setTradeId(notification.get(0).getTradeId());
		notfi.setTradePartner(notification.get(0).getTradePartner());
		notfi.setTradeStatus(notification.get(0).getTradeStatus());
		notfi.setTradingPrice(notification.get(0).getTradingPrice());
		p2pExchangeNotificationDao.save(notfi);
		webSocketChatHandler.sendDataToUserExchange(notification);
		return true;

	}

	@Override
	public Response<Map<String, Object>> getChatHistory(Integer page, Integer pageSize) {
		try {
			Page<ChatDetails> getTradeList;
			Long getTotalCount;
			if (page != null && pageSize != null) {
				getTradeList = chatDao.findAll(PageRequest.of(page, pageSize, Direction.DESC, MESSAGEID));
				getTotalCount = chatDao.count();
				if (getTradeList.hasContent()) {
					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put(RESULT_LIST, getTradeList.getContent());
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, P2P_EXCHANGE_CHAT_DATA_GET_SUCCESSFULLY, responseMap);
				} else {
					return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
				}
			} else {
				return new Response<>(205, ENTER_PAGE_AND_PAGESIZE);

			}
		} catch (Exception e) {
			throw new RunTimeException(SOMETHING_WENT_WRONG);

		}

	}

	@Override
	public Response<Map<String, Object>> getChatHistoryByTradeId(String tradeId, Integer page, Integer pageSize) {
		try {
			Page<ChatDetails> getTradeList;
			Long getTotalCount;
			if (page != null && pageSize != null && tradeId != null) {

				getTradeList = chatDao.findByTradeId(tradeId, PageRequest.of(page, pageSize, Direction.ASC, MESSAGEID));
				getTotalCount = chatDao.countByTradeId(tradeId);

				if (getTradeList.hasContent()) {
					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put(RESULT_LIST, getTradeList.getContent());
					responseMap.put(TOTAL_COUNT, getTotalCount);
					return new Response<>(SUCCESS_CODE, P2P_EXCHANGE_CHAT_DATA_GET_SUCCESSFULLY, responseMap);
				}

				else {
					return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);
				}
			} else {
				return new Response<>(205, ENTER_PAGE_AND_PAGESIZE);
			}
		} catch (Exception e) {
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}

	}

	@Override
	public Response<List<ChatDetails>> getChatThroughFordmin(Long adminId, String tradeId, Long userId) {
		List<ChatDetails> data = chatDao.findByToUserIdInAndFromUserIdInAndTradeId(Arrays.asList(userId, adminId),
				Arrays.asList(userId, adminId), tradeId);
		List<ChatDetails> datas = new LinkedList<>();
		for (ChatDetails obj : data) {
			obj.setMessageId(obj.getMessageId());
			obj.setCreationTime(obj.getCreationTime());
			obj.setFileUrl(obj.getFileUrl());
			obj.setFromUserId(obj.getFromUserId());
			obj.setMessage(obj.getMessage());
			obj.setSender(obj.getSender());
			obj.setReciever(obj.getReciever());
			obj.setProfileImageOfBuyer(obj.getProfileImageOfBuyer());
			obj.setProfileImageOfSeller(obj.getProfileImageOfSeller());

			obj.setRoleId(obj.getRoleId());
			datas.add(obj);
		}
		if (!datas.isEmpty()) {
			return new Response<>(1620, messageSource.getMessage(RESPONSE_SUCCESS, new Object[0], Locale.US), datas);
		} else {
			return new Response<>(1621, messageSource.getMessage(RESPONSE_NO_DATA, new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> readNotificationExchnageData(Long userId) {
		try {
			List<P2pExchangeNotification> getp2pExchangeNotificationList;

			if (userId != null) {
				getp2pExchangeNotificationList = p2pExchangeNotificationDao.findByToUserId(userId);
				getp2pExchangeNotificationList.parallelStream().forEachOrdered(p2pExchangeNotification -> {
					p2pExchangeNotification.setIsSeen(true);
					p2pExchangeNotificationDao.save(p2pExchangeNotification);
				});
			}
			return new Response<>(SUCCESS_CODE, NOTIFICATION_READ_SUCCESSFULLY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new Response<>(DATA_NOT_FOUND_CODE, DATA_NOT_FOUND);

	}

	@Override
	public Response<List<Long>> sendChatDataToUser(Long userId) throws IOException {

		List<Long> mm = new ArrayList<>();

		List<ChatDetails> getActiveUserByUserId = chatDao.findByToUserId(userId);

		if (!getActiveUserByUserId.isEmpty()) {

			for (ChatDetails user : getActiveUserByUserId) {

				mm.add(user.getToUserId());
			}

			for (Long id : mm) {

				Map<String, String> setData = new HashMap<>();
				setData.put("EMAIL_TOKEN", "email");
				setData.put("URL_TOKEN", "url");
				String email = "email";
				EmailDto emailDto = new EmailDto(id, "announcement", email, setData);

				sendNotification(emailDto);
			}

			return new Response<>(200, "Success", mm);

		}

		else {
			return new Response<>(200, "user is not active");
		}
	}

	@Override
	@Transactional
	public Response<Object> deleteWholeNotification(Long fkuserId) {
		List<NotificationData> isDataExists = notificationDataDao.findByFkUserId(fkuserId);
		if (!isDataExists.isEmpty()) {
			NotificationData data = new NotificationData();
			for (NotificationData noti : isDataExists) {
				data = noti;
			}
			notificationDataDao.deleteByFkUserId(fkuserId);
			return new Response<>(200, "Data Deleted Successfully");
		}
		return new Response<>(205, "Data DoesNot Exists");

	}

}
