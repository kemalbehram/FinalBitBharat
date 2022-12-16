package com.mobiloitte.usermanagement.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.twilio.rest.api.v2010.account.Message;

@Component
public class AWSSNSUtil {

	private AWSSNSUtil() {
	}

	private static final Logger LOGGER = LogManager.getLogger(AWSSNSUtil.class);

	@Value("${aws.sns.access.key}")
	private String aws_sns_access_key;

	@Value("${aws.sns.secret.key}")
	private String aws_sns_secret_key;

	@Value("${aws.region}")
	private String aws_region;

	public Boolean sendSms(Integer otp, String destNumber) {
		String message = "Dear user, your authentication OTP is: " + otp;
		try {
			BasicAWSCredentials awsCredentials = new BasicAWSCredentials(aws_sns_access_key, aws_sns_secret_key);
			AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(aws_region).build();

			Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
			smsAttributes.put("AWS.SNS.SMS.SMSType",
					new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));

			smsAttributes.put("AWS.SNS.SMS.SenderID",
					new MessageAttributeValue().withStringValue("CryptoBiz").withDataType("String"));

			PublishResult result = snsClient.publish(new PublishRequest().withMessage(message)
					.withPhoneNumber(destNumber).withMessageAttributes(smsAttributes));

			LOGGER.info("message {}", result);
			return true;
		} catch (Exception e) {
			LOGGER.error("Some error occured while sending SMS{}", e.getMessage());
			return false;
		}
	}

	public Integer generateRandomOtp() {
		return 100000 + new Random().nextInt(999999);
	}

	public Boolean sendSms(String phoneNo, String body, Integer otp) {
		String message = "body" + otp;
		try {
			BasicAWSCredentials awsCredentials = new BasicAWSCredentials(aws_sns_access_key, aws_sns_secret_key);
			AmazonSNS snsClient = AmazonSNSClientBuilder.standard()
					.withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(aws_region).build();

			Map<String, MessageAttributeValue> smsAttributes = new HashMap<String, MessageAttributeValue>();
			smsAttributes.put("AWS.SNS.SMS.SMSType",
					new MessageAttributeValue().withStringValue("Transactional").withDataType("String"));

			smsAttributes.put("AWS.SNS.SMS.SenderID",
					new MessageAttributeValue().withStringValue("CryptoBiz").withDataType("String"));

			PublishResult result = snsClient.publish(new PublishRequest().withMessage(message).withPhoneNumber(body)
					.withMessageAttributes(smsAttributes));

			LOGGER.info("message {}", result);
			return true;
		} catch (Exception e) {
			LOGGER.error("Some error occured while sending SMS{}", e.getMessage());
			return false;
		}
	}

}
