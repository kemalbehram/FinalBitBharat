package com.mobiloitte.usermanagement.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.BasicTradeService;
import com.mobiloitte.usermanagement.util.MailSender;

@Service 
public class BasicTradeServiceImpl implements BasicTradeService {

	@Autowired
	MailSender mailSender;

	@Override
	public Response<Object> sendPaymentRejectEmail(String email, Double amount) {

		String subjectOf = "Reject Payment Notification";
		String adminMessage = "Transaction  Of Amount: "+amount+" is Rejected By Admin";
		boolean sendPaymentRejectEmail = mailSender.sendPaymentRejectEmail(email, subjectOf, adminMessage);
		if (sendPaymentRejectEmail) 
			return new Response<>(200, "Email Send Sucessfully");
		return new Response<>(201, "Unable To Send Email");
	}

}
