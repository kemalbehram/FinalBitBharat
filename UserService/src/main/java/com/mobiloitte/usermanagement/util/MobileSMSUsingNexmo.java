/*package com.mobiloitte.usermanagement.util;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.nexmo.client.NexmoClient;
import com.nexmo.client.NexmoClientException;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.messages.TextMessage;
import com.nexmo.client.verify.CheckResponse;
import com.nexmo.client.verify.VerifyResponse;

public class MobileSMSUsingNexmo {

	protected MobileSMSUsingNexmo() {
	}
	private static final Logger LOGGER = LogManager.getLogger(MobileSMSUsingNexmo.class);

	public static NexmoClient getClient() {

		return new NexmoClient.Builder().apiKey("00ae7d84").apiSecret("pHMtjQrmC2o8pYjk").build();
	}

	*//** NEXMO INTEGRATION *//*
	public static String send(String phone, String message) throws ServletException, IOException {
		String mobileNumber = phone;
		NexmoClient client = null;
		String apiKey = "00ae7d84";
		String apiSecret = "pHMtjQrmC2o8pYjk";
		String fromNumber = "NEXMO";
		String toNumber = mobileNumber; *//** "919650287832"; *//*
		String msg = message; *//** "Web Trading Mobiloitte Verification Code 88888 "; *//*
		String status = "-1";
		try {
			client = new NexmoClient.Builder().apiKey(apiKey).apiSecret(apiSecret).build();
			SmsSubmissionResponse responses = client.getSmsClient()
					.submitMessage(new TextMessage(fromNumber, toNumber, msg));

			status = String.valueOf(responses);
		} catch (NexmoClientException e) {
			throw new ServletException(e);
		}
		return status;
	}

	// generating sms-code
	public static String sendTwoFactorAuthenticationCode(String mobileNumber) {
		NexmoClient client = getClient();
		try {
			VerifyResponse ongoingVerify = client.getVerifyClient().verify(mobileNumber, "otp-verification");
			return ongoingVerify.getRequestId();
		} catch (Exception e) {
			LOGGER.debug("Otp not sent");
			return null;
		}
	}

	public static CheckResponse verify2Fcode(String requestId, String code) {
		NexmoClient client = getClient();
		CheckResponse result = null;
		try {
			result = client.getVerifyClient().check(requestId, code);
			return result;
		} catch (Exception e) {
			return result;
		}

	}
}
*/