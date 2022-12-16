package com.mobiloitte.notification.feign;

import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.notification.dto.RoleListDto;
import com.mobiloitte.notification.dto.UserEmailAndNameDto;
import com.mobiloitte.notification.dto.UserProfileDto;
import com.mobiloitte.notification.model.Response;



@FeignClient("${exchange.application.user-service}")
public interface UserClient {
	@PostMapping("/get-email-roles")
	public List<String> getNotificationPreference(@RequestBody List<Long> rolesId);

	@PostMapping("/get-roles-id")
	public Response<List<RoleListDto>> getRolesDataForNotification(@RequestBody List<Long> rolesId);

	@GetMapping("/get-role-id")
	public Response<Long> getRoleIdByName(@RequestParam String role);

	@GetMapping("/get-roles")
	public Response<Object> getRoles();
	@GetMapping("/my-account-p2p")
	public Response<UserProfileDto> getUserByUserId(@RequestParam Long userId);
	
	@GetMapping("/get-role-by-user")
	public Response<Long> getRoleFromUser(@RequestParam("userId") Long userId);

	@GetMapping("get-user-details-id")
	public Response<Map<String, Object>> getUserDetails(@RequestHeader Long userId);

	@GetMapping(value = "/get-user-information-by-id")
	public Response<Map<String, String>> getEmailNameAndPhoneNo(@RequestParam Long userId);

	@GetMapping(value = "/get-all-user-information-by-role-id")
	public List<Object> getAllUserInfoByRoleId(@RequestParam Long roleId);
	
	@GetMapping("/get-name-email")
	public Response<UserEmailAndNameDto> getEmailAndName(@RequestParam Long userId);

}
