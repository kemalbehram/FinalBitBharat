package com.mobiloitte.content.service;

import java.util.List;

import com.mobiloitte.content.dto.FAQDto;
import com.mobiloitte.content.model.Response;

public interface FaqService {

	Response<Object> addNewFAQ(FAQDto fAQDto);

//	Response<Object> getFAQByTopicName(String topicName, Integer page, Integer pageSize);

//	Response<Object> getOnlyTopicOfFaq();

	Response<Object> deleteFAQ(Long faqId);

	Response<Object> updateFAQ(FAQDto fAQDto);

	Response<Object> getFAQById(Long faqId);

//	Response<Object> getFAQList(Long userId, Integer page, Integer pageSize, String topicKey);
//
//	Response<Object> getFAQByIdForWebsite(Long faqId);
//
//	Response<Object> getFAQListForWebsite(Integer page, Integer pageSize, String topicKey);
//
//	Response<Object> addContactDetails(List<String> email, List<String> mobileNo);
//
//	Response<Object> getContactDetails();
//
//	Response<Object> updateContactDetails(Long id, String data, String isDelete);
//
//	Response<Object> viewGuarenteedPayOuts(Long payOutsId);
//
//	Response<Object> listGuarenteedPayOuts(Long userId, Integer page, Integer pageSize);
//
//	Response<Object> deleteGuarenteedPayOuts(Long payOutsId);
//
//	Response<Object> getFAQByTopicNameWebsite(String topicName, Object object, Object object2);


}
