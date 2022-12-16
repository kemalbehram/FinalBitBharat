package com.mobiloitte.usermanagement.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;

@Component
public class CloudinaryUtil {

	private static final Logger LOGGER = LogManager.getLogger(CloudinaryUtil.class);

	@Autowired
	private CloudinaryConfig config;

	public String uploadFile(MultipartFile multipartFile) {
		String url = null;
		if (null != multipartFile) {
			Map<String, Object> map = new HashMap<>();
			map.put("cloud_name", config.getCloudName());
			map.put("api_key", config.getApiKey());
			map.put("api_secret", config.getApiSecret());
			String filename = multipartFile.getOriginalFilename();
			String extension = filename.substring(filename.lastIndexOf('.') + 1);
			try {
				File file = File.createTempFile("img", extension);
				multipartFile.transferTo(file);
				Cloudinary cloudinary = new Cloudinary(map);
				Map<?, ?> result = null;
				Map<String, Object> request = new HashMap<>();
				request.put("resource_type", "auto");
				result = cloudinary.uploader().upload(file, request);
				url = (String) result.get("secure_url");
			} catch (IllegalStateException | IOException e1) {
				LOGGER.debug("file not found");
			}

		}

		return url;
	}

	public String uploadFile(String base64ImageData) {
		String url = null;
		if (null != base64ImageData) {
			Map<String, Object> map = new HashMap<>();
			map.put("cloud_name", config.getCloudName());
			map.put("api_key", config.getApiKey());
			map.put("api_secret", config.getApiSecret());
			try {
				Map<?, ?> result = null;
				Cloudinary cloudinary = new Cloudinary(map);
				Map<String, Object> request = new HashMap<>();
				request.put("resource_type", "auto");
				result = cloudinary.uploader().upload(base64ImageData, request);
				url = (String) result.get("secure_url");
			} catch (IOException e) {
				LOGGER.catching(e);
			}

		}

		return url;
	}

}
