package com.mobiloitte.notification.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mobiloitte.notification.constants.EmailConstants;
import com.mobiloitte.notification.constants.MessageConstant;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.sendgrid.helpers.mail.objects.Personalization;

@Component
public class MailSender extends MessageConstant {

	private static final Logger LOGGER = LogManager.getLogger(MailSender.class);

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

	public final String uploadDirectory = System.getProperty("user.dir") + "\\uploads";

	// adding link with button on template and convert into as a string form using

	public static final String TAGSELFCLOSING = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)/\\>";
	public static final String TAGSTART = "\\<\\w+((\\s+\\w+(\\s*\\=\\s*(?:\".*?\"|'.*?'|[^'\"\\>\\s]+))?)+\\s*|\\s*)\\>";
	public static final String TAGEND = "\\</\\w+\\>";
	public static final String HTMLENTITY = "&[a-zA-Z][a-zA-Z0-9]+;";
	public final Pattern htmlPattern = Pattern.compile(
			"(" + TAGSTART + ".*" + TAGEND + ")|(" + TAGSELFCLOSING + ")|(" + HTMLENTITY + ")", Pattern.DOTALL);

	public boolean isHtml(String s) {
		boolean ret = false;
		if (s != null) {
			ret = htmlPattern.matcher(s).find();
		}
		return ret;
	}

	public boolean sendEmail(String messages, List<String> emailTo, String subject) {
		Boolean isValid = isHtml(subject);
		String subjectSecure = "";
		if (Boolean.TRUE.equals(isValid)) {
			return false;
		} else {
			subjectSecure = subject;
			Personalization personalization = new Personalization();
			emailTo.parallelStream().forEachOrdered(email -> personalization.addTo(new Email(email)));
			try {
				Content content = new Content("text/html", messages);
				Email from = new Email(fromAddress);
				Mail mail = new Mail();
				mail.setFrom(from);
				mail.setSubject(subjectSecure);
				mail.addPersonalization(personalization);
				mail.addContent(content);
				SendGrid sg = new SendGrid(sendGridApiKey);
				Request request = new Request();
				request.setMethod(Method.POST);
				request.setEndpoint("mail/send");
				request.setBody(mail.build());
				sg.api(request);

			} catch (IOException ex) {
				return false;
			}
		}
		return true;
	}

	// sending mail using SMTP
	public boolean sendEmailWithSMTP(String messages, List<String> emailTo, String subject) {

		Boolean isValid = isHtml(subject);
		String subjectSecure = "";
		if (Boolean.TRUE.equals(isValid) || subject.isEmpty()) {
			return false;
		} else {
			subjectSecure = subject;
			try {
				Properties props = System.getProperties();
				props.put(MAIL_TRANSPORT_PROTOCOL, "smtp");
				props.put(MAIL_SMTP_PORT, 587);
				props.put(MAIL_SMTP_STARTTLS_ENABLE, "true");
				props.put(MAIL_SMTP_AUTH, "true");

				Session session = Session.getDefaultInstance(props);

				for (String toEmail : emailTo) {
					MimeMessage msg = new MimeMessage(session);
					msg.setFrom(new InternetAddress(emailSender, "systems@cryptonomics.co.th"));
					msg.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
					msg.setSubject(subject);
					msg.setContent(messages, TEXT_HTML);
					Transport transport = session.getTransport();
					transport.connect(smtpServerHost, smtpUserName, smtpUserPassword);
					transport.sendMessage(msg, msg.getAllRecipients());
				}

			} catch (Exception ex) {
				LOGGER.debug(MAIL_NOT_SENT);
				return false;
			}

		}

		return true;
	}

	public boolean sendEmailWithoutEmailLoop(String messages, String emailTo, String subject) {

		Boolean isValid = isHtml(subject);
		String subjectSecure = "";
		if (Boolean.TRUE.equals(isValid) || subject.isEmpty()) {
			return false;
		} else {
			subjectSecure = subject;
			try {
				Email toEmail = new Email(emailTo);
				LOGGER.debug(messages);

				Content content = new Content("text/html", messages);
				Email from = new Email(fromAddress);
				Mail mail = new Mail(from, subjectSecure, toEmail, content);
				SendGrid sg = new SendGrid(sendGridApiKey);
				Request request = new Request();
				request.setMethod(Method.POST);
				request.setEndpoint("mail/send");
				request.setBody(mail.build());
				sg.api(request);
			} catch (IOException ex) {
				LOGGER.debug("mail not sent");
				LOGGER.debug(ex);
				return false;
			}
		}
		return true;
	}

	public String sendEmailTemplate(String messages, String subject) throws IOException {

		String filePath = TEMPLATES + projectName + EmailConstants.EMAIL;
		org.jsoup.nodes.Document document = Jsoup.parse(MailSender.class.getResourceAsStream(filePath), UTF_8, "");
		document.getElementById(MAIL_BODY).html(messages);
		document.getElementById(SUBJECT).html(subject);
		return document.html();
	}

}
