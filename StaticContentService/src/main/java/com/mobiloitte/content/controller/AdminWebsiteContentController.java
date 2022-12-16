package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.WebsiteContentDto;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.AdminWebsiteContentService;

@RestController
public class AdminWebsiteContentController {

	@Autowired
	private AdminWebsiteContentService adminWebsiteContentService;

	@GetMapping("/get-website-content-all-data")
	public Response<Object> getWebsiteContentData(@RequestHeader Long userId) {
		return adminWebsiteContentService.getWebsiteContentAllData(userId);
	}

	@GetMapping("/get-website-content-page-data")
	public Response<Object> getWebsiteContentData(@RequestHeader Long userId, @RequestParam String pageName) {
		return adminWebsiteContentService.getWebsiteContentDataByPageName(userId, pageName);
	}

	@PostMapping("/update-website-content-page-data")
	public Response<Object> updateWebsitePageData(@RequestHeader Long userId,
			@RequestBody WebsiteContentDto websiteContentDto) {
		return adminWebsiteContentService.updateWebsiteContentData(userId, websiteContentDto);
	}
	@PostMapping("/add-app-content-page-data")
	public Response<Object> addWebsitePageData(@RequestHeader Long userId,
			@RequestBody WebsiteContentDto websiteContentDto) {
		return adminWebsiteContentService.addWebsiteContentData(userId, websiteContentDto);
	}

}
