package com.mobiloitte.usermanagement.serviceimpl;

import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.usermanagement.constants.MessageConstant;
import com.mobiloitte.usermanagement.dao.SmsDetailsDao;
import com.mobiloitte.usermanagement.dao.UserDao;
import com.mobiloitte.usermanagement.dao.UserDetailsDao;
import com.mobiloitte.usermanagement.dto.ActivityLogDto;
import com.mobiloitte.usermanagement.dto.ProfileUpdateDto;
import com.mobiloitte.usermanagement.dto.ResetPasswordDto;
import com.mobiloitte.usermanagement.dto.ResetPasswordPhoneNo;
import com.mobiloitte.usermanagement.dto.VerifyGoogleCodeDto;
import com.mobiloitte.usermanagement.dto.VerifySmsCodeDto;
import com.mobiloitte.usermanagement.enums.LogType;
import com.mobiloitte.usermanagement.exception.UserNotFoundException;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.SmsDetails;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.model.UserDetail;
import com.mobiloitte.usermanagement.service.UserMobileService;
import com.mobiloitte.usermanagement.util.AWSSNSUtil;
import com.mobiloitte.usermanagement.util.ActivityMainClass;
import com.mobiloitte.usermanagement.util.ActivityUtil;
import com.mobiloitte.usermanagement.util.MailSender;
import com.mobiloitte.usermanagement.util.TwoFaType;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

@Service
public class UserMobileServiceImpl extends MessageConstant implements UserMobileService {

	private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);

	public static final String SEND_FROM = "sendFrom";

	public static final String DESC = "desc";

	public static final String SEND_TO = "emailTo";

	public static final String EMAIL_SUBJECT = "subjectOf";

	public static final String OTP = "otp";

	public static final String INVALIDGOOGLECODE = "usermanagement.invalid.goolgecode";

	public static final String USERSUCCESS = "usermanagement.success";

	public static final String USERNOTFIUND = "User not found for verifying OTP with the Id : %d";

	public static final String TOKEN = "?token=";

	public static final String TOKENEXPIRED = "usermanagement.token.expired";

	public static final String USERFAIL = "usermanagement.signup.email.failure";

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserDetailsDao userDetailsDao;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SmsDetailsDao smsDetailsDao;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private AWSSNSUtil awsSnsUtil;

	ModelMapper modelMapper = new ModelMapper();

	@Value("${tokenSecretKey}")
	private String tokenSecretKey;

	@Value("${jwtconfig.expirationTime}")
	private int expirationTime;

	@Value("${tokenExpirationTime}")
	private int tokenExpirationTime;

	@Value("${otpExpirationTime}")
	private int otpExpirationTime;

	@Autowired
	MailSender mailSend;

	@Value("${spring.project.name}")
	private String projectName;

	@Value("${user.kyc.limit.enabled}")
	private boolean userKycLimitEnabled;

	@Autowired
	private ActivityUtil activityUtil;

	@Value("${isActivityEnabled}")
	private Boolean isActivityEnabled;

	@Override
	@Transactional
	public Response<Object> completeSignup(ProfileUpdateDto profileUpdateDto, String email) {
		Optional<User> verifyUser = userDao.findByEmail(email);

		if (verifyUser.isPresent()) {
			User checkUser = verifyUser.get();
			User user = mergeWithExistingUserMobileApp(checkUser, profileUpdateDto);
			User savedUser = userDao.save(user);
			if (isActivityEnabled) {
				ActivityLogDto activityLogDto = new ActivityLogDto("SignUpMobile", savedUser.getUserId(),
						savedUser.getUserId(), profileUpdateDto.getIpAddress(), profileUpdateDto.getLocation());
				ActivityLogDto activityLogDtos = new ActivityLogDto(LogType.UPDATED, "SignUp with mobile Successfully",
						savedUser.getEmail(), savedUser.getRole().getRole().name());
				activityUtil.isActivitySaved(new ActivityMainClass(activityLogDto, activityLogDtos));
			}
			return new Response<>(200, "Signup successfull");
		} else {
			throw new UserNotFoundException(
					String.format("User not found with the id : %d for update the profile ", email));
		}
	}

	private User mergeWithExistingUserMobileApp(User checkUser, ProfileUpdateDto profileUpdateDto) {
		UserDetail userDetail = checkUser.getUserDetail();
		userDetail.setFirstName(profileUpdateDto.getFirstName());
		userDetail.setLastName(profileUpdateDto.getLastName());
		userDetail.setCountry(profileUpdateDto.getCountry());
		userDetail.setCity(profileUpdateDto.getCity());
		userDetail.setGender(profileUpdateDto.getGender());
		userDetail.setState(profileUpdateDto.getState());
		userDetail.setImageUrl(profileUpdateDto.getImageUrl());
		userDetail.setAddress(profileUpdateDto.getAddress());
		userDetail.setDob(profileUpdateDto.getDob());
		userDetail.setPhoneNo(profileUpdateDto.getPhoneNo());
		userDetail.setCountryCode(profileUpdateDto.getCountryCode());
		userDetail.setPnWithoutCountryCode(profileUpdateDto.getPnWithoutCountryCode());
		checkUser.setUserDetail(userDetail);
		return checkUser;
	}

	@Override
	public Response<Object> forgetPasswordMobiloitApp(String email, String mobileNo) {
		SmsDetails saveOtp;
		Optional<User> u = userDao.findByEmail(email);

		if (email != null) {

			Integer otp = checkForUniqueOTP();
			String otp1 = String.valueOf(otp);

			Map<String, Object> sendMailMap = new HashMap<>();

			sendMailMap.put(SEND_FROM, "info@bitbharat.world");
			sendMailMap.put(SEND_TO, email);
			sendMailMap.put(DESC, "Your one time otp:");
			sendMailMap.put(EMAIL_SUBJECT, "YOUR OTP");
			sendMailMap.put(OTP, otp1);
			if (u.isPresent()) {
				boolean success = mailSend.sendMailContactUs(sendMailMap);

				if (success) {
					Optional<SmsDetails> checkUser = smsDetailsDao.findByUserId(u.get().getUserId());
					if (checkUser.isPresent()) {
						saveOtp = checkUser.get();
						saveOtp.setCreateTime(new Date());
					} else {
						saveOtp = new SmsDetails();
						saveOtp.setUserId(u.get().getUserId());
					}
					saveOtp.setOtp(otp);
					smsDetailsDao.save(saveOtp);
					return new Response<>(200, "An otp has been sent to your registered email-Id");

				} else {
					return new Response<>(205, messageSource.getMessage(USERFAIL, new Object[0], Locale.US));
				}

			}
			return new Response<>(205, "User does not exists . Please Register To Bit Bharat");
		} else if (mobileNo != null) {
			Optional<UserDetail> uf = userDetailsDao.findByPhoneNo(mobileNo);

			Integer otpForMobile = checkForUniqueOTP();

			Boolean status = awsSnsUtil.sendSms(otpForMobile, mobileNo);
			if (Boolean.TRUE.equals(status)) {
				Optional<SmsDetails> checkUser = smsDetailsDao.findByUserId(uf.get().getUserDetailId());
				if (checkUser.isPresent()) {
					saveOtp = checkUser.get();
					saveOtp.setCreateTime(new Date());
				} else {
					saveOtp = new SmsDetails();
					saveOtp.setUserId(uf.get().getUserDetailId());
				}
				saveOtp.setOtp(otpForMobile);
				smsDetailsDao.save(saveOtp);
				return new Response<>(200,
						messageSource.getMessage("usermanagement.otp.send.success", new Object[0], Locale.US));
			}
		} else {
			return new Response<>(205,
					messageSource.getMessage("usermanagement.phoneno.not.exist", new Object[0], Locale.US));
		}
		return null;

	}

	private Integer checkForUniqueOTP() {
		Integer otp = awsSnsUtil.generateRandomOtp();
		if (!Boolean.TRUE.equals(smsDetailsDao.existsByOtp(otp)))
			return otp;
		else
			return checkForUniqueOTP();
	}

	@Override
	public Response<Object> verifySmsCodeMobileApp(String email, @Valid VerifySmsCodeDto verifySmsCodeDto) {
		Optional<User> userDetail = userDao.findByEmail(email);
		Optional<User> userDetails = userDao.findById(userDetail.get().getUserId());
		if (userDetails.isPresent()) {
			Optional<SmsDetails> checkOtp = smsDetailsDao.findByUserId(userDetail.get().getUserId());
			if (checkOtp.isPresent()) {
				long currentTime = System.currentTimeMillis();
				long creationTime = checkOtp.get().getCreateTime().getTime();
				if (currentTime < creationTime + otpExpirationTime) {
					if (checkOtp.get().getOtp().equals(verifySmsCodeDto.getOtp())) {
						smsDetailsDao.deleteById(checkOtp.get().getOtpId());
						return new Response<>(200, "otp verfied successfully", email);
					} else {
						return new Response<>(201,
								messageSource.getMessage("usermanagement.sms.otp.invalid", new Object[0], Locale.US));
					}
				} else {
					return new Response<>(205,
							messageSource.getMessage("usermanagement.sms.otp.expired", new Object[0], Locale.US));
				}
			} else {
				throw new UserNotFoundException(String.format(USERNOTFIUND, userDetail.get().getUserId()));
			}
		} else {
			throw new UserNotFoundException(
					String.format("No user found with userId : %d", userDetail.get().getUserId()));
		}
	}

	@Override
	public Response<Object> resetPasswordMobileApp(ResetPasswordDto resetPasswordDto) {
		Optional<User> details = userDao.findByEmail(resetPasswordDto.getEmail());
		User user;
		Optional<User> checkUser = userDao.findByUserId(details.get().getUserId());
		if (checkUser.isPresent()) {
			user = checkUser.get();
			user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDto.getPassword()));
			userDao.save(user);
			return new Response<>(200, "password successfully update.");

		} else {
			return new Response<>(205, "user not found");
		}
	}

	@Override
	public Response<Object> googleAuthenticationMobileApp(String email) {
		Optional<User> details = userDao.findByEmail(email);
		if (details.isPresent()) {
			Response<Object> response = null;
			Map<String, Object> data = new HashMap<>();
			Optional<User> user = userDao.findByUserId(details.get().getUserId());
			LOGGER.debug("user details are :", user);
			if (user.isPresent()) {
				UserDetail userDetail = user.get().getUserDetail();
				if (userDetail.getTwoFaType() == TwoFaType.NONE || userDetail.getTwoFaType() == TwoFaType.SKIP) {
					GoogleAuthenticator authenticator = new GoogleAuthenticator();
					GoogleAuthenticatorKey credentials = authenticator.createCredentials();

					String qrCodeUrl = GoogleAuthenticatorQRGenerator.getOtpAuthURL(projectName, user.get().getEmail(),
							credentials);
					data.put("secretKey", credentials.getKey());
					data.put("qrCode", qrCodeUrl);
					response = new Response<>(data);
				} else if (userDetail.getTwoFaType() == TwoFaType.GOOGLE) {
					response = new Response<>(400, messageSource.getMessage("usermanagement.googleAuth.already.enabled",
							new Object[0], Locale.US));
				} else if (userDetail.getTwoFaType() == TwoFaType.SMS) {
					response = new Response<>(400, messageSource.getMessage("usermanagement.googleAuth.smsAuth.enabled",
							new Object[0], Locale.US));
				}
			} else {
				throw new UserNotFoundException(String.format(
						"User not found for google authentication with the id : %d", details.get().getUserId()));
			}
			return response;
		} else {
			return new Response<>("User not found for google authentication with the id : %d");
		}
	}

	@Override
	public Response<Object> verifyGoogleCodeMobileApp(String email, VerifyGoogleCodeDto verifyGoogleCodeDto) {
		Optional<User> details = userDao.findByEmail(email);

		Response<Object> response = null;
		if (details.isPresent()) {
			Optional<User> verifyUser = userDao.findByUserId(details.get().getUserId());
			String secretKey = verifyGoogleCodeDto.getSecretKey();
			LOGGER.debug("user details are :", verifyUser);
			if (verifyUser.isPresent()) {
				if (secretKey == null) {
					secretKey = verifyUser.get().getUserDetail().getSecretKey();
				}
				GoogleAuthenticator authenticator = new GoogleAuthenticator();
				if (authenticator.authorize(secretKey, verifyGoogleCodeDto.getCode())) {
					User user = verifyUser.get();
					user.getUserDetail().setSecretKey(secretKey);
					user.getUserDetail().setTwoFaType(TwoFaType.GOOGLE);
					userDao.save(user);
					response = new Response<>(200, messageSource.getMessage(USERSUCCESS, new Object[0], Locale.US));
				} else {
					response = new Response<>(205,
							messageSource.getMessage(INVALIDGOOGLECODE, new Object[0], Locale.US));
				}
			} else {
				throw new UserNotFoundException(String.format("User not found for google verifiication with id : %d",
						details.get().getUserId()));
			}
			return response;
		} else {

			return new Response<>(205, messageSource.getMessage(USERNOTFIUND, new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> resetPasswordPhoneNoMobileApp(@Valid ResetPasswordPhoneNo resetPasswordDto) {
		Optional<UserDetail> details = userDetailsDao.findByPhoneNo(resetPasswordDto.getPhoneNo());
		if (details.isPresent()) {
			User user;
			Optional<User> checkUser = userDao.findByUserId(details.get().getUser().getUserId());
			if (checkUser.isPresent()) {
				user = checkUser.get();
				user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDto.getPassword()));
				userDao.save(user);
				return new Response<>(200, "password successfully update.");

			} else {
				return new Response<>(205, "user not found");
			}
		}
		return new Response<>(205, "USER_PHONENO_NOTFOUND");
	}
}
