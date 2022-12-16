package com.mobiloitte.microservice.wallet.constants;

/**
 * The Class EmailConstants.
 * 
 * @author Ankush Mohapatra
 */
public class EmailConstants {

	/**
	 * Instantiates a new email constants.
	 */
	protected EmailConstants() {
	}

	/** The Constant TO_ADDRESS. */
	public static final String TO_ADDRESS = "toAddress";

	/** The Constant APPROVE_URL. */
	public static final String APPROVE_URL = "approveUrl";

	/** The Constant REJECT_URL. */
	public static final String REJECT_URL = "rejectUrl";

	/** The Constant EMAIL_TO. */
	public static final String EMAIL_TO = "emailTo";

	/** The Constant SUBJECT_OF. */
	public static final String SUBJECT_OF = "subjectOf";

	/** The Constant COINTYPE. */
	public static final String COIN_TYPE = "coinType";

	/** The Constant USER_NAME. */
	public static final String USER_NAME = "userName";

	public static final String EMAIL_TOKEN = "[:email]";
	public static final String COIN_TOKEN = "[:coinname]";
	public static final String DATE_TOKEN = "[:date]";

	public static final String AMOUNT_TOKEN = "[:amount]";
	public static final String COIN_ADDRESS_TOKEN = "[:coin_address]";

	/** The Constant WITHDRAW_REQUEST. */
	public static final String WITHDRAW_REQUEST = "Withdraw Request";

	public static final Object WITHDRAW_SUCCESS = "Withdraw successfull";
	public static final Object WITHDRAW_REQUEST_INR = "Withdraw Inr request";
	public static final Object WITHDRAW_SUCCESS_INR = "Withdraw Inr successfull";
	public static final Object WITHDRAW_REJECT_INR = "Withdraw Inr rejected";
	public static final Object DEPOSIT_REQUEST = "Deposit Inr request";
	public static final Object DEPOSIT_SUCCESS = "Deposit Inr success";
	public static final Object DEPOSIT_REJECTED = "Deposit Inr rejected";
	public static final Object INERNAL_TRANSFER = "Internal transfer";

}
