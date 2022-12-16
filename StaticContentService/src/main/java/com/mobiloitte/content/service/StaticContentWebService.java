package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.SubmitContactUsDto;
import com.mobiloitte.content.model.Response;

public interface StaticContentWebService {

	Response<Object> getWhyChooseUsData();

	Response<Object> getStartedData();

	Response<Object> getStaticPageData(String pageKey);

	Response<Object> getFaqList(Integer page, Integer pageSize);

	Response<Object> getContactUsDetails(Integer page, Integer pageSize, Long fromDate, Long toDate);

	Response<Object> submitContactUsRequest(SubmitContactUsDto submitContactUsDto);

	Response<Object> getContactUsList(Long contactUsId);

}
