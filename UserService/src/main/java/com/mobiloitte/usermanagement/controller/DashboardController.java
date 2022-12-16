package com.mobiloitte.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.DashboardService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/admin/dashboard")
@Api(value = "API for Admin Dashboard")
public class DashboardController {

	@Autowired
	private DashboardService dashboardService;

	@ApiOperation(value = "API to get total registered user count")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@GetMapping("/totalUserCount")
	public Response<Object> getTotalUserCount() {
		return dashboardService.getTotalUserCount();
	}

	@ApiOperation(value = "API to get user count by user status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@GetMapping("/getUserCountByStatus")
	public Response<Object> getUserCountByStatus() {
		return dashboardService.getTotalUserCountByStatus();
	}

	@ApiOperation(value = "API to get kyc pending count")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@GetMapping("/pendingKycCount")
	public Response<Object> getPendingKycCount() {
		return dashboardService.getPendingKycCuont();
	}

	@ApiOperation(value = "API to get dashboard count api")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@GetMapping("/dashboardApi")
	public Response<Object> dashboardCountApi(@RequestHeader Long userId) {
		return dashboardService.dashboardCountApi();
	}
	
	@GetMapping("/total-kyc-count")
	public Response<Object> getTotalKycCount() {
		return dashboardService.getTotalKycCount();
	}

}