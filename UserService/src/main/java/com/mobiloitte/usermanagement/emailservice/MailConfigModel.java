package com.mobiloitte.usermanagement.emailservice;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Arvind Kumar
 *
 */
public class MailConfigModel {
	private String host;
	private String port;
	private String channel;
	private String password;
	private String username;
	private String contentType;

	private static MailConfigModel instance = null;
	private static final Logger LOGGER = LogManager.getLogger(MailConfigModel.class);

	protected MailConfigModel() {
		try {
			this.channel = "TLS";
			this.contentType = "text/html";
			this.port = "587";
			this.host = "smtp.sendgrid.net";
			this.username = "AKIAICF3IUN4WB3US6LQ";

		} catch (Exception e) {
				LOGGER.debug("config error");
				}
	}

	public static MailConfigModel getInstance() {
		if (instance == null) {
			instance = new MailConfigModel();
		}
		return instance;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}