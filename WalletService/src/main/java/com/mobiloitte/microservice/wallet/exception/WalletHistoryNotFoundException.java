package com.mobiloitte.microservice.wallet.exception;

/**
 * The Class WalletHistoryNotFoundException.
 * @author Ankush Mohapatra
 */
public class WalletHistoryNotFoundException extends RuntimeException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new wallet history not found exception.
	 */
	public WalletHistoryNotFoundException() {
		super();
	}

	/**
	 * Instantiates a new wallet history not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public WalletHistoryNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Instantiates a new wallet history not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public WalletHistoryNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new wallet history not found exception.
	 *
	 * @param message the message
	 */
	public WalletHistoryNotFoundException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new wallet history not found exception.
	 *
	 * @param cause the cause
	 */
	public WalletHistoryNotFoundException(Throwable cause) {
		super(cause);
	}
}
