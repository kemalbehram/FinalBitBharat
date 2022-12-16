
package com.mobiloitte.p2p.content.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.mobiloitte.p2p.content.dto.UserEmailAndNameDto;
import com.mobiloitte.p2p.content.dto.UserProfileDto;
import com.mobiloitte.p2p.content.enums.KycStatus;
import com.mobiloitte.p2p.content.model.Response;


@FeignClient("${exchange.application.user-service}")
public interface UserClient {

	@GetMapping("get-kyc-status")
	public Response<KycStatus> getKycStatus(@RequestParam Long userId) ;


	@GetMapping("/get-name-email")
	public Response<UserEmailAndNameDto> getEmailAndName(@RequestParam Long userId);

	@GetMapping("/my-account")
	public Response<UserProfileDto> getUserByUserId(@RequestParam Long userId);
	
	@GetMapping("get-phone-number")
	public Response<String> getPhoneNo(@RequestParam Long userId);
	
}
