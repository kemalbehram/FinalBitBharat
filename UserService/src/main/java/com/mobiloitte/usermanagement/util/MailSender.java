package com.mobiloitte.usermanagement.util;

import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

//import com.mobiloitte.content.utils.MailSender;
import com.mobiloitte.usermanagement.constants.EmailConstants;
//import com.mobiloitte.content.constants.EmailConstants
import com.mobiloitte.usermanagement.constants.MessageConstant;
import com.mobiloitte.usermanagement.dto.KycEmailAlertDto;
import com.mobiloitte.usermanagement.dto.LoginDto;
import com.mobiloitte.usermanagement.emailservice.UserManagementEmailConstants;
import com.sendgrid.Content;
import com.sendgrid.Email;
import com.sendgrid.Mail;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;

@Component
public class MailSender extends MessageConstant {

	private static final Logger LOGGER = LogManager.getLogger(MailSender.class);

	private static final String TEMPLATE = "/Templates/";
	private static final String UTF8 = "UTF-8";
	private static final String TEXTHTML = "text/html";
	private static final String MAILSEND = "mail/send";
	private static final String MAILPROTOCOL = "mail.transport.protocol";
	private static final String MAILNOTSEND = "mail not sent";
	private static final String SMTPPORT = "mail.smtp.port";
	private static final String SMTPSTARTTLSENABLE = "mail.smtp.starttls.enable";
	private static final String SMTPAUTH = "mail.smtp.auth";

	public static final String SEND_FROM = "sendFrom";

	public static final String DESC = "desc";

	public static final String SEND_TO = "emailTo";

	public static final String EMAIL_SUBJECT = "subjectOf";

	public static final String OTP = "otp";

	@Value("${spring.project.name}")
	private String projectName;

	@Value("${sendGrid.Api.Key}")
	private String sendGridApiKey;

	@Value("${sendGrid.Email.From}")
	private String fromAddress;

	@Value("${host}")
	private String smtpServerHost;

	@Value("${smtp.username}")
	private String smtpUserName;

	@Value("${smtp.password}")
	private String smtpUserPassword;

	@Value("${email.sender}")
	private String emailSender;

	public String getEmailBodyForResetLinkForgetPassword(String resetLink, String email) throws IOException {
		String filePath = "/Templates/" + projectName + UserManagementEmailConstants.FORGETPASSWORDTEMPLATE;
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("url").attr("href", resetLink);
		document.getElementById("email").appendText(email);
		return document.toString();
	}

	public Boolean sendMailToFeedbackSubmissionSuccess(Map<String, Object> sendMailMap, String langCode) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			String message = getEmailBodySubscriptionSuccess(sendMailMap, langCode);
			Content content = new Content("text/html", message);
			Email from = new Email(fromAddress);
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

	public String getEmailBodyForUserverifyEmail(String resetLink, String email) throws IOException {
		String filePath = "/Templates/" + projectName + UserManagementEmailConstants.VERIFYUSERTEMPLATE;
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("otp").appendText(resetLink);
		document.getElementById("email").appendText(email);
		return document.toString();
	}

	public String getEmailBodyForverifyIpAddress(String resetLink, String ipAddress) throws IOException {
		String filePath = "/Templates/" + projectName + UserManagementEmailConstants.VERIFYIPADDRESSTEMPLATE;
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("webUrl").attr("href", resetLink);
		document.getElementById("ipAddress").appendText(ipAddress);
		return document.toString();
	}

	public String getLoginDataForLoginAlert(LoginDto loginDto) throws IOException {
		String filePath = "/Templates/" + projectName + UserManagementEmailConstants.LOGINALERT;
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("getIpAddress").appendText(loginDto.getIpAddress());
		document.getElementById("Browser").appendText(loginDto.getUserAgent());
		String date = String.valueOf(loginDto.getCreateTime());
		document.getElementById("Time").appendText(date);

		return document.toString();
	}

	public String getEmailBodyForWelcomeEmail() throws IOException {
		String filePath = "/Templates/" + projectName + UserManagementEmailConstants.WELCOMEALERT;
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		return document.toString();
	}

	public String getKycAlertTemplate(KycEmailAlertDto kycEmailAlert) throws IOException {
		String filePath = "/Templates/" + projectName + "/kyc_status_alert.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("docname").appendText(kycEmailAlert.getDocName());
		document.getElementById("status").appendText(kycEmailAlert.getStatus());
		document.getElementById("datetime").appendText(new Date().toString());
		return document.toString();
	}

	public boolean sendResetLinkForgetPassword(String emailTo, String subjectOf, String resetLink, String userName) {
		Email to = new Email(emailTo);
		try {
			String message = getEmailBodyForResetLinkForgetPassword(resetLink, userName);
			Content content = new Content("text/html", message);
			Email from = new Email(fromAddress);
			Mail mail = new Mail(from, subjectOf + "  @" + new Date(), to, content);
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

	public Boolean sendMailToApproveSubmissionSuccess(Map<String, Object> sendMailMap, String langCode) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			String message = getEmailBodyApproveSubscriptionSuccess(sendMailMap, langCode);
			Content content = new Content("text/html", message);
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

	public Boolean sendMailTorEJECTSubmissionSuccess(Map<String, Object> sendMailMap, String langCode) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			String message = getEmailBodyRejectSubscriptionSuccess(sendMailMap, langCode);
			Content content = new Content("text/html", message);
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
	
	public Boolean susspenedUser(Map<String, Object> sendMailMap, String langCode) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			String message = suspendUser(sendMailMap, langCode);
			Content content = new Content("text/html", message);
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
	
	public Boolean unsuspend(Map<String, Object> sendMailMap, String langCode) {
		Email to = new Email(String.valueOf(sendMailMap.get(EmailConstants.EMAIL_TO)));
		try {
			String message = unsuspended(sendMailMap, langCode);
			Content content = new Content("text/html", message);
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

	private String getEmailBodyApproveSubscriptionSuccess(Map<String, Object> mailData, String lanCode)
			throws IOException {
		String filePath = "";
		if (lanCode.equalsIgnoreCase("en"))
			filePath = "/Templates/Crypto" + "/subscription.html";

		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");

		return document.toString();
	}

	private String getEmailBodyRejectSubscriptionSuccess(Map<String, Object> mailData, String lanCode)
			throws IOException {
		String filePath = "";
		if (lanCode.equalsIgnoreCase("en"))
			filePath = "/Templates/Crypto" + "/subscription2.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");

		return document.toString();
	}
	private String suspendUser(Map<String, Object> mailData, String lanCode)
			throws IOException {
		String filePath = "";
		if (lanCode.equalsIgnoreCase("en"))
			filePath = "/Templates/Crypto" + "/suspend.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");

		return document.toString();
	}
	
	private String unsuspended(Map<String, Object> mailData, String lanCode)
			throws IOException {
		String filePath = "";
		if (lanCode.equalsIgnoreCase("en"))
			filePath = "/Templates/Crypto" + "/Unsuspend.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");

		return document.toString();
	}

	public boolean sendEmailIpAddressVerification(String emailTo, String subjectOf, String resetLink,
			String ipAddress) {
		Email to = new Email(emailTo);
		try {
			String message = getEmailBodyForverifyIpAddress(resetLink, ipAddress);
			Content content = new Content("text/html", message);
			Email from = new Email(fromAddress);
			Mail mail = new Mail(from, subjectOf + "  @" + new Date(), to, content);
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

	public boolean sendResetLinkVerifyUser(String emailTo, String subjectOf, String verifyLink, String userName) {
		Email to = new Email(emailTo);
		try {

			String message = getEmailBodyForUserverifyEmail(verifyLink, userName);
			Content content = new Content("text/html", message);
			Email from = new Email(fromAddress);
			Mail mail = new Mail(from, subjectOf + "  @" + new Date(), to, content);
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
			filePath = "/Templates/Crypto" + "/feedback.html";

		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");

		return document.toString();
	}

	public boolean sendResetLinkForgetPasswordWithSMTP(String emailTo, String subjectOf, String resetLink) {
		try {
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", 587);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props);
			String message = getEmailBodyForResetLinkForgetPassword(resetLink, emailTo);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(emailSender, "harold"));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
			msg.setSubject(subjectOf);
			msg.setContent(message, "text/html");

			Transport transport = session.getTransport();
			transport.connect(smtpServerHost, smtpUserName, smtpUserPassword);
			transport.sendMessage(msg, msg.getAllRecipients());
		} catch (Exception ex) {
			LOGGER.debug("mail not sent");
			return false;
		}

		return true;

	}

	public boolean sendResetLinkVerifyUserWithSMTP(String emailTo, String subjectOf, String verifyLink) {
		try {
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", 587);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props);
			String message = getEmailBodyForUserverifyEmail(verifyLink, emailTo);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(emailSender, "harold"));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
			msg.setSubject(subjectOf);
			msg.setContent(message, "text/html");

			Transport transport = session.getTransport();
			transport.connect(smtpServerHost, smtpUserName, smtpUserPassword);
			transport.sendMessage(msg, msg.getAllRecipients());
		} catch (Exception ex) {
			LOGGER.debug("mail not sent");
			return false;
		}

		return true;

	}

	private String getEmailBodyForPaymentRejectEmail(String adminMessage, String emailTo) throws IOException {
		String filePath = "/Templates/" + "kryptoro" + UserManagementEmailConstants.PAYMENTREJECTEMAIL;
		org.jsoup.nodes.Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("adminMessage").appendText(adminMessage);
		return document.toString();
	}

	public boolean sendPaymentRejectEmail(String emailTo, String subjectOf, String adminMessage) {
		try {
			Properties props = System.getProperties();
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.port", 587);
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.auth", "true");
			Session session = Session.getDefaultInstance(props);
			String message = getEmailBodyForPaymentRejectEmail(adminMessage, emailTo);
			MimeMessage msg = new MimeMessage(session);
			msg.setFrom(new InternetAddress(emailSender, "kryptoro"));
			msg.setRecipient(Message.RecipientType.TO, new InternetAddress(emailTo));
			msg.setSubject(subjectOf);
			msg.setContent(message, "text/html");

			Transport transport = session.getTransport();
			transport.connect(smtpServerHost, smtpUserName, smtpUserPassword);
			transport.sendMessage(msg, msg.getAllRecipients());
		} catch (Exception ex) {
			LOGGER.debug("mail not sent");
			return false;
		}

		return true;

	}

	public boolean sendKycAlertMail(KycEmailAlertDto kycEmailAlert, String subjectOf) {
		Email to = new Email(kycEmailAlert.getUserEmail());
		try {
			String message = getKycAlertTemplate(kycEmailAlert);
			Content content = new Content("text/html", message);
			Email from = new Email(fromAddress);
			Mail mail = new Mail(from, subjectOf + "  @" + new Date(), to, content);
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

	public boolean sendWelcomeMail(String emailTo, String subjectOf, String userName) {
		Email to = new Email(emailTo);
		try {
			String message = getEmailBodyForWelcomeEmail();
			Content content = new Content("text/html", message);
			Email from = new Email(fromAddress);
			Mail mail = new Mail(from, subjectOf + "  @" + new Date(), to, content);
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

	public boolean sendMailContactUs(Map<String, Object> sendMailMap) {
		Email to = new Email(String.valueOf(sendMailMap.get(SEND_TO)));
		try {
			String message = getEmailBody(sendMailMap);
			Content content = new Content(TEXTHTML, message);
			Email from = new Email(fromAddress);
			Mail mail = new Mail(from, String.valueOf(sendMailMap.get(EMAIL_SUBJECT)), to, content);
			SendGrid sg = new SendGrid(sendGridApiKey);
			Request request = new Request();
			request.setMethod(Method.POST);
			request.setEndpoint(MAILSEND);
			request.setBody(mail.build());
			sg.api(request);
		} catch (IOException ex) {
			LOGGER.debug("Mail not sent, some exception occured");
			return false;
		}
		return true;
	}
	public String getEmailBody(Map<String, Object> sendMailMap) throws IOException {
		String filePath = "/Templates/" + "Crypto" + "/forget-Pass.html";
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		document.getElementById("otp").appendText(String.valueOf(sendMailMap.get(OTP)));
		return document.toString();
	}

	public Boolean sendEailAfterBlockUser(String email, String subject) {
		Email to = new Email(email);
		try {
			String message = getEmailBodyForBlockedUser(email);
			Content content = new Content("text/html", message);
			Email from = new Email(fromAddress);
			Mail mail = new Mail(from, subject + "  @" + new Date(), to, content);
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
	public Boolean sendEailAfterBlockUser1(String email, String subject) {
		Email to = new Email(email);
		try {
			String message = getEmailBodyForBlockedUser1(email);
			Content content = new Content("text/html", message);
			Email from = new Email(fromAddress);
			Mail mail = new Mail(from, subject + "  @" + new Date(), to, content);
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

	public String getEmailBodyForBlockedUser(String email) throws IOException {
		String filePath = "/Templates/" + projectName + UserManagementEmailConstants.BLOCKEDUSER;
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		return document.toString();
	}
	public String getEmailBodyForBlockedUser1(String email) throws IOException {
		String filePath = "/Templates/" + projectName + UserManagementEmailConstants.BLOCKEDUSER;
		Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), "UTF-8", "");
		return document.toString();
	}
}