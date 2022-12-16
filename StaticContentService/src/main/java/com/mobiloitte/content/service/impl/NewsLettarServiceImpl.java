package com.mobiloitte.content.service.impl;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.dao.NewsLettarDao;
import com.mobiloitte.content.dao.NewsLetterStaticDao;
import com.mobiloitte.content.dto.NewsLetterStaticDto;
import com.mobiloitte.content.dto.UpdateNewsLetterStaticDto;
import com.mobiloitte.content.entities.NewsLettar;
import com.mobiloitte.content.entities.NewsLetterStatic;
import com.mobiloitte.content.enums.NewsLettarStatus;
import com.mobiloitte.content.fiegn.NotificationClient;
import com.mobiloitte.content.fiegn.UserClient;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.NewsLettarService;


@Service
public class NewsLettarServiceImpl implements NewsLettarService {
	
	@Autowired
	private NewsLettarDao newsLettarDao;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private UserClient userClient;
	

	@Autowired
	private NotificationClient notificationClient;
	
	@Autowired
	private NewsLetterStaticDao newsLetterStaticDao;
	



	@Override
	public Response<Object> getAllEmail(Integer page, Integer pageSize) {

		if(page !=null && pageSize!=null) {
			List<NewsLettar> list=newsLettarDao.findByOrderBySentDateTimeDesc(PageRequest.of(page, pageSize));
			
			if(!list.isEmpty()) {
				 return new Response<>(200,"News letter list found successfully",list);
			}
			else {
				return new Response<>(205,"data is not present");
			}
		}
		else {
		}
		return null;
	}

	@Override
	public Response<Object> getNewsLetter(Long newsLetterId) {

		Optional<NewsLettar> letterExist =newsLettarDao.findByNewsLetterId(newsLetterId);
		
		if(letterExist.isPresent()) {
			 return new Response<>(200,"News letter details found successfully",letterExist);
			
		}
		else {
			return new Response<>(205,"details are not present");
		}
	}

	@Override
	public Response<Object> deleteNewsLetter(Long newsLetterId) {
		Optional<NewsLettar> letterExist =newsLettarDao.findByNewsLetterId(newsLetterId);
		if(letterExist.isPresent()) {
			
			letterExist.get().setNewsLettarStatus(NewsLettarStatus.DELETED);
			 newsLettarDao.save(letterExist.get());
			 return new Response<>(200,"News letter details deleted successfully");
		}
		else {
			return new Response<>(205,"details are not present");
		}
	}

	@Override
	public Response<Object> addNewsLetter(NewsLetterStaticDto newsLettarDto) {

		Optional<NewsLetterStatic> letterExist =	newsLetterStaticDao.findByTitle(newsLettarDto.getTitle());
		if(!letterExist.isPresent()) {
			NewsLetterStatic newsLetterStatic = new NewsLetterStatic();
			
			newsLetterStatic.setCreationDate(new Date());
			newsLetterStatic.setDescription(newsLettarDto.getDescription());
			newsLetterStatic.setImage(newsLettarDto.getImage());
			newsLetterStatic.setNewsLettarStatus(NewsLettarStatus.ACTIVE);
			newsLetterStatic.setTitle(newsLettarDto.getTitle());
			newsLetterStaticDao.save(newsLetterStatic);
			 return new Response<>(200,"News letter posted successfully");
		}
		else {
			return new Response<>(205,"title already exists");
		}
	}

	@Override
	public Response<Object> getStaticNewsLetter(Long newsLetterId) {

		Optional<NewsLetterStatic> letterExist =newsLetterStaticDao.findById(newsLetterId);
		
		if(letterExist.isPresent()) {
			 return new Response<>(200,"News letter details found successfully",letterExist);
			
		}
		else {
			return new Response<>(205,"details are not present");
		}
	}

	@Override
	public Response<Object> getStaticNewsLetterByTitle(String title) {
		Optional<NewsLetterStatic> letterExist =	newsLetterStaticDao.findByTitle(title);
		if(letterExist.isPresent()) {
			 return new Response<>(200,"News letter details found successfully",letterExist);
		}
		else {
			return new Response<>(205,"details are not present");
		}
	}

	@Override
	public Response<Object> getAllStaticNewsLetterList(Integer page, Integer pageSize) {
		List<NewsLetterStatic> list=newsLetterStaticDao.findByOrderByCreationDateDesc(PageRequest.of(page, pageSize));
		
		if(!list.isEmpty()) {
			 return new Response<>(200,"News letter list found successfully",list);
		}
		else {
			return new Response<>(205,"data is not present");
		}
	}

	@Override
	public Response<Object> addNewsLetter(NewsLettarStatus status,Long newsLetterId) {

		Optional<NewsLetterStatic> letterExist =newsLetterStaticDao.findById(newsLetterId);
		
		if(letterExist.isPresent()) {
			
			if(status==NewsLettarStatus.ACTIVE) {
				
				letterExist.get().setNewsLettarStatus(NewsLettarStatus.ACTIVE);
				newsLetterStaticDao.save(letterExist.get());
				
				 return new Response<>(200,"News letter active successfully");
			}
			
			else if(status==NewsLettarStatus.BLOCK) {
				letterExist.get().setNewsLettarStatus(NewsLettarStatus.BLOCK);
				newsLetterStaticDao.save(letterExist.get());
				return new Response<>(200,"News letter blocked successfully");
			}
			else {
			return null;
			}
		}
		
		
		else {
			return new Response<>(205,"data is not present");
		}
		
		
	}

	@Override
	public Response<Object> getStaticNewsLetterByTitleForWebsite(String title) {
		Optional<NewsLetterStatic> letterExist =	newsLetterStaticDao.findByTitleAndNewsLettarStatus(title,NewsLettarStatus.ACTIVE);
		if(letterExist.isPresent()) {
			 return new Response<>(200,"News letter details found successfully",letterExist);
		}
		else {
			return new Response<>(205,"details are not present");
		}
	}

	@Override
	public Response<Object> updateNewsLetter(UpdateNewsLetterStaticDto updatenewsLettarDto, Long newsLetterId) {
		Optional<NewsLetterStatic> letterExist =newsLetterStaticDao.findById(newsLetterId);
		if(letterExist.isPresent()) {
			
			letterExist.get().setDescription(updatenewsLettarDto.getDescription());
			letterExist.get().setImage(updatenewsLettarDto.getImage());
			letterExist.get().setTitle(updatenewsLettarDto.getTitle());
			newsLetterStaticDao.save(letterExist.get());
			 return new Response<>(200,"News letter updated successfully");
		}
		
		else {
			return new Response<>(205,"details are not present");
		}
	}

	@Override
	public Response<Object> getAllNewsLetterForWebsite() {

		List<NewsLetterStatic> letterExist=newsLetterStaticDao.findByNewsLettarStatusOrderByCreationDateDesc(NewsLettarStatus.ACTIVE);
		Map<String,Object> map=new HashMap<>();
		Long count=newsLetterStaticDao.countByNewsLettarStatus(NewsLettarStatus.ACTIVE);
		if(!letterExist.isEmpty()) {
			map.put("details", letterExist);
			map.put("count", count);
			 return new Response<>(200,"Details Found Successfullly",map);
		}
		 return new Response<>(205,"Details Not Found Successfullly");
	}

	
	
	}


