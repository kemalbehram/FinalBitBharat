package com.mobiloitte.content.service;

import java.util.List;

import com.mobiloitte.content.dto.ContactUsCryptobiz;
import com.mobiloitte.content.dto.ContactUsDto;
import com.mobiloitte.content.entities.ContactUs;
import com.mobiloitte.content.model.Response;

/**
 * The Interface ContactUsService.
 * @author Ankush Mohapatra
 */
public interface ContactUsService {
	
	/**
	 * Contact us.
	 *
	 * @param contactUsDto the contact us dto
	 * @return the response
	 */
	Response<String> contactUs(ContactUsDto contactUsDto);
	
	Response<Boolean> contactUsForCryptobiz(ContactUsCryptobiz contactUsDto);
	
	Response<Boolean> resolveTicket(Long ticketId);

	Response<List<ContactUs>> getTicketDetails(Integer page, Integer size);
	
	Response<ContactUs> getTicketDetails(Long ticketId);
}
