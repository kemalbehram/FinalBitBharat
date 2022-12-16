package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.SearchAndFilterTicketDto;
import com.mobiloitte.content.dto.SupportReplyDto;
import com.mobiloitte.content.enums.TicketStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.AdminSuppotService;

@RestController
public class AdminSupportController {

	@Autowired
	private AdminSuppotService adminSuppotService;

	@GetMapping("/view-ticket-detail")
	public Response<Object> viewDicketDetail(@RequestHeader Long userId, @RequestParam Long ticketId) {
		return adminSuppotService.viewSupportTicket(userId, ticketId);
	}

	@PostMapping("/search-and-filter-ticket-list")
	public Response<Object> searchAndFilterTicketList(@RequestHeader Long userId,
			@RequestBody SearchAndFilterTicketDto searchAndFilterTicketDto) {
		return adminSuppotService.searchAndFilterTicketList(userId, searchAndFilterTicketDto);
	}

	@PostMapping("/change-ticket-status")
	public Response<Object> changeTicketStatus(@RequestHeader Long userId, @RequestParam Long ticketId,
			@RequestParam TicketStatus ticketStatus) {
		return adminSuppotService.changeTicketStatus(userId, ticketId, ticketStatus);
	}
	@PostMapping("/support-ticket-reply")
	public Response<Object> replySupportTicket(@RequestBody SupportReplyDto supportReplyDto) {
		return adminSuppotService.replySupportRequest(supportReplyDto);
	}

	@GetMapping("/get-ticket-reply-data")
	public Response<Object> getReplySupportTicketData(@RequestParam long ticketId) {
		return adminSuppotService.getReplySupportTicketData(ticketId);
	}

}
