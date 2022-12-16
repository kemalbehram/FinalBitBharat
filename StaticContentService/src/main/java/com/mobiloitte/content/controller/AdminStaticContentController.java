package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.UpdateStaticContentDto;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.AdminStaticContentService;

@RestController
public class AdminStaticContentController {

	@Autowired
	private AdminStaticContentService adminStaticContentService;

	@GetMapping("/get-all-static-content-data")
	public Response<Object> getAllStaticContentData(@RequestHeader Long userId) {
		return adminStaticContentService.getAllStaticData(userId);
	}

	@GetMapping("/get-static-page-data-by-page-key")
	public Response<Object> getStaticContentPageData(@RequestHeader Long userId, @RequestParam String pageKey) {
		return adminStaticContentService.getStaticDataByPageKey(userId, pageKey);
	}

	@PostMapping("/update-static-content-data")
	public Response<Object> updateStaticContentData(@RequestHeader Long userId,
			@RequestBody UpdateStaticContentDto updateStaticContentDTO) {
		return adminStaticContentService.updateStaticContentData(userId, updateStaticContentDTO);
	}

}
