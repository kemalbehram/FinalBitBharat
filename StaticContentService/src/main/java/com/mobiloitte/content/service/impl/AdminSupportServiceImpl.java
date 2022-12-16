package com.mobiloitte.content.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.mobiloitte.content.constants.EmailConstants;
import com.mobiloitte.content.dao.SupportTicketDao;
import com.mobiloitte.content.dao.TicketReplyDao;
import com.mobiloitte.content.dto.EmailDto;
import com.mobiloitte.content.dto.SearchAndFilterTicketDto;
import com.mobiloitte.content.dto.SupportReplyDto;
import com.mobiloitte.content.dto.TicketDetailDto;
import com.mobiloitte.content.entities.SupportTicket;
import com.mobiloitte.content.entities.TicketReplyData;
import com.mobiloitte.content.enums.TicketStatus;
import com.mobiloitte.content.fiegn.NotificationClient;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.AdminSuppotService;

@Service
public class AdminSupportServiceImpl implements AdminSuppotService {

	@Autowired
	private SupportTicketDao supportTicketDao;

	@Autowired
	private EntityManager em;

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private TicketReplyDao ticketReplyDao;

	@Override
	public Response<Object> viewSupportTicket(Long userId, Long ticketId) {
		try {
		Optional<SupportTicket> ticketDetail = supportTicketDao.findById(ticketId);
		if (ticketDetail.isPresent()) {
			return new Response<>(200,
					messageSource.getMessage("support.ticket.detail.fetch.successfully", new Object[0], Locale.US),
					ticketDetail);
		} else {
			return new Response<>(201,
					messageSource.getMessage("support.ticket.detail.not.fetched", new Object[0], Locale.US));
		}
		}
		catch (Exception e) {
			return new Response<>(500,"something went wrong");
					
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> searchAndFilterTicketList(Long userId, SearchAndFilterTicketDto searchAndFilterTicketDto) {
		
		Map<String, Object> map = new HashMap<>();
		try {
			StringBuilder query = new StringBuilder(
					"select  ticketId, name, email, subject, description, imageUrl, ticketStatus, createdAt from SupportTicket");
			List<String> conditions = new ArrayList<>();
			if (searchAndFilterTicketDto.getSearch() != null) {
				conditions.add("((email like :search))");
			}
			if (searchAndFilterTicketDto.getTicketStatus() != null) {
				conditions.add("(ticketStatus =:ticketStatus)");
			}
			if (searchAndFilterTicketDto.getFromDate() != null) {
				conditions.add("(createdAt >=:fromDate)");
			}
			if (searchAndFilterTicketDto.getToDate() != null) {
				conditions.add("(createdAt <=:toDate)");
			}
			if (!conditions.isEmpty()) {
				query.append(" where ");
				query.append(String.join(" and ", conditions.toArray(new String[0])));
			}
			query.append(" order by ticketId desc ");
			Query createQuery = em.createQuery(query.toString());

			if (searchAndFilterTicketDto.getSearch() != null)
				createQuery.setParameter("search", '%' + searchAndFilterTicketDto.getSearch() + '%');

			if (searchAndFilterTicketDto.getTicketStatus() != null)
				createQuery.setParameter("ticketStatus", searchAndFilterTicketDto.getTicketStatus());

			if (searchAndFilterTicketDto.getFromDate() != null)
				createQuery.setParameter("fromDate", new Date(searchAndFilterTicketDto.getFromDate()));

			if (searchAndFilterTicketDto.getToDate() != null)
				createQuery.setParameter("toDate", new Date(searchAndFilterTicketDto.getToDate()));
			int filteredResultCount = createQuery.getResultList().size();
			createQuery.setFirstResult(searchAndFilterTicketDto.getPage() * searchAndFilterTicketDto.getPageSize());
			createQuery.setMaxResults(searchAndFilterTicketDto.getPageSize());
			List<Object[]> list = createQuery.getResultList();
			List<TicketDetailDto> response = list.parallelStream().map(o -> {
				TicketDetailDto dto = new TicketDetailDto();
				dto.setTicketId((Long) o[0]);
				dto.setName((String) o[1]);
				dto.setEmail((String) o[2]);
				dto.setSubject((String) o[3]);
				dto.setDescription((String) o[4]);
				dto.setImnageUrl((String) o[5]);
				dto.setTicketStatus((TicketStatus) o[6]);
				dto.setCreatedAt((Date) o[7]);
				return dto;
			}).collect(Collectors.toList());
			if (!response.isEmpty()) {
				map.put("size", filteredResultCount);
				map.put("list", response);
				return new Response<>(200,
						messageSource.getMessage("support.ticket.list.fetched.successfully", new Object[0], Locale.US),
						map);
			} else {
				map.put("size", 0);
				map.put("list", Collections.emptyList());
				return new Response<>(201,
						messageSource.getMessage("support.ticket.list.not.fetched", new Object[0], Locale.US), map);
			}
		} catch (Exception e) {
			map.put("size", 0);
			map.put("list", Collections.emptyList());
			return new Response<>(201,
					messageSource.getMessage("support.ticket.list.not.fetched", new Object[0], Locale.US), map);
		}
	}

	@Override
	public Response<Object> changeTicketStatus(Long userId, Long ticketId, TicketStatus ticketStatus) {
		try {
			Optional<SupportTicket> supportTicket = supportTicketDao.findById(ticketId);
			if (supportTicket.isPresent()) {
				SupportTicket ticket = supportTicket.get();
				ticket.setTicketStatus(ticketStatus);
				supportTicketDao.save(ticket);
				Map<String, String> setData = new HashMap<>();
				setData.put(EmailConstants.USER_EMAIL_TOKEN, ticket.getEmail());
				setData.put(EmailConstants.REFERENCENO_TOKEN, "#" + ticket.getTicketId().toString());
				setData.put(EmailConstants.STATUS_TOKEN, String.valueOf(ticket.getStatus()));
				setData.put(EmailConstants.DATE_TOKEN, String.valueOf(ticket.getUpdatedAt()));
				setData.put(EmailConstants.QUOTE_DATE_TOKEN, String.valueOf(ticket.getCreatedAt()));
				setData.put(EmailConstants.QUOTE_DETAILS_TOKEN, ticket.getSubject());
				setData.put(EmailConstants.SUBJECTNAMEFIELD_TOKEN, ticket.getTicketId().toString());
				EmailDto emailDto = new EmailDto(null, "ticket_status_update", ticket.getEmail(), setData);
				notificationClient.sendNotification(emailDto);
				return new Response<>(200, messageSource.getMessage("support.ticket.status.change.successfully",
						new Object[0], Locale.US));
			} else {
				return new Response<>(201,
						messageSource.getMessage("support.ticket.does.not.exist", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(201,
					messageSource.getMessage("support.ticket.status.not.changed", new Object[0], Locale.US));
		}
	}
	@Override
	public Response<Object> replySupportRequest(SupportReplyDto supportReplyDto) {
		try {

			Optional<SupportTicket> supportTicketData = supportTicketDao.findById(supportReplyDto.getTicketId());
			if (supportTicketData.isPresent()) {
				Map<String, String> sendEmailData = new HashMap<>();

				sendEmailData.put(EmailConstants.EMAIL_TOKEN, supportTicketData.get().getEmail());
				sendEmailData.put(EmailConstants.DESCRIPTION, supportReplyDto.getMessage());
				sendEmailData.put(EmailConstants.SUBJECTNAMEFIELD_TOKEN, String.valueOf(supportReplyDto.getTicketId()));
				sendEmailData.put(EmailConstants.IMAGE_URL_TOKEN, String.valueOf(supportReplyDto.getImageUrl()));

				EmailDto emailDto = new EmailDto(null, "ENGLISH_LANGUAGE_CODE, TICKET_REPLY",
						supportTicketData.get().getEmail(), sendEmailData);
				Boolean isEmailSend = notificationClient.sendNotification(emailDto);

				if (isEmailSend.equals(Boolean.TRUE)) {
					TicketReplyData replyData = new TicketReplyData();

					replyData.setCreatedAt(new Date());
					replyData.setImageUrl(supportReplyDto.getImageUrl());
					replyData.setMessage(supportReplyDto.getMessage());
					replyData.setTicketId(supportReplyDto.getTicketId());
					ticketReplyDao.save(replyData);
				}

				return new Response<>(201,
						messageSource.getMessage("SUPPORT_TICKET_EMAIL_SENT_SUCCESSFULLY", new Object[0], Locale.US));
			} else {
				return new Response<>(205,
						messageSource.getMessage("SUPPORT_TICKET_EMAIL_SENT_FAILURE", new Object[0], Locale.US));
			}

		} catch (Exception e) {
			return new Response<>(205,
					messageSource.getMessage("SUPPORT_TICKET_EMAIL_SENT_FAILURE", new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> getReplySupportTicketData(Long ticketId) {
		Optional<SupportTicket> supportTicketData = supportTicketDao.findById(ticketId);
		if (supportTicketData.isPresent()) {
			List<TicketReplyData> supportTicketReplyListData = ticketReplyDao.findByTicketId(ticketId);

			if (!supportTicketReplyListData.isEmpty()) {
				return new Response<>(201,
						messageSource.getMessage("SUPPORT_TICKET_REPLY_DATA_GET_SUCCESSFULLY", new Object[0], Locale.US),
						supportTicketReplyListData);
			} else {
				return new Response<>(205,
						messageSource.getMessage("SUPPORT_TICKET_REPLY_DATA_NOT_FOUND", new Object[0], Locale.US),
						Collections.EMPTY_LIST);
			}

		} else {
			return new Response<>(205,
					messageSource.getMessage("SUPPORT_TICKET_REPLY_DATA_NOT_FOUND", new Object[0], Locale.US),
					Collections.EMPTY_LIST);
		}
	}

	


}
