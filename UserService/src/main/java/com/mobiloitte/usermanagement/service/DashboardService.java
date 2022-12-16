package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.model.Response;

public interface DashboardService {

	public Response<Object> getTotalUserCount();

	public Response<Object> getTotalUserCountByStatus();

	public Response<Object> getPendingKycCuont();

	public Response<Object> dashboardCountApi();

	public Response<Object> getTotalKycCount();

}