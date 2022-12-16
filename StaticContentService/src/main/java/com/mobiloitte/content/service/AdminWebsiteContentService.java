package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.WebsiteContentDto;
import com.mobiloitte.content.model.Response;

public interface AdminWebsiteContentService {

	Response<Object> getWebsiteContentAllData(Long userId);

	Response<Object> getWebsiteContentDataByPageName(Long userId, String pageName);

	Response<Object> updateWebsiteContentData(Long userId, WebsiteContentDto websiteContentDto);
	
	Response<Object> addWebsiteContentData(Long userId, WebsiteContentDto websiteContentDto);




}
