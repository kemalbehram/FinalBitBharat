package com.mobiloitte.p2p.content.utils;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiloitte.p2p.content.constants.OtherConstants;

/**
 * The Class APIUtils.
 * @author Ankush Mohapatra
 */
@Component
public class APIUtils implements OtherConstants{
	
	/**
	 * Instantiates a new API utils.
	 */
	protected APIUtils() {
	}
	
	/** The Constant LOGGER. */
	private static final Logger LOGGER = LogManager.getLogger(APIUtils.class);

	/**
	 * Extract get API data.
	 *
	 * @param apiUri the api uri
	 * @param timeout the timeout
	 * @return the string
	 */
	public static String extractGetAPIData(String apiUri,int timeout){
		try {
		HttpClient client = HttpClients.createDefault();
		URIBuilder builder = new URIBuilder(apiUri);
		String uri = builder.build().toString();
		HttpGet method = new HttpGet(uri);
		RequestConfig requestConfig = RequestConfig.custom()
				  .setSocketTimeout(timeout)
				  .setConnectTimeout(timeout)
				  .setConnectionRequestTimeout(timeout)
				  .build();
		method.setConfig(requestConfig);
		HttpResponse response = client.execute(method);
		HttpEntity entity = response.getEntity();
		return EntityUtils.toString(entity, UTF_8);
		}catch (Exception e) {
			LOGGER.catching(e);
			return null;
		}
	}
	
	/**
	 * Extract post API data.
	 *
	 * @param apiUri the api uri
	 * @param timeout the timeout
	 * @param entity the entity
	 * @return the string
	 */
	public static String extractPostAPIData(String apiUri,int timeout,HttpEntity entity){
		try {
			HttpClient client = HttpClients.createDefault();
			URIBuilder builder = new URIBuilder(apiUri);
			String uri = builder.build().toString();
			HttpPost method = new HttpPost(uri);
			RequestConfig requestConfig = RequestConfig.custom()
					  .setSocketTimeout(timeout)
					  .setConnectTimeout(timeout)
					  .setConnectionRequestTimeout(timeout)
					  .build();
			method.setConfig(requestConfig);
			method.setEntity(entity);
			HttpResponse response = client.execute(method);
			
			HttpEntity entity1 = response.getEntity();
		return EntityUtils.toString(entity1, UTF_8);
		}catch (Exception e) {
			LOGGER.catching(e);	
			return null;
		}
	}
	
	/**
	 * Gets the java object from json string.
	 *
	 * @param <T> the generic type
	 * @param jsonString the json string
	 * @param class1 the class 1
	 * @return the java object from json string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static <T> T getJavaObjectFromJsonString(String jsonString, Class<T> class1) throws IOException 
	{
		return new ObjectMapper().readValue(jsonString, class1);
	}

	
}
