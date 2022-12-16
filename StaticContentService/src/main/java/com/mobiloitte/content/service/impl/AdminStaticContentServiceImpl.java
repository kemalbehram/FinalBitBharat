package com.mobiloitte.content.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.constants.MessageConstant;
import com.mobiloitte.content.dao.StaticContentDao;
import com.mobiloitte.content.dto.UpdateStaticContentDto;
import com.mobiloitte.content.entities.StaticContent;
import com.mobiloitte.content.fiegn.ActivityClient;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.AdminStaticContentService;

@Service
public class AdminStaticContentServiceImpl extends MessageConstant implements AdminStaticContentService {

	@Autowired
	private StaticContentDao staticContentDao;

	@Autowired
	private ActivityClient activityClient;

	@Override
	public Response<Object> getAllStaticData(Long userId) {
		try {
		List<StaticContent> staticContent = staticContentDao.findByIsDeletedFalseOrderByStaticContentIdAsc();
		if (!staticContent.isEmpty()) {
			return new Response<>(200, STATIC_CONTENT_DATA_FETCH_SUCCESSFULLY, staticContent);
		} else {
			return new Response<>(201, STATIC_CONTENT_DATA_NOT_FOUND);
		}
		}
		catch (Exception e) {
			return new Response<>(500,SOMETHING_WENT_WRONG);
					
		}
	}

	@Override
	public Response<Object> getStaticDataByPageKey(Long userId, String pageKey) {
		try {
		Optional<StaticContent> pageData = staticContentDao.findByPageKeyAndIsDeletedFalse(pageKey);
		if (pageData.isPresent()) {
			return new Response<>(200, STATIC_CONTENT_PAGE_DATA_FETCH_SUCCESSFULLY, pageData);
		} else {
			return new Response<>(201, STATIC_CONTENT_PAGE_DATA_NOT_FOUND);
		}
		}
		catch (Exception e) {
			return new Response<>(500,SOMETHING_WENT_WRONG);
					
		}
	}

	@Override
	public Response<Object> updateStaticContentData(Long userId, UpdateStaticContentDto updateStaticContentDto) {
		try {
		Optional<StaticContent> contentData = staticContentDao
				.findByPageKeyAndIsDeletedFalse(updateStaticContentDto.getPageKey());
		if (contentData.isPresent()) {
			StaticContent staticContent = contentData.get();
			staticContent.setPageKey(updateStaticContentDto.getPageKey());
			staticContent.setPageData(updateStaticContentDto.getPageData());
			staticContentDao.save(staticContent);
			return new Response<>(200, STATIC_CONTENT_PAGE_DATA_UPDATED_SUCCESSFULLY);
		} else {
			return new Response<>(201, STATIC_CONTENT_PAGE_DATA_NOT_FOUND);
		}
	}
	
	catch (Exception e) {
		return new Response<>(500,SOMETHING_WENT_WRONG);
				
	}

}
	}
