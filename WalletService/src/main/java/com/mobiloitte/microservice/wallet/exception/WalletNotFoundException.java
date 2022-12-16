package com.mobiloitte.microservice.wallet.exception;

/**
 * The Class WalletNotFoundException.
 * @author Ankush Mohapatra
 */
public class WalletNotFoundException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new wallet not found exception.
	 */
	public WalletNotFoundException() {
		super();
	}

	/**
	 * Instantiates a new wallet not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public WalletNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Instantiates a new wallet not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public WalletNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new wallet not found exception.
	 *
	 * @param message the message
	 */
	public WalletNotFoundException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new wallet not found exception.
	 *
	 * @param cause the cause
	 */
	public WalletNotFoundException(Throwable cause) {
		super(cause);
	}

}
