package com.mobiloitte.server.apigateway.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("security.url.mappings")
@Component
public class UrlMappings {
	private List<String> user;
	private List<String> admin;
	private List<String> subAdmin;
	private List<String> guest;
	private List<String> none;
	private List<String> developer;

	public List<String> getUser() {
		return user;
	}

	public void setUser(List<String> user) {
		this.user = user;
	}

	public List<String> getAdmin() {
		return admin;
	}

	public void setAdmin(List<String> admin) {
		this.admin = admin;
	}

	public List<String> getGuest() {
		return guest;
	}

	public void setGuest(List<String> guest) {
		this.guest = guest;
	}

	public List<String> getNone() {
		return none;
	}

	public void setNone(List<String> none) {
		this.none = none;
	}

	public List<String> getDeveloper() {
		return developer;
	}

	public void setDeveloper(List<String> developer) {
		this.developer = developer;
	}

	public List<String> getSubAdmin() {
		return subAdmin;
	}

	public void setSubAdmin(List<String> subAdmin) {
		this.subAdmin = subAdmin;
	}

}
