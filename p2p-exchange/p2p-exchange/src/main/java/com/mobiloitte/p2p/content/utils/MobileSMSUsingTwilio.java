package com.mobiloitte.p2p.content.utils;


import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

@Component
public class MobileSMSUsingTwilio {

	protected MobileSMSUsingTwilio() {

	}

	@Value("${sms.authtoken}")
	private String authToken;

	@Value("${sms.accountsid}")
	private String accountSid;

	@Value("${sms.from.mobileNo}")
	private String fromMobileNumber;

	// twilio
	private static final int MAX_VERIFICATION_CODE = 100000;
	private static final int MIN_VERIFICATION_CODE = 999999;

	public static Integer generateVerificationCode() {
		return new Random().nextInt(MIN_VERIFICATION_CODE - MAX_VERIFICATION_CODE + 1) + MAX_VERIFICATION_CODE;
	}

	public static TwilioRestClient twilioRestClient(String twilioAccountSid, String twilioAuthToken) {
		return new TwilioRestClient.Builder(twilioAccountSid, twilioAuthToken).build();
	}

	// Twilio Send msg
	public Message sendCode(String sendTo, String bodyToSend) {
		final PhoneNumber to = new PhoneNumber(sendTo);
		final PhoneNumber from = new PhoneNumber(fromMobileNumber);
		final String body = bodyToSend;
		return new MessageCreator(to, from, body).create(twilioRestClient(accountSid, authToken));
	}

}
