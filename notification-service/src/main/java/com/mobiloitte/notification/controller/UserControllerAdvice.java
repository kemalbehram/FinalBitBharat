package com.mobiloitte.notification.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.mobiloitte.notification.exception.UserNotFoundException;
import com.mobiloitte.notification.model.ErrorResponse;

@ControllerAdvice
public class UserControllerAdvice {

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> userNotFoundException(UserNotFoundException e) {
		return ResponseEntity.badRequest().body(new ErrorResponse(420, e.getMessage(), "USER NOT FOUND"));
	}

}