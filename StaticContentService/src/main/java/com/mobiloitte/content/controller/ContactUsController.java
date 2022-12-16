package com.mobiloitte.content.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.content.dto.ContactUsCryptobiz;
import com.mobiloitte.content.dto.ContactUsDto;
import com.mobiloitte.content.entities.ContactUs;
import com.mobiloitte.content.model.Constants;
import com.mobiloitte.content.model.Response;
import com.mobiloitte.content.service.ContactUsService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * The Class ContactUsController.
 * 
 * @author Ankush Mohapatra
 */
//@RestController
//@Api(value = "Contact Us API")
public class ContactUsController extends Constants {

	/** The contact us service. */
	@Autowired
	private ContactUsService contactUsService;

	/**
	 * Request contact us.
	 *
	 * @param contactUsDto the contact us dto
	 * @return the response
	 */
	@ApiOperation(value = "API for support")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Support mail sent successfully"),
			@ApiResponse(code = 205, message = "Mail sending failed / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/support")
	public Response<String> requestContactUs(@Valid @RequestBody ContactUsDto contactUsDto) {
		if (contactUsDto != null) {
			return contactUsService.contactUs(contactUsDto);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API for Contact Us")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ticket submitted successfully"),
			@ApiResponse(code = 205, message = "Mail sending failed / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/contact-us")
	public Response<Boolean> requestContactUs(@Valid @RequestBody ContactUsCryptobiz contactUsDto) {
		if (contactUsDto != null) {
			return contactUsService.contactUsForCryptobiz(contactUsDto);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to resolve ticket")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ticket resolved successfully"),
			@ApiResponse(code = 205, message = "Mail sending failed / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/admin/resolve-ticket")
	public Response<Boolean> resolveTicket(@RequestParam Long ticketId) {
		if (ticketId != null) {
			return contactUsService.resolveTicket(ticketId);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to get contact us details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ticket details fetched successfully"),
			@ApiResponse(code = 205, message = "Mail sending failed / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/admin/get-ticket-details")
	public Response<ContactUs> contactUsDetails(@RequestParam Long ticketId) {
		if (ticketId != null) {
			return contactUsService.getTicketDetails(ticketId);
		} else {
			return new Response<>(BAD_REQUEST_CODE, BAD_REQUEST_EXCEPTION);
		}
	}

	@ApiOperation(value = "API to get contact us details list")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Tickets list fetched successfully"),
			@ApiResponse(code = 205, message = "Mail sending failed / Exception Occured Internally"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 500, message = "Internal Server Error"), })
	@PostMapping(value = "/admin/get-ticket-list")
	public Response<List<ContactUs>> ticketsList(@RequestParam("page") Integer page,
			@RequestParam("size") Integer size) {
		return contactUsService.getTicketDetails(page, size);
	}
}
