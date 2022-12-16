package com.mobiloitte.usermanagement.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class FcmNotificationSender {
	
	public Boolean callKycFcmNotify(String fcmKey, String body)
	{
		Map<String, Object> notification = new HashMap<>();
		notification.put("title", "Kryptoro");
		notification.put("body", body);
		notification.put("sound", "assets/tone/fcm_tone.mp3");
		notification.put("click_action", "FCM_PLUGIN_ACTIVITY");
		notification.put("icon", "assets/img/kto_push_icon.png");
		
		Map<String, Object> data = new HashMap<>();
		data.put("param1", "value1");
		data.put("param2", "value2");
		
		Map<String, Object> entity = new HashMap<>();
		entity.put("notification", notification);
		entity.put("data", data);
		entity.put("to", fcmKey);
		entity.put("priority", "high");
		entity.put("restricted_package_name", "");

		try {
			String apiBaseUri ="https://fcm.googleapis.com/fcm/send";
			executePostRequestsForFCMNotifyKyc(apiBaseUri, 10*1000, entity);
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	public static String executePostRequestsForFCMNotifyKyc(String apiUri,int timeout,Map<String,Object> entity){
		try {
			HttpClient client = HttpClients.createDefault();
			URIBuilder builder = new URIBuilder(apiUri);
			String uri = builder.build().toString();
			HttpPost method = new HttpPost(uri);
			method.setHeader("Content-Type", "application/json");
			method.setHeader("Authorization", "key=AIzaSyAS12aIQNsm2kI_58ibzjQHJqzMl-N3cX4");
			RequestConfig requestConfig = RequestConfig.custom()
					  .setSocketTimeout(timeout)
					  .setConnectTimeout(timeout)
					  .setConnectionRequestTimeout(timeout)
					  .build();
			method.setConfig(requestConfig);
			 String jsonString = getJsonStringByMap(entity);
			method.setEntity(new StringEntity(jsonString));
			HttpResponse response = client.execute(method);
			
			HttpEntity entity1 = response.getEntity();
		return EntityUtils.toString(entity1, "UTF-8");
		}catch (Exception e) {
			return null;
		}
	}
	
	public static String getJsonStringByMap(Map<String,Object> inputMap)
	{
		ObjectMapper mapperObj = new ObjectMapper();
		String jsonResp = null;
		try {
			jsonResp = mapperObj.writeValueAsString(inputMap);
		} catch (IOException e) {
		}
		return jsonResp;
	}

}
