package com.mobiloitte.microservice.wallet.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.microservice.wallet.exception.BadRequestException;
import com.mobiloitte.microservice.wallet.exception.BankNotFoundException;
import com.mobiloitte.microservice.wallet.model.ErrorResponse;


/**
 * @author Vijay Sahu
 *
 */
@ControllerAdvice(assignableTypes = BankController.class)
@RestController
public class BankControllerAdvice {

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<ErrorResponse> userNotFoundException(BadRequestException ex) {
		return ResponseEntity.badRequest().body(new ErrorResponse(400, ex.getMessage(), "BAD_REQUEST"));
	}

	@ExceptionHandler(BankNotFoundException.class)
	public ResponseEntity<ErrorResponse> bankNotFoundException(BankNotFoundException ex) {
		return ResponseEntity.badRequest().body(new ErrorResponse(400, ex.getMessage(), "BANK_NOT_FOUND"));
	}
}