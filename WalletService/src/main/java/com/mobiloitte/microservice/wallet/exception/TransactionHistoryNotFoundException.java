package com.mobiloitte.microservice.wallet.exception;

/**
 * The Class TransactionHistoryNotFoundException.
 * @author Ankush Mohapatra
 */
public class TransactionHistoryNotFoundException extends RuntimeException{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new transaction history not found exception.
	 */
	public TransactionHistoryNotFoundException() {
		super();
	}

	/**
	 * Instantiates a new transaction history not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public TransactionHistoryNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Instantiates a new transaction history not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public TransactionHistoryNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new transaction history not found exception.
	 *
	 * @param message the message
	 */
	public TransactionHistoryNotFoundException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new transaction history not found exception.
	 *
	 * @param cause the cause
	 */
	public TransactionHistoryNotFoundException(Throwable cause) {
		super(cause);
	}

}
