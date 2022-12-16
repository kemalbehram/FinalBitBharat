package com.mobiloitte.content.service;

import org.springframework.web.multipart.MultipartFile;

import com.mobiloitte.content.dto.BannerDto;
import com.mobiloitte.content.enums.BannerStatus;
import com.mobiloitte.content.model.Response;

public interface BannerService {

	Response<Object> addBanner(BannerDto bannerDto);

	Response<Object> getAllBannerForWebsite();

	Response<Object> getBanner(Long bannerId);

	Response<Object> activeBlockBanner(BannerStatus status, Long bannerId);

	Response<Object> getBannerList(Integer page, Integer pageSize);

	Response<Object> uploadFile(MultipartFile file);

}
