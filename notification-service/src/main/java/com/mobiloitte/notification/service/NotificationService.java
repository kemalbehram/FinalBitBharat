package com.mobiloitte.notification.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import com.mobiloitte.notification.dto.EmailDto;
import com.mobiloitte.notification.model.ChatData;
import com.mobiloitte.notification.model.ChatDetails;
import com.mobiloitte.notification.model.NotificationData;
import com.mobiloitte.notification.model.Response;

public interface NotificationService {
	Boolean sendNotification(EmailDto emailDto) throws IOException;

	Response<Object> getNotificationByKey(String key) throws IOException, URISyntaxException;

	Response<Object> notificationReadByUser(Long userId);

	Response<List<NotificationData>> getNotificationData(String role, Long userId);

	Response<List<ChatDetails>> getChatData(Long userId);

	Response<List<ChatData>> getSupportChatData(String username,String toUsername);

	Boolean sendNotificationForExchange(Map<String, Object> emailMap);

	Response<Map<String, Object>> getChatHistory(Integer page, Integer pageSize);

	Response<Map<String, Object>> getChatHistoryByTradeId(String tradeId, Integer page, Integer pageSize);

	Response<Object> readNotificationExchnageData(Long userId);

	Response<List<ChatDetails>> getChatDataFordmin(String tradeId);

	Response<List<ChatDetails>> getChatThroughFordmin(Long adminId, String tradeId, Long userId);

	Response<List<Long>> sendChatDataToUser(Long userId) throws IOException;

	Response<Object> deleteWholeNotification(Long fkuserId);



}
