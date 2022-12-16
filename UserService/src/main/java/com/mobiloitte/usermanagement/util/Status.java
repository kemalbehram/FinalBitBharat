package com.mobiloitte.usermanagement.util;

public enum Status {
	SUCCESS(200, "Success"),

	USER_DOESNOT_EXIST(201, "User doesn't exist."),

	EMAIL_ALREADY_EXIST(201, "Email address already exist."),

	PHONE_NUMBER_ALREADY_EXIST(201, "Phone number already exist."),

	EMAIL_NOT_EXIST(201, "Email address doesn't exist."),

	INCORRECT_PASSWORD(201, "Incorrect password."),

	INCORRECT_OLD_PASSWORD(201, "Incorrect old password."),

	BAD_REQUEST(400, "Bad request."),

	SERVER_ERROR(500, "Server error."),

	PLEASE_ENTER_TOKEN_FIRST(201, "Please enter token first"),

	PLEASE_ENTER_VALID_OTP(201, "Please enter a valid OTP"),

	GOOGLE_OTP_VERIFIED(200, "Google OTP verified"),

	DATA_CANT_BE_ADDED(201, "data can't be added"),

	CONTENT_ALREADY_EXIST(203, "Content Already Exist"),

	DATA_ADDED(200, "data added"),

	PLEASE_LOGIN_FIRST(201, "Please login first"),

	FAILURE(201, "failed"), VALIDATION_ERROR(400, "Validation Error"),

	CONTENT_NOT_FOUND(404, "Content not found"),

	CONTENT_DELETED_SUCCESSFULLY(200, "Content deleted successfully!"),

	CONTENT_UPDATED_SUCCESSFULLY(200, "Content updated successfully!"),

	CONTENT_NOT_DELETED(201, "Content not deleted"),

	CONTENT_NOT_UPDATED(201, "can't update"),

	GOOGLE_AUTH_NOT_ENABLED(201, "google Auth not enabled"),

	SMS_SUCCESS(200, "sms_success"),

	OTP_VERIFIED_SUCCESSFULLY(200, "otp_verified_successfully"),

	PLEASE_ENTER_OTP_AGAIN(201, "please_enter_otp_again"),

	USER_NOT_DELETED(201, "user not deleted");

	private final int value;

	private final String message;

	Status(int value, String messege) {
		this.value = value;
		this.message = messege;
	}

	public int value() {
		return this.value;
	}

	public String message() {
		return this.message;
	}

	@Override
	public String toString() {
		return Integer.toString(value);
	}
}
