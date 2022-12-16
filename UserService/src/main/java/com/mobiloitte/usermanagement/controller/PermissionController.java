package com.mobiloitte.usermanagement.controller;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.PermissionService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("admin")
public class PermissionController {

	@Autowired
	private PermissionService permissionService;

	@ApiOperation(value = "API to get all master permission list permission")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("get-master-permissions")
	public Response<Object> getMasterPermissionList(@RequestHeader Long userId) {
		return permissionService.getMasterPermissionList();
	}

	@ApiOperation(value = "API to get all role permission")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("get-all-role-permissions")
	public Response<Object> getAllRolePermission(@RequestHeader Long userId) {
		return permissionService.getAllRolePermission();
	}

	@ApiOperation(value = "API to get email wise role permission")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("get-email-wise-permission")
	public Response<Object> getEmailWiseRolePermission(@RequestHeader Long userId, @RequestParam String email)
			throws IOException, URISyntaxException {
		return permissionService.getEmailWiseRolePermission(email);
	}

	@ApiOperation(value = "API to get role wise role permission")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("get-role-wise-role-permission")
	public Response<Object> getRoleWiseRolePermission(/* @RequestHeader Long userId, */@RequestParam String roleId) {
		return permissionService.getRoleWiseRolePermission(roleId);
	}

	@GetMapping("get-roles-and-permission-data")
	public Response<Object> getRolesAndPermissionData(@RequestHeader Long userId) {
		return permissionService.getRolesAndPermissionData(userId);
	}

	@ApiOperation(value = "API to get users role permission for permission metric")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/get-users-role-permissions-metric")
	public Response<Object> getUsersRolePermissionForPermissionMetric(@RequestParam Long userId) {
		return permissionService.getUsersRolePermission(userId);
	}

	
	@SuppressWarnings("unchecked")
	@ApiOperation(value = "API to get users role permission for permission metric")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@GetMapping("/get-users-role-permissions-metrics")
	public List<String> getUsersRolePermissionForPermissionMetrics(@RequestParam String userName) {
		Response<Object> response = permissionService.getUsersRolePermissionForPermissionMetric(userName);
		List<String> list = new ArrayList<>();
		if (response.getStatus() == 2012 && response.getData() != null) {
			list = (List<String>) response.getData();
		}
		return list;
	}
}