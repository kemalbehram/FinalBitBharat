package com.mobiloitte.usermanagement.feign;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.usermanagement.dto.SearchAndFilterDto;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.util.ActivityMainClass;

@FeignClient("${exchange.application.activity-service}")
public interface ActivityClient {

	@PostMapping(value = "/save-activity-log")
	public Response<Object> saveActivityLogs(@RequestBody ActivityMainClass activityMainClass);

	@GetMapping(value = "/user-activity-logs")
	public Response<List<Map<String, Object>>> getUserActivityLogs(@RequestParam Long userId);

	@GetMapping("/users-activity-logs")
	public Map<String, Object> getUsersActivityLog(@RequestParam Long userId, @RequestParam int page,
			@RequestParam int pageSize, @RequestParam(required = false) Long toDate,
			@RequestParam(required = false) Long fromDate);

	@PostMapping(value = "/search-and-filter-admin-and-staff-activity-logs")
	public Map<String, Object> searchAndFilterActivitylogsOfAdminAndStaff(@RequestHeader Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) throws URISyntaxException, IOException;

	@PostMapping(value = "/search-and-filter-all-customers-activity-logs")
	public Map<String, Object> searchAndFilterAllActivitylogsOfCustomers(@RequestHeader Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) throws URISyntaxException, IOException;

	@PostMapping(value = "/search-and-filter-staffs-activity-logs")
	public Map<String, Object> searchAndFilterActivitylogsOfStaff(@RequestHeader Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) throws URISyntaxException, IOException;

	@PostMapping("/search-and-filter-customers-activity-logs")
	public Map<String, Object> searchAndFilterActivitylogsOfCustomer(@RequestParam String email,
			@RequestBody SearchAndFilterDto searchAndFilterDto);

}