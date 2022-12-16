package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.UpdateStaticContentDto;
import com.mobiloitte.content.model.Response;

public interface AdminStaticContentService {

	Response<Object> getAllStaticData(Long userId);

	Response<Object> getStaticDataByPageKey(Long userId, String pageKey);

	Response<Object> updateStaticContentData(Long userId, UpdateStaticContentDto updateStaticContentDTO);

}
