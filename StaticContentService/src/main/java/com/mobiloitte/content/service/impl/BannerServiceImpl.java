package com.mobiloitte.content.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mobiloitte.content.dao.BannerDao;
import com.mobiloitte.content.dto.BannerDto;
import com.mobiloitte.content.entities.Banner;
import com.mobiloitte.content.enums.BannerStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.BannerService;
import com.mobiloitte.content.utils.CloudinaryUtil;

@Service
public class BannerServiceImpl implements BannerService {

	@Autowired
	private BannerDao bannerDao;

	@Autowired
	private CloudinaryUtil cloudinaryUtil;

	@Override
	public Response<Object> addBanner(BannerDto bannerDto) {


		Optional<Banner> bannerExists = bannerDao.findByImageUrl(bannerDto.getImageUrl());
		if (!bannerExists.isPresent()) {
			Banner banner = new Banner();
			banner.setUrl(bannerDto.getUrl());
			banner.setCreationTime(new Date());
			banner.setImageUrl(bannerDto.getImageUrl());
			banner.setDescription(bannerDto.getDescription());
			banner.setBannerStatus(BannerStatus.ACTIVE);
			bannerDao.save(banner);
			return new Response<>(200, "Banner Details Saved Successfully");
		}

		else {

			return new Response<>(200, "Banner Details Saved Unsuccessfully");
		}
	}

	@Override
	public Response<Object> getAllBannerForWebsite() {
		List<Banner> letterExist = bannerDao.findByBannerStatusOrderByCreationTimeDesc(BannerStatus.ACTIVE);
		Map<String, Object> map = new HashMap<>();
		Long count = bannerDao.countByBannerStatus(BannerStatus.ACTIVE);
		if (!letterExist.isEmpty()) {
			map.put("details", letterExist);
			map.put("count", count);
			return new Response<>(200, "Details Found Successfullly", map);
		}
		return new Response<>(205, "Details Not Found Successfullly");
	}

	@Override
	public Response<Object> getBanner(Long bannerId) {
		Optional<Banner> letterExist = bannerDao.findById(bannerId);

		if (letterExist.isPresent()) {
			return new Response<>(200, "Banner details found successfully", letterExist);

		} else {
			return new Response<>(205, "details are not present");
		}
	}

	@Override
	public Response<Object> activeBlockBanner(BannerStatus status, Long bannerId) {
		Optional<Banner> letterExist = bannerDao.findById(bannerId);

		if (letterExist.isPresent()) {

			if (status == BannerStatus.ACTIVE) {

				letterExist.get().setBannerStatus(BannerStatus.ACTIVE);
				bannerDao.save(letterExist.get());

				return new Response<>(200, "Banner active successfully");
			}

			else if (status == BannerStatus.BLOCK) {
				letterExist.get().setBannerStatus(BannerStatus.BLOCK);
				bannerDao.save(letterExist.get());
				return new Response<>(200, "Banner blocked successfully");
			} else {
				return null;
			}
		}

		else {
			return new Response<>(205, "data is not present");
		}
	}

	@Override
	public Response<Object> getBannerList(Integer page, Integer pageSize) {
		List<Banner> list = bannerDao.findByOrderByCreationTimeDesc(PageRequest.of(page, pageSize));

		if (!list.isEmpty()) {
			return new Response<>(200, "Banner list found successfully", list);
		} else {
			return new Response<>(205, "data is not present");
		}
	}

	@Override
	public Response<Object> uploadFile(MultipartFile uploadFile) {
		String url = cloudinaryUtil.uploadFile(uploadFile);
		return new Response<>(url);
	}

}
