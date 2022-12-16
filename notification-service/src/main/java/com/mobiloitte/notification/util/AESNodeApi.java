package com.mobiloitte.notification.util;

import java.io.IOException;
import java.net.URI;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiloitte.notification.exception.UserNotFoundException;

@Component
public class AESNodeApi {

	@Value("${aes.url}")
	private String aesUrl;
	private static final String BREAK_MESSAGE = "OOPs, our server is on break !";

	@Nullable
	public String encrypt(Object data) {
		final String baseUrl = aesUrl + "/encrypt";
		URI uri = null;
		try {
			uri = new URI(baseUrl);
			return new RestTemplate().postForObject(uri, data, String.class);
		} catch (Exception ex) {
			throw new UserNotFoundException(BREAK_MESSAGE);
		}
	}

	public String decrypt(Object data) {
		final String baseUrl = aesUrl + "/decrypt";
		URI uri = null;
		try {
			uri = new URI(baseUrl);
			return new RestTemplate().postForObject(uri, data, String.class);
		} catch (Exception ex) {
			throw new UserNotFoundException(BREAK_MESSAGE);
		}
	}

	public String decryptObject(Object data) {
		final String baseUrl = aesUrl + "/decryptAM";
		URI uri = null;
		try {
			uri = new URI(baseUrl);
			return new RestTemplate().postForObject(uri, data, String.class);
		} catch (Exception ex) {
			throw new UserNotFoundException(BREAK_MESSAGE);
		}
	}

	public String encryptObject(Object data) {
		final String baseUrl = aesUrl + "/encryptAM";
		URI uri = null;
		try {
			uri = new URI(baseUrl);
			return new RestTemplate().postForObject(uri, data, String.class);
		} catch (Exception ex) {
			throw new UserNotFoundException(BREAK_MESSAGE);
		}
	}

	public static <T> T getJavaObjectFromJsonString(String jsonString, Class<T> class1) throws IOException {
		return new ObjectMapper().readValue(jsonString, class1);
	}

}