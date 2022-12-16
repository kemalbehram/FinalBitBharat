package com.mobiloitte.content.fiegn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.mobiloitte.content.dto.ActivityLogDto;
import com.mobiloitte.content.model.Response;

@FeignClient("${exchange.application.activity-service}")
public interface ActivityClient {
	@PostMapping("/save-activity-logs")
	public Response<Object> saveActivityLog(@RequestBody ActivityLogDto activityLogDto);

}
