package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.SubmitContactUsDto;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.StaticContentWebService;

@RestController
public class StaticContentWebController {

	@Autowired
	private StaticContentWebService staticContentWebService;

	@GetMapping("/get-why-choose-us-data")
	public Response<Object> getWhyChooseUsData() {
		return staticContentWebService.getWhyChooseUsData();
	}

	@GetMapping("/get-started-data")
	public Response<Object> getStartedData() {
		return staticContentWebService.getStartedData();
	}

	@GetMapping("/get-static-page-data")
	public Response<Object> getStaticPageData(@RequestParam String pageKey) {
		return staticContentWebService.getStaticPageData(pageKey);
	}

	@GetMapping("/get-faq-list")
	public Response<Object> getFaqList(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		return staticContentWebService.getFaqList(page, pageSize);
	}

	@GetMapping("/get-contact-us-details")
	public Response<Object> getContactUsDetails(@RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate) {
		return staticContentWebService.getContactUsDetails(page, pageSize,fromDate,toDate);
	}

	@PostMapping("/submit-contact-us-request")
	public Response<Object> submitContactUsRequest(@RequestBody SubmitContactUsDto submitContactUsDto) {
		return staticContentWebService.submitContactUsRequest(submitContactUsDto);
	}

	@GetMapping("/get-contact-us-list")
	public Response<Object> getContactUsList(@RequestParam Long contactUsId) {
		return staticContentWebService.getContactUsList(contactUsId);
	}
}
