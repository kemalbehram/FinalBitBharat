package com.mobiloitte.content.service;

import java.util.List;

import com.mobiloitte.content.dto.BannerDto;
import com.mobiloitte.content.dto.BannerUpdateDto;
import com.mobiloitte.content.dto.DeleteContentRequestDto;
import com.mobiloitte.content.dto.StaticContentDTO;
import com.mobiloitte.content.dto.UpdateContentRequestDto;
import com.mobiloitte.content.dto.WebContentDto;
import com.mobiloitte.content.dto.WebContentUpdateDto;
import com.mobiloitte.content.entities.Banner;
import com.mobiloitte.content.entities.StaticContent;
import com.mobiloitte.content.entities.WebsiteContent;
import com.mobiloitte.content.model.Response;

/**
 * The Interface StaticContentService.
 * @author Ankush Mohapatra
 */
public interface StaticContentService {

	/**
	 * Adds the static content.
	 *
	 * @param staticContentDTO the static content DTO
	 * @return the response
	 */
	Response<String> addStaticContent(StaticContentDTO staticContentDTO);
	
	/**
	 * Update static content.
	 *
	 * @param updateContentRequestDto the update content request dto
	 * @return the response
	 */
	Response<String> updateStaticContent(UpdateContentRequestDto updateContentRequestDto);
	
	/**
	 * Delete static content.
	 *
	 * @param deleteContentRequestDto the delete content request dto
	 * @return the response
	 */
	Response<String> deleteStaticContent(DeleteContentRequestDto deleteContentRequestDto);
	
	/**
	 * Gets the all static content.
	 *
	 * @return the all static content
	 */
	Response<List<StaticContent>> getAllStaticContent();
	
	/**
	 * Gets the static content by page key.
	 *
	 * @param pageKey the page key
	 * @return the static content by page key
	 */
	Response<StaticContent> getStaticContentByPageKey(String pageKey);
	
	/**
	 * Adds the banner details.
	 *
	 * @param bannerDto the banner dto
	 * @return the response
	 */
	Response<String> addBannerDetails(BannerDto bannerDto);
	
	/**
	 * Gets the banner list.
	 *
	 * @return the banner list
	 */
	Response<List<Banner>> getBannerList();
	
	/**
	 * Update banner.
	 *
	 * @param bannerDto the banner dto
	 * @return the response
	 */
	Response<String> updateBanner(BannerUpdateDto bannerDto);
	
	/**
	 * Adds the web content details.
	 *
	 * @param webContentDto the web content dto
	 * @return the response
	 */
	Response<String> addWebContentDetails(WebContentDto webContentDto);

	/**
	 * Gets the web content list.
	 *
	 * @return the web content list
	 */
	Response<List<WebsiteContent>> getWebContentList();

	/**
	 * Update web content.
	 *
	 * @param webContentDto the web content dto
	 * @return the response
	 */
	Response<String> updateWebContent(WebContentUpdateDto webContentDto);
	
}
