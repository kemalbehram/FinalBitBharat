package com.mobiloitte.usermanagement.util;

import com.mobiloitte.usermanagement.dto.ActivityLogDto;

public class ActivityMainClass {

	private ActivityLogDto activityLogDto;
	private ActivityLogDto activityLogDtos;

	public ActivityMainClass(ActivityLogDto activityLogDto, ActivityLogDto activityLogDtos) {
		super();
		this.activityLogDto = activityLogDto;
		this.activityLogDtos = activityLogDtos;
	}

	public ActivityLogDto getActivityLogDto() {
		return activityLogDto;
	}

	public void setActivityLogDto(ActivityLogDto activityLogDto) {
		this.activityLogDto = activityLogDto;
	}

	public ActivityLogDto getActivityLogDtos() {
		return activityLogDtos;
	}

	public void setActivityLogDtos(ActivityLogDto activityLogDtos) {
		this.activityLogDtos = activityLogDtos;
	}
}
