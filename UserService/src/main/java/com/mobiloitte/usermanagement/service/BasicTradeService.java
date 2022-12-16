package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.model.Response;

public interface BasicTradeService {
	public Response<Object> sendPaymentRejectEmail(String email, Double amount);
}
