package com.mobiloitte.content.service;

import com.mobiloitte.content.dto.SearchAndFilterTicketDto;
import com.mobiloitte.content.dto.SupportReplyDto;
import com.mobiloitte.content.enums.TicketStatus;
import com.mobiloitte.content.model.Response;

public interface AdminSuppotService {

	Response<Object> viewSupportTicket(Long userId, Long ticketId);
	
	Response<Object> searchAndFilterTicketList(Long userId, SearchAndFilterTicketDto searchAndFilterTicketDto);

	Response<Object> changeTicketStatus(Long userId, Long ticketId, TicketStatus ticketStatus);

	Response<Object> replySupportRequest(SupportReplyDto supportReplyDto);

//	Response<Object> getReplySupportTicketData(long ticketId);

	Response<Object> getReplySupportTicketData(Long ticketId);
	
}
