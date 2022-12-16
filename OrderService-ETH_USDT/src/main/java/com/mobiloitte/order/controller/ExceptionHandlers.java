package com.mobiloitte.order.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mobiloitte.order.exception.FailedToBlockBalanceException;
import com.mobiloitte.order.exception.OrderNotFoundException;
import com.mobiloitte.order.exception.TradingException;
import com.mobiloitte.order.model.ErrorResponse;

@ControllerAdvice
public class ExceptionHandlers {

	@ExceptionHandler(FailedToBlockBalanceException.class)
	public ResponseEntity<ErrorResponse> failedToBlockBalance(FailedToBlockBalanceException e) {
		return ResponseEntity.ok(new ErrorResponse(205, e.getMessage(), "TRADE_ERROR"));
	}

	@ExceptionHandler(TradingException.class)
	public ResponseEntity<ErrorResponse> failedToPlaceOrder(TradingException e) {
		return ResponseEntity.ok(new ErrorResponse(205, e.getMessage(), "TRADE_ERROR"));
	}
	@ExceptionHandler(OrderNotFoundException.class)
	public ResponseEntity<ErrorResponse> orderNotFound(OrderNotFoundException e) {
		return ResponseEntity.ok(new ErrorResponse(205, e.getMessage(), "ORDER_ERROR"));
	}
}
