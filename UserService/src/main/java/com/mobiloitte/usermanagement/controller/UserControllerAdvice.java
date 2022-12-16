package com.mobiloitte.usermanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mobiloitte.usermanagement.exception.UserNotFoundException;
import com.mobiloitte.usermanagement.model.ErrorResponse;

@ControllerAdvice
public class UserControllerAdvice {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(400, e.getMessage(), "USER NOT FOUND"));

	}

}
