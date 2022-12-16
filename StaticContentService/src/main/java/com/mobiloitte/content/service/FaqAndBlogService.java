package com.mobiloitte.content.service;

import java.util.List;

import com.mobiloitte.content.dto.BlogDto;
import com.mobiloitte.content.dto.FAQDto;
//import com.mobiloitte.content.dto.GuarenteedPayOutsDto;
import com.mobiloitte.content.model.Response;

public interface FaqAndBlogService {

	Response<Object> addNewFAQ(FAQDto fAQDto);

	Response<Object> getFAQByTopicName(String topicName, Integer page, Integer pageSize);

	Response<Object> getOnlyTopicOfFaq();

	Response<Object> deleteFAQ(Long faqId);

	Response<Object> updateFAQ(FAQDto fAQDto);

	Response<Object> getFAQById(Long faqId);

	Response<Object> getFAQList(Long userId, Integer page, Integer pageSize, String topicKey);

	Response<Object> getFAQByIdForWebsite(Long faqId);

	Response<Object> getFAQListForWebsite(Integer page, Integer pageSize, String topicKey);

	Response<Object> addNewBlog(BlogDto blogDto);

	Response<Object> deleteBlog(Long blogId);

	Response<Object> updateBlog(BlogDto blogDto);

	Response<Object> getBlogById(Long blogId);

	Response<Object> getBlogList(Long userId, Integer page, Integer pageSize);

	Response<Object> getBlogByIdForWebsite(Long blogId);

	Response<Object> addContactDetails(List<String> email, List<String> mobileNo);

	Response<Object> getContactDetails();

	Response<Object> updateContactDetails(Long id, String data, String isDelete);

	Response<Object> getFAQByTopicNameWebsite(String topicName, Object object, Object object2);

	Response<Object> getBlogListForWebsite(Integer page, Integer pageSize, String title);

	Response<Object> getFaqList(String question, Integer page, Integer pageSize);

}
