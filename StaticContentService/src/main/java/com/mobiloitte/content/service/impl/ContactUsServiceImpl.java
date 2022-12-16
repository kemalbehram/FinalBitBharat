package com.mobiloitte.content.service.impl;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.content.dao.ContactUsDao;
import com.mobiloitte.content.dto.ContactUsCryptobiz;
import com.mobiloitte.content.dto.ContactUsDto;
import com.mobiloitte.content.entities.ContactUs;
import com.mobiloitte.content.model.Constants;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.ContactUsService;
import com.mobiloitte.content.utils.MailSender;

/**
 * The Class ContactUsServiceImpl.
 * 
 * @author Ankush Mohapatra
 */
@Service
public class ContactUsServiceImpl extends Constants implements ContactUsService {

	/** The mail sender. */
	@Autowired
	private MailSender mailSender;

	@Autowired
	private ContactUsDao contactUsDao;

	@Value("${support.email.address}")
	private String toMail;

	/** The Constant SUBJECT. */
	public static final String SUBJECT = "New Support Message";

	private String toMail1 = "support@cryptobiz.exchange";

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.mobiloitte.content.service.ContactUsService
	 * #contactUs(com.mobiloitte.content.dto.ContactUsDto)
	 */
	@Override
	public Response<String> contactUs(ContactUsDto contactUsDto) {
		try {
		Map<String, Object> sendMailMap = new HashMap<>();
		sendMailMap.put(SEND_FROM, contactUsDto.getFromEmail());
		sendMailMap.put(SEND_TO, toMail);
		sendMailMap.put(DESC, contactUsDto.getDescription());
		sendMailMap.put(EMAIL_SUBJECT, SUBJECT);
		sendMailMap.put(ISSUE_QUES, contactUsDto.getIssue());
		sendMailMap.put(FROM_NAME, contactUsDto.getName());
		/*
		 * if(contactUsDto.getScreenShot() != null &&
		 * !contactUsDto.getScreenShot().equals(BLANK)) { sendMailMap.put(SCREEN_SHOT,
		 * contactUsDto.getScreenShot()); }
		 */
		boolean isSend = mailSender.sendMail(sendMailMap);
		if (isSend) {
			return new Response<>(SUCCESS_CODE, SUPPORT_MAIL_SENT_SUCCESSFULLY);
		} else {
			return new Response<>(FAILURE_CODE, MAIL_SENDING_FAILED);
		}
		}
		catch (Exception e) {
			return new Response<>(500,"something went wrong");
					
		}
	}

	@Override
	@Transactional
	public Response<Boolean> contactUsForCryptobiz(ContactUsCryptobiz contactUsDto) {
		try {
			Map<String, Object> sendMailMap = new HashMap<>();
			sendMailMap.put(SEND_FROM, contactUsDto.getEmail());
			sendMailMap.put(SEND_TO, toMail1);
			sendMailMap.put(DESC, contactUsDto.getMessage());
			sendMailMap.put(EMAIL_SUBJECT, SUBJECT);
			sendMailMap.put(ISSUE_QUES, contactUsDto.getPhoneNo());
			sendMailMap.put(FROM_NAME, contactUsDto.getName());
			/*
			 * if(contactUsDto.getScreenShot() != null &&
			 * !contactUsDto.getScreenShot().equals(BLANK)) { sendMailMap.put(SCREEN_SHOT,
			 * contactUsDto.getScreenShot()); }
			 */
			boolean contactUsIsSend = mailSender.sendMailContactUs(sendMailMap);
			if (contactUsIsSend) {
				contactUsDao.save(new ContactUs(contactUsDto.getEmail(), contactUsDto.getPhoneNo(),
						contactUsDto.getName(), contactUsDto.getMessage(), false));
				return new Response<>(SUCCESS_CODE, "Mail send to admin successfully.");
			} else {

				return new Response<>(FAILURE_CODE, "Mail not send");
			}
		} catch (Exception e) {
			return new Response<>(FAILURE_CODE, "Internal exception occured.");
		}
	}

	@Override
	@Transactional
	public Response<Boolean> resolveTicket(Long ticketId) {
		try {
			Optional<ContactUs> contactUs = contactUsDao.findById(ticketId);
			if (contactUs.isPresent()) {
				contactUs.get().setIsResolved(true);
				contactUsDao.save(contactUs.get());
				return new Response<>(SUCCESS_CODE, "Ticket resolved successfully");
			} else
				return new Response<>(FAILURE_CODE, "Ticket not found");
		} catch (Exception e) {
			return new Response<>(FAILURE_CODE, "Internal exception occured.");
		}
	}

	@Override
	public Response<List<ContactUs>> getTicketDetails(Integer page, Integer size) {
		try {
			Page<ContactUs> getTickets = contactUsDao
					.findAll(PageRequest.of(page, size, Direction.DESC, "contactUsId"));
			if (getTickets.hasContent())
				return new Response<>(SUCCESS_CODE, "Ticket fetched", getTickets.getContent());
			else
				return new Response<>(SUCCESS_CODE, "No tickets found", Collections.emptyList());
		} catch (Exception e) {
			return new Response<>(FAILURE_CODE, "Internal exception occured.", Collections.emptyList());
		}
	}

	@Override
	public Response<ContactUs> getTicketDetails(Long ticketId) {
		try {
			Optional<ContactUs> contactUs = contactUsDao.findById(ticketId);
			if (contactUs.isPresent())
				return new Response<>(SUCCESS_CODE, "Ticket details fetched", contactUs.get());
			else
				return new Response<>(FAILURE_CODE, "Ticket not found");
		} catch (Exception e) {
			return new Response<>(FAILURE_CODE, "Internal exception occured.");
		}
	}

}
