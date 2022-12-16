package com.mobiloitte.usermanagement.service;

import javax.validation.Valid;

import com.mobiloitte.usermanagement.dto.ProfileUpdateDto;
import com.mobiloitte.usermanagement.dto.ResetPasswordDto;
import com.mobiloitte.usermanagement.dto.ResetPasswordPhoneNo;
import com.mobiloitte.usermanagement.dto.VerifyGoogleCodeDto;
import com.mobiloitte.usermanagement.dto.VerifySmsCodeDto;
import com.mobiloitte.usermanagement.model.Response;

public interface UserMobileService {

	Response<Object> forgetPasswordMobiloitApp(String email, String mobileNo);

	Response<Object> verifySmsCodeMobileApp(String email, @Valid VerifySmsCodeDto verifySmsCodeDto);

	Response<Object> resetPasswordMobileApp(@Valid ResetPasswordDto resetPasswordDto);

	Response<Object> googleAuthenticationMobileApp(String email);

	Response<Object> verifyGoogleCodeMobileApp(String email, VerifyGoogleCodeDto verifyGoogleDto);

	Response<Object> completeSignup(ProfileUpdateDto profileUpdateDto, String email);

	Response<Object> resetPasswordPhoneNoMobileApp(@Valid ResetPasswordPhoneNo resetPasswordDto);

}
