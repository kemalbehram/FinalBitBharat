package com.mobiloitte.content.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
import com.mobiloitte.content.model.Constants;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.StaticContentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class StaticContentController.
 *
 * @author Ankush Mohapatra
 */
@RestController
//@Api(value = "Static Content API")
public class StaticContentController extends Constants {

	/** The static content service. */
	@Autowired
	private StaticContentService staticContentService;

	/**
	 * Request add static content.
	 *
	 * @param staticContentDTO the static content DTO
	 * @return the response
	 */
	@ApiOperation(value = "API to add new Static Content")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Static content saved successfully"),
			@ApiResponse(code = 205, message = "Static content already exist / Updation failed / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/admin/static-content/add-new-static-content")
	public Response<String> requestAddStaticContent(@Valid @RequestBody StaticContentDTO staticContentDTO) {
		if (staticContentDTO != null) {
			return staticContentService.addStaticContent(staticContentDTO);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Request update static content.
	 *
	 * @param updateContentRequestDto the update content request dto
	 * @return the response
	 */
	@ApiOperation(value = "API to update a Static Content")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Static content updated successfully"),
			@ApiResponse(code = 205, message = "No such static content found / Updation failed / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/admin/static-content/update-static-content")
	public Response<String> requestUpdateStaticContent(
			@Valid @RequestBody UpdateContentRequestDto updateContentRequestDto) {
		if (updateContentRequestDto != null) {
			return staticContentService.updateStaticContent(updateContentRequestDto);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Request delete static content.
	 *
	 * @param deleteContentRequestDto the delete content request dto
	 * @return the response
	 */
	@ApiOperation(value = "API to delete a Static Content")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Static content deleted successfully"),
			@ApiResponse(code = 205, message = "No such static content found / Deletion failed / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/admin/static-content/delete-static-content")
	public Response<String> requestDeleteStaticContent(
			@Valid @RequestBody DeleteContentRequestDto deleteContentRequestDto) {
		if (deleteContentRequestDto != null) {
			return staticContentService.deleteStaticContent(deleteContentRequestDto);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	/**
	 * Requestget all static contents.
	 *
	 * @return the response
	 */
	@ApiOperation(value = "API to get all Static Contents")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "All static content fetched / No static content found"),
			@ApiResponse(code = 205, message = "Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/admin/static-content/get-all-static-content")
	public Response<List<StaticContent>> requestgetAllStaticContents() {
		return staticContentService.getAllStaticContent();
	}

	/**
	 * Requestget static content by key.
	 *
	 * @param pageKey the page key
	 * @return the response
	 */
	@ApiOperation(value = "API to get Static Contents by Page Key")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Static content found"),
			@ApiResponse(code = 205, message = "No content found / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@GetMapping(value = "/get-static-content")
	public Response<StaticContent> requestgetStaticContentByKey(
			@RequestParam(required = true, value = "pageKey") String pageKey) {
		return staticContentService.getStaticContentByPageKey(pageKey);
	}

	/**
	 * Save banner details.
	 *
	 * @param bannerDto the banner dto
	 * @return the response
	 */
	@ApiOperation(value = "API to add banner")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Banner data exists / Updation Failed / Exception Occured Internally"), })
	@PostMapping("admin/static-content/save-banner")
	public Response<String> saveBannerDetails(@RequestBody BannerDto bannerDto) {
		return staticContentService.addBannerDetails(bannerDto);
	}

	/**
	 * Gets the banner list.
	 *
	 * @return the banner list
	 */
	@ApiOperation(value = "API to get banner list ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "No Data Found / Exception Occured Internally"), })
	@GetMapping("admin/static-content/get-banner-list")
	public Response<List<Banner>> getBannerList() {
		return staticContentService.getBannerList();
	}

	/**
	 * Update banner.
	 *
	 * @param bannerDto the banner dto
	 * @return the response
	 */
	@ApiOperation(value = "API to update banner")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "No Data Found / Exception Occured Internally"), })
	@PostMapping("admin/static-content/update-banner")
	public Response<String> updateBanner(@RequestBody BannerUpdateDto bannerDto) {
		return staticContentService.updateBanner(bannerDto);
	}

	/**
	 * Save web content details.
	 *
	 * @param webContentDto the web content dto
	 * @return the response
	 */
	@ApiOperation(value = "API to add website content")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Website Content data exists / Updation Failed / Exception Occured Internally"), })
	@PostMapping("admin/static-content/save-website-content")
	public Response<String> saveWebContentDetails(@RequestBody WebContentDto webContentDto) {
		return staticContentService.addWebContentDetails(webContentDto);
	}

	/**
	 * Gets the web content.
	 *
	 * @return the web content
	 */
	@ApiOperation(value = "API to get website content list ")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "No Data Found / Exception Occured Internally"), })
	@GetMapping("admin/static-content/get-webcontent-list")
	public Response<List<WebsiteContent>> getWebContent() {
		return staticContentService.getWebContentList();
	}

	/**
	 * Update web content.
	 *
	 * @param webContentDto the web content dto
	 * @return the response
	 */
	@ApiOperation(value = "API to update website content")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "No Data Found / Exception Occured Internally"), })
	@PostMapping("admin/static-content/update-webcontent")
	public Response<String> updateWebContent(@RequestBody WebContentUpdateDto webContentDto) {
		return staticContentService.updateWebContent(webContentDto);
	}

}
