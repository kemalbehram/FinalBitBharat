package com.mobiloitte.microservice.wallet.exception;

/**
 * The Class StorageDetailsNotFoundException.
 * @author Ankush Mohapatra
 */
public class StorageDetailsNotFoundException extends RuntimeException{

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/**
	 * Instantiates a new storage details not found exception.
	 */
	public StorageDetailsNotFoundException() {
		super();
	}

	/**
	 * Instantiates a new storage details not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public StorageDetailsNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Instantiates a new storage details not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public StorageDetailsNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new storage details not found exception.
	 *
	 * @param message the message
	 */
	public StorageDetailsNotFoundException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new storage details not found exception.
	 *
	 * @param cause the cause
	 */
	public StorageDetailsNotFoundException(Throwable cause) {
		super(cause);
	}

}
