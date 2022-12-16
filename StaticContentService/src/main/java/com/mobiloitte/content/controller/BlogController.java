package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.model.Constants;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.FaqAndBlogService;

@RestController
public class BlogController extends Constants {

	@Autowired
	private FaqAndBlogService faqAndBlogService;

	@GetMapping(value = "/get-Blog-by-id")
	public Response<Object> getBlogByIdForWebsite(@RequestParam Long blogId) {
		if (blogId != null) {
			return faqAndBlogService.getBlogByIdForWebsite(blogId);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@GetMapping(value = "/get-Blog-list")
	public Response<Object> getBlogListForWebsite(@RequestParam Integer page,@RequestParam Integer pageSize,@RequestParam(required = false) String title) {

		return faqAndBlogService.getBlogListForWebsite(page,pageSize,title);

	}
}
