package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mobiloitte.content.dto.AnnouncementDto;
import com.mobiloitte.content.dto.BannerDto;
import com.mobiloitte.content.enums.AnnouncementStatus;
import com.mobiloitte.content.enums.BannerStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.AnnouncementService;
import com.mobiloitte.content.service.BannerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class BannerController {

	@Autowired
	private BannerService bannerService;
	
	@PostMapping("/add-banner")
	public Response<Object>addBanner(@RequestBody BannerDto bannerDto) {
		return bannerService.addBanner(bannerDto);
		
	}
	
	@GetMapping("/get-all-banner-for-website")
	public Response<Object> getAllBannerForWebsite() {
		return bannerService.getAllBannerForWebsite();
	}
	
	@GetMapping("/get-banner-by-id")
	public Response<Object> getBanner(@RequestParam Long bannerId,@RequestHeader Long userId) {
		return bannerService.getBanner(bannerId);
	}
	
	@PostMapping("/active-block-banner")
	public Response<Object>activeBlockBanner(@RequestParam BannerStatus status,@RequestParam Long bannerId,@RequestHeader Long userId) {
		return bannerService.activeBlockBanner(status,bannerId);
		
	}
	@GetMapping("/get-banner-list")
	public Response<Object> getBannerList(@RequestHeader Long userId,Integer page,Integer pageSize) {
		return bannerService.getBannerList(page,pageSize);
	}
	
	@ApiOperation(value = "API to upload the file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping(value = "/upload-file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public Response<Object> uploadFile(@RequestParam MultipartFile file) {
		return bannerService.uploadFile(file);
	}
	
}
