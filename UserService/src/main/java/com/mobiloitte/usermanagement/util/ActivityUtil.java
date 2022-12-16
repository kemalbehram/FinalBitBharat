package com.mobiloitte.usermanagement.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.mobiloitte.usermanagement.feign.ActivityClient;

@Component
public class ActivityUtil {
	
	private static final Logger LOGGER = LogManager.getLogger(ActivityUtil.class);

	@Autowired
	private ActivityClient activityClient;

	public Boolean isActivitySaved(ActivityMainClass activityMainClass)
	{
		try {
			activityClient.saveActivityLogs(activityMainClass);
			return true;
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			return false;
		}
	}
}
