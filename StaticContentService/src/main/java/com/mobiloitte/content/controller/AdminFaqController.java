package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.FAQDto;
import com.mobiloitte.content.model.Constants;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.FaqAndBlogService;

@RestController
@RequestMapping(value = "/admin/static-content")
public class AdminFaqController extends Constants {

	@Autowired
	private FaqAndBlogService faqService; 

	@PostMapping(value = "/add-new-FAQ")
	public Response<Object> addNewFAQ(@RequestBody FAQDto fAQDto) {
		if (fAQDto != null) {
			return faqService.addNewFAQ(fAQDto);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@PostMapping(value = "/delete-FAQ-by-id")
	public Response<Object> deleteFAQ(@RequestParam Long faqId) {
		if (faqId != null) {
			return faqService.deleteFAQ(faqId);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@PostMapping(value = "/update-FAQ-by-id")
	public Response<Object> updateFAQ(@RequestBody FAQDto fAQDto) {
		if (fAQDto != null) {
			return faqService.updateFAQ(fAQDto);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@GetMapping(value = "/get-FAQ-by-id")
	public Response<Object> getFAQById(@RequestParam Long faqId) {
		if (faqId != null) {
			return faqService.getFAQById(faqId);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}

//
//	@GetMapping(value = "/get-FAQ-list")
//	public Response<Object> getFAQList(@RequestHeader Long userId, @RequestParam(required = false) Integer page,
//			@RequestParam(required = false) Integer pageSize, @RequestParam String topicKey) {
//
//		return casinoFaqAndBlog.getFAQList(userId, page, pageSize, topicKey);
//
//	}
//
//	@GetMapping(value = "/get-FAQ-by-topic-name")
//	public Response<Object> getFAQByTopicName(@RequestParam String topicName,
//			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
//		if (topicName != null) {
//			return casinoFaqAndBlog.getFAQByTopicName(topicName, page, pageSize);
//		} else {
//			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
//		}
//	}
//
//	@GetMapping(value = "/get-only-topic-of-faq")
//	public Response<Object> getOnlyTopicOfFaq() {
//		return casinoFaqAndBlog.getOnlyTopicOfFaq();
//
//	}
	}
		@GetMapping(value = "/get-FAQ-list")
		public Response<Object> getFaqList(@RequestParam(required=false) String question,
				@RequestParam(required=true,value="page") Integer page,
				@RequestParam(required=true,value="pagesize") Integer pageSize){
			return faqService.getFaqList(question,page,pageSize);
		}


}