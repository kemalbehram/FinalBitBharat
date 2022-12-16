package com.mobiloitte.content.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.content.dto.NewsLetterStaticDto;
import com.mobiloitte.content.dto.UpdateNewsLetterStaticDto;
import com.mobiloitte.content.enums.NewsLettarStatus;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.NewsLettarService;

@RestController
public class NewsLettarController {
	
	@Autowired
	private NewsLettarService newsLettarService;
	

	@PostMapping("/add-news-letter")
	public Response<Object>addNewsLetter(@RequestBody NewsLetterStaticDto newsLettarDto) {
		return newsLettarService.addNewsLetter(newsLettarDto);
		
	}
	
	@GetMapping("/get-static-news-letter-by-id")
	public Response<Object> getStaticNewsLetter(@RequestParam Long newsLetterId) {
		return newsLettarService.getStaticNewsLetter(newsLetterId);
	}
	
	@GetMapping("/get-news-letter-by-title")
	public Response<Object> getStaticNewsLetterByTitle(@RequestParam String title) {
		return newsLettarService.getStaticNewsLetterByTitle(title);
	}
	
	@GetMapping("/get-static-news-letter-list")
	public Response<Object> getAllStaticNewsLetterList(@RequestHeader Long userId,Integer page,Integer pageSize) {
		return newsLettarService.getAllStaticNewsLetterList(page,pageSize);
	}
	
	
	@PostMapping("/active-block-news-letter")
	public Response<Object>activeBlockNewsLetter(@RequestParam NewsLettarStatus status,@RequestParam Long newsLetterId) {
		return newsLettarService.addNewsLetter(status,newsLetterId);
		
	}
	
	@GetMapping("/get-news-letter-by-title-for-website")
	public Response<Object> getStaticNewsLetterByTitleForWebsite(@RequestParam String title) {
		return newsLettarService.getStaticNewsLetterByTitleForWebsite(title);
	}
	
	@PostMapping("/update-news-letter")
	public Response<Object>updateNewsLetter(@RequestBody UpdateNewsLetterStaticDto updatenewsLettarDto,@RequestParam Long newsLetterId) {
		return newsLettarService.updateNewsLetter(updatenewsLettarDto,newsLetterId);
		
	}
	@GetMapping("/get-all-news-letter-for-website")
	public Response<Object> getAllNewsLetterForWebsite() {
		return newsLettarService.getAllNewsLetterForWebsite();
	}
}

