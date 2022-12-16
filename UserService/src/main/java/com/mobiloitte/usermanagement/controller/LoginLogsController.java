package com.mobiloitte.usermanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.LoginLogsService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/admin/logs")
public class LoginLogsController {

	@Autowired
	private LoginLogsService loginLogsService;

	@ApiOperation(value = "API to search and filter all admin login details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/search-all-admin-login-details")
	public Response<Object> searchAndFilterAllAdminLoginDetails(@RequestHeader Long userId,
			@RequestParam(required = false) Long fromDate, @RequestParam(required = false) Long toDate,
			@RequestParam(required = false) String search, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		return loginLogsService.searchAndFilterAllAdminLoginDetails(userId, fromDate, toDate, search, page, pageSize);
	}

	@ApiOperation(value = "API to search and filter all staff login details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/search-all-staff-login-details")
	public Response<Object> searchAndFilterAllStaffLoginDetails(@RequestHeader Long userId,
			@RequestParam(required = false) Long fromDate, @RequestParam(required = false) Long toDate,
			@RequestParam(required = false) String search, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		return loginLogsService.searchAndFilterAllStaffLoginDetails(userId, fromDate, toDate, search, page, pageSize);
	}

	@ApiOperation(value = "API to search and filter all user login details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/search-all-user-login-details")
	public Response<Object> searchAndFilterAllUserLoginDetails(@RequestHeader Long userId,
			@RequestParam(required = false) Long fromDate, @RequestParam(required = false) Long toDate,
			@RequestParam(required = false) String search, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		return loginLogsService.searchAndFilterAllUserLoginDetails(userId, fromDate, toDate, search, page, pageSize);
	}

	@ApiOperation(value = "API to get user login details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/get-user-login-details")
	public Response<Object> getUserLoginDetails(@RequestHeader Long userId, @RequestParam Long userIdForLoginDetails,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize) {
		if (page == null && pageSize == null) {
			return loginLogsService.getUserLoginDetailsWithoutPagination(userIdForLoginDetails);
		} else {
			return loginLogsService.getUserLoginDetails(userIdForLoginDetails, page, pageSize);

		}
	}
}
