package com.mobiloitte.usermanagement.serviceimpl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import javax.validation.Valid;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.usermanagement.constants.MessageConstant;
import com.mobiloitte.usermanagement.dao.AdminDao;
import com.mobiloitte.usermanagement.dao.AdminreferalDao;
import com.mobiloitte.usermanagement.dao.DeviceTokenDetailsDao;
import com.mobiloitte.usermanagement.dao.EmailSmsDao;
import com.mobiloitte.usermanagement.dao.KycDao;
import com.mobiloitte.usermanagement.dao.PhoneSmaDao;
import com.mobiloitte.usermanagement.dao.ReferalComissionDao;
import com.mobiloitte.usermanagement.dao.RoleDao;
import com.mobiloitte.usermanagement.dao.SmsDetailsDao;
import com.mobiloitte.usermanagement.dao.TokenDao;
import com.mobiloitte.usermanagement.dao.UserDao;
import com.mobiloitte.usermanagement.dao.UserDetailsDao;
import com.mobiloitte.usermanagement.dao.UserKycLimitDao;
import com.mobiloitte.usermanagement.dao.UserLoginDetailsDao;
import com.mobiloitte.usermanagement.dao.UserSecurityDetailsDao;
import com.mobiloitte.usermanagement.dto.ChangePasswordDto;
import com.mobiloitte.usermanagement.dto.DeviceTokenDto;
import com.mobiloitte.usermanagement.dto.EmailDto;
import com.mobiloitte.usermanagement.dto.EmailDtoNew;
import com.mobiloitte.usermanagement.dto.EmailSmsDto;
import com.mobiloitte.usermanagement.dto.ListUserDto;
import com.mobiloitte.usermanagement.dto.LoginDto;
import com.mobiloitte.usermanagement.dto.PhoneOtp;
import com.mobiloitte.usermanagement.dto.ProfileUpdateDto;
import com.mobiloitte.usermanagement.dto.ResetPasswordDto;
import com.mobiloitte.usermanagement.dto.SignupDto;
import com.mobiloitte.usermanagement.dto.StorageDetailDto;
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
import com.mobiloitte.usermanagement.enums.P2pStatus;
import com.mobiloitte.usermanagement.enums.ReferalEnum;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.SocialType;
import com.mobiloitte.usermanagement.enums.UserSecurityStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.exception.UserNotFoundException;
import com.mobiloitte.usermanagement.feign.NotificationClient;
import com.mobiloitte.usermanagement.feign.WalletClient;
import com.mobiloitte.usermanagement.model.AdminReferal;
import com.mobiloitte.usermanagement.model.DeviceTokenDetails;
import com.mobiloitte.usermanagement.model.EmailSmsDetails;
import com.mobiloitte.usermanagement.model.KYC;
import com.mobiloitte.usermanagement.model.ReferalComission;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.Role;
import com.mobiloitte.usermanagement.model.SmsDetails;
import com.mobiloitte.usermanagement.model.TokenDetails;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.model.UserDetail;
import com.mobiloitte.usermanagement.model.UserKycLimit;
import com.mobiloitte.usermanagement.model.UserLoginDetail;
import com.mobiloitte.usermanagement.model.UserSecurityDetails;
import com.mobiloitte.usermanagement.service.UserService;
import com.mobiloitte.usermanagement.util.APIUtils;
import com.mobiloitte.usermanagement.util.AWSSNSUtil;
import com.mobiloitte.usermanagement.util.ActivityUtil;
import com.mobiloitte.usermanagement.util.MailSender;
import com.mobiloitte.usermanagement.util.MobileSMSUsingTwilio;
import com.mobiloitte.usermanagement.util.TwoFaType;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.Message.Status;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import com.warrenstrange.googleauth.GoogleAuthenticatorQRGenerator;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserServiceImpl extends MessageConstant implements UserService {
	private static final Logger LOGGER = LogManager.getLogger(UserServiceImpl.class);
	public static final int USDT_TIMEOUT = 180000;
	@Autowired
	private UserDao userDao;

	@Autowired
	private NotificationClient notificationClient;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private EmailSmsDao emailSmsDao;
	@Autowired
	private AdminreferalDao adminreferalDao;
	@Autowired
	private UserDetailsDao userDetailsDao;

	@Autowired
	private ReferalComissionDao referalComissionDao;
	@Autowired
	private UserSecurityDetailsDao userSecurityDetailsDao;

	@Autowired
	private UserLoginDetailsDao userLoginDetailDao;

	@Autowired
	private DeviceTokenDetailsDao deviceTokenDetailsDao;
	@Autowired
	private AdminDao adminUserDao;
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private SmsDetailsDao smsDetailsDao;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	private WalletClient walletClient;

	@Autowired
	private PhoneSmaDao phoneSmaDao;

	@Autowired
	private UserKycLimitDao userKycLimitDao;

	@Autowired
	private AWSSNSUtil awsSnsUtil;

	@Autowired
	private MobileSMSUsingTwilio mobileSMSUsingTwilio;
	@Autowired
	private KycDao kycDao;
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

	@Autowired
	private TokenDao tokenDao;

	@Value("${user.kyc.limit.enabled}")
	private boolean userKycLimitEnabled;

	@Autowired
	private ActivityUtil activityUtil;

	@Value("${isActivityEnabled}")
	private Boolean isActivityEnabled;

	public static final String STRING_TXT = "string";
	public static final String BLANK_TXT = " ";

	public static final int INT_SIX_NUMBER = 6;
	private static String apiKey = "2c1d6e89-43c9-45af-a935-08929d351c22";
	private static final String USERNOTFIUND = "User not found for verifying OTP with the Id : %d";
	private String key = "c375242417d3cd106ff3ffacf958a9eecd664a6eef00756649578baf8e06d62f";

	@Override
	public Response<User> signupService(SignupDto signupDto, Locale locale) {
		boolean existingUserWithPhoneNo = false;
		User user = modelMapper.map(signupDto, User.class);
		UserDetail userDetail = modelMapper.map(signupDto, UserDetail.class);
		DeviceTokenDetails deviceTokenDetails = modelMapper.map(signupDto, DeviceTokenDetails.class);
		List<DeviceTokenDetails> details = new ArrayList<>();
		details.add(deviceTokenDetails);
		String data1 = null;
		if (signupDto.getSocialType() == null) {
			userDetail.setSocialType(null);
		} else {
			userDetail.setSocialType(SocialType.valueOf(signupDto.getSocialType()));
		}
		String phoneNo = userDetail.getPhoneNo();

		Role roleDto = modelMapper.map(signupDto, Role.class);
		if (signupDto.getRoleStatus() == null) {
			roleDto.setRole(RoleStatus.USER);
		}
		Optional<Role> existRole = roleDao.findByRole(roleDto.getRole());

		LOGGER.debug("Detail using Role", existRole);
		boolean existByEmail = userDao.existsByEmail(signupDto.getEmail());
		if (signupDto.getPhoneNo() != null) {
			existingUserWithPhoneNo = userDetailsDao.existsByPhoneNo(signupDto.getPhoneNo());
		}

		if (signupDto.getMyRefferalCode().equals(BLANK_TXT) || signupDto.getMyRefferalCode().equals(STRING_TXT)
				|| signupDto.getMyRefferalCode() == null) {
			userDetail
					.setMyRefferalCode(RandomStringUtils.randomAlphabetic(INT_SIX_NUMBER).toUpperCase(Locale.ENGLISH));

		} else {
			userDetail
					.setMyRefferalCode(RandomStringUtils.randomAlphabetic(INT_SIX_NUMBER).toUpperCase(Locale.ENGLISH));
			userDetail.setReferredCode(signupDto.getMyRefferalCode());
		}
		Optional<User> userNotVerified = userDao.findByUserStatusAndEmail(UserStatus.UNVERIFIED, user.getEmail());
		if (userNotVerified.isPresent()) {
			Response<Object> success = sendVerifyUserEmail(user.getEmail(), "string", locale);
			return new Response<>(200, "An OTP has been sent on your entered email ID. Please validate the account");
		}
		if (!existByEmail && !existingUserWithPhoneNo && existRole.isPresent()) {
			userDetail.setIndirectrefer("0");
			userDetail.setDirectReferCount("0");
			userDetail.setTotalReferalPrice(BigDecimal.ZERO);
			userDetail.setTierTwoReferal(BigDecimal.ZERO);
			userDetail.setTierThreeReferal(BigDecimal.ZERO);
			userDetail.setRegisterBonus(BigDecimal.ZERO);
			Optional<UserDetail> data = userDetailsDao.findByMyRefferalCode(signupDto.getReferredCode());
			if (data.isPresent()) {
				String data11 = data.get().getReferredCode();
				System.out.println(data11);
				userDetail.setFinalReferal(data.get().getReferredCode());

			}
			user.setPassword(bCryptPasswordEncoder.encode(signupDto.getPassword()));
			user.setRole(existRole.get());
			user.setUserDetail(userDetail);
			userDetail.setP2pStatus(P2pStatus.UNBLOCK);
			user.setDeviceTokenDetails(details);
			user.setUserStatus(UserStatus.UNVERIFIED);
			userDetail.setUser(user);
			deviceTokenDetails.setUser(user);

			if (signupDto.getRandomId() != null) {
				user.setRandomId(signupDto.getRandomId());
			} else {
				String generatedAlphaNumeric = RandomStringUtils.randomAlphanumeric(8);
				user.setRandomId(generatedAlphaNumeric);
			}

			User userObj = userDao.save(user);
			if (deviceTokenDetails.getDeviceToken() != null || deviceTokenDetails.getDeviceType() != null) {
				deviceTokenDetailsDao.save(deviceTokenDetails);
			}
			if (userKycLimitEnabled) {
				UserKycLimit u1 = new UserKycLimit();
				u1.setCoinName("USD");
				u1.setLimitValue(0.0);
				u1.setUserId(userObj.getUserId());
				userKycLimitDao.save(u1);
			}
			System.out.println(userDetail.getFinalReferal());
			Response<Object> success = sendVerifyUserEmail(userObj.getEmail(), signupDto.getWebUrl(), locale);
			if (success.getStatus() == 200) {
				return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], locale));
			} else {
				return new Response<>(201,
						messageSource.getMessage("usermanagement.signup.email.failure", new Object[0], locale));
			}
		} else if (existByEmail) {
			return new Response<>(205,
					messageSource.getMessage("usermanagement.signup.email.exist", new Object[0], locale));

		} else if (existingUserWithPhoneNo) {
			return new Response<>(205,
					messageSource.getMessage("usermanagement.signup.phone.exist", new Object[0], locale));
		}
		return new Response<>(205,
				messageSource.getMessage("usermanagement.signup.role.not.exist", new Object[0], locale));

	}

	private Response<Object> sendMobileOtp(String mobileNo) {
		SmsDetails saveOtp;

		Optional<UserDetail> uf = userDetailsDao.findByPhoneNo(mobileNo);
		String phoneNo = uf.get().getPhoneNo();
//		if (!phoneNo.contains("+91")) {
//			phoneNo = "+91" + phoneNo;
//		}
		String body = "Otp Send";
		Integer otp = MobileSMSUsingTwilio.generateVerificationCode();
		Message message = mobileSMSUsingTwilio.sendCode(phoneNo, body, otp);
		if (!message.getStatus().equals(Status.FAILED) && !message.getStatus().equals(Status.UNDELIVERED)) {
			Optional<SmsDetails> checkUser = smsDetailsDao.findByUserId(uf.get().getUserDetailId());
			if (checkUser.isPresent()) {
				saveOtp = checkUser.get();
				saveOtp.setCreateTime(new Date());
			} else {
				saveOtp = new SmsDetails();
				saveOtp.setUserId(uf.get().getUserDetailId());
			}
			saveOtp.setOtp(otp);
			smsDetailsDao.save(saveOtp);
			return new Response<>(200,
					messageSource.getMessage("usermanagement.otp.send.success", new Object[0], Locale.US));
		} else {
			return new Response<>(205, "otp send failed");

		}
	}

	private void deleteBySecrateKey(String browseSecrateKey) {

	}

	@Override
	public Response<Object> getrole(String email) {
		Optional<User> role = userDao.findByEmail(email);
		if (role.isPresent()) {
			return new Response<>(200, "success", role.get().getRole().getRole().toString());
		}
		Optional<UserDetail> getDetailsByEmail1 = userDetailsDao.findByPhoneNo(email);
		if (getDetailsByEmail1.isPresent()) {
			return new Response<>(200, "success", getDetailsByEmail1.get().getUser().getRole().getRole().toString());
		} else
			return new Response<>(205, "fail");
	}

	@Override
	public Response<Object> googleAuthentication(long userId) {
		Response<Object> response = null;
		Map<String, Object> data = new HashMap<>();
		Optional<User> user = userDao.findByUserId(userId);
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
			throw new UserNotFoundException(
					String.format("User not found for google authentication with the id : %d", userId));
		}
		return response;
	}

	@Override
	public Response<Object> googleVerification(Long userId, VerifyGoogleCodeDto verifyGoogleCodeDto) {
		Response<Object> response = null;
		Optional<User> verifyUser = userDao.findByUserId(userId);
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
				UserSecurityDetails userSecurityDetails = new UserSecurityDetails();
				userSecurityDetails.setAcitvityMessage("Enabled Google authenticator");
				userSecurityDetails.setCreateTime(new Date());
				userSecurityDetails.setIpAddess(verifyGoogleCodeDto.getIpAddress());
				userSecurityDetails.setSource(verifyGoogleCodeDto.getSource());
				userSecurityDetails.setStatus(UserSecurityStatus.COMPLETED);
				userSecurityDetails.setUser(user);
				userSecurityDetailsDao.save(userSecurityDetails);
				response = new Response<>(200,
						messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
			} else {
				response = new Response<>(205,
						messageSource.getMessage("usermanagement.invalid.goolgecode", new Object[0], Locale.US));
			}
		} else {
			throw new UserNotFoundException(
					String.format("User not found for google verifiication with id : %d", userId));
		}
		return response;
	}

	@Override
	public Response<UserDetailsDto> getUserByEmail(String search) {

		Optional<User> getDetailsByEmail = userDao.findByEmail(search);

		LOGGER.debug("user details are :", getDetailsByEmail);
		if (getDetailsByEmail.isPresent()) {
			UserDetailsDto details = new UserDetailsDto();
			details.setUsername(getDetailsByEmail.get().getEmail());
			details.setPhoneNo(getDetailsByEmail.get().getUserDetail().getPhoneNo());
			details.setPassword(getDetailsByEmail.get().getPassword());
			details.setUserId(getDetailsByEmail.get().getUserId());
			details.setTwoFaType(getDetailsByEmail.get().getUserDetail().getTwoFaType());
			details.setUserStatus(getDetailsByEmail.get().getUserStatus());
			details.setRandomId(getDetailsByEmail.get().getRandomId());
			details.setRole(getDetailsByEmail.get().getRole().getRole());
			details.setPrevilage(getDetailsByEmail.get().getPrevilage());
			details.setZipCode(getDetailsByEmail.get().getUserDetail().getZipCode());
			Long userId = getDetailsByEmail.get().getUserId();
			Optional<UserLoginDetail> userLoginDetail = userLoginDetailDao
					.findTopByUserUserIdOrderByUserLoginIdDesc(userId);
			if (userLoginDetail.isPresent()) {
				Response<Date> lastTransactionDate = walletClient.getUserLastTransactionDate(userId);
				details.setIpAdress(userLoginDetail.get().getIpAddress());
				details.setUserLastLoginIpAddress(userLoginDetail.get().getIpAddress());
				details.setUserLastLoginBrowserPrint(userLoginDetail.get().getBrowserPrint());
				details.setUserLastLoginTime(userLoginDetail.get().getCreateTime());
				details.setUserLastLoginUserAgent(userLoginDetail.get().getUserAgent());
				if (lastTransactionDate != null)
					details.setLastTransactionDate(lastTransactionDate.getData());
			}
			return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US),
					details);
		} else {

			Optional<UserDetail> getDetailsByEmail1 = userDetailsDao.findByPhoneNo(search);
			if (getDetailsByEmail1.isPresent()) {
				UserDetailsDto details = new UserDetailsDto();
				details.setUsername(getDetailsByEmail1.get().getUser().getEmail());
				details.setPhoneNo(getDetailsByEmail1.get().getPhoneNo());
				details.setPassword(getDetailsByEmail1.get().getUser().getPassword());
				details.setUserId(getDetailsByEmail1.get().getUser().getUserId());
				details.setTwoFaType(getDetailsByEmail1.get().getTwoFaType());
				details.setUserStatus(getDetailsByEmail1.get().getUser().getUserStatus());
				details.setRandomId(getDetailsByEmail1.get().getUser().getRandomId());
				details.setRole(getDetailsByEmail1.get().getUser().getRole().getRole());
				details.setPrevilage(getDetailsByEmail1.get().getUser().getPrevilage());
				details.setZipCode(getDetailsByEmail1.get().getZipCode());
				Long userId = getDetailsByEmail1.get().getUser().getUserId();
				Optional<UserLoginDetail> userLoginDetail = userLoginDetailDao
						.findTopByUserUserIdOrderByUserLoginIdDesc(userId);
				if (userLoginDetail.isPresent()) {
					Response<Date> lastTransactionDate = walletClient.getUserLastTransactionDate(userId);
					details.setIpAdress(userLoginDetail.get().getIpAddress());
					details.setUserLastLoginIpAddress(userLoginDetail.get().getIpAddress());
					details.setUserLastLoginBrowserPrint(userLoginDetail.get().getBrowserPrint());
					details.setUserLastLoginTime(userLoginDetail.get().getCreateTime());
					details.setUserLastLoginUserAgent(userLoginDetail.get().getUserAgent());
					if (lastTransactionDate != null)
						details.setLastTransactionDate(lastTransactionDate.getData());
				}
				return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US),
						details);

			}
			throw new UserNotFoundException(
					String.format("User not found for getting details with the email : %s", search));
		}
	}

	@Override
	public Response<UserProfileDto> getUserByUserId(Long userId) {
		Optional<User> getUserDetailsByUserId = userDao.findByUserId(userId);
		LOGGER.debug("user details are :", getUserDetailsByUserId);
		if (getUserDetailsByUserId.isPresent()) {
			UserProfileDto accountDetail = new UserProfileDto();
			accountDetail.setEmail(getUserDetailsByUserId.get().getEmail());
			accountDetail.setUserStatus(getUserDetailsByUserId.get().getUserStatus());
			accountDetail.setUserId(getUserDetailsByUserId.get().getUserId());
			accountDetail.setTwoFaType(getUserDetailsByUserId.get().getUserDetail().getTwoFaType());
			accountDetail.setFirstName(getUserDetailsByUserId.get().getUserDetail().getFirstName());
			accountDetail.setMiddleName(getUserDetailsByUserId.get().getUserDetail().getMiddleName());
			accountDetail.setLastName(getUserDetailsByUserId.get().getUserDetail().getLastName());
			accountDetail.setPhoneNo(getUserDetailsByUserId.get().getUserDetail().getPhoneNo());
			accountDetail.setRole(getUserDetailsByUserId.get().getRole().getRole());
			accountDetail.setCountry(getUserDetailsByUserId.get().getUserDetail().getCountry());
			accountDetail.setCity(getUserDetailsByUserId.get().getUserDetail().getCity());
			accountDetail.setGender(getUserDetailsByUserId.get().getUserDetail().getGender());
			accountDetail.setState(getUserDetailsByUserId.get().getUserDetail().getState());
			accountDetail.setImageUrl(getUserDetailsByUserId.get().getUserDetail().getImageUrl());
			accountDetail.setAddress(getUserDetailsByUserId.get().getUserDetail().getAddress());
			accountDetail.setPrevilage(getUserDetailsByUserId.get().getPrevilage());
			accountDetail.setDob(getUserDetailsByUserId.get().getUserDetail().getDob());
			accountDetail.setCountryCode(getUserDetailsByUserId.get().getUserDetail().getCountryCode());
			accountDetail.setZipCode(getUserDetailsByUserId.get().getUserDetail().getZipCode());
			accountDetail
					.setPnWithoutCountryCode(getUserDetailsByUserId.get().getUserDetail().getPnWithoutCountryCode());
			accountDetail.setCreationTime(getUserDetailsByUserId.get().getCreateTime());
			accountDetail.setRandomId(getUserDetailsByUserId.get().getRandomId());
			accountDetail.setEmailVerificationTime(getUserDetailsByUserId.get().getEmailVerificationTime());
			accountDetail.setMyRefferalCode(getUserDetailsByUserId.get().getUserDetail().getMyRefferalCode());
			accountDetail.setReferredCode(getUserDetailsByUserId.get().getUserDetail().getReferredCode());
			accountDetail.setP2pStatus(getUserDetailsByUserId.get().getUserDetail().getP2pStatus());
			List<KYC> kycs = getUserDetailsByUserId.get().getKyc();
			if (!kycs.isEmpty()) {
				KYC kyc = new KYC();
				for (KYC kyc2 : kycs) {
					kyc = kyc2;
				}
				accountDetail.setKyc(kyc);
			}
			return new Response<>(accountDetail);
		} else {
			throw new UserNotFoundException(
					String.format("user not found for getting account details with the Id : %d", userId));
		}
	}

	@Override
	public Response<Object> sendSmsCode(Long userId) {

		Optional<User> userDetails = userDao.findByUserId(userId);
		LOGGER.debug("user details are :", userDetails);
		SmsDetails saveOtp;
		if (userDetails.isPresent()) {
			String sendTo = userDetails.get().getUserDetail().getPhoneNo();
			if (sendTo != null) {
				if (!sendTo.contains("+")) {
					sendTo = "+" + sendTo;
				}

				String body = "Otp Send";
				Integer sendotp = MobileSMSUsingTwilio.generateVerificationCode();
				Message message = mobileSMSUsingTwilio.sendCode(sendTo, body, sendotp);
				if (!message.getStatus().equals(Status.FAILED) && !message.getStatus().equals(Status.UNDELIVERED)) {
					Optional<SmsDetails> checkUser = smsDetailsDao.findByUserId(userId);
					if (checkUser.isPresent()) {
						saveOtp = checkUser.get();
						saveOtp.setCreateTime(new Date());
					} else {
						saveOtp = new SmsDetails();
						saveOtp.setUserId(userId);
					}
					saveOtp.setOtp(sendotp);
					smsDetailsDao.save(saveOtp);
					return new Response<>(200,
							messageSource.getMessage("usermanagement.otp.send.success", new Object[0], Locale.US));
				}
			} else {
				return new Response<>(205,
						messageSource.getMessage("usermanagement.phoneno.not.exist", new Object[0], Locale.US));
			}
			return new Response<>(205,
					messageSource.getMessage("usermanagement.otp.send.failed", new Object[0], Locale.US));
		} else {
			throw new UserNotFoundException(
					String.format("user not found for getting account details with the Id : %d", userId));
		}

	}

	@Override
	public Response<Object> verifySms(Long userId, VerifySmsCodeDto verifySmsCodeDto) {
		Optional<User> userDetails = userDao.findById(userId);
		if (userDetails.isPresent()) {
			Optional<SmsDetails> checkOtp = smsDetailsDao.findByUserId(userId);
			if (checkOtp.isPresent()) {
				long currentTime = System.currentTimeMillis();
				long creationTime = checkOtp.get().getCreateTime().getTime();
				if (currentTime < creationTime + otpExpirationTime) {
					if (checkOtp.get().getOtp().equals(verifySmsCodeDto.getOtp())) {
						smsDetailsDao.deleteById(checkOtp.get().getOtpId());
						userDetails.get().getUserDetail().setTwoFaType(TwoFaType.SMS);
						userDao.save(userDetails.get());
						UserSecurityDetails userSecurityDetails = new UserSecurityDetails();
						userSecurityDetails.setAcitvityMessage("Enabled SMS authenticator");
						userSecurityDetails.setCreateTime(new Date());
						userSecurityDetails.setIpAddess(verifySmsCodeDto.getIpAddress());
						userSecurityDetails.setSource(verifySmsCodeDto.getSource());
						userSecurityDetails.setStatus(UserSecurityStatus.COMPLETED);
						userSecurityDetails.setUser(userDetails.get());
						userSecurityDetailsDao.save(userSecurityDetails);
						return new Response<>(200,
								messageSource.getMessage("usermanagement.sms.otp.verified", new Object[0], Locale.US));
					} else {
						return new Response<>(201,
								messageSource.getMessage("usermanagement.sms.otp.invalid", new Object[0], Locale.US));
					}
				} else {
					return new Response<>(205,
							messageSource.getMessage("usermanagement.sms.otp.expired", new Object[0], Locale.US));
				}
			} else {
				throw new UserNotFoundException(
						String.format("User not found for verifying OTP with the Id : %d", userId));
			}
		} else {
			throw new UserNotFoundException(String.format("No user found with userId : %d", userId));
		}
	}

	@Override
	public Response<Object> twoFaDisable(TwoFaDisableDto twoFaDisableDto, Long userId) {
		Optional<User> userDetail = userDao.findByUserId(userId);
		if (userDetail.isPresent()) {
			String secretKey = userDetail.get().getUserDetail().getSecretKey();
			GoogleAuthenticator authenticator = new GoogleAuthenticator();
			if (authenticator.authorize(secretKey, (twoFaDisableDto.getOtp()))) {
				User user = userDetail.get();
				user.getUserDetail().setSecretKey("NULL");
				user.getUserDetail().setTwoFaType(TwoFaType.NONE);
				userDao.save(user);
				UserSecurityDetails userSecurityDetails = new UserSecurityDetails();
				userSecurityDetails.setAcitvityMessage("Disabled Google authenticator");
				userSecurityDetails.setCreateTime(new Date());
				userSecurityDetails.setIpAddess(twoFaDisableDto.getIpAddress());
				userSecurityDetails.setSource(twoFaDisableDto.getSource());
				userSecurityDetails.setStatus(UserSecurityStatus.COMPLETED);
				userSecurityDetails.setUser(user);
				userSecurityDetailsDao.save(userSecurityDetails);
				return new Response<>(200,
						messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
			}
			return new Response<>(205,
					messageSource.getMessage("usermanagement.invalid.goolgecode", new Object[0], Locale.US));

		} else {
			throw new UserNotFoundException(
					String.format("user not found for disable the 2FA with the id : %d", userId));
		}

	}

	@Override
	public Response<Object> userStatus(String userStatus, Long userId) {
		Optional<User> userDetails = userDao.findByUserId(userId);
		if (userDetails.isPresent()) {
			User user = userDetails.get();
			if (userStatus.equalsIgnoreCase("block")) {
				user.setUserStatus(UserStatus.BLOCK);
				userDao.save(user);
			}
			return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
		} else {
			throw new UserNotFoundException(String.format("no user found for the kycId : %d", userId));
		}
	}

	@Override
	public Response<Object> forgetPassword(String email, String webUrl, String ipAddress, String location) {
		Optional<User> emailVerification = userDao.findByEmailAndUserStatus(email, UserStatus.ACTIVE);
		LOGGER.debug("user details are :", emailVerification);
		if (emailVerification.isPresent()) {
			String token1 = UUID.randomUUID().toString();
			String url = String.join(TOKEN, webUrl, token1);

			Map<String, String> setData = new HashMap<>();
			setData.put(EMAIL_TOKEN, email);
			setData.put(URL_TOKEN, url);
			EmailDto emailDto = new EmailDto(emailVerification.get().getUserId(),
					"reset_password_email_with_link_to_reset_password", email, setData);

			Boolean success = notificationClient.sendNotification(emailDto);

			if (Boolean.TRUE.equals(success)) {
				Optional<TokenDetails> token = tokenDao.findByUserId(emailVerification.get().getUserId());
				TokenDetails tokenDetails;
				if (token.isPresent()) {
					tokenDetails = token.get();
					tokenDetails.setCreateTime(new Date());
				} else {
					tokenDetails = new TokenDetails();
					tokenDetails.setUserId(emailVerification.get().getUserId());
				}
				tokenDetails.setToken(token1);
				tokenDao.save(tokenDetails);
				return new Response<>(200,
						messageSource.getMessage("usermanagement.forget.password.messgae", new Object[0], Locale.US));
			} else {
				return new Response<>(205,
						messageSource.getMessage("usermanagement.failure", new Object[0], Locale.US));
			}
		} else {
			throw new UserNotFoundException(String.format("Please register with us"));
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> resetPassword(ResetPasswordDto resetPasswordDto) {
		String token = resetPasswordDto.getToken();
		User user;
		Response<Object> validToken = checkValidToken(token);
		if (validToken.getStatus() == 200) {
			Optional<TokenDetails> tokenDetails = (Optional<TokenDetails>) validToken.getData();
			if (tokenDetails.isPresent()) {
				Optional<User> checkUser = userDao.findByUserId(tokenDetails.get().getUserId());
				LOGGER.debug("user details are :", checkUser);
				if (checkUser.isPresent()) {
					user = checkUser.get();
					user.setPassword(bCryptPasswordEncoder.encode(resetPasswordDto.getPassword()));
					userDao.save(user);
				}
				tokenDao.deleteById(tokenDetails.get().getTokenId());
			}
		} else if (validToken.getStatus() == 205) {
			return new Response<>(205,
					messageSource.getMessage(USERMANAGEMENT_TOKEN_EXPIRED, new Object[0], Locale.US));
		} else {
			return new Response<>(400,
					messageSource.getMessage(USERMANAGEMENT_RESETPASSWORD_ALREADY_CHANGED, new Object[0], Locale.US));
		}
		return new Response<>(200, "Password Reset Successfully");
	}

	@Override
	public Response<Object> checkValidToken(String token) {
		Optional<TokenDetails> verifyToken = tokenDao.findByToken(token);
		if (verifyToken.isPresent()) {
			long currentTime = System.currentTimeMillis();
			long creationTime = verifyToken.get().getCreateTime().getTime();
			if (currentTime < creationTime + tokenExpirationTime) {
				return new Response<>(200, "Success", verifyToken);
			} else {
				return new Response<>(205,
						messageSource.getMessage(USERMANAGEMENT_TOKEN_EXPIRED, new Object[0], Locale.US));
			}
		} else {
			return new Response<>(400,
					messageSource.getMessage(USERMANAGEMENT_TOKEN_INVALID, new Object[0], Locale.US));
		}
	}

	private int checkTokervaildByEmail(String email) {
		Optional<TokenDetails> verifyToken = tokenDao.findByEmail(email);
		if (verifyToken.isPresent()) {
			long currentTime = System.currentTimeMillis();
			long creationTime = verifyToken.get().getCreateTime().getTime();
			if (currentTime < creationTime + tokenExpirationTime) {
				return 200;
			} else {
				return 201;
			}
		} else {
			return 202;
		}
	}

	@Override
	public Response<Object> changePassword(ChangePasswordDto changePasswordDto, Long userId) {
		Optional<User> verifyUser = userDao.findByUserId(userId);
		LOGGER.debug("user details are :", verifyUser);
		if (verifyUser.isPresent()) {
			if (bCryptPasswordEncoder.matches(changePasswordDto.getOldPassword(), verifyUser.get().getPassword())) {
				if (!bCryptPasswordEncoder.matches(changePasswordDto.getNewPassword(),
						verifyUser.get().getPassword())) {
					User user = verifyUser.get();
					user.setPassword(bCryptPasswordEncoder.encode(changePasswordDto.getNewPassword()));
					userDao.save(user);
					return new Response<>(200, messageSource.getMessage("usermanagement.password.changed.successfully",
							new Object[0], Locale.US));
				} else {
					return new Response<>(205,
							messageSource.getMessage("usermanagement.change.password.oldpassword.newpassword.different",
									new Object[0], Locale.US));
				}
			} else {
				return new Response<>(205, messageSource.getMessage(
						"usermanagement.change.password.oldpassword.not.matched", new Object[0], Locale.US));
			}
		}
		throw new UserNotFoundException(
				String.format("could not change the password ,User not found with id : %d ", userId));
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> verifyUser(String token) {
		Response<Object> validToken = checkValidToken(token);
		if (validToken.getStatus() == 200) {
			Optional<TokenDetails> tokenDetails = (Optional<TokenDetails>) validToken.getData();
			Optional<User> randomId = userDao.findByUserId(tokenDetails.get().getUserId());
			if (tokenDetails.isPresent()) {
				Optional<User> verifyUser = userDao.findByUserId(tokenDetails.get().getUserId());
				LOGGER.debug("user details are :", verifyUser);
				if (verifyUser.isPresent()) {
					Response<String> walletResponse = walletClient.createWallet(tokenDetails.get().getUserId(),
							randomId.get().getRandomId());
					if (walletResponse.getStatus() == 200) {
						User user = verifyUser.get();
						user.setUserStatus(UserStatus.UNVERIFIED);
						user.setEmailVerificationTime(new Date());
						userDao.save(user);
						Response<Object> success1 = sendMobileOtp(randomId.get().getUserDetail().getPhoneNo());
						if (success1.getStatus() == 200) {
							return new Response<>(200,
									"An OTP has been sent on your entered MobileNo. Please validate the account");
						}

						mailSend.sendWelcomeMail(verifyUser.get().getEmail(), "Welcome to Crypto Currency",
								verifyUser.get().getEmail());
						tokenDao.deleteById(tokenDetails.get().getTokenId());

						return new Response<>(200, messageSource.getMessage("usermanagement.verifyuser.success",
								new Object[0], Locale.US));
					} else {
						return new Response<>(205, messageSource.getMessage("usermanagement.verifyuser.resend.email",
								new Object[0], Locale.US));
					}
				}
			}
			return new Response<>(400,
					messageSource.getMessage("usermanagement.verifyuser.already.verified", new Object[0], Locale.US));
		} else if (validToken.getStatus() == 205) {
			return new Response<>(205,
					messageSource.getMessage("usermanagement.token.expired", new Object[0], Locale.US));
		} else {
			return new Response<>(400,
					messageSource.getMessage("usermanagement.verifyuser.already.verified", new Object[0], Locale.US));
		}

	}

	@Override
	@Transactional
	public Response<Object> profileUpdate(ProfileUpdateDto profileUpdateDto, Long userId) {
		Optional<User> verifyUser = userDao.findByUserId(userId);
		LOGGER.debug("user details are :", verifyUser);
		if (verifyUser.isPresent()) {
			verifyUser.get().setRandomId(profileUpdateDto.getRandomId());
			User checkUser = verifyUser.get();
			User user = mergeWithExistingUser(checkUser, profileUpdateDto);
			userDao.save(user);
			return new Response<>(200,
					messageSource.getMessage("usermanagement.profileupdate.success", new Object[0], Locale.US));
		} else {
			throw new UserNotFoundException(
					String.format("User not found with the id : %d for update the profile ", userId));
		}
	}

	private User mergeWithExistingUser(User checkUser, ProfileUpdateDto profileUpdateDto) {
		UserDetail userDetail = checkUser.getUserDetail();
		userDetail.setFirstName(profileUpdateDto.getFirstName());
		userDetail.setLastName(profileUpdateDto.getLastName());
		userDetail.setMiddleName(profileUpdateDto.getMiddleName());
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
		userDetail.setZipCode(profileUpdateDto.getZipCode());

		checkUser.setUserDetail(userDetail);
		return checkUser;

	}

	public String generateToken(User user) {
		long now = System.currentTimeMillis();
		return Jwts.builder().setSubject(user.getEmail()).claim("userId", user.getUserId())
				.claim("email", user.getEmail()).setIssuedAt(new Date(now))
				.setExpiration(new Date(now + expirationTime * 1000))
				.signWith(SignatureAlgorithm.HS512, tokenSecretKey.getBytes()).compact();
	}

	@Override
	public Response<Object> sendVerifyUserEmail(String email, String webUrl, Locale locale) {
		Optional<User> user = userDao.findByEmail(email);
		if (user.isPresent()) {
			Integer otp = MobileSMSUsingTwilio.generateVerificationCodeMobileApp();
			String token1 = otp.toString();
			if (mailSend.sendResetLinkVerifyUser(user.get().getEmail(), "Verify User", token1,
					user.get().getUserDetail().getFirstName())) {
				Optional<TokenDetails> token = tokenDao.findByUserId(user.get().getUserId());
				TokenDetails tokenDetails;
				if (token.isPresent()) {
					tokenDetails = token.get();
					tokenDetails.setCreateTime(new Date());
				} else {
					tokenDetails = new TokenDetails();
					tokenDetails.setUserId(user.get().getUserId());
				}
				tokenDetails.setToken(token1);
				tokenDao.save(tokenDetails);
				Map<String, String> setData = new HashMap<>();
				setData.put(MessageConstant.EMAIL_TOKEN, email);

				EmailDto emailDto = new EmailDto(user.get().getUserId(), "registration_welcome_email",
						user.get().getEmail(), setData);

				return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], locale));
			}
			return new Response<>(205,
					messageSource.getMessage("usermanagement.signup.email.failure", new Object[0], locale));
		} else {
			throw new UserNotFoundException(
					String.format("User not found with the email : %s for sending the email ", email));
		}
	}

	@Override
	public Response<User> saveLoginDetail(LoginDto loginDto, Long userId) {
		UserLoginDetail userLoginDetail = modelMapper.map(loginDto, UserLoginDetail.class);
		User user = new User();
		user.setUserId(userId);
		user.setUserStatus(loginDto.getUserStatus());
		user.setEmail(loginDto.getEmail());
		userLoginDetail.setUser(user);
		userLoginDetailDao.save(userLoginDetail);
		List<UserLoginDetail> userLoginDetails = userLoginDetailDao.findByUserUserId(userId);
		userLoginDetail = userLoginDetails.get(0);
		loginDto.setIpAddress(userLoginDetail.getIpAddress());
		loginDto.setCreateTime(userLoginDetail.getCreateTime());
		loginDto.setEmail(userLoginDetail.getEmail());
		loginDto.setUserAgent(userLoginDetail.getUserAgent());
		loginDto.setBrowserPrint(userLoginDetail.getBrowserPrint());
		return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
	}

	@Override
	public Response<UserEmailDto> getEmail(Long userId) {
		Optional<User> user = userDao.findByUserId(userId);
		if (user.isPresent()) {
			UserEmailDto userEmailDto = new UserEmailDto();
			userEmailDto.setEmail(user.get().getEmail());
			return new Response<>(userEmailDto);
		}
		return new Response<>(201, "Not Found");
	}

	@Override
	public Response<Object> skipTwoFa(Long userId, String ipAddress, String source) {
		Optional<User> checkUser = userDao.findByUserId(userId);
		if (checkUser.isPresent()) {
			User user = checkUser.get();
			UserDetail userDetail = user.getUserDetail();
			if (userDetail.getTwoFaType().equals(TwoFaType.NONE)) {
				userDetail.setTwoFaType(TwoFaType.SKIP);
				user.setUserDetail(userDetail);
				userDao.save(user);
				UserSecurityDetails userSecurityDetails = new UserSecurityDetails();
				userSecurityDetails.setAcitvityMessage("Skipped Security authenticator");
				userSecurityDetails.setCreateTime(new Date());
				userSecurityDetails.setIpAddess(ipAddress);
				userSecurityDetails.setSource(source);
				userSecurityDetails.setStatus(UserSecurityStatus.SKIP);
				userSecurityDetails.setUser(user);
				userSecurityDetailsDao.save(userSecurityDetails);
				return new Response<>(200,
						messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
			} else {
				return new Response<>(201,
						messageSource.getMessage("usermanagement.first.disable.auth", new Object[0], Locale.US));
			}
		} else {
			throw new UserNotFoundException(String.format("User not found with the userId : %d for TwoFa ", userId));
		}
	}

	@Override
	public Response<UserEmailAndNameDto> getEmailAndName(Long userId) {
		Optional<User> user = userDao.findByUserId(userId);
		Optional<KYC> data = kycDao.findByUserUserId(userId);
		if (user.isPresent()) {
			UserEmailAndNameDto userEmailAndNameDto = new UserEmailAndNameDto();
			String name = user.get().getUserDetail().getFirstName() + " " + user.get().getUserDetail().getLastName();
			userEmailAndNameDto.setEmail(user.get().getEmail());
			userEmailAndNameDto.setName(name);
			userEmailAndNameDto.setUserId(userId);
			userEmailAndNameDto.setImageUrl(user.get().getUserDetail().getImageUrl());
			userEmailAndNameDto.setTwoFaType(user.get().getUserDetail().getTwoFaType());
			userEmailAndNameDto.setUserStatus(user.get().getUserStatus());
			Long diff = ChronoUnit.DAYS.between(user.get().getCreateTime().toInstant(), new Date().toInstant());
			System.out.println(diff);
			userEmailAndNameDto.setJoinigDate(String.valueOf(user.get().getCreateTime()));
			userEmailAndNameDto.setJoinDays(diff);
			if (data.isPresent()) {
				userEmailAndNameDto.setKyc(String.valueOf(data.get().getKycStatus()));
			}
			return new Response<>(userEmailAndNameDto);
		}
		return new Response<>(205, "Not Found");
	}

	@Override
	public Response<Object> sendEmailToVerifyIPAddress(VerifyIPAddressDto verifyIPAddressDto) {
		Optional<User> checkUser = userDao.findByUserId(verifyIPAddressDto.getUserId());
		LOGGER.debug("user details are :", checkUser);
		if (checkUser.isPresent()) {
			String token = UUID.randomUUID().toString();
			String url = String.join("?token=", verifyIPAddressDto.getWebUrl(), token);
			boolean success = mailSend.sendEmailIpAddressVerification(checkUser.get().getEmail(),
					"Verify Your IP Address", url, verifyIPAddressDto.getIpAddress());
			if (success) {
				Optional<TokenDetails> token1 = tokenDao.findByUserId(verifyIPAddressDto.getUserId());
				TokenDetails tokenDetails;
				if (token1.isPresent()) {
					tokenDetails = token1.get();
					tokenDetails.setCreateTime(new Date());
				} else {
					tokenDetails = new TokenDetails();
					tokenDetails.setUserId(verifyIPAddressDto.getUserId());
				}
				tokenDetails.setToken(token);
				tokenDetails.setIpAddress(verifyIPAddressDto.getIpAddress());
				tokenDao.save(tokenDetails);
				return new Response<>(200,
						messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
			} else {
				return new Response<>(205,
						messageSource.getMessage("usermanagement.signup.email.failure", new Object[0], Locale.US));
			}
		} else {
			throw new UserNotFoundException(
					String.format("No user found with userId address: %d", verifyIPAddressDto.getUserId()));
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> verifyIpAddress(VerifyIPAddressDto verifyIPAddressDto) {
		String token = verifyIPAddressDto.getToken();
		Response<Object> validToken = checkValidToken(token);
		if (validToken.getStatus() == 200) {
			Optional<TokenDetails> tokenDetails = (Optional<TokenDetails>) validToken.getData();
			if (tokenDetails.isPresent()) {
				LoginDto loginDto = new LoginDto();
				loginDto.setIpAddress(tokenDetails.get().getIpAddress());
				saveLoginDetail(loginDto, tokenDetails.get().getUserId());
				tokenDao.deleteById(tokenDetails.get().getTokenId());
			}
		} else if (validToken.getStatus() == 205) {
			return new Response<>(205,
					messageSource.getMessage("usermanagement.token.expired", new Object[0], Locale.US));
		} else {
			return new Response<>(400,
					messageSource.getMessage("usermanagement.ipaddress.already.verified", new Object[0], Locale.US));
		}
		return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));

	}

	@Override
	public Response<Object> saveDeviceTokenDetails(Long userId, DeviceTokenDto deviceTokenDto) {
		DeviceTokenDetails deviceTokenDetails = new DeviceTokenDetails();
		deviceTokenDetails.setDeviceToken(deviceTokenDto.getDeviceToken());
		deviceTokenDetails.setDeviceType(deviceTokenDto.getDeviceType());
		User user = new User();
		user.setUserId(userId);
		deviceTokenDetails.setUser(user);
		deviceTokenDetailsDao.save(deviceTokenDetails);
		return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
	}

	@Override
	@Transactional
	public Response<Object> smsDisable(TwoFaDisableDto twoFaDisableDto, Long userId) {
		Optional<User> userDetail = userDao.findByUserId(userId);
		if (userDetail.isPresent()) {
			Optional<SmsDetails> checkOtp = smsDetailsDao.findByUserId(userId);
			if (checkOtp.isPresent()) {
				if (checkOtp.get().getOtp().equals(twoFaDisableDto.getOtp())) {
					User user = userDetail.get();
					user.getUserDetail().setSecretKey("NULL");
					user.getUserDetail().setTwoFaType(TwoFaType.NONE);
					userDao.save(user);
					smsDetailsDao.deleteById(checkOtp.get().getOtpId());
					UserSecurityDetails userSecurityDetails = new UserSecurityDetails();
					userSecurityDetails.setAcitvityMessage("Disabled SMS authenticator");
					userSecurityDetails.setCreateTime(new Date());
					userSecurityDetails.setIpAddess(twoFaDisableDto.getIpAddress());
					userSecurityDetails.setSource(twoFaDisableDto.getSource());
					userSecurityDetails.setStatus(UserSecurityStatus.COMPLETED);
					userSecurityDetails.setUser(user);
					userSecurityDetailsDao.save(userSecurityDetails);
					return new Response<>(200,
							messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
				}
				return new Response<>(205,
						messageSource.getMessage("usermanagement.invalidcode", new Object[0], Locale.US));
			} else {
				throw new UserNotFoundException(
						String.format("User not found for verifying OTP with the Id : %d", userId));
			}

		} else {
			throw new UserNotFoundException(
					String.format("user not found for disable the SMS with the id : %d", userId));
		}
	}

	@Override
	@Transactional
	public Response<Object> deleteDeviceToken(Long userId, DeviceTokenDto deviceTokenDto) {
		try {
			Optional<DeviceTokenDetails> deviceTokenDetails = deviceTokenDetailsDao
					.findByUserUserIdAndDeviceToken(userId, deviceTokenDto.getDeviceToken());
			if (deviceTokenDetails.isPresent()) {
				deviceTokenDetailsDao.deleteById(deviceTokenDetails.get().getDeviceTokenId());
				return new Response<>(200,
						messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
			} else {
				throw new UserNotFoundException(
						String.format("deviceToken not found with the userId  : %d for delete the token ", userId));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException("Something went wrong");
		}

	}

	@Override
	public Response<KycStatus> getKycStatus(Long userId) {

		Optional<User> getUserDetailsByUserId = userDao.findByUserId(userId);
		if (getUserDetailsByUserId.isPresent()) {
			List<KYC> kycs = getUserDetailsByUserId.get().getKyc();
			if (!kycs.isEmpty()) {
				KYC kyc = new KYC();
				for (KYC kyc2 : kycs) {
					kyc = kyc2;
				}
				return new Response<>(200, "success", kyc.getKycStatus());
			} else
				return new Response<>(205, "failure");
		} else
			return new Response<>(205, "failure");
	}

	@Override
	public Response<String> getPhoneNumber(Long userId) {

		Optional<UserDetail> checkUser = userDetailsDao.findById(userId);
		if (checkUser.isPresent()) {
			return new Response<>(200, "Success", checkUser.get().getPhoneNo());
		} else {
			return new Response<>(205, "failure");

		}
	}

	private Integer checkForUniqueOTP() {
		Integer otp = awsSnsUtil.generateRandomOtp();
		if (!Boolean.TRUE.equals(smsDetailsDao.existsByOtp(otp)))
			return otp;
		else
			return checkForUniqueOTP();
	}

	@Override
	public Response<String> getAdminEmailId() {
		try {
			List<User> getAdminDetails = userDao.findUserByRoleRole(RoleStatus.ADMIN);
			if (!getAdminDetails.isEmpty())
				return new Response<>(200, "Admin email address fetched", getAdminDetails.get(0).getEmail());
			else
				return new Response<>(205, "No admin found, create one!!!");
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException("Something went wrong");
		}
	}

	@Override
	public Response<Object> getUserLoginDetails(Long userId, Long userIdForLoginHistoy) {
		try {
			List<UserLoginDetail> userLoginDetails = userLoginDetailDao
					.findByUserUserIdOrderByUserLoginIdDesc(userIdForLoginHistoy);
			if (!userLoginDetails.isEmpty())
				return new Response<>(200, messageSource
						.getMessage("usermanagement.get.user.login.details.successfully", new Object[0], Locale.US),
						userLoginDetails);
			else {
				return new Response<>(201,
						messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage());
			throw new RuntimeException("Something went wrong");
		}
	}

	@Override
	public Response<Object> getUserList(Long userId) {
		List<User> list = userDao.findByUserStatusAndRoleRole(UserStatus.ACTIVE, RoleStatus.SUBADMIN);
		List<ListUserDto> data = new LinkedList<>();

		if (!list.isEmpty()) {

			list.parallelStream().forEachOrdered(a -> {
				ListUserDto dto = new ListUserDto();
				dto.setEmail(a.getEmail());
				dto.setUserId(a.getUserId());
				dto.setUserName(a.getUserDetail().getFirstName() + a.getUserDetail().getLastName());
				data.add(dto);
			});
			return new Response<>(200, "No admin found, create one!!!", data);
		} else

			return new Response<>(205, "No user found, create one!!!");
	}

	@Override
	public Response<Object> updateId(Long userId, String oldRandomId, String newRandomId) {
		Optional<User> data = userDao.findByRandomId(oldRandomId);
		if (data.isPresent()) {
			data.get().setRandomId(newRandomId);
			userDao.save(data.get());
			return new Response<>(200, "randomId updated SuccessFully.");

		} else
			return new Response<>(205, "No randomId found, create one!!!");
	}

	@Override
	public Response<Object> blockedUser(Long userId) {
		try {
			Optional<User> userExist = userDao.findByUserId(userId);
			if (userExist.isPresent()) {
				User user = userExist.get();
				user.setUserStatus(UserStatus.BLOCK);
				userDao.save(user);
				return new Response<>(200, "USER_BLOCKED_SUCCESSFULLY");
			} else {
				return new Response<>(201, "USER_DOES_NOT_EXIST");
			}
		} catch (Exception e) {
			return new Response<>(203, messageSource.getMessage(SOMETHING_WENT_WRONG, new Object[0], Locale.US));

		}

	}

	@Override
	public Response<Object> sendVerifyUserOtp(Long userId, Locale locale) {
		Optional<User> user = userDao.findByUserId(userId);
		if (user.isPresent()) {
			Integer otp = MobileSMSUsingTwilio.generateVerificationCode();
			String token1 = otp.toString();
			if (mailSend.sendResetLinkVerifyUser(user.get().getEmail(), "Verify User", token1,
					user.get().getUserDetail().getFirstName())) {
				Optional<EmailSmsDetails> token = emailSmsDao.findByUserId(user.get().getUserId());
				EmailSmsDetails tokenDetails;
				if (token.isPresent()) {
					tokenDetails = token.get();
					tokenDetails.setCreateTime(new Date());
				} else {
					tokenDetails = new EmailSmsDetails();
					tokenDetails.setUserId(user.get().getUserId());
				}
				tokenDetails.setEmailOtp(token1);
				emailSmsDao.save(tokenDetails);
				Map<String, String> setData = new HashMap<>();
				setData.put(MessageConstant.EMAIL_TOKEN, user.get().getEmail());
				EmailDto emailDto = new EmailDto(user.get().getUserId(), "otp_welcome_email", user.get().getEmail(),
						setData);
				return new Response<>(200, "An Otp has been sent to your registered email-Id");
			}
			return new Response<>(205, "mail not sent  with the email", user.get().getEmail());
		} else {
			throw new UserNotFoundException(
					String.format("User not found with the email : %s for sending the email ", user.get().getEmail()));
		}
	}

	@Override
	public Response<Object> verifyEmailSmsCode(Long userId, @Valid EmailSmsDto emailSmsDto) {
		Optional<User> userDetails = userDao.findById(userId);
		if (userDetails.isPresent()) {
			Optional<EmailSmsDetails> checkOtp = emailSmsDao.findByUserId(userId);
			if (checkOtp.isPresent()) {
				long currentTime = System.currentTimeMillis();
				long creationTime = checkOtp.get().getCreateTime().getTime();
				if (currentTime < creationTime + otpExpirationTime) {
					if (checkOtp.get().getEmailOtp().equals(emailSmsDto.getEmailOtp())) {
						emailSmsDao.deleteById(checkOtp.get().getOtpId());
						userDetails.get().getUserDetail().setTwoFaType(TwoFaType.EMAIL);
						userDao.save(userDetails.get());
						UserSecurityDetails userSecurityDetails = new UserSecurityDetails();
						userSecurityDetails.setAcitvityMessage("Enabled SMS authenticator");
						userSecurityDetails.setCreateTime(new Date());
						userSecurityDetails.setIpAddess(emailSmsDto.getIpAddress());
						userSecurityDetails.setSource(emailSmsDto.getSource());
						userSecurityDetails.setStatus(UserSecurityStatus.COMPLETED);
						userSecurityDetails.setUser(userDetails.get());
						userSecurityDetailsDao.save(userSecurityDetails);
						return new Response<>(200,
								messageSource.getMessage("usermanagement.sms.otp.verified", new Object[0], Locale.US));
					} else {
						return new Response<>(201,
								messageSource.getMessage("usermanagement.sms.otp.invalid", new Object[0], Locale.US));
					}
				} else {
					return new Response<>(205,
							messageSource.getMessage("usermanagement.sms.otp.expired", new Object[0], Locale.US));
				}
			} else {
				throw new UserNotFoundException(
						String.format("User not found for verifying OTP with the Id : %d", userId));
			}
		} else {
			throw new UserNotFoundException(String.format("No user found with userId : %d", userId));
		}
	}

	@Override
	@Transactional
	public Response<Object> emailsmsDisable(TwoFaEmailDisableDto twoFaEmailDisableDto, Long userId) {
		Optional<User> userDetail = userDao.findByUserId(userId);
		if (userDetail.isPresent()) {
			Optional<EmailSmsDetails> checkOtp = emailSmsDao.findByUserId(userId);
			if (checkOtp.isPresent()) {
				if (checkOtp.get().getEmailOtp().equals(twoFaEmailDisableDto.getEmailOtp())) {
					User user = userDetail.get();
					user.getUserDetail().setSecretKey("NULL");
					user.getUserDetail().setTwoFaType(TwoFaType.NONE);
					userDao.save(user);
					emailSmsDao.deleteById(checkOtp.get().getOtpId());
					UserSecurityDetails userSecurityDetails = new UserSecurityDetails();
					userSecurityDetails.setAcitvityMessage("Disabled SMS authenticator");
					userSecurityDetails.setCreateTime(new Date());
					userSecurityDetails.setIpAddess(twoFaEmailDisableDto.getIpAddress());
					userSecurityDetails.setSource(twoFaEmailDisableDto.getSource());
					userSecurityDetails.setStatus(UserSecurityStatus.COMPLETED);
					userSecurityDetails.setUser(user);
					userSecurityDetailsDao.save(userSecurityDetails);
					return new Response<>(200,
							messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
				}
				return new Response<>(205,
						messageSource.getMessage("usermanagement.invalidcode", new Object[0], Locale.US));
			} else {
				throw new UserNotFoundException(
						String.format("User not found for verifying OTP with the Id : %d", userId));
			}

		} else {
			throw new UserNotFoundException(
					String.format("user not found for disable the SMS with the id : %d", userId));
		}
	}

	@Override
	public Response<Object> sendPhoneNoCode(String mobileNo) {

		Optional<UserDetail> userDetails = userDetailsDao.findByPhoneNo(mobileNo);
		LOGGER.debug("user details are :", userDetails);
		if (userDetails.isPresent()) {
			String sendTo = userDetails.get().getPhoneNo();
			if (sendTo != null) {
				if (!sendTo.contains("+")) {
					sendTo = "+" + sendTo;
				}

				String body = "Otp Send";
				Integer sendotp = MobileSMSUsingTwilio.generateVerificationCode();

				Message message = mobileSMSUsingTwilio.sendCode(sendTo, body, sendotp);
				if (!message.getStatus().equals(Status.FAILED) && !message.getStatus().equals(Status.UNDELIVERED)) {

					Optional<SmsDetails> checkUser = smsDetailsDao.findByUserId(userDetails.get().getUserDetailId());
					if (checkUser.isPresent()) {
						checkUser.get().setCreateTime(new Date());
					} else {
						checkUser.get().setPhoneNo(mobileNo);
					}
					checkUser.get().setUserId(userDetails.get().getUserDetailId());
					checkUser.get().setOtp(sendotp);
					smsDetailsDao.save(checkUser.get());
					return new Response<>(200,
							messageSource.getMessage("usermanagement.otp.send.success", new Object[0], Locale.US));
				}
			} else {
				return new Response<>(205,
						messageSource.getMessage("usermanagement.phoneno.not.exist", new Object[0], Locale.US));
			}
			return new Response<>(205,
					messageSource.getMessage("usermanagement.otp.send.failed", new Object[0], Locale.US));
		} else {
			return new Response<>(205, "User does not exists . Please Register To Bit Bharat");
		}
	}

	@Override
	public Response<Object> checkValidotp(Integer otp) {
		Optional<SmsDetails> verifyToken = smsDetailsDao.findByOtp(otp);
		if (verifyToken.isPresent()) {
			long currentTime = System.currentTimeMillis();
			long creationTime = verifyToken.get().getCreateTime().getTime();
			if (currentTime < creationTime + tokenExpirationTime) {
				return new Response<>(200, "Success", verifyToken);
			} else {
				return new Response<>(205,
						messageSource.getMessage(USERMANAGEMENT_TOKEN_EXPIRED, new Object[0], Locale.US));
			}
		} else {
			return new Response<>(400,
					messageSource.getMessage(USERMANAGEMENT_TOKEN_INVALID, new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> verifyphoneSmsCode(String phoneNo, @Valid PhoneOtp emailSmsDto) {
		Response<Object> validToken = checkValidotp(emailSmsDto.getOtp());
		if (validToken.getStatus() == 200) {
			Optional<UserDetail> userDetails = userDetailsDao.findByPhoneNo(phoneNo);
			if (userDetails.isPresent()) {
				Optional<SmsDetails> checknumber = smsDetailsDao.findByOtp(emailSmsDto.getOtp());
				Optional<User> verifyUser = userDao.findByUserId(checknumber.get().getUserId());
				LOGGER.debug("user details are :", verifyUser);
				if (verifyUser.isPresent()) {

					if (checknumber.get().getOtp().equals(emailSmsDto.getOtp())) {

						User user = verifyUser.get();
						user.setUserStatus(UserStatus.ACTIVE);
						user.setEmailVerificationTime(new Date());

						userDao.save(user);
						checknumber.get().setOtp(emailSmsDto.getOtp());
						return new Response<>(200, "Otp Verified Successfully");
					} else {
						return new Response<>(205, "Otp Already Verified");

					}
				} else {
					return new Response<>(205, "User Not Found");
				}

			} else {

				return new Response<>(205, "PhoneNo not found");
			}

		} else if (validToken.getStatus() == 205) {

			return new Response<>(205, "Otp Expired");
		} else {
			return new Response<>(205, "Invalid OTP");
		}
	}

	@Override
	public Response<Object> verifyEmailCode(Long userId, EmailDtoNew verifySmsCodeDto) {
		Optional<User> userDetails = userDao.findById(userId);
		if (userDetails.isPresent()) {
			Optional<SmsDetails> checkOtp = smsDetailsDao.findByUserId(userId);
			if (checkOtp.isPresent()) {
				long currentTime = System.currentTimeMillis();
				long creationTime = checkOtp.get().getCreateTime().getTime();
				if (currentTime < creationTime + otpExpirationTime) {
					if (checkOtp.get().getOtp().equals(verifySmsCodeDto.getOtp())) {
						smsDetailsDao.deleteById(checkOtp.get().getOtpId());
						userDetails.get().getUserDetail().setTwoFaType(TwoFaType.EMAIL);
						userDao.save(userDetails.get());
						UserSecurityDetails userSecurityDetails = new UserSecurityDetails();
						userSecurityDetails.setAcitvityMessage("Enabled Email authenticator");
						userSecurityDetails.setCreateTime(new Date());
						userSecurityDetails.setIpAddess(verifySmsCodeDto.getIpAddress());
						userSecurityDetails.setSource(verifySmsCodeDto.getSource());
						userSecurityDetails.setStatus(UserSecurityStatus.COMPLETED);
						userSecurityDetails.setUser(userDetails.get());
						userSecurityDetailsDao.save(userSecurityDetails);
						return new Response<>(200, "Otp Verified Successfully");
					} else {
						return new Response<>(201, "Otp is invalid");
					}
				} else {
					return new Response<>(205, "Otp Expired");
				}
			} else {
				throw new UserNotFoundException(
						String.format("User not found for verifying OTP with the Id : %d", userId));
			}
		} else {
			throw new UserNotFoundException(String.format("No user found with userId : %d", userId));
		}
	}

	@Override
	public Response<Object> deactivateAccount(Long userId) {
		Optional<User> userExists = userDao.findById(userId);
		if (userExists.isPresent()) {
			userExists.get().setUserStatus(UserStatus.DEACTIVATE);
			userDao.save(userExists.get());
			return new Response<>(200, "Account Deactivated");
		}
		return new Response<>(205, "User Not Present");
	}

	@Override
	public Response<Object> getTotalReffalCount(String myReferralCode, Long userId) {
		Map<String, Object> map1 = new HashMap<>();
		Map<String, Object> map5 = new HashMap<>();
		List<UserDetail> getUserDetails = userDetailsDao.findByReferredCode(myReferralCode);
		List<UserDetail> getUserDetails1 = userDetailsDao.findByFinalReferal(myReferralCode);
		Optional<UserDetail> getUser = userDetailsDao.findByMyRefferalCode(myReferralCode);
		List<AdminReferal> admindata = adminreferalDao.findAll();

		Map<String, Object> map2 = new HashMap<>();
		Map<String, Object> map = new HashMap<>();
		String sizeNew = String.valueOf(getUserDetails1.size());
		String size = String.valueOf(getUserDetails.size());
		String countByMyReferralCode = size;
		BigDecimal earn;
		if (getUser.isPresent()) {
			if (admindata.get(0).getLimit().compareTo(getUser.get().getTotalReferalPrice()) == 1) {
				if (Long.valueOf(countByMyReferralCode) >= 1) {

					for (UserDetail data : getUserDetails) {
						Optional<KYC> getKycStatus = kycDao.findByKycStatusAndUserUserId(KycStatus.ACCEPTED,
								data.getUser().getUserId());
						if (getKycStatus.isPresent()) {
							if (getKycStatus.get().getKycStatus().equals(KycStatus.ACCEPTED)) {
								Response<StorageDetailDto> response = walletClient.getStorageDetailsCoinHotNew("XINDIA",
										"HOT");
								System.out.print("1732");
								earn = response.getData().getHotWalletBalance()
										.subtract(admindata.get(0).getReferalAmountRegister());
								admindata.get(0).setAvailablefund(earn);
								walletClient.updateBalance(earn, "XINDIA");
								BigDecimal walletbalance = admindata.get(0).getReferalAmountRegister();
								BigDecimal data1 = admindata.get(0).getDistributedFund();
								BigDecimal value = data1.add(walletbalance);
								admindata.get(0).setDistributedFund(value);

								walletClient.updateWalletNew(walletbalance, getUser.get().getUser().getUserId(),
										"XINDIA");
								getUser.get().setTotalReferalPrice(getUser.get().getTotalReferalPrice()
										.add(admindata.get(0).getReferalAmountRegister()));
								ReferalComission referalComission = new ReferalComission();
								try {
									String apiBaseUri1 = "http://3.213.77.140:3015/eth/getMarketPriceById?ids=india";
									String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, USDT_TIMEOUT);
									LOGGER.info(responseString1);
									Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1,
											Map.class);
									if (allData1.containsKey("USD")) {
										LOGGER.info(allData1.get("USD"));
										BigDecimal newValue = BigDecimal
												.valueOf(Double.valueOf(String.valueOf(allData1.get("USD"))));
										LOGGER.info(newValue);
										referalComission.setLiveAmount(newValue);
										referalComission.setDepositLiveAmount(
												(admindata.get(0).getReferalAmountRegister().multiply(newValue)));
									}
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
								referalComission.setAmount(admindata.get(0).getReferalAmountRegister());
								referalComission.setCoinName("XINDIA");
								referalComission.setEmail(data.getUser().getEmail());
								referalComission.setMobileNo(data.getPhoneNo());
								referalComission.setUserId(data.getUser().getUserId());
								referalComission.setUserName(data.getFirstName() + " " + data.getLastName());
								referalComission.setReferedId(userId);
								referalComissionDao.save(referalComission);
								data.setReferredCode(null);
								System.out.print("1753");
								getUser.get().setTierTwoReferal(getUser.get().getTierTwoReferal()
										.add(admindata.get(0).getReferalAmountRegister()));
								adminreferalDao.save(admindata.get(0));

								userDetailsDao.save(data);
								userDetailsDao.save(getUser.get());

								Long add = Long.valueOf(1);
								Long add1 = Long.valueOf(getUser.get().getDirectReferCount());
								Long referDirect = Long.sum(add1, add);
								getUser.get().setDirectReferCount(referDirect.toString());
								userDetailsDao.save(getUser.get());
								map.put("Count", getUser.get().getDirectReferCount());
								map.put("Status", ReferalEnum.COMPLETED);
								map.put("Earn", getUser.get().getTotalReferalPrice());
							}
						}
					}

				}
			}
			if (admindata.get(0).getLimit().compareTo(getUser.get().getTotalReferalPrice()) == 1) {
				if (Long.valueOf(sizeNew) >= 1) {
					for (UserDetail data3 : getUserDetails1) {
						Optional<KYC> getKycStatus1 = kycDao.findByKycStatusAndUserUserId(KycStatus.ACCEPTED,
								data3.getUser().getUserId());

						if (getKycStatus1.isPresent()) {
							Response<StorageDetailDto> response = walletClient.getStorageDetailsCoinHotNew("XINDIA",
									"HOT");
							if (getKycStatus1.get().getKycStatus().equals(KycStatus.ACCEPTED)) {
								earn = response.getData().getHotWalletBalance()
										.subtract(admindata.get(0).getReferalAmountRegister());
								admindata.get(0).setAvailablefund(earn);
								walletClient.updateBalance(earn, "XINDIA");
								BigDecimal walletbalance1 = admindata.get(0).getReferalAmountRegister();
								BigDecimal data2 = admindata.get(0).getDistributedFund();
								BigDecimal value1 = data2.add(walletbalance1);
								admindata.get(0).setDistributedFund(value1);
								System.out.print("1791");
								walletClient.updateWalletNew(walletbalance1, getUser.get().getUser().getUserId(),
										"XINDIA");
								getUser.get().setTotalReferalPrice(getUser.get().getTotalReferalPrice()
										.add(admindata.get(0).getReferalAmountRegister()));
								getUser.get().setTierThreeReferal(getUser.get().getTierThreeReferal()
										.add(admindata.get(0).getReferalAmountRegister()));
								ReferalComission referalComission = new ReferalComission();
								try {
									String apiBaseUri1 = "http://3.213.77.140:3015/eth/getMarketPriceById?ids=india";
									String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, USDT_TIMEOUT);
									LOGGER.info(responseString1);
									Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1,
											Map.class);
									if (allData1.containsKey("USD")) {
										LOGGER.info(allData1.get("USD"));
										BigDecimal newValue = BigDecimal
												.valueOf(Double.valueOf(String.valueOf(allData1.get("USD"))));
										LOGGER.info(newValue);
										referalComission.setLiveAmount(newValue);
										referalComission.setDepositLiveAmount(
												(admindata.get(0).getReferalAmountRegister().multiply(newValue)));
									}
								} catch (Exception e) {
									throw new RuntimeException(e);
								}
								referalComission.setAmount(admindata.get(0).getReferalAmountRegister());
								referalComission.setCoinName("XINDIA");
								referalComission.setEmail(data3.getUser().getEmail());
								referalComission.setMobileNo(data3.getPhoneNo());
								referalComission.setUserId(data3.getUser().getUserId());
								referalComission.setUserName(data3.getFirstName() + " " + data3.getLastName());
								referalComission.setReferedId(userId);
								referalComissionDao.save(referalComission);

								data3.setFinalReferal(null);
								adminreferalDao.save(admindata.get(0));
								userDetailsDao.save(getUser.get());

								Long add = Long.valueOf(1);
								Long add1 = Long.valueOf(getUser.get().getIndirectrefer());
								Long referDirect = Long.sum(add1, add);
								getUser.get().setIndirectrefer(referDirect.toString());
								userDetailsDao.save(getUser.get());
								map1.put("Count", getUser.get().getIndirectrefer());
								map1.put("Status", ReferalEnum.COMPLETED);
								map1.put("Earn", getUser.get().getTotalReferalPrice());
							}
						}

					}

				}
			}
			String new1 = String.valueOf(getUser.get().getRegisterBonus());
			String new12 = "0.00";
			if (Long.valueOf(countByMyReferralCode) == 0 && new1.equals(new12)) {
				if (getUser.get().getUser().getUserStatus().equals(UserStatus.ACTIVE)) {
					Response<StorageDetailDto> response = walletClient.getStorageDetailsCoinHotNew("XINDIA", "HOT");
					earn = response.getData().getHotWalletBalance()
							.subtract(admindata.get(0).getReferalAmountRegister());
					admindata.get(0).setAvailablefund(earn);
					walletClient.updateBalance(earn, "XINDIA");
					BigDecimal walletbalance = admindata.get(0).getReferalAmountRegister();
					BigDecimal data1 = admindata.get(0).getDistributedFund();
					BigDecimal value = data1.add(walletbalance);
					admindata.get(0).setDistributedFund(value);
					ReferalComission referalComission = new ReferalComission();
					try {
						String apiBaseUri1 = "http://3.213.77.140:3015/eth/getMarketPriceById?ids=india";
						String responseString1 = APIUtils.extractGetAPIData(apiBaseUri1, USDT_TIMEOUT);
						LOGGER.info(responseString1);
						Map<String, Object> allData1 = APIUtils.getJavaObjectFromJsonString(responseString1, Map.class);
						if (allData1.containsKey("USD")) {
							LOGGER.info(allData1.get("USD"));
							BigDecimal newValue = BigDecimal
									.valueOf(Double.valueOf(String.valueOf(allData1.get("USD"))));
							LOGGER.info(newValue);
							referalComission.setAmount(admindata.get(0).getReferalAmountRegister());
							referalComission.setLiveAmount(newValue);
							referalComission.setDepositLiveAmount(
									(admindata.get(0).getReferalAmountRegister().multiply(newValue)));
						}
					} catch (Exception e) {
						throw new RuntimeException(e);
					}
					referalComission.setCoinName("XINDIA");
					referalComission.setType("DIRECT");
					walletClient.updateWalletNew(walletbalance, userId, "XINDIA");
					getUser.get().setRegisterBonus(walletbalance);
					adminreferalDao.save(admindata.get(0));
					userDetailsDao.save(getUser.get());

					map5.put("Count", countByMyReferralCode);
					map5.put("Status", ReferalEnum.COMPLETED);
					map5.put("Earn", getUser.get().getTotalReferalPrice());

				}
			}
			map2.put("1", map5);
			map2.put("2", map);
			map2.put("3", map1);
			return new Response<>(200, "total refferal friends ", map2);
		}

		else {

			return new Response<>(205, "No Data Present");

		}

	}

	@Override
	public Response<Object> Tier1Referal(String myReferralCode, Long userId) {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> map1 = new HashMap<>();
		Map<String, Object> map2 = new HashMap<>();

		Map<String, Object> mapFinal = new HashMap<>();
		Optional<User> isOptional = userDao.findByUserStatusAndUserId(UserStatus.ACTIVE, userId);
		if (isOptional.isPresent()) {
			map.put("Count", 0);
			map.put("Status", ReferalEnum.COMPLETED);
			map.put("Earn", isOptional.get().getUserDetail().getRegisterBonus());
			if (isOptional.get().getUserDetail().getDirectReferCount() != "0") {
				map1.put("Count", isOptional.get().getUserDetail().getDirectReferCount());
				map1.put("Status", ReferalEnum.COMPLETED);
				map1.put("Earn", isOptional.get().getUserDetail().getTierTwoReferal());
			}
			if (isOptional.get().getUserDetail().getIndirectrefer() != "0") {
				map2.put("Count", isOptional.get().getUserDetail().getIndirectrefer());
				map2.put("Status", ReferalEnum.COMPLETED);
				map2.put("Earn", isOptional.get().getUserDetail().getTierThreeReferal());
			}

			mapFinal.put("Tier1", map);
			mapFinal.put("Tier2", map1);
			mapFinal.put("Tier3", map2);
			return new Response<>(200, "Referal List", mapFinal);
		}
		return new Response<>(205, "Not Found");
	}

	@Override
	public Response<Object> refferUser(Long userId) {

		Map<String, Object> map = new HashMap<>();
		List<ReferalComission> fullList = referalComissionDao.findByReferedIdAndTypeNull(userId);
		Optional<UserDetail> data = userDetailsDao.findByUserDetailId(userId);
		Optional<User> exists = userDao.findByUserId(userId);
		if (exists.isPresent()) {
			if (!fullList.isEmpty()) {
				map.put("FullList", fullList);
			}
			if (data.isPresent()) {
				map.put("TotalEarn", data.get().getTotalReferalPrice().add(data.get().getRegisterBonus()));
			}
			return new Response<>(200, "Data Fetched Successfully", map);
		}
		return new Response<>(205, "No Data Found");

	}

	@Override
	public Response<Object> cmc() throws JSONException {

		String result = null;
		String uri = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest?CMC_PRO_API_KEY=2c1d6e89-43c9-45af-a935-08929d351c22";
		List<NameValuePair> paratmers = new ArrayList<NameValuePair>();
		paratmers.add(new BasicNameValuePair("start", "1"));
		paratmers.add(new BasicNameValuePair("limit", "5000"));
		paratmers.add(new BasicNameValuePair("sort", "market_cap"));
		paratmers.add(new BasicNameValuePair("cryptocurrency_type", "all"));
		paratmers.add(new BasicNameValuePair("tag", "all"));

		try {
			result = makeAPICall(uri, paratmers);

		} catch (IOException e) {
			System.out.println("Error: cannont access content - " + e.toString());
		} catch (URISyntaxException e) {
			System.out.println("Error: Invalid URL " + e.toString());
		}
		return new Response<>(200, "Response Content", result);
	}

	public static String makeAPICall(String uri, List<NameValuePair> parameters)
			throws URISyntaxException, IOException {
		String response_content = "";

		URIBuilder query = new URIBuilder(uri);
		query.addParameters(parameters);

		CloseableHttpClient client = HttpClients.createDefault();
		HttpGet request = new HttpGet(query.build());

		request.setHeader(HttpHeaders.ACCEPT, "application/json");
		request.addHeader("X-CMC_PRO_API_KEY", apiKey);

		CloseableHttpResponse response = client.execute(request);

		try {
			HttpEntity entity = response.getEntity();
			response_content = EntityUtils.toString(entity);
			EntityUtils.consume(entity);
		} finally {
			response.close();
		}

		return response_content;
	}

	@Override
	public Response<Object> registredUser() {
		Role role = new Role();
		role.setRole(RoleStatus.USER);
		long count = adminUserDao.countByRoleRole(RoleStatus.USER);
		return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US), count);
	}

	@Override
	public Response<UserEmailAndNameDto> fullUser() {
		List<User> newData = userDao.findAll();

		if (!newData.isEmpty()) {
			for (User user : newData) {
				UserEmailAndNameDto andNameDto = new UserEmailAndNameDto();
				andNameDto.setUserId(user.getUserId());
				return new Response<>(andNameDto);
			}
		}
		return new Response<>(205, "No Data");

	}

	@Override
	public Response<List<ReferalComission>> referCommisionList(Long userId,String coinName) {
		List<ReferalComission> data = referalComissionDao.findByReferedIdAndTypeAndCoinName(userId, "DIRECT", coinName);
		if (!data.isEmpty()) {
			return new Response<>(200, "Success", data);
		}

		return new Response<>(205, "No Data Found",data);
	}
}
