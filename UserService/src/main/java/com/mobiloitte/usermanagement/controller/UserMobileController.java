package com.mobiloitte.usermanagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.dto.ProfileUpdateDto;
import com.mobiloitte.usermanagement.dto.ResetPasswordDto;
import com.mobiloitte.usermanagement.dto.ResetPasswordPhoneNo;
import com.mobiloitte.usermanagement.dto.VerifyGoogleCodeDto;
import com.mobiloitte.usermanagement.dto.VerifySmsCodeDto;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.service.UserMobileService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class UserMobileController {

	@Autowired
	private UserMobileService userMobileService;

	@ApiOperation(value = "API to send email for the forget password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "No user found with email address"), })
	@GetMapping("/forget-password-mobile-app")
	public Response<Object> forgetPasswordMobiloitApp(@RequestParam(required = false) String email,
			@RequestParam(required = false) String mobileNo) {
		return userMobileService.forgetPasswordMobiloitApp(email, mobileNo);
	}

	@ApiOperation(value = "API to verify sms code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Verified"),
			@ApiResponse(code = 205, message = "Unverified"), })
	@PostMapping("/verify-sms-code-mobile-app")
	public Response<Object> verifySmsCodeMobileApp(@RequestParam String email,
			@Valid @RequestBody VerifySmsCodeDto verifySmsCodeDto) {
		return userMobileService.verifySmsCodeMobileApp(email, verifySmsCodeDto);

	}

	@ApiOperation(value = "API to reset the password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/reset-password-mobile-app")
	public Response<Object> resetPasswordMobileApp(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
		return userMobileService.resetPasswordMobileApp(resetPasswordDto);
	}

	@ApiOperation(value = "API to enable Google Auth")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Google 2FA already enabled\""),
			@ApiResponse(code = 400, message = "SMS 2FA enable. Please disable it first to enable Google 2FA"), })
	@GetMapping("/google-auth-mobile-app")
	public Response<Object> googleAuthenticationMobileApp(@RequestParam String email) {
		return userMobileService.googleAuthenticationMobileApp(email);
	}

	@ApiOperation(value = "API to verify google code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/verify-google-code-mobile-app")
	public Response<Object> verifyGoogleCodeMobileApp(@RequestParam String email,
			@RequestBody VerifyGoogleCodeDto verifyGoogleDto) {
		return userMobileService.verifyGoogleCodeMobileApp(email, verifyGoogleDto);
	}

	@ApiOperation(value = "API to update the profile of the user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/complete-signup-mobile-app")
	public Response<Object> completeSignup(@RequestBody ProfileUpdateDto profileUpdateDto, @RequestParam String email) {
		return userMobileService.completeSignup(profileUpdateDto, email);
	}
	
	@PostMapping("/Reset-Password-PhoneNo-Mobile-App")
	public Response<Object> resetPasswordPhoneNoMobileApp(@Valid @RequestBody ResetPasswordPhoneNo resetPasswordDto){
		return userMobileService.resetPasswordPhoneNoMobileApp(resetPasswordDto);
	}

}
