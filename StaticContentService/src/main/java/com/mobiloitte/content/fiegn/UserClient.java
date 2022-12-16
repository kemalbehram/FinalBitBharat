package com.mobiloitte.content.fiegn;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.content.dto.UserDetailsDto;
import com.mobiloitte.content.model.Response;

@FeignClient(value = "${exchange.application.user-service}")
public interface UserClient {
	
	
	@GetMapping("/get-user")
	Response<UserDetailsDto> getUserByEmail(@RequestParam("email") String email);
	
	@GetMapping("/get-user-by-email")
	public Response<Object> getUserByEmail1(@RequestParam String email);

}
