package com.mobiloitte.usermanagement.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.amazonaws.services.pinpoint.AmazonPinpoint;
import com.amazonaws.services.pinpoint.AmazonPinpointClientBuilder;
import com.amazonaws.services.pinpoint.model.AddressConfiguration;
import com.amazonaws.services.pinpoint.model.ChannelType;
import com.amazonaws.services.pinpoint.model.DirectMessageConfiguration;
import com.amazonaws.services.pinpoint.model.MessageRequest;
import com.amazonaws.services.pinpoint.model.SMSMessage;
import com.amazonaws.services.pinpoint.model.SendMessagesRequest;

public class AWSPinpointSms {

	private AWSPinpointSms() {}

	private static final Logger LOGGER = LogManager.getLogger(AWSPinpointSms.class);

	public static final String REGION = "us-east-1";

	public static final String ORIGINATIONNUMBER = "+12065550199";

	public static final String DESTINATIONNUMBER = "+14255550142";

	public static final String MESSAGE = "This message was sent through Amazon Pinpoint "
			+ "using the AWS SDK for Java. Reply STOP to "
			+ "opt out.";

	public static final String APPID = "ce796be37f32f178af652b26eexample";

	public static final String MESSAGETYPE = "TRANSACTIONAL";

	public static final String REGISTEREDKEYWORD = "HELP";

	public static final String SENDERID = "MySenderID";

	public static Boolean sendSMS(String destNumber, String message) {
		try {               
			Map<String,AddressConfiguration> addressMap = new HashMap<>();

			addressMap.put(destNumber, new AddressConfiguration()
					.withChannelType(ChannelType.SMS));

			AmazonPinpoint client = AmazonPinpointClientBuilder.standard()
					.withRegion(REGION).build();

			SendMessagesRequest request = new SendMessagesRequest()
					.withApplicationId(APPID)
					.withMessageRequest(new MessageRequest()
							.withAddresses(addressMap)                                   
							.withMessageConfiguration(new DirectMessageConfiguration()
									.withSMSMessage(new SMSMessage()
											.withBody(message)
											.withMessageType(MESSAGETYPE)
											.withOriginationNumber(ORIGINATIONNUMBER)
											.withSenderId(SENDERID)
											.withKeyword(REGISTEREDKEYWORD)
											)
									)
							);
			LOGGER.info("Sending message...");               
			client.sendMessages(request);
			LOGGER.info("Message sent!");
			return true;
		} catch (Exception ex) {
			LOGGER.info("The message wasn't sent. Error message: " 
					+ ex.getMessage());
			return false;
		}
	}
}
