package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.NewsLetterStaticDto;
import com.mobiloitte.content.dto.UpdateNewsLetterStaticDto;
import com.mobiloitte.content.enums.NewsLettarStatus;
import com.mobiloitte.content.model.Response;

public interface NewsLettarService {
	Response<Object> getAllEmail(Integer page, Integer pageSize);

	Response<Object> getNewsLetter(Long newsLetterId);

	Response<Object> deleteNewsLetter(Long newsLetterId);

	Response<Object> addNewsLetter(NewsLetterStaticDto newsLettarDto);

	Response<Object> getStaticNewsLetter(Long newsLetterId);

	Response<Object> getStaticNewsLetterByTitle(String title);

	Response<Object> getAllStaticNewsLetterList(Integer page, Integer pageSize);

	Response<Object> addNewsLetter(NewsLettarStatus status, Long newsLetterId);

	Response<Object> getStaticNewsLetterByTitleForWebsite(String title);

	Response<Object> updateNewsLetter(UpdateNewsLetterStaticDto updatenewsLettarDto, Long newsLetterId);

	Response<Object> getAllNewsLetterForWebsite();

}
