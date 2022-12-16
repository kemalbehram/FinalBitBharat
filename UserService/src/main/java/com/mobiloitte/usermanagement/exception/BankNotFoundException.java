package com.mobiloitte.usermanagement.exception;

/**
 * @author Vijay Sahu
 *
 */
public class BankNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public BankNotFoundException() {
		super();
	}

	public BankNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BankNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public BankNotFoundException(String message) {
		super(message);
	}

	public BankNotFoundException(Throwable cause) {
		super(cause);
	}

}