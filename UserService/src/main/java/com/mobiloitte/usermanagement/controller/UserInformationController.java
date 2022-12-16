package com.mobiloitte.usermanagement.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.dto.RoleListDto;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.UserInformationService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class UserInformationController {

	@Autowired
	private UserInformationService userInformationService;

	@PostMapping("/get-roles-id")
	public Response<List<RoleListDto>> getRolesDataForNotification(@Valid @RequestBody @Min(1) List<Long> rolesId) {
		return userInformationService.getRolesDataForNotification(rolesId);

	}

	@GetMapping("/get-role-id")
	public Response<Long> getRoleIdByName(@RequestParam String role)
			 {
		return userInformationService.getRoleIdByName(role);
	}

	@ApiOperation(value = "API to get user security history details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/get-user-security-history-details")
	public Response<Object> getUserSecurityHistoryDetails(@RequestHeader Long userId,
			@RequestParam Long userIdForSecurityDetails, @RequestParam(required = false) Integer page,
			@RequestParam(required = false) Integer pageSize) {
		if (page == null && pageSize == null) {
			return userInformationService.getUserSecurityHistoryDetailsWithoutPagination(userIdForSecurityDetails);
		} else {
			return userInformationService.getUserSecurityHistoryDetails(userIdForSecurityDetails, page, pageSize);

		}
	}
}