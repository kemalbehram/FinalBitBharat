package com.mobiloitte.content.service.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.google.common.base.CaseFormat;
import com.mobiloitte.content.dao.FaqDao;
import com.mobiloitte.content.dao.FaqDataDao;
import com.mobiloitte.content.dto.FAQDto;
import com.mobiloitte.content.entities.FAQ;
import com.mobiloitte.content.entities.FaqData;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.FaqService;

@Service
public class FaqServiceImpl implements FaqService {

	@Autowired
	private FaqDao faqDao;

	@Autowired
	private FaqDataDao faqDataDao;

	@Override
	public Response<Object> addNewFAQ(FAQDto fAQDto) {
		if (fAQDto.getQuestion() != null && fAQDto.getAnswer() != null && fAQDto.getTopicKey() != null) {

			Optional<FAQ> isFaqExisted = faqDao.findByTopicKey(fAQDto.getTopicKey());
			String newStr = fAQDto.getTopicKey().replaceAll("_", " ");

			if (isFaqExisted.isPresent()) {
				FaqData faq = new FaqData();
				faq.setTopicKey(fAQDto.getTopicKey());
				faq.setTopicName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, newStr));
				faq.setAnswer(fAQDto.getAnswer());
				faq.setQuestion(fAQDto.getQuestion());
				faq.setIsDeleted(Boolean.FALSE);
				faqDataDao.save(faq);
			} else {
				FAQ faqObj = new FAQ();
				faqObj.setTopicKey(fAQDto.getTopicKey());
				faqObj.setTopicName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, newStr));

				FAQ afterSave = faqDao.save(faqObj);

				FaqData faq = new FaqData();
				faq.setTopicKey(fAQDto.getTopicKey());
				faq.setTopicName(CaseFormat.UPPER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, newStr));
				faq.setAnswer(fAQDto.getAnswer());
				faq.setQuestion(fAQDto.getQuestion());
				faq.setIsDeleted(Boolean.FALSE);
				faq.setFkFaqId(afterSave.getFaqId());
				faqDataDao.save(faq);

			}

			return new Response<>(200, "New Faq Details Add successfully");
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}

	}

	

	@Override
	public Response<Object> deleteFAQ(Long faqId) {
		if (faqId != null) {
			Optional<FaqData> data = faqDataDao.findByFaqDataId(faqId);
			if (!data.isPresent()) {
				return new Response<>(205, "FAQ Detail Not Present.");
			}
			FaqData faq = data.get();
			faq.setIsDeleted(Boolean.TRUE);
			faqDataDao.save(faq);
			return new Response<>(200, " Faq Details Deleted successfully");
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}
	}

	@Override
	public Response<Object> updateFAQ(FAQDto fAQDto) {
		if (fAQDto.getFaqId() != null) {
			Optional<FaqData> data = faqDataDao.findByFaqDataId(fAQDto.getFaqId());
			if (!data.isPresent()) {
				return new Response<>(205, "FAQ Detail Not Present.");
			}
			FaqData faq = data.get();
			if (fAQDto.getAnswer() != null)
				faq.setAnswer(fAQDto.getAnswer());
			if (fAQDto.getQuestion() != null)
				faq.setQuestion(fAQDto.getQuestion());
			faq.setIsDeleted(Boolean.FALSE);

			faqDataDao.save(faq);
			return new Response<>(200, "Faq Details Update successfully");
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}
	}

	@Override
	public Response<Object> getFAQById(Long faqId) {
		if (faqId != null) {
			Optional<FaqData> data = faqDataDao.findByFaqDataId(faqId);
			if (!data.isPresent()) {
				return new Response<>(205, "FAQ Detail Not Present.");
			}
			FAQDto FAQDto = new FAQDto();
			FAQDto.setAnswer(data.get().getAnswer());
			FAQDto.setQuestion(data.get().getQuestion());
			FAQDto.setIsDeleted(data.get().getIsDeleted());
			FAQDto.setFaqId(data.get().getFkFaqId());
			return new Response<>(200, "Faq Details Update successfully", FAQDto);
		} else {
			return new Response<>(205, "Please Enter Valid Details");
		}
	}	
}
