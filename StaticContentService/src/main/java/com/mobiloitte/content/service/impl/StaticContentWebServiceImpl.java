package com.mobiloitte.content.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.constants.EmailConstants;
import com.mobiloitte.content.dao.ContactUsDao;
import com.mobiloitte.content.dao.ContactUsDetailDao;
import com.mobiloitte.content.dao.FaqDao;
import com.mobiloitte.content.dao.FaqDataDao;
import com.mobiloitte.content.dao.GetStartedDao;
import com.mobiloitte.content.dao.StaticContentDao;
import com.mobiloitte.content.dao.WhyChooseUsDao;
import com.mobiloitte.content.dto.EmailDto;
import com.mobiloitte.content.dto.SubmitContactUsDto;
import com.mobiloitte.content.entities.ContactUs;
import com.mobiloitte.content.entities.FAQ;
import com.mobiloitte.content.entities.FaqData;
import com.mobiloitte.content.entities.GetStarted;
import com.mobiloitte.content.entities.StaticContent;
import com.mobiloitte.content.entities.WhyChooseUs;
import com.mobiloitte.content.fiegn.NotificationClient;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.StaticContentWebService;

@Service
public class StaticContentWebServiceImpl implements StaticContentWebService {

	@Autowired
	private WhyChooseUsDao whyChooseUsDao;

	@Autowired
	private GetStartedDao getStartedDao;

	@Autowired
	private StaticContentDao staticContentDao;

	@Autowired
	private FaqDao faqDao;

	@Autowired
	private FaqDataDao faqDataDao;

	@Autowired
	private ContactUsDetailDao contactUsDetailDao;

	@Autowired
	private ContactUsDao contactUsDao;

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Response<Object> getWhyChooseUsData() {
		try {
			List<WhyChooseUs> whyChooseUsData = whyChooseUsDao.findAll();
			if (!whyChooseUsData.isEmpty()) {
				return new Response<>(200,
						messageSource.getMessage("why.choose.us.data.get.successfully", new Object[0], Locale.US),
						whyChooseUsData);
			} else {
				return new Response<>(201,
						messageSource.getMessage("why.choose.us.data.not.fetched", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	@Override
	public Response<Object> getStartedData() {
		try {
			List<GetStarted> getStartedData = getStartedDao.findAll();
			if (!getStartedData.isEmpty()) {
				return new Response<>(200,
						messageSource.getMessage("get.started.data.fetch.successfully", new Object[0], Locale.US),
						getStartedData);
			} else {
				return new Response<>(201,
						messageSource.getMessage("get.started.data.not.found", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	@Override
	public Response<Object> getStaticPageData(String pageKey) {
		try {
			Optional<StaticContent> staticData = staticContentDao.findByPageKeyAndIsDeletedFalse(pageKey);
			if (staticData.isPresent()) {
				return new Response<>(200,
						messageSource.getMessage("static.page.data.fetch.successfully", new Object[0], Locale.US),
						staticData);
			} else {
				return new Response<>(201,
						messageSource.getMessage("static.page.data.not.fetched", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	@Override
	public Response<Object> getFaqList(Integer page, Integer pageSize) {
		try {
			if (page != null || pageSize != null) {
				List<FaqData> faqList = faqDataDao
						.findByIsDeletedFalse(PageRequest.of(page, pageSize, Direction.DESC, "faqDataId"));
				if (!faqList.isEmpty()) {
					return new Response<>(200,
							messageSource.getMessage("faq.list.fetch.successfully", new Object[0], Locale.US), faqList);
				} else {
					return new Response<>(201,
							messageSource.getMessage("faq.list.not.fetched", new Object[0], Locale.US));
				}
			} else {
				List<FaqData> faqList = faqDataDao.findByIsDeletedFalse();
				if (!faqList.isEmpty()) {
					return new Response<>(200,
							messageSource.getMessage("faq.list.fetch.successfully", new Object[0], Locale.US), faqList);
				} else {
					return new Response<>(201,
							messageSource.getMessage("faq.list.not.fetched", new Object[0], Locale.US));
				}
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");

		}
	}

	@Override
	public Response<Object> getContactUsDetails(Integer page, Integer pageSize,Long fromDate, Long toDate) {
		try {
			if(fromDate==null && toDate==null) {
			List<ContactUs> contactUsDetails = contactUsDetailDao
					.findByOrderByCreationTimeDesc(PageRequest.of(page, pageSize));
			if (!contactUsDetails.isEmpty()) {
				Map<String, Object> map=new HashMap<>();
				map.put("List", contactUsDetails);
				map.put("Count", contactUsDetailDao.count());
				return new Response<>(200,
						messageSource.getMessage("contact.us.detail.found.successfully", new Object[0], Locale.US),
						map);
			} else {
				return new Response<>(201,
						messageSource.getMessage("contact.us.detail.not.found", new Object[0], Locale.US));
			}
			}else if (fromDate!=null && toDate!=null) {
				List<ContactUs> contactUsDetails = contactUsDetailDao
						.findByCreationTimeBetween(new Date(fromDate),new Date(toDate),PageRequest.of(page, pageSize));
				if (!contactUsDetails.isEmpty()) {
					Map<String, Object> map=new HashMap<>();
					map.put("List", contactUsDetails);
					map.put("Count", contactUsDetailDao.count());
					return new Response<>(200,
							messageSource.getMessage("contact.us.detail.found.successfully", new Object[0], Locale.US),
							map);
				} else {
					return new Response<>(201,
							messageSource.getMessage("contact.us.detail.not.found", new Object[0], Locale.US));
				}
			}
		} catch (Exception e) {
			return new Response<>(500, "something went wrong");
		}
		return null;
		
	}

	@Override
	public Response<Object> submitContactUsRequest(SubmitContactUsDto submitContactUsDto) {
		try {
			ContactUs contactUs = new ContactUs();
			contactUs.setEmail(submitContactUsDto.getEmail());
			contactUs.setName(submitContactUsDto.getName());
			contactUs.setMessage(submitContactUsDto.getMessage());
			contactUs.setPhoneNo(submitContactUsDto.getPhoneNo());
			contactUs.setIsResolved(Boolean.FALSE);
			contactUsDao.save(contactUs);
//			Map<String, String> setData = new HashMap<>();
//			setData.put(EmailConstants.USER_EMAIL_TOKEN, contactUs.getEmail());
//			EmailDto emailDto = new EmailDto(null, "inquiry_form_submission", contactUs.getEmail(), setData);
//			notificationClient.sendNotification(emailDto);
			return new Response<>(200,
					messageSource.getMessage("contact.us.request.submitted", new Object[0], Locale.US));
		} catch (Exception e) {
			return new Response<>(201,
					messageSource.getMessage("contact.us.request.not.submitted", new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> getContactUsList(Long contactUsId) {
		List<ContactUs> list = contactUsDao.findByContactUsId(contactUsId);
		if (!list.isEmpty()) {
			return new Response<>(200, "ContactUs list found successfully", list);
		} else {
			return new Response<>(205, "Data is not present");
		}
	}
}
