package com.mobiloitte.order.exception;

public class FailedToBlockBalanceException extends TradingException {
	private static final long serialVersionUID = 1L;

	public FailedToBlockBalanceException() {
		super();
	}

	public FailedToBlockBalanceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FailedToBlockBalanceException(String message, Throwable cause) {
		super(message, cause);
	}

	public FailedToBlockBalanceException(String message) {
		super(message);
	}

	public FailedToBlockBalanceException(Throwable cause) {
		super(cause);
	}

}
