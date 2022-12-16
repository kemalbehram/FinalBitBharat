package com.mobiloitte.server.authorization.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.server.authorization.dto.EmailDtoNew;
import com.mobiloitte.server.authorization.dto.EmailDtoVerfiy;
import com.mobiloitte.server.authorization.dto.GoogleTwoFaCodeRequest;
import com.mobiloitte.server.authorization.dto.VerfiyPhoneDto;
import com.mobiloitte.server.authorization.dto.VerifyGoogleCodeDto;
import com.mobiloitte.server.authorization.dto.VerifySmsCodeDto;
import com.mobiloitte.server.authorization.feign.UserClient;
import com.mobiloitte.server.authorization.model.Response;
import com.mobiloitte.server.authorization.model.User;
import com.mobiloitte.server.authorization.service.UserDetailsServiceImpl;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class TwoFaController {

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Autowired
	private UserClient userClient;

//	@ApiOperation("API to verify sms code when SMS 2FA is enabled. Returns Authorization token on successfull attempt")
//	@ApiResponses({ @ApiResponse(code = 200, message = "2FA Successfully Verified"),
//			@ApiResponse(code = 401, message = "Invalid OTP") })
//	@PostMapping("verify-sms")
//	public Response<String> verifySmsCode(@RequestBody VerifySmsCodeDto otp, @RequestHeader Long userId) {
//		Response<VerfiyPhoneDto> response = userClient.verifySmsCode(userId, otp);
//		if (response.getStatus() == 200) {
//			User user = userDetailsServiceImpl.loadUserByUsername(response.getData().getEmail());
//			user.setAuthenticated(true);
//			String token = userDetailsServiceImpl.generateToken(user);
//			return new Response<>(200, "2FA Successfully Verified", token);
//		} else {
//			return new Response<>(401, "Invalid OTP");
//		}
//	}

	@ApiOperation("API to verify sms code when SMS 2FA is enabled. Returns Authorization token on successfull attempt")
	@ApiResponses({ @ApiResponse(code = 200, message = "2FA Successfully Verified"),
			@ApiResponse(code = 401, message = "Invalid OTP") })
	@PostMapping("verify-sms")
	public Response<String> verifySmsCode(@RequestBody VerifySmsCodeDto otp, @RequestHeader Long userId,
			@RequestHeader String username) {
		Response<String> response = userClient.verifySmsCode(userId, otp);
		if (response.getStatus() == 200) {
			User user = userDetailsServiceImpl.loadUserByUsername(username);
			user.setAuthenticated(true);
			String token = userDetailsServiceImpl.generateToken(user);
			return new Response<>(200, "2FA Successfully Verified", token);
		} else {
			return new Response<>(401, "Invalid OTP");
		}
	}

	@GetMapping("send-sms-code")
	@ApiOperation("API to send sms code to the registered phone number when SMS 2FA is enabled. Returns token which is to be send as Authorization Header value when verifying the sms code")
	@ApiResponses({ @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 401, message = "Failed to send SMS") })
	public Response<String> sendSmsCode(@RequestHeader Long userId, @RequestHeader String username) {
		Response<String> response = userClient.sendSmsCode(userId);
		if (response.getStatus() == 200) {
			String token = userDetailsServiceImpl.generateToken(username, response.getData());
			return new Response<>(200, "OTP sent to the registered MobileNo", token);
		} else {
			return new Response<>(401, "Failed to send SMS");
		}
	}

	@PostMapping("verify-google")
	@ApiOperation("API to verify google code when GOOGLE 2FA is enabled. Returns Authorization token on successfull attempt")
	@ApiResponses({ @ApiResponse(code = 200, message = "2FA Successfully Verified"),
			@ApiResponse(code = 401, message = "Google authentication wrong code") })
	public Response<String> verifyGoogleCode(@RequestBody GoogleTwoFaCodeRequest otp, @RequestHeader Long userId,
			@RequestHeader String username) {
		Response<String> response = userClient.verifyGoogleCode(userId, new VerifyGoogleCodeDto(otp.getOtp()));
		if (response.getStatus() == 200) {
			User user = userDetailsServiceImpl.loadUserByUsername(username);
			user.setAuthenticated(true);
			String token = userDetailsServiceImpl.generateToken(user);
			return new Response<>(200, "2FA Successfully Verified", token);
		}
		return new Response<>(401, "Google authentication code wrong");
	}
//
//	@ApiOperation("API to verify sms code when SMS 2FA is enabled. Returns Authorization token on successfull attempt")
//	@ApiResponses({ @ApiResponse(code = 200, message = "2FA Successfully Verified"),
//			@ApiResponse(code = 401, message = "Invalid OTP") })
	@PostMapping("/verify-Email-code")
	public Response<String> verifyEmailCode(@RequestBody EmailDtoNew codeDto, @RequestHeader Long userId,
			@RequestHeader String username) {
		Response<String> response = userClient.verifyEmailCode(userId, codeDto);
		if (response.getStatus() == 200) {
			User user = userDetailsServiceImpl.loadUserByUsername(username);
			user.setAuthenticated(true);
			String token = userDetailsServiceImpl.generateToken(user);
			return new Response<>(200, "2FA Successfully Verified", token);
		} else {
			return new Response<>(401, "Invalid OTP");
		}
	}

}
