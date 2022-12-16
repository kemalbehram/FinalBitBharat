package com.mobiloitte.content.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.constants.EmailConstants;
import com.mobiloitte.content.dao.SupportTicketDao;
import com.mobiloitte.content.dto.EmailDto;
import com.mobiloitte.content.dto.SupportDto;
import com.mobiloitte.content.entities.SupportTicket;
import com.mobiloitte.content.enums.TicketStatus;
import com.mobiloitte.content.fiegn.NotificationClient;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.SupportService;
import com.mobiloitte.content.utils.MailSender;

@Service
public class SupportServicreImpl implements SupportService {

	@Autowired
	private SupportTicketDao supportDao;

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private MailSender mailSender;

	@Override
	public Response<Object> submitSupportRequest(SupportDto supportDto) {
		try {
			SupportTicket supportTicket = new SupportTicket();
			supportTicket.setName(supportDto.getName());
			supportTicket.setEmail(supportDto.getEmail());
			supportTicket.setCategory(supportDto.getCategory());
			supportTicket.setSubject(supportDto.getSubject());
			supportTicket.setDescription(supportDto.getDescription());
			supportTicket.setTicketStatus(TicketStatus.INPROGRESS);
			supportTicket.setImageUrl(supportDto.getImageUrl());
			supportTicket.setStatus(Boolean.TRUE);
			supportDao.save(supportTicket);
			Map<String, String> setData = new HashMap<>();
			setData.put(EmailConstants.USER_EMAIL_TOKEN, supportDto.getEmail());
			setData.put(EmailConstants.STATUS_TOKEN, String.valueOf(supportTicket.getTicketStatus()));
			setData.put(EmailConstants.DATE_TOKEN, String.valueOf(new Date()));
			setData.put(EmailConstants.DETAILS_TOKEN, supportTicket.getSubject());
			setData.put(EmailConstants.REFERENCENO_TOKEN, supportTicket.getTicketId().toString());
			setData.put(EmailConstants.SUBJECTNAMEFIELD_TOKEN, supportTicket.getTicketId().toString());
			setData.put("isWeb", String.valueOf(Boolean.TRUE));
			setData.put("isEmail", String.valueOf(Boolean.TRUE));
			EmailDto emailDto = new EmailDto(null, "new_ticket_registration", supportDto.getEmail(), setData);
			notificationClient.sendNotification(emailDto);
			return new Response<>(200,
					messageSource.getMessage("support.ticket.submitted.successfully", new Object[0], Locale.US));
		} catch (Exception e) {
			return new Response<>(203,
					messageSource.getMessage("support.ticket.submitted.failure", new Object[0], Locale.US));
		}
	}

}
