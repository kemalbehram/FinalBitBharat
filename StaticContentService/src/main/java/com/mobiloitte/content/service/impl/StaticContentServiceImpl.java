package com.mobiloitte.content.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.content.dao.BannerDao;
import com.mobiloitte.content.dao.StaticContentDao;
import com.mobiloitte.content.dao.WebsiteContentDao;
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

/**
 * The Class StaticContentServiceImpl.
 * 
 * @author Ankush Mohapatra
 */
@Service
public class StaticContentServiceImpl extends Constants implements StaticContentService {

	/** The static content dao. */
	@Autowired
	private StaticContentDao staticContentDao;

	@Autowired
	private BannerDao bannerDao;

	@Autowired
	private WebsiteContentDao webContentDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.content.service.StaticContentService
	 * #addStaticContent(com.mobiloitte.content.dto.StaticContentDTO)
	 */
	@Override
	@Transactional
	public Response<String> addStaticContent(StaticContentDTO staticContentDTO) {
		try {
			if (!isPageExist(staticContentDTO.getPageKey())) {
				StaticContent saveStaticContentData = saveStaticContent(staticContentDTO);
				if (saveStaticContentData != null) {
					return new Response<>(SUCCESS_CODE, STATIC_CONTENT_SAVED_SUCCESSFULLY);
				} else {
					return new Response<>(FAILURE_CODE, UPDATION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, STATIC_CONTENT_ALREADY_EXIST);
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	/**
	 * Checks if is page exist.
	 *
	 * @param pageKey the page key
	 * @return true, if is page exist
	 */
	private boolean isPageExist(String pageKey) {
		int isFound = staticContentDao.countByPageKey(pageKey);
		if (isFound > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Save static content.
	 *
	 * @param staticContentDTO the static content DTO
	 * @return the static content
	 */
	private StaticContent saveStaticContent(StaticContentDTO staticContentDTO) {
		try {
			StaticContent staticContent = new StaticContent();
			staticContent.setPageKey(staticContentDTO.getPageKey());
			staticContent.setPageData(staticContentDTO.getPageData());
			staticContent.setIsDeleted(false);
			staticContent = staticContentDao.save(staticContent);
			return staticContent;
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.content.service.StaticContentService
	 * #updateStaticContent(com.mobiloitte.content.dto.UpdateContentRequestDto)
	 */
	@Override
	@Transactional
	public Response<String> updateStaticContent(UpdateContentRequestDto updateContentRequestDto) {
		try {
			Optional<StaticContent> getContentById = staticContentDao.findById(updateContentRequestDto.getContentId());
			if (getContentById.isPresent()) {
				StaticContent updateStaticContentData = updateStaticContent(getContentById.get(),
						updateContentRequestDto.getPageData());
				if (updateStaticContentData != null) {
					return new Response<>(SUCCESS_CODE, STATIC_CONTENT_UPDATED_SUCCESSFULLY);
				} else {
					return new Response<>(FAILURE_CODE, UPDATION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, NO_SUCH_STATIC_CONTENT_FOUND);
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	/**
	 * Update static content.
	 *
	 * @param staticContent the static content
	 * @param pageData      the page data
	 * @return the static content
	 */
	private StaticContent updateStaticContent(StaticContent staticContent, String pageData) {
		try {
			staticContent.setPageData(pageData);
			staticContent = staticContentDao.save(staticContent);
			return staticContent;
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.content.service.StaticContentService
	 * #deleteStaticContent(com.mobiloitte.content.dto.DeleteContentRequestDto)
	 */
	@Override
	@Transactional
	public Response<String> deleteStaticContent(DeleteContentRequestDto deleteContentRequestDto) {
		try {
			Optional<StaticContent> getContentById = staticContentDao.findById(deleteContentRequestDto.getContentId());
			if (getContentById.isPresent()) {
				StaticContent deleteStaticContentData = deleteStaticContent(getContentById.get());
				if (deleteStaticContentData != null) {
					return new Response<>(SUCCESS_CODE, STATIC_CONTENT_DELETED_SUCCESSFULLY);
				} else {
					return new Response<>(FAILURE_CODE, DELETION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, NO_SUCH_STATIC_CONTENT_FOUND);
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	/**
	 * Delete static content.
	 *
	 * @param staticContent the static content
	 * @return the static content
	 */
	private StaticContent deleteStaticContent(StaticContent staticContent) {
		try {
			staticContent.setIsDeleted(true);
			staticContent = staticContentDao.save(staticContent);
			return staticContent;
		} catch (Exception e) {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.content.service.StaticContentService
	 * #getAllStaticContent()
	 */
	@Override
	public Response<List<StaticContent>> getAllStaticContent() {
		try {
			List<StaticContent> getAllContents = staticContentDao.findByIsDeleted(false);
			if (getAllContents != null && !getAllContents.isEmpty()) {
				return new Response<>(SUCCESS_CODE, ALL_STATIC_CONTENT_FETCHED, getAllContents);
			} else {
				return new Response<>(SUCCESS_CODE, NO_STATIC_CONTENT_FOUND, Collections.emptyList());
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.content.service.StaticContentService
	 * #getStaticContentByPageKey(java.lang.String)
	 */
	@Override
	public Response<StaticContent> getStaticContentByPageKey(String pageKey) {
		try {
			Optional<StaticContent> getPageByPageKey = staticContentDao.findByPageKey(pageKey);
			if (getPageByPageKey.isPresent()) {
				return new Response<>(SUCCESS_CODE, STATIC_CONTENT_FOUND, getPageByPageKey.get());
			} else {
				return new Response<>(FAILURE_CODE, String.format(NO_CONTENT_FOUND_FOR_THE_KEY, pageKey));
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	@Override
	@Transactional
	public Response<String> addBannerDetails(BannerDto bannerDto) {
		try {
			List<Banner> getBannerList = bannerDao.findAll();
			if (getBannerList.isEmpty()) {
				Banner saveBanner = saveBannerData(bannerDto);
				if (saveBanner != null) {
					return new Response<>(SUCCESS_CODE, BANNER_DATA_SAVED_SUCCESSFULLY);
				} else {
					return new Response<>(FAILURE_CODE, UPDATION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, BANNER_DATA_EXISTS);
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	private Banner saveBannerData(BannerDto bannerDto) {
		try {
			Banner banner = new Banner();
			banner.setDescription(bannerDto.getDescription());
			banner.setImageUrl(bannerDto.getImageUrl());
			banner = bannerDao.save(banner);
			return banner;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Response<List<Banner>> getBannerList() {
		try {
			List<Banner> getBannerList = bannerDao.findAll();
			if (!getBannerList.isEmpty()) {
				return new Response<>(SUCCESS_CODE, BANNER_DATA_FETCHED, getBannerList);
			} else {
				return new Response<>(FAILURE_CODE, NO_DATA_FOUND);
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	@Override
	@Transactional
	public Response<String> updateBanner(BannerUpdateDto bannerDto) {
		try {
			Optional<Banner> getBannerDetails = bannerDao.findByBannerId(bannerDto.getBannerId());
			if (getBannerDetails.isPresent()) {
				getBannerDetails.get().setUrl(bannerDto.getUrl());
				getBannerDetails.get().setDescription(bannerDto.getDescription());
				getBannerDetails.get().setImageUrl(bannerDto.getImageUrl());
				bannerDao.save(getBannerDetails.get());
				return new Response<>(SUCCESS_CODE, BANNER_DATA_UPDATED_SUCCESSFULLY);
			} else {
				return new Response<>(FAILURE_CODE, NO_DATA_FOUND);
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	@Override
	@Transactional
	public Response<String> addWebContentDetails(WebContentDto webContentDto) {
		try {
			List<WebsiteContent> getWebsiteContentList = webContentDao.findAll();
			if (getWebsiteContentList.isEmpty()) {
				WebsiteContent saveWebContent = saveWebsiteContentData(webContentDto);
				if (saveWebContent != null) {
					return new Response<>(SUCCESS_CODE, WEBSITE_CONTENT_DATA_SAVED_SUCCESSFULLY);
				} else {
					return new Response<>(FAILURE_CODE, UPDATION_FAILED);
				}
			} else {
				return new Response<>(FAILURE_CODE, WEBSITE_CONTENT_DATA_EXISTS);
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	private WebsiteContent saveWebsiteContentData(WebContentDto webContentDto) {
		try {
			WebsiteContent websiteContent = new WebsiteContent();
			websiteContent.setPageName(webContentDto.getMessage());
			websiteContent.setDescription(webContentDto.getQuote());
			websiteContent = webContentDao.save(websiteContent);
			return websiteContent;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public Response<List<WebsiteContent>> getWebContentList() {
		try {
			List<WebsiteContent> getWebContentList = webContentDao.findAll();
			if (!getWebContentList.isEmpty()) {
				return new Response<>(SUCCESS_CODE, WEBSITE_CONTENT_DATA_FETCHED, getWebContentList);
			} else {
				return new Response<>(FAILURE_CODE, NO_DATA_FOUND);
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	@Override
	@Transactional
	public Response<String> updateWebContent(WebContentUpdateDto webContentDto) {
		try {
			Optional<WebsiteContent> getWebContentDetails = webContentDao.findById(webContentDto.getWebContentId());
			if (getWebContentDetails.isPresent()) {
				getWebContentDetails.get().setPageName(webContentDto.getMessage());
				getWebContentDetails.get().setDescription(webContentDto.getQuote());
				webContentDao.save(getWebContentDetails.get());
				return new Response<>(SUCCESS_CODE, WEBSITE_CONTENT_DATA_UPDATED_SUCCESSFULLY);
			} else {
				return new Response<>(FAILURE_CODE, NO_DATA_FOUND);
			}
		}

		catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

}
