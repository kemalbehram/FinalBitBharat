package com.mobiloitte.usermanagement.exception;

public class RunTimeException extends RuntimeException {

	private static final long serialVersionUID = 2725291042538330775L;

	public RunTimeException() {
		super();
	}

	public RunTimeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public RunTimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public RunTimeException(String message) {
		super(message);
	}

	public RunTimeException(Throwable cause) {
		super(cause);
	}

}