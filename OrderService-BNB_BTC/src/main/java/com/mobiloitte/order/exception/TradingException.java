package com.mobiloitte.order.exception;

public class TradingException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public TradingException() {
		super();
	}

	public TradingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public TradingException(String message, Throwable cause) {
		super(message, cause);
	}

	public TradingException(String message) {
		super(message);
	}

	public TradingException(Throwable cause) {
		super(cause);
	}

}
