package com.mobiloitte.microservice.wallet.exception;

/**
 * The Class CoinNotFoundException.
 * @author Ankush Mohapatra
 */
public class CoinNotFoundException extends RuntimeException{
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new coin not found exception.
	 */
	public CoinNotFoundException() {
		super();
	}

	/**
	 * Instantiates a new coin not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 * @param enableSuppression the enable suppression
	 * @param writableStackTrace the writable stack trace
	 */
	public CoinNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	/**
	 * Instantiates a new coin not found exception.
	 *
	 * @param message the message
	 * @param cause the cause
	 */
	public CoinNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	/**
	 * Instantiates a new coin not found exception.
	 *
	 * @param message the message
	 */
	public CoinNotFoundException(String message) {
		super(message);
	}

	/**
	 * Instantiates a new coin not found exception.
	 *
	 * @param cause the cause
	 */
	public CoinNotFoundException(Throwable cause) {
		super(cause);
	}

}
