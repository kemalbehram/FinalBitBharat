package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.SupportDto;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.SupportService;

@RestController
public class SupportController {

	@Autowired
	private SupportService supportService;

	@PostMapping("/submit-support-ticket")
	public Response<Object> submitSupportTicket(@RequestBody SupportDto supportDto) {
		return supportService.submitSupportRequest(supportDto);
	}

}
