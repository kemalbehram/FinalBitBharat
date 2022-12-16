package com.mobiloitte.content.utils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import com.mobiloitte.content.constants.EmailConstants;
import com.mobiloitte.content.model.Constants;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

/**
 * The Class MailSender.
 * 
 * @author Ankush Mohapatra
 */
@Component
public class MailSender extends Constants {

	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(MailSender.class);

	/** The send grid api key. */
	@Value("${sendGrid.Api.Key}")
	private String sendGridApiKey;
	
	@Value("${sendGrid.Email.From}")
	private String fromAddress;

//	@Value("${email.sender}")
//	private String emailSender;

	/**
	 * Gets the email body.
	 *
	 * @param sendMailMap the send mail map
	 * @return the email body
	 */
	// set Email body by adding HTML Template
	public String getEmailBody(Map<String, Object> sendMailMap) {
		return "<html><body><b>" + sendMailMap.get(ISSUE_QUES) + "</b><br/> ISSUES :" + sendMailMap.get(DESC)
				+ "<br> Issue was from : " + sendMailMap.get(SEND_FROM) + "<br> user name : "
				+ sendMailMap.get(FROM_NAME) + "</body></html>";
	}

	public String getEmailBodyContactUs(Map<String, Object> sendMailMap) {
		return "<html><body><div style='padding: 10px 15px;'><b>Mobile number :</b>" + sendMailMap.get(ISSUE_QUES)
				+ "<br/></div><div style='height:100px;width:500px;border:1px solid #ccc;font:16px/26px Georgia, Garamond, Serif;overflow:auto;'><b> Message from user :</b>"
				+ sendMailMap.get(DESC) + "</div><div style='padding: 10px 15px;'> <b>Address of user :</b> "
				+ sendMailMap.get(SEND_FROM) + "</div><div style='padding: 10px 15px;'><b>user name  : </b>"
				+ sendMailMap.get(FROM_NAME) + "</div></body></html>";
	}

	/**
	 * Send mail.
	 *
	 * @param sendMailMap the send mail map
	 * @return true, if successful
	 */
	// sending mail using SENDGRID
	public boolean sendMail(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(SEND_TO)));
		try {
			String message = getEmailBody(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(String.valueOf(fromAddress));
			Mail mail = new Mail(from, String.valueOf(sendMailMap.get(EMAIL_SUBJECT)), to, content);
			/*
			 * if(String.valueOf(sendMailMap.get(SCREEN_SHOT)) != null &&
			 * !String.valueOf(sendMailMap.get(SCREEN_SHOT)).equals(BLANK)) {
			 * mail.addAttachments(addAttachments(String.valueOf(sendMailMap.get(SCREEN_SHOT
			 * )))); }
			 */
			SendGrid sg = new SendGrid(sendGridApiKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sg.api(request);
		} catch (IOException ex) {
			LOGGER.debug("Mail not sent, some exception occured");
			return false;
		}
		return true;
	}

	public boolean sendMailContactUs(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(SEND_TO)));
		try {
			String message = getEmailBodyContactUs(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(String.valueOf(fromAddress));
			Mail mail = new Mail(from, String.valueOf(sendMailMap.get(EMAIL_SUBJECT)), to, content);
			/*
			 * if(String.valueOf(sendMailMap.get(SCREEN_SHOT)) != null &&
			 * !String.valueOf(sendMailMap.get(SCREEN_SHOT)).equals(BLANK)) {
			 * mail.addAttachments(addAttachments(String.valueOf(sendMailMap.get(SCREEN_SHOT
			 * )))); }
			 */
			SendGrid sg = new SendGrid(sendGridApiKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sg.api(request);
		} catch (IOException ex) {
			LOGGER.debug("Mail not sent, some exception occured");
			return false;
		}
		return true;
	}

	/**
	 * Adds the attachemts.
	 *
	 * @param imageBase64 the image base 64
	 * @return the attachments
	 */
	/*
	 * private Attachments addAttachments(String imageBase64) { Attachments
	 * attachment = new Attachments(); byte[] decoded =
	 * Base64.decodeBase64(imageBase64); String contentType = new
	 * Tika().detect(decoded); String[] words=contentType.split("/");
	 * attachment.setContent(imageBase64); attachment.setType(contentType);
	 * attachment.setFilename("screenshot."+words[1]);
	 * attachment.setDisposition("attachment"); attachment.setContentId("Banner");
	 * return attachment; }
	 */

	public Boolean sendMailToTicketSubmissionSuccess(Map<String, Object> sendMailMap, String langCode) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodySubscriptionSuccess(sendMailMap, langCode);
			Content content = new Content("text/html", message);
//			Email from = new Email(emailSender);
			Email from = new Email(String.valueOf(fromAddress));
			Mail mail = new Mail(from, String.valueOf(sendMailMap.get(EmailConstants.SUBJECT_OF)) + "  @" + new Date(),
					to, content);
			SendGrid sg = new SendGrid(sendGridApiKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sg.api(request);
		} catch (IOException ex) {
			LOGGER.debug("mail not sent");
			return false;
		}
		return true;
	}
	public Boolean sendMailToListingSubmissionSuccess(Map<String, Object> sendMailMap, String langCode) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyListingSuccess(sendMailMap, langCode);
			Content content = new Content("text/html", message);
//			Email from = new Email(emailSender);
			Email from = new Email(String.valueOf(fromAddress));
			Mail mail = new Mail(from, String.valueOf(sendMailMap.get(EmailConstants.SUBJECT_OF)) + "  @" + new Date(),
					to, content);
			SendGrid sg = new SendGrid(sendGridApiKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sg.api(request);
		} catch (IOException ex) {
			LOGGER.debug("mail not sent");
			return false;
		}
		return true;
	}

	public Boolean sendMailToCareerSubmissionSuccess(Map<String, Object> sendMailMap, String langCode) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyCareerSuccess(sendMailMap, langCode);
			Content content = new Content("text/html", message);
//			Email from = new Email(emailSender);
			Email from = new Email(String.valueOf(fromAddress));
			Mail mail = new Mail(from, String.valueOf(sendMailMap.get(EmailConstants.SUBJECT_OF)) + "  @" + new Date(),
					to, content);
			SendGrid sg = new SendGrid(sendGridApiKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint("mail/send");
			request.setBody(mail.build());
			sg.api(request);
		} catch (IOException ex) {
			LOGGER.debug("mail not sent");
			return false;
		}
		return true;
	}

	private String getEmailBodySubscriptionSuccess(Map<String, Object> mailData, String lanCode) throws IOException {
		String filePath = "";
		if (lanCode.equalsIgnoreCase("en"))
			filePath = "/Templates/crypto" + "/subscription.html";
//		if (lanCode.equalsIgnoreCase("es"))
//			filePath = "/Templates/crypto" + "/withdrawCon_sp.html";
//		if (lanCode.equalsIgnoreCase("gmn"))
//			filePath = "/Templates/crypto" + "/withdrawCon_gmn.html";

		// filePath = "/Templates/crypto" + "/withdraw_success.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
//		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
//		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
//		document.getElementById("datetime").appendText(new Date().toString());
//		document.getElementById("userName").appendText(String.valueOf(mailData.get("userName")));
//		document.getElementById("EMAIL").appendText(String.valueOf(sendMailMap.get("EMAIL")));
		return document.toString();
	}
	private String getEmailBodyListingSuccess(Map<String, Object> mailData, String lanCode) throws IOException {
		String filePath = "";
		if (lanCode.equalsIgnoreCase("en"))
			filePath = "/Templates/crypto" + "/subscription2.html";
//		if (lanCode.equalsIgnoreCase("es"))
//			filePath = "/Templates/crypto" + "/withdrawCon_sp.html";
//		if (lanCode.equalsIgnoreCase("gmn"))
//			filePath = "/Templates/crypto" + "/withdrawCon_gmn.html";

		// filePath = "/Templates/crypto" + "/withdraw_success.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
//		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
//		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
//		document.getElementById("datetime").appendText(new Date().toString());
//		document.getElementById("userName").appendText(String.valueOf(mailData.get("userName")));
//		document.getElementById("EMAIL").appendText(String.valueOf(sendMailMap.get("EMAIL")));
		return document.toString();
	}

	private String getEmailBodyCareerSuccess(Map<String, Object> mailData, String lanCode) throws IOException {
		String filePath = "";
		if (lanCode.equalsIgnoreCase("en"))
			filePath = "/Templates/crypto" + "/subscription1.html";
//		if (lanCode.equalsIgnoreCase("es"))
//			filePath = "/Templates/crypto" + "/withdrawCon_sp.html";
//		if (lanCode.equalsIgnoreCase("gmn"))
//			filePath = "/Templates/crypto" + "/withdrawCon_gmn.html";

		// filePath = "/Templates/crypto" + "/withdraw_success.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
//		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
//		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
//		document.getElementById("datetime").appendText(new Date().toString());
//		document.getElementById("userName").appendText(String.valueOf(mailData.get("userName")));
//		document.getElementById("EMAIL").appendText(String.valueOf(sendMailMap.get("EMAIL")));
		return document.toString();
	}

}