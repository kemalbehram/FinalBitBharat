package com.mobiloitte.p2p.content.model;

/**
 * The Class Constants.
 * @author Ankush Mohapatra
 */
public class Constants {
	
	/**
	 * Instantiates a new constants.
	 */
	protected Constants () {
	}
	
	/** The Constant SEND_FROM. */
	//Email Map Constants
	public static final String SEND_FROM = "sendFrom";
	
	/** The Constant DESC. */
	public static final String DESC = "desc";
	
	/** The Constant SEND_TO. */
	public static final String SEND_TO = "emailTo";
	
	/** The Constant EMAIL_SUBJECT. */
	public static final String EMAIL_SUBJECT = "subjectOf";
	
	/** The Constant SCREEN_SHOT. */
	public static final String SCREEN_SHOT = "screenshot";
	
	/** The Constant ISSUE_QUES. */
	public static final String ISSUE_QUES = "issueQues";
	
	/** The Constant BLANK. */
	public static final String BLANK = "";

	//Response Status Codes
	
	/** The Constant SUCCESS_CODE. */
	public static final int SUCCESS_CODE = 200;
	
	/** The Constant FAILURE_CODE. */
	public static final int FAILURE_CODE = 205;
	
	/** The Constant BAD_REQUEST_CODE. */
	public static final int BAD_REQUEST_CODE=400;
	
	//Response Messages
	
	/** The Constant STATIC_CONTENT_SAVED_SUCCESSFULLY. */
	public static final String STATIC_CONTENT_SAVED_SUCCESSFULLY = "Static content saved successfully";
	
	/** The Constant STATIC_CONTENT_UPDATED_SUCCESSFULLY. */
	public static final String STATIC_CONTENT_UPDATED_SUCCESSFULLY = "Static content updated successfully";
	
	/** The Constant STATIC_CONTENT_DELETED_SUCCESSFULLY. */
	public static final String STATIC_CONTENT_DELETED_SUCCESSFULLY = "Static content deleted successfully";
	
	/** The Constant ALL_STATIC_CONTENT_FETCHED. */
	public static final String ALL_STATIC_CONTENT_FETCHED = "All static content fetched";
	
	/** The Constant STATIC_CONTENT_FOUND. */
	public static final String STATIC_CONTENT_FOUND = "Static content found";
	
	/** The Constant NO_CONTENT_FOUND_FOR_THE_KEY. */
	public static final String NO_CONTENT_FOUND_FOR_THE_KEY = "No content found for the key: %s";
	
	/** The Constant NO_STATIC_CONTENT_FOUND. */
	public static final String NO_STATIC_CONTENT_FOUND = "No static content found";
	
	/** The Constant UPDATION_FAILED. */
	public static final String UPDATION_FAILED = "Updation failed, some exception occured";
	
	/** The Constant DELETION_FAILED. */
	public static final String DELETION_FAILED = "Deletion failed, some exception occured";
	
	/** The Constant STATIC_CONTENT_ALREADY_EXIST. */
	public static final String STATIC_CONTENT_ALREADY_EXIST = "Static content already exist";
	
	/** The Constant NO_SUCH_STATIC_CONTENT_FOUND. */
	public static final String NO_SUCH_STATIC_CONTENT_FOUND = "No such static content found";

	/** The Constant BAD_REQUEST_EXCEPTION. */
	public static final String BAD_REQUEST_EXCEPTION = "Bad Request Exception";
	
	/** The Constant SUPPORT_MAIL_SENT_SUCCESSFULLY. */
	public static final String SUPPORT_MAIL_SENT_SUCCESSFULLY = "Support mail sent successfully";
	
	/** The Constant MAIL_SENDING_FAILED. */
	public static final String MAIL_SENDING_FAILED = "Mail sending failed";
}
