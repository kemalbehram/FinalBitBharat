package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.BlogDto;
import com.mobiloitte.content.model.Constants;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.FaqAndBlogService;

@RestController
@RequestMapping(value = "/admin/static-content")
public class AdminBlogController extends Constants {

	@Autowired
	private FaqAndBlogService faqAndBlogService;

	@PostMapping(value = "/add-new-Blog")
	public Response<Object> addNewBlog(@RequestBody BlogDto blogDto) {
		if (blogDto != null) {
			return faqAndBlogService.addNewBlog(blogDto);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@PostMapping(value = "/delete-Blog-by-id")
	public Response<Object> deleteBlog(@RequestParam Long blogId) {
		if (blogId != null) {
			return faqAndBlogService.deleteBlog(blogId);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@PostMapping(value = "/update-Blog-by-id")
	public Response<Object> updateBlog(@RequestBody BlogDto blogDto) {
		if (blogDto != null) {
			return faqAndBlogService.updateBlog(blogDto);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@GetMapping(value = "/get-Blog-by-id")
	public Response<Object> getBlogById(@RequestParam Long blogId) {
		if (blogId != null) {
			return faqAndBlogService.getBlogById(blogId);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@GetMapping(value = "/get-Blog-list")
	public Response<Object> getBlogList(@RequestHeader Long userId, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {

		return faqAndBlogService.getBlogList(userId, page, pageSize);

	}

}
