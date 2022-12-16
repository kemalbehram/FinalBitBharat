package com.mobiloitte.usermanagement.service;

import java.util.List;
import java.util.Locale;

import javax.validation.Valid;

import org.springframework.boot.configurationprocessor.json.JSONException;

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

public interface UserService {

	Response<User> signupService(SignupDto signupDto, Locale locale);

	Response<Object> googleAuthentication(long userId);

	Response<Object> googleVerification(Long userId, VerifyGoogleCodeDto verifyGoogleDto);

	Response<Object> sendSmsCode(Long userId);

	Response<Object> verifySms(Long userId, VerifySmsCodeDto verifySmsCodeDto);

	Response<Object> forgetPassword(String email, String weburl, String ipAddress, String location);

	Response<Object> resetPassword(ResetPasswordDto resetPasswordDto);

	Response<Object> verifyUser(String token);

	Response<Object> changePassword(ChangePasswordDto changePasswordDto, Long userId);

	Response<Object> profileUpdate(ProfileUpdateDto profileUpdateDto, Long userId);

	Response<UserProfileDto> getUserByUserId(Long userId);

	Response<Object> sendVerifyUserEmail(String email, String webUrl, Locale locale);

	Response<Object> twoFaDisable(TwoFaDisableDto twoFaDisableDto, Long userId);

	Response<Object> userStatus(String userStatus, Long userId);

	Response<User> saveLoginDetail(LoginDto loginDto, Long userId);

	Response<Object> checkValidToken(String token);

	Response<UserEmailDto> getEmail(Long userId);

	Response<Object> skipTwoFa(Long userId, String ipAddress, String source);

	Response<UserEmailAndNameDto> getEmailAndName(Long userId);

	Response<Object> sendEmailToVerifyIPAddress(VerifyIPAddressDto verifyIPAddressDto);

	Response<Object> verifyIpAddress(VerifyIPAddressDto verifyIPAddressDto);

	Response<Object> saveDeviceTokenDetails(Long userId, DeviceTokenDto deviceTokenDto);

	Response<Object> smsDisable(TwoFaDisableDto twoFaDisableDto, Long userId);

	Response<Object> deleteDeviceToken(Long userId, DeviceTokenDto deviceTokenDto);

	Response<KycStatus> getKycStatus(Long userId);

	Response<String> getPhoneNumber(Long userId);

	Response<String> getAdminEmailId();

	Response<Object> getUserLoginDetails(Long userId, Long userIdForLoginHistoy);

	Response<Object> getUserList(Long userId);

	Response<Object> updateId(Long userId, String oldRandomId, String newRandomId);

	Response<UserDetailsDto> getUserByEmail(String search);

	Response<Object> blockedUser(Long userId);

	Response<Object> sendVerifyUserOtp(Long userId, Locale locale);

	Response<Object> verifyEmailSmsCode(Long userId, @Valid EmailSmsDto emailSmsDto);

	Response<Object> emailsmsDisable(TwoFaEmailDisableDto twoFaEmailDisableDto, Long userId);

	Response<Object> sendPhoneNoCode(String phoneNo);

	Response<Object> verifyphoneSmsCode(String phoneNo, @Valid PhoneOtp emailSmsDto);

	Response<Object> checkValidotp(Integer otp);

	Response<Object> verifyEmailCode(Long userId, @Valid EmailDtoNew verifySmsCodeDto);

	Response<Object> deactivateAccount(Long userId);

	Response<Object> getrole(String email);

	Response<Object> getTotalReffalCount(String myReferralCode, Long userId);

	Response<Object> Tier1Referal(String myReferralCode, Long userId);

	Response<Object> refferUser(Long userId);

	Response<Object> cmc() throws JSONException;

	Response<Object> registredUser();

	Response<UserEmailAndNameDto> fullUser();

	Response<List<ReferalComission>> referCommisionList(Long userId, String coinName);

	

}
