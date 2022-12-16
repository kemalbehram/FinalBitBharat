package com.mobiloitte.microservice.wallet.utils;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mobiloitte.microservice.wallet.constants.EmailConstants;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

@Component
public class MailSender {

	private static final Logger LOGGER = LogManager.getLogger(MailSender.class);

	@Value("${host}")
	private String smtpServerHost;

	@Value("${smtp.username}")
	private String smtpUserName;

	@Value("${spring.project.name}")
	private String projectName;

	@Value("${smtp.password}")
	private String smtpUserPassword;

	@Value("${email.sender}")
	private String emailSender;

	@Value("${sendGrid.Api.Key}")
	private String sendGridApiKey;

	public static String getEmailBody(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/withdraw_request.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		document.getElementById("approveUrl").attr("href", String.valueOf(mailData.get("approveUrl")));
		document.getElementById("rejectUrl").attr("href", String.valueOf(mailData.get("rejectUrl")));
		return document.toString();
	}

	public static String getEmailBodyForDepositConfirmation(Map<String, String> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/deposit_alert.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(mailData.get("amount"));
		document.getElementById("coinname").appendText(mailData.get("coinname"));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}

	public static String getEmailBodyForLowStorageBalanceAlert(Map<String, String> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/low_balance_alert.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(mailData.get("amount"));
		document.getElementById("coinname").appendText(mailData.get("coinname"));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}

	public static String getStorageIssueAlert(String mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/storage_issue_alert.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("message").appendText(mailData);
		return document.toString();
	}

	public static String getEmailBodyWithdrawSuccess1(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/internal_transfer.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}

	public static String getEmailBodyWithdrawSuccess2(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/internal_transfer_receiver.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}

	public static String getEmailBodyWithdrawSuccess(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/withdraw_success.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}

	public static String getEmailBodyWithdrawSuccess3(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/depositinr.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}
	public static String getEmailBodyWithdrawSuccess4(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/withdrawinr.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}
	public static String getEmailBodyWithdrawSuccess7(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/withdrawinrsuccess.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}
	public static String getEmailBodyWithdrawSuccess8(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/withdrawinrrejected.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}
	public static String getEmailBodyWithdrawSuccess5(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/depositinrsuccess.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}
	public static String getEmailBodyWithdrawSuccess6(Map<String, Object> mailData) throws IOException {
		String filePath = "/Templates/crypto" + "/depositinrrejected.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("amount").appendText(String.valueOf(mailData.get("amount")));
		document.getElementById("coinType").appendText(String.valueOf(mailData.get("coinType")));
		document.getElementById("toAddress").appendText(String.valueOf(mailData.get("toAddress")));
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}
	/*
	 * public static String getEmailBody(Map<String, Object> sendMailMap){ return
	 * "<html> <body> <p> Dear, <b>"+sendMailMap.get("userName")
	 * +"</b> We have sent you this email in response to your request to withdraw "
	 * +sendMailMap.get("amount")+" "+sendMailMap.get("coinType")+" to "+sendMailMap
	 * .get("toAddress")+"<br>"+
	 * "Ensure the details you have submitted above are accurate, as Kryptoro cannot be held liable for incorrect details provided."
	 * +"</p> </body> </html>";
	 * 
	 * }
	 */

	// sending mail using SMTP
	// AWS SMTP
	/*
	 * public boolean sendMailToApproveWithdraw(Map<String, Object> sendMailMap) {
	 * try { Properties props = System.getProperties();
	 * props.put("mail.transport.protocol", "smtp"); props.put("mail.smtp.port",
	 * 587); props.put("mail.smtp.starttls.enable", "true");
	 * props.put("mail.smtp.auth", "true"); Session session =
	 * Session.getDefaultInstance(props); String message =
	 * getEmailBody(sendMailMap); MimeMessage msg = new MimeMessage(session);
	 * msg.setFrom(new InternetAddress(emailSender, "bittrade"));
	 * msg.setRecipient(Message.RecipientType.TO, new
	 * InternetAddress(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO))));
	 * msg.setSubject(String.valueOf(sendMailMap.get(EmailConstants.SUBJECT_OF)));
	 * msg.setContent(message, "text/html");
	 * 
	 * Transport transport = session.getTransport();
	 * transport.connect(smtpServerHost, smtpUserName, smtpUserPassword);
	 * transport.sendMessage(msg, msg.getAllRecipients()); } catch (Exception ex) {
	 * LOGGER.debug("mail not sent "+ex.getMessage()); return false; }
	 * 
	 * return true;
	 * 
	 * }
	 */

	public boolean sendMailToApproveWithdraw(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBody(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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

	public boolean sendMailForDepositConfirmation(Map<String, String> sendMailMap) {
		Email to = new Email(sendMailMap.get(EmailConstants.EMAIL_TO));
		try {
			// for template
			String message = getEmailBodyForDepositConfirmation(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
			Mail mail = new Mail(from, sendMailMap.get(EmailConstants.SUBJECT_OF) + "  @" + new Date(), to, content);
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

	public boolean sendMailForLowStorageBalanceAlert(Map<String, String> sendMailMap) {
		Email to = new Email(sendMailMap.get(EmailConstants.EMAIL_TO));
		try {
			// for template
			String message = getEmailBodyForLowStorageBalanceAlert(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
			Mail mail = new Mail(from, sendMailMap.get(EmailConstants.SUBJECT_OF), to, content);
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

	public boolean sendMailForStorageIssueAlert(String sendMailMap, String toMail) {
		Email to = new Email(toMail);
		try {
			// for template
			String message = getStorageIssueAlert(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
			Mail mail = new Mail(from, "Storage Issue Alert", to, content);
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

	public boolean sendMailToApproveWithdrawSuccess(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyWithdrawSuccess(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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

	public boolean sendMailToApproveWithdrawSuccess3(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyWithdrawSuccess4(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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
	public boolean sendMailToApproveWithdrawSuccess5(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyWithdrawSuccess5(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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
	public boolean sendMailToApproveWithdrawSuccess6(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyWithdrawSuccess6(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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
	public boolean sendMailToApproveWithdrawSuccess4(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyWithdrawSuccess4(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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
	public boolean sendMailToApproveWithdrawSuccess7(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyWithdrawSuccess7(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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
	public boolean sendMailToApproveWithdrawSuccess8(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyWithdrawSuccess8(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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
	public boolean sendMailToApproveWithdrawSuccess1(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyWithdrawSuccess1(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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

	public boolean sendMailToApproveWithdrawSuccess2(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			// for template
			String message = getEmailBodyWithdrawSuccess2(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(emailSender);
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

	public String sendMailforBulkPurchase(Map<String, Object> sendMailMap) {

		String emailbody = "<html>" + "<body>" + "<div style='padding: 10px 15px;'><b>Email Id: </b>"
				+ sendMailMap.get("EMAIL") + "</div>" + "<div style='padding: 10px 15px;'><b>Name Of User: </b>"
				+ sendMailMap.get("NAME") + "</div>" + "<div style='padding: 10px 15px;'><b>Name Of Country: </b>"
				+ sendMailMap.get("COUNTRYNAME") + "</div>" + "<div style='padding: 10px 15px;'><b>Coin Details: : </b>"
				+ sendMailMap.get("COIN") + "</div>" + "<div style='padding: 10px 15px;'><b>Message From User: </b>"
				+ sendMailMap.get("MESSAGE") + "</div>" + "<div style='padding: 10px 15px;'><b>Mode Of Payment : </b>"
				+ sendMailMap.get("PAYMENTMODE") + "</div>" + "<div style='padding: 10px 15px;'><b>Phone Number: </b>"
				+ sendMailMap.get("PHONE") + "</div>" + "<div style='padding: 10px 15px;'><b>Quantity: </b>"
				+ sendMailMap.get("QUANTITY") + "</div>" + "<div style='padding: 10px 15px;'><b>State: </b>"
				+ sendMailMap.get("STATE") + "</div>"
				+ "<br><br><div style='padding: 10px 15px;'><b>Thanks & Regards</b><br>" + sendMailMap.get("NAME")
				+ "</div>" + "</body>" + "</html>";

		return emailbody;

	}

	public boolean sendMailSenderforBulkPurchase(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get("TOMail")));
		try {
			String message = sendMailforBulkPurchase(sendMailMap);
			Content content = new Content("text/html", message);
			Email from = new Email(String.valueOf(sendMailMap.get("EMAIL")));
			Mail mail = new Mail(from, String.valueOf(sendMailMap.get("SUBJECT")), to, content);
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
	/*
	 * public void sendMailForCoinPurchaseRequest(BulkPurchaseDto bulkPurchaseDto) {
	 * Email to = new
	 * Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO))); try { // for
	 * template String message = getEmailBody(sendMailMap); Content content = new
	 * Content("text/html", message); Email from = new Email(emailSender); Mail mail
	 * = new Mail(from, String.valueOf(sendMailMap.get(EmailConstants.SUBJECT_OF)) +
	 * "  @" + new Date(), to, content); SendGrid sg = new SendGrid(sendGridApiKey);
	 * Request request = new Request(); request.setMethod(Method.POST);
	 * request.setEndpoint("mail/send"); request.setBody(mail.build());
	 * sg.api(request); } catch (IOException ex) { LOGGER.debug("mail not sent");
	 * return false; } return true;
	 * 
	 * 
	 * }
	 */

}