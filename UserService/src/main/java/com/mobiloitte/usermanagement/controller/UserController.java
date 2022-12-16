package com.mobiloitte.usermanagement.controller;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.dto.ChangePasswordDto;
import com.mobiloitte.usermanagement.dto.DeviceTokenDto;
import com.mobiloitte.usermanagement.dto.EmailDtoNew;
import com.mobiloitte.usermanagement.dto.EmailSmsDto;
import com.mobiloitte.usermanagement.dto.LoginDto;
import com.mobiloitte.usermanagement.dto.PhoneOtp;
import com.mobiloitte.usermanagement.dto.ProfileUpdateDto;
import com.mobiloitte.usermanagement.dto.ResetPasswordDto;
import com.mobiloitte.usermanagement.dto.SignupDto;
import com.mobiloitte.usermanagement.dto.TwoFaDisableDto;
import com.mobiloitte.usermanagement.dto.TwoFaEmailDisableDto;
import com.mobiloitte.usermanagement.dto.UserDetailsDto;
import com.mobiloitte.usermanagement.dto.UserEmailAndNameDto;
import com.mobiloitte.usermanagement.dto.UserEmailDto;
import com.mobiloitte.usermanagement.dto.UserProfileDto;
import com.mobiloitte.usermanagement.dto.VerifyGoogleCodeDto;
import com.mobiloitte.usermanagement.dto.VerifyIPAddressDto;
import com.mobiloitte.usermanagement.dto.VerifySmsCodeDto;
import com.mobiloitte.usermanagement.enums.KycStatus;
import com.mobiloitte.usermanagement.model.ReferalComission;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.service.AdminService;
import com.mobiloitte.usermanagement.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private AdminService adminService;

	@ApiOperation(value = "API for signUp")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 200, message = "Unable to send email"),
			@ApiResponse(code = 205, message = "Email already registered"),
			@ApiResponse(code = 205, message = "Phone number already registered"), })
	@PostMapping("/signup")
	public Response<User> signup(@Valid @RequestBody SignupDto signupDto, Locale locale) {
		return userService.signupService(signupDto, locale);
	}

	@ApiOperation(value = "API to send email for the forget password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Failure"),
			@ApiResponse(code = 400, message = "No user found with email address"), })
	@GetMapping("/forget-password")
	public Response<Object> forgetPassword(@RequestParam String email, @RequestParam String webUrl,
			@RequestParam(required = false) String ipAddress, @RequestParam(required = false) String location) {
		return userService.forgetPassword(email, webUrl, ipAddress, location);
	}

	@ApiOperation(value = "API to enable Google Auth")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Google 2FA already enabled\""),
			@ApiResponse(code = 400, message = "SMS 2FA enable. Please disable it first to enable Google 2FA"), })
	@GetMapping("/google-auth")
	public Response<Object> googleAuthentication(@RequestHeader Long userId) {
		return userService.googleAuthentication(userId);
	}

	@GetMapping("/get-role")
	public Response<Object> getrole(@RequestParam String email) {
		return userService.getrole(email);
	}

	@ApiOperation(value = "API to verify google code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/verify-google-code")
	public Response<Object> verifyGoogleCode(@RequestHeader Long userId,
			@RequestBody VerifyGoogleCodeDto verifyGoogleDto) {
		return userService.googleVerification(userId, verifyGoogleDto);
	}

	@ApiOperation(value = "API to get user details by email address")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/get-user")
	public Response<UserDetailsDto> getUserByEmail(@RequestParam("email") String search) {
		return userService.getUserByEmail(search);
	}

	@ApiOperation(value = "API to get user account details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/my-account-p2p")
	public Response<UserProfileDto> getUserByUserIdForP2p(@RequestParam Long userId) {
		return userService.getUserByUserId(userId);
	}

	@ApiOperation(value = "API to get user account details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/my-account")
	public Response<UserProfileDto> getUserByUserId(@RequestHeader Long userId) {
		return userService.getUserByUserId(userId);
	}

	@ApiOperation(value = "API to send sms authentication code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "failure"), })
	@GetMapping("/send-sms-code")
	public Response<Object> sendSmsCode(@RequestHeader Long userId) {
		return userService.sendSmsCode(userId);
	}

	@ApiOperation(value = "API to verify sms code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Verified"),
			@ApiResponse(code = 205, message = "Unverified"), })
	@PostMapping("/verify-sms-code")
	public Response<Object> verifySmsCode(@RequestHeader Long userId,
			@Valid @RequestBody VerifySmsCodeDto verifySmsCodeDto) {
		return userService.verifySms(userId, verifySmsCodeDto);
	}

	@ApiOperation(value = "API to disable the twoFa")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping(value = "/twoFa-disable")
	public Response<Object> twoFaDisable(@RequestBody TwoFaDisableDto twoFaDisableDto, @RequestHeader Long userId) {
		return userService.twoFaDisable(twoFaDisableDto, userId);
	}

	@ApiOperation(value = "API to reset the password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/reset-password")
	public Response<Object> resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto) {
		return userService.resetPassword(resetPasswordDto);
	}

	@ApiOperation(value = "API to change the password")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "Old password and new password must be different"),
			@ApiResponse(code = 205, message = "Old password does not match"), })
	@PostMapping("/change-password")
	public Response<Object> changePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto,
			@RequestHeader Long userId) {
		return userService.changePassword(changePasswordDto, userId);
	}

	@ApiOperation(value = "API to verify the user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 400, message = "Invalid Token"), })
	@GetMapping("/verify-user")
	public Response<Object> verifyUser(@RequestParam String token) {
		return userService.verifyUser(token);
	}

	@ApiOperation(value = "API to update the profile of the user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/profile-update")
	public Response<Object> profileUpdate(@RequestBody ProfileUpdateDto profileUpdateDto, @RequestHeader Long userId) {
		return userService.profileUpdate(profileUpdateDto, userId);
	}

	@ApiOperation(value = "API to resend email to the user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "failure"), })
	@GetMapping("/resend-verify-email")
	public Response<Object> resendEmail(@RequestParam String email, @RequestParam String webUrl, Locale locale) {
		return userService.sendVerifyUserEmail(email, webUrl, locale);
	}

	@ApiOperation(value = "API to validate the token")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "failure"), })
	@GetMapping("/validate-token")
	public Response<Object> validateToken(@RequestParam String token) {
		return userService.checkValidToken(token);
	}

	@ApiOperation(value = "API for save-login-detail")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/save-login-detail")
	public Response<User> saveLoginDetail(@RequestBody LoginDto loginDto, @RequestHeader Long userId) {
		return userService.saveLoginDetail(loginDto, userId);
	}

	@ApiOperation(value = "API for get Email by userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/get-email")
	public Response<UserEmailDto> getEmail(@RequestBody UserEmailDto userEmailDto) {
		return userService.getEmail(userEmailDto.getUserId());
	}

	@ApiOperation(value = "API to skip the twoFa")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping(value = "/skip-twoFa")
	public Response<Object> skipTwoFa(@RequestHeader Long userId, @RequestParam(required = false) String ipAddress,
			@RequestParam(required = false) String source) {
		return userService.skipTwoFa(userId, ipAddress, source);
	}

	@ApiOperation(value = "API for get Email And Name by userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/get-name-email")
	public Response<UserEmailAndNameDto> getEmailAndName(@RequestParam Long userId) {
		return userService.getEmailAndName(userId);
	}

	@ApiOperation(value = "API for get Email And Name by userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/send-ip-verify-mail")
	public Response<Object> sendEmailToVerifyIP(@RequestBody VerifyIPAddressDto verifyIPAddressDto) {
		return userService.sendEmailToVerifyIPAddress(verifyIPAddressDto);

	}

	@PutMapping("update-randomId")
	public Response<Object> updateId(@RequestHeader Long userId, @RequestParam String oldRandomId,
			@RequestParam String newRandomId) {
		return userService.updateId(userId, oldRandomId, newRandomId);
	}

	@ApiOperation(value = "API for get Email And Name by userId")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/verify-ip-address")
	public Response<Object> verifyIPAddress(@RequestBody VerifyIPAddressDto verifyIPAddressDto) {
		return userService.verifyIpAddress(verifyIPAddressDto);

	}

	@ApiOperation(value = "API for get user kyc limit by user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/get-user-kyc-limit")
	public Response<Object> getUserKycLimit(@RequestParam Long userId) {
		return adminService.getUserKycLimit(userId);
	}

	@ApiOperation(value = "API for save device token details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/save-device-token")
	public Response<Object> saveDeviceTokenDetails(@RequestHeader Long userId,
			@RequestBody DeviceTokenDto deviceTokenDto) {
		return userService.saveDeviceTokenDetails(userId, deviceTokenDto);
	}

	@ApiOperation(value = "API to disable the SMS")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping(value = "/sms-auth-disable")
	public Response<Object> smsAuthDisable(@RequestBody TwoFaDisableDto twoFaDisableDto, @RequestHeader Long userId) {
		return userService.smsDisable(twoFaDisableDto, userId);
	}

	@ApiOperation(value = "API for delete device token for sending the notification")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping(value = "/delete-device-token")
	public Response<Object> deleteDeviceToken(@RequestHeader Long userId, @RequestBody DeviceTokenDto deviceTokenDto) {
		return userService.deleteDeviceToken(userId, deviceTokenDto);
	}

	@GetMapping("/get-kyc-status")
	public Response<KycStatus> getKycStatus(@RequestParam Long userId) {
		return userService.getKycStatus(userId);
	}

	@GetMapping("/get-phone-number")
	public Response<String> getPhoneNo(@RequestHeader Long userId) {
		return userService.getPhoneNumber(userId);
	}

	@GetMapping("/get-admin-email")
	public Response<String> getAdminEmailId() {
		return userService.getAdminEmailId();
	}

	@ApiOperation(value = "API for get user login details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping(value = "/get-user-login-details")
	public Response<Object> getUserLoginDetails(@RequestHeader Long userId, @RequestParam Long userIdForLoginHistoy) {
		return userService.getUserLoginDetails(userId, userIdForLoginHistoy);
	}

	@ApiOperation(value = "API to get user list")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/user-list")
	public Response<Object> getUserList(@RequestHeader Long userId) {
		return userService.getUserList(userId);
	}

	@ApiOperation(value = "API for block user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/block-user")
	public Response<Object> blockedUser(@RequestHeader Long userId) {
		return userService.blockedUser(userId);
	}

	@ApiOperation(value = "API to resend otp to the user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "failure"), })
	@GetMapping("/resend-verify-otp")
	public Response<Object> resendOtp(@RequestHeader Long userId, Locale locale) {
		return userService.sendVerifyUserOtp(userId, locale);
	}

	@ApiOperation(value = "API to verify Email Sms code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Verified"),
			@ApiResponse(code = 205, message = "Unverified"), })
	@PostMapping("/verify-Email-Sms-code")
	public Response<Object> verifyEmailSmsCode(@RequestHeader Long userId,
			@Valid @RequestBody EmailSmsDto emailSmsDto) {
		return userService.verifyEmailSmsCode(userId, emailSmsDto);
	}

	@ApiOperation(value = "API to disable the EMAIL SMS")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping(value = "/email-sms-auth-disable")
	public Response<Object> emailsmsAuthDisable(@RequestBody TwoFaEmailDisableDto twoFaEmailDisableDto,
			@RequestHeader Long userId) {
		return userService.emailsmsDisable(twoFaEmailDisableDto, userId);
	}

	@ApiOperation(value = "API to send phoneNo authentication code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"),
			@ApiResponse(code = 205, message = "failure"), })
	@GetMapping("/send-phoneNo-code")
	public Response<Object> sendPhoneNoCode(@RequestParam String phoneNo) {
		return userService.sendPhoneNoCode(phoneNo);
	}

	@ApiOperation(value = "API to verify phone Sms code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Verified"),
			@ApiResponse(code = 205, message = "Unverified"), })
	@PostMapping("/verify-phone-Sms-code")
	public Response<Object> verifyphoneSmsCode(@RequestParam String phoneNo, @Valid @RequestBody PhoneOtp emailSmsDto) {
		return userService.verifyphoneSmsCode(phoneNo, emailSmsDto);
	}

	@PostMapping("/verify-Email-code")
	public Response<Object> verifyEmailCode(@RequestHeader Long userId,
			@Valid @RequestBody EmailDtoNew verifySmsCodeDto) {
		return userService.verifyEmailCode(userId, verifySmsCodeDto);
	}

	@PostMapping("/Deativate-Account")
	public Response<Object> deactivateAccount(@RequestHeader Long userId) {
		return userService.deactivateAccount(userId);
	}

	@ApiOperation(value = "API to get total refferal count")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@GetMapping("/totalReffalCount")
	public Response<Object> getTotalReffalCount(@RequestHeader Long userId, @RequestParam String myReferralCode) {
		return userService.getTotalReffalCount(myReferralCode, userId);
	}

	@ApiOperation(value = "API to get total refferal count")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success") })
	@GetMapping("/Tier1Referal")
	public Response<Object> Tier1Referal(@RequestHeader Long userId, @RequestParam String myReferralCode) {
		return userService.Tier1Referal(myReferralCode, userId);
	}

	@ApiOperation(value = "API to view reffer user by coupan code")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/Referal-List")
	public Response<Object> refferUser(@RequestHeader Long userId) throws Exception {
		return userService.refferUser(userId);
	}

	@GetMapping("/cmc-data")
	public Response<Object> cmc() throws JSONException {
		return userService.cmc();
	}

	@GetMapping("/registered-user")
	public Response<Object> registredUser() {
		return userService.registredUser();
	}

	@GetMapping("/full-user-list")
	public Response<UserEmailAndNameDto> fullUser() {
		return userService.fullUser();
	}
	
	@GetMapping("/refer-comission-List")
	public Response<List<ReferalComission>> referCommisionList(@RequestParam Long userId,@RequestParam String coinName) {
		return userService.referCommisionList(userId,coinName);
	}

}