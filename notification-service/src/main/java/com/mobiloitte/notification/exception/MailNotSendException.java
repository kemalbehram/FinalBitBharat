package com.mobiloitte.notification.exception;

public class MailNotSendException extends RuntimeException {

	private static final long serialVersionUID = 8827826081537918906L;

	public MailNotSendException() {
		super();
	}

	public MailNotSendException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MailNotSendException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailNotSendException(String message) {
		super(message);
	}

	public MailNotSendException(Throwable cause) {
		super(cause);
	}

}
