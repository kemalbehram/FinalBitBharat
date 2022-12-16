package com.mobiloitte.notification.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.notification.dto.EmailDto;
import com.mobiloitte.notification.model.ChatData;
import com.mobiloitte.notification.model.ChatDetails;
import com.mobiloitte.notification.model.NotificationData;
import com.mobiloitte.notification.model.Response;
import com.mobiloitte.notification.service.NotificationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class NotificationController {

	@Autowired
	private NotificationService notificationService;

	@ApiOperation(value = "API to Send Notification Messsage")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/send-notification")
	public Boolean sendNotification(@RequestBody EmailDto emailDto) throws IOException {
		return notificationService.sendNotification(emailDto);
	}

	@ApiOperation(value = "API to Send Notification For P2P Exchange Messsage")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/send-notification-for-p2p-exchange")
	public Boolean sendNotificationForExchange(@RequestBody Map<String, Object> emailMap) throws IOException {
		return notificationService.sendNotificationForExchange(emailMap);
	}

	@ApiOperation(value = "API to Read Notification")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@GetMapping("/read-notification")
	public Response<Object> readNotifications(@RequestHeader("userId") Long userId) {
		return notificationService.notificationReadByUser(userId);
	}

	@ApiOperation(value = "API to Get Notification Data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@GetMapping("/get-notification-data")
	public Response<List<NotificationData>> getNotificationData(@RequestParam String role,
			@RequestHeader Long userId) {
		String roles = role.replace("ROLE_", "");
		return notificationService.getNotificationData(role, userId);
	}

	@ApiOperation(value = "API to Get Chat Data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@GetMapping("/get-chat-data")
	public Response<List<ChatDetails>> getChatData(@RequestHeader Long userId) {
		return notificationService.getChatData(userId);
	}
	@ApiOperation(value = "API to Get Chat Data for admin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@GetMapping("/get-chat-history-through-admin")
	public Response<List<ChatDetails>> getChatThroughFordmin(@RequestParam Long adminId, @RequestParam String tradeId,
			@RequestParam Long userId) {
		return notificationService.getChatThroughFordmin(adminId, tradeId, userId);
	}

	
	@ApiOperation(value = "API to Get Chat Data for admin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@GetMapping("/get-chat-data-for-admin")
	public Response<List<ChatDetails>> getChatDataFordmin(@RequestParam String tradeId) {
		return notificationService.getChatDataFordmin(tradeId);
	}

	@ApiOperation(value = "API to Get Chat Data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@GetMapping("/get-support-chat-data")
	public Response<List<ChatData>> getSupportChatData(@RequestHeader String username, @RequestParam(required = false) String toUsername) {
		return notificationService.getSupportChatData(username,toUsername);
	}

	@ApiOperation(value = "API for get Exchange Chat History For Admin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "API for Get Chat History successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-chat-history-for-admin")
	public Response<Map<String, Object>> getChatHistory(@RequestParam(required = true, value = "page") Integer page,
			@RequestParam(required = true, value = "pageSize") Integer pageSize) {
		return notificationService.getChatHistory(page, pageSize);

	}

	@ApiOperation(value = "API for get Exchange Chat History By TradeId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "API for Get  Exchange Chat History successfully"),
			@ApiResponse(code = 205, message = "Add Not Created/ Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping("/get-chat-history-for-admin-by-tradeid")
	public Response<Map<String, Object>> getChatHistoryByTradeId(@RequestParam(required = true) String tradeId,
			@RequestParam(required = true, value = "page") Integer page,
			@RequestParam(required = true, value = "pageSize") Integer pageSize) {

		return notificationService.getChatHistoryByTradeId(tradeId, page, pageSize);

	}

	@PostMapping("/read-notification-exchange")
	public Response<Object> readNotificationExchnageData(@RequestHeader Long userId) throws IOException {
		return notificationService.readNotificationExchnageData(userId);

	}
	
	@ApiOperation(value = "API to send Chat Data to User")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@GetMapping("/send-chat-data-to-user")
	public Response<List<Long>> sendChatDataToUser(@RequestHeader Long userId) throws IOException {
		return notificationService.sendChatDataToUser(userId);
	}
	
	@DeleteMapping("/delete-notification")
	public Response<Object> deleteWholeNotification(@RequestHeader Long userId){
		return notificationService.deleteWholeNotification(userId);
	}
	
}
