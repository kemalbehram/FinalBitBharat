package com.mobiloitte.content.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.dao.WebsiteContentDao;
import com.mobiloitte.content.dto.WebsiteContentDto;
import com.mobiloitte.content.entities.WebsiteContent;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.AdminWebsiteContentService;

@Service
public class AdminWebsiteContentServiceImpl implements AdminWebsiteContentService {
	@Autowired
	private WebsiteContentDao websiteContentDao;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Response<Object> getWebsiteContentAllData(Long userId) {
		try {
		List<WebsiteContent> websiteContent = websiteContentDao.findAll();
		if (!websiteContent.isEmpty()) {
			return new Response<>(200, "Website Content Data Fetch Successfully", websiteContent);
		} else {
			return new Response<>(201, "Website Content Data Not Found");
		}
		}
		catch (Exception e) {
			return new Response<>(500,"something went wrong");
					
		}
	}

	@Override
	public Response<Object> getWebsiteContentDataByPageName(Long userId, String pageName) {
		try {
		Optional<WebsiteContent> content = websiteContentDao.findByPageName(pageName);
		if (content.isPresent()) {
			return new Response<>(200, "Website Content Page Data Fetch Successfully", content);
		} else {
			return new Response<>(201, "Website Content Page Data Not Found");
		}
		}
		catch (Exception e) {
			return new Response<>(500,"something went wrong");
					
		}
	}

	@Override
	public Response<Object> updateWebsiteContentData(Long userId, WebsiteContentDto websiteContentDto) {
		try {
		Optional<WebsiteContent> content = websiteContentDao.findByPageName(websiteContentDto.getPageName());
		if (content.isPresent()) {
			WebsiteContent webContent = content.get();
			webContent.setPageName(websiteContentDto.getPageName());
			webContent.setDescription(websiteContentDto.getDescription());
			websiteContentDao.save(webContent);
			return new Response<>(200, "Website Content Page Data Updated Successfully");
		} else {
			return new Response<>(201, "Website Content Page Data Not Found");
		}
	}
	
	catch (Exception e) {
		return new Response<>(500,"something went wrong");
				
	}

}
	@Override
	public Response<Object> addWebsiteContentData(Long userId, WebsiteContentDto websiteContentDto) {
		Optional<WebsiteContent> content = websiteContentDao.findByPageName(websiteContentDto.getPageName());
		if (!content.isPresent()) {
			WebsiteContent webContent = new WebsiteContent();
			webContent.setPageName(websiteContentDto.getPageName());
			webContent.setDescription(websiteContentDto.getDescription());
			webContent.setImageUrl(websiteContentDto.getImageUrl());
			websiteContentDao.save(webContent);
			return new Response<>(200, "Website Content Page Data Added Successfully");
		} else {
			return new Response<>(201, "Website Content Page Data Already Present");
		}
	}
	}
