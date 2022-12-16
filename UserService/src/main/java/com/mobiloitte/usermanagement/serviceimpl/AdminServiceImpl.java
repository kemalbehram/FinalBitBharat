package com.mobiloitte.usermanagement.serviceimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mobiloitte.usermanagement.constants.EmailConstants;
import com.mobiloitte.usermanagement.constants.MessageConstant;
import com.mobiloitte.usermanagement.dao.AdminActionDao;
import com.mobiloitte.usermanagement.dao.AdminreferalDao;
import com.mobiloitte.usermanagement.dao.RoleDao;
import com.mobiloitte.usermanagement.dao.UserDao;
import com.mobiloitte.usermanagement.dao.UserDetailsDao;
import com.mobiloitte.usermanagement.dao.UserKycLimitDao;
import com.mobiloitte.usermanagement.dao.UserLoginDetailsDao;
import com.mobiloitte.usermanagement.dto.ActivityLogDto;
import com.mobiloitte.usermanagement.dto.AddAdminDto;
import com.mobiloitte.usermanagement.dto.AdminDetailsDto;
import com.mobiloitte.usermanagement.dto.AdminDto;
import com.mobiloitte.usermanagement.dto.CommonDto;
import com.mobiloitte.usermanagement.dto.EmailDto;
import com.mobiloitte.usermanagement.dto.NotificationDto;
import com.mobiloitte.usermanagement.dto.ReferalDto;
import com.mobiloitte.usermanagement.dto.SearchAndFilterDto;
import com.mobiloitte.usermanagement.dto.SubAdminDto;
import com.mobiloitte.usermanagement.dto.SuspendUserDto;
import com.mobiloitte.usermanagement.dto.UpdateUserKycDto;
import com.mobiloitte.usermanagement.dto.UserListDto;
import com.mobiloitte.usermanagement.enums.AdminAction;
import com.mobiloitte.usermanagement.enums.LogType;
import com.mobiloitte.usermanagement.enums.NotiType;
import com.mobiloitte.usermanagement.enums.P2pStatus;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.exception.RunTimeException;
import com.mobiloitte.usermanagement.exception.UserNotFoundException;
import com.mobiloitte.usermanagement.feign.NotificationClient;
import com.mobiloitte.usermanagement.model.AdminActionDetails;
import com.mobiloitte.usermanagement.model.AdminReferal;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.Role;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.model.UserDetail;
import com.mobiloitte.usermanagement.model.UserKycLimit;
import com.mobiloitte.usermanagement.model.UserLoginDetail;
import com.mobiloitte.usermanagement.service.AdminService;
import com.mobiloitte.usermanagement.util.ActivityMainClass;
import com.mobiloitte.usermanagement.util.ActivityUtil;
import com.mobiloitte.usermanagement.util.MailSender;
import com.mobiloitte.usermanagement.util.TwoFaType;

@Service
public class AdminServiceImpl extends MessageConstant implements AdminService {

	private static final Logger LOGGER = LogManager.getLogger(AdminServiceImpl.class);

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserDetailsDao userDetailsDao;

	@Autowired
	private AdminActionDao adminActionDao;
	@Autowired
	private AdminreferalDao adminreferalDao;
	@Autowired
	UserServiceImpl userServiceImpl;
	@Autowired
	private MailSender mailSender;

	@Autowired
	UserKycLimitDao userKycLimitDao;
	@Autowired
	private UserDetailsDao detailsDao;
	@Autowired
	private UserLoginDetailsDao userLoginDetailsDao;

	@Autowired
	private MessageSource messageSource;
	@Autowired
	private NotificationClient notificationClient;
	@Autowired
	private EntityManager em;

	ModelMapper modelMapper = new ModelMapper();

	@Value("${tokenSecretKey}")
	private String tokenSecretKey;

	@Value("${jwtconfig.expirationTime}")
	private int expirationTime;

	@Value("${tokenExpirationTime}")
	private int tokenExpirationTime;

	@Value("${spring.project.name}")
	private String projectName;

	@Autowired
	private ActivityUtil activityUtil;

	@Value("${isActivityEnabled}")
	private Boolean isActivityEnabled;
	@Autowired
	MailSender mailSend;

	@Override
	public Response<Object> userStatus(Long userId, String role, String username, String userStatus,
			Long userIdForStatusUpdate, String ipAddress, String location) {
		Optional<User> userDetails = userDao.findByUserId(userIdForStatusUpdate);
		if (userDetails.isPresent()) {
			User user = userDetails.get();
			if (userStatus.equalsIgnoreCase("block")) {
				user.setUserStatus(UserStatus.BLOCK);

				String subject = "Blocked User Information Mail";
				Boolean check = mailSend.sendEailAfterBlockUser(userDetails.get().getEmail(), subject);
				Map<String, String> setData = new HashMap<>();
				setData.put(EMAIL_TOKEN, userDetails.get().getEmail());
				EmailDto emailDto = new EmailDto(userDetails.get().getUserId(), BLOCKED_ACCOUNT,
						userDetails.get().getEmail(), setData);
				notificationClient.sendNotification(emailDto);
				userDao.save(user);
			} else if (userStatus.equalsIgnoreCase("active")) {
				user.setUserStatus(UserStatus.ACTIVE);
				String subject = "UnBlocked User Information Mail";
				Boolean check = mailSend.sendEailAfterBlockUser1(userDetails.get().getEmail(), subject);
				Map<String, String> setData = new HashMap<>();
				setData.put(EMAIL_TOKEN, userDetails.get().getEmail());
				EmailDto emailDto = new EmailDto(userDetails.get().getUserId(), UNBLOCKED_ACCOUNT,
						userDetails.get().getEmail(), setData);
				notificationClient.sendNotification(emailDto);
				userDao.save(user);
			}
			if (isActivityEnabled) {
				ActivityLogDto activityLogDto = new ActivityLogDto("UpdateUserStatus", userDetails.get().getUserId(),
						userDetails.get().getUserId(), ipAddress, location);
				ActivityLogDto activityLogDtos = new ActivityLogDto(LogType.UPDATED, "Update User Status Successfully",
						userDetails.get().getEmail(), userDetails.get().getRole().getRole().name());
				activityUtil.isActivitySaved(new ActivityMainClass(activityLogDto, activityLogDtos));
			}
			return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));

		} else {
			throw new UserNotFoundException(String.format("no user found for the kycId : %d", userIdForStatusUpdate));
		}
	}

	@Override
	public Response<User> deleteUserDetail(Long userId, String role, String username, Long userIdToDelete,
			String ipAddress, String location) {
		Optional<User> userDetails = userDao.findByUserId(userIdToDelete);
		if (userDetails.isPresent()) {
			User user = userDetails.get();
			user.setUserStatus(UserStatus.DELETED);
			userDao.save(user);
			if (isActivityEnabled) {
				ActivityLogDto activityLogDto = new ActivityLogDto("DeleteUser", userIdToDelete,
						userDetails.get().getUserId(), ipAddress, location);
				ActivityLogDto activityLogDtos = new ActivityLogDto(LogType.DELETED, "Delete User Successfully",
						userDetails.get().getEmail(), userDetails.get().getRole().getRole().name());
				activityUtil.isActivitySaved(new ActivityMainClass(activityLogDto, activityLogDtos));
			}
			return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
		} else {
			throw new UserNotFoundException(String.format("no data found for the id : %d", userIdToDelete));
		}
	}

	@Override
	public Response<Object> createSubAdmin(Long userId, SubAdminDto subAdminDto) {

		Optional<User> userDetails = userDao.findById(userId);
		if (userDetails.isPresent()) {
			Optional<Role> roles = roleDao.findById(userDetails.get().getRole().getRoleId());
			if (roles.isPresent() && roles.get().getRole() == RoleStatus.SUBADMIN) {
				return new Response<>(205, messageSource.getMessage(
						"usermanagement.you.dont.have.permission.for.this.action", new Object[0], Locale.US));
			}
		}

		Optional<Role> existRole = roleDao.findByRole(subAdminDto.getRoleStatus());
		boolean existByEmail = userDao.existsByEmail(subAdminDto.getEmail());
		boolean existingUserWithPhoneNo = userDetailsDao.existsByPhoneNo(subAdminDto.getPhoneNo());
		if (existRole.isPresent() && !existByEmail && !existingUserWithPhoneNo) {
			User user = new User();
			user.setCreateTime(new Date());
			user.setEmail(subAdminDto.getEmail());
			user.setUpdateTime(new Date());
			UserDetail userDetail2 = new UserDetail();
			userDetail2.setAddress(subAdminDto.getAddress());
			userDetail2.setCity(subAdminDto.getCity());
			userDetail2.setCountry(subAdminDto.getCountry());
			userDetail2.setCountryCode(subAdminDto.getCountryCode());
			userDetail2.setCreateTime(new Date());
			userDetail2.setDob(subAdminDto.getDob());
			userDetail2.setZipCode(subAdminDto.getZipCode());
			userDetail2.setFirstName(subAdminDto.getFirstName());
			userDetail2.setGender(subAdminDto.getGender());
			userDetail2.setImageUrl(subAdminDto.getImageUrl());
			userDetail2.setLastName(subAdminDto.getLastName());
			userDetail2.setPhoneNo(subAdminDto.getPhoneNo());
			userDetail2.setPnWithoutCountryCode(null);
			userDetail2.setSecretKey(null);
			userDetail2.setSocialId(null);
			userDetail2.setSocialType(null);
			userDetail2.setState(subAdminDto.getState());
			userDetail2.setTwoFaType(TwoFaType.NONE);
			userDetail2.setUpdateTime(new Date());
			user.setRole(existRole.get());
			user.setUserDetail(userDetail2);
			user.setUserStatus(UserStatus.ACTIVE);
			if (existRole.get().getRole() != RoleStatus.ADMIN && userDetails.isPresent()) {
				AdminActionDetails actionDetails = new AdminActionDetails();
				actionDetails.setUser(userDetails.get());
				actionDetails.setAdminAction(AdminAction.CREATE_SUBADMIN);
				actionDetails.setMessage("create the SUBADMIN");
				adminActionDao.save(actionDetails);
			} else if (existRole.get().getRole() == RoleStatus.ADMIN && userDetails.isPresent()) {
				AdminActionDetails actionDetails = new AdminActionDetails();
				actionDetails.setUser(userDetails.get());
				actionDetails.setAdminAction(AdminAction.CREATE_ADMIN);
				actionDetails.setMessage("create the ADMIN");
				adminActionDao.save(actionDetails);
			}
			user.setPrevilage(subAdminDto.getPrevilage());
			User savedUser = userDao.save(user);
			if (isActivityEnabled && existRole.get().getRole() == RoleStatus.SUBADMIN) {
				ActivityLogDto activityLogDto = new ActivityLogDto("SubAdmin", savedUser.getUserId(),
						userDetails.get().getUserId(), subAdminDto.getIpAddress(), subAdminDto.getLocation());
				ActivityLogDto activityLogDtos = new ActivityLogDto(LogType.CREATED, "Create SubAdmin Successfully",
						userDetails.get().getEmail(), userDetails.get().getRole().getRole().name());
				activityUtil.isActivitySaved(new ActivityMainClass(activityLogDto, activityLogDtos));
			} else if (isActivityEnabled && existRole.get().getRole() == RoleStatus.ADMIN) {
				ActivityLogDto activityLogDto = new ActivityLogDto("AddAdmin", savedUser.getUserId(),
						userDetails.get().getUserId(), subAdminDto.getIpAddress(), subAdminDto.getLocation());
				ActivityLogDto activityLogDtos = new ActivityLogDto(LogType.CREATED, "Add Admin Successfully",
						userDetails.get().getEmail(), userDetails.get().getRole().getRole().name());
				activityUtil.isActivitySaved(new ActivityMainClass(activityLogDto, activityLogDtos));
			}
			Response<Object> success = userServiceImpl.forgetPassword(savedUser.getEmail(), subAdminDto.getWebUrl(), "",
					"");
			if (success.getStatus() == 200) {
				return new Response<>(200, "Sub-admin created successfully");
			} else {
				return new Response<>(201,
						messageSource.getMessage("usermanagement.signup.email.failure", new Object[0], Locale.US));
			}

		} else if (existByEmail) {
			return new Response<>(205,
					messageSource.getMessage("usermanagement.signup.email.exist", new Object[0], Locale.US));
		} else if (existingUserWithPhoneNo) {
			return new Response<>(205,
					messageSource.getMessage("usermanagement.signup.phone.exist", new Object[0], Locale.US));
		} else {
			return new Response<>(400, messageSource.getMessage("usermanagement.failure", new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> editSubAdmin(Long userId, String username, String role, @Valid SubAdminDto subAdminDto) {
		Optional<User> userDetails = userDao.findById(userId);
		if (userDetails.isPresent()) {
			Optional<Role> roles = roleDao.findById(userDetails.get().getRole().getRoleId());
			if (roles.isPresent() && roles.get().getRole() == RoleStatus.SUBADMIN) {
				return new Response<>(205, messageSource.getMessage(
						"usermanagement.you.dont.have.permission.for.this.action", new Object[0], Locale.US));
			}
		}
		Optional<User> userDetailToUpdate = userDao.findByUserId(subAdminDto.getUserIdToUpdate());
		Optional<Role> existRole = roleDao.findByRole(subAdminDto.getRoleStatus());
		boolean existByEmail = userDao.existsByEmail(subAdminDto.getEmail());
		if (userDetailToUpdate.isPresent()) {
			if (existByEmail) {
				if (existRole.isPresent()) {
					userDetailToUpdate.get().setUpdateTime(new Date());
					userDetailToUpdate.get().getUserDetail().setAddress(subAdminDto.getAddress());
					userDetailToUpdate.get().getUserDetail().setCity(subAdminDto.getCity());
					userDetailToUpdate.get().getUserDetail().setCountry(subAdminDto.getCountry());
					userDetailToUpdate.get().getUserDetail().setCountryCode(subAdminDto.getCountryCode());
					userDetailToUpdate.get().getUserDetail().setUpdateTime(new Date());
					userDetailToUpdate.get().getUserDetail().setDob(subAdminDto.getDob());
					userDetailToUpdate.get().getUserDetail().setFirstName(subAdminDto.getFirstName());
					userDetailToUpdate.get().getUserDetail().setGender(subAdminDto.getGender());
					userDetailToUpdate.get().getUserDetail().setImageUrl(subAdminDto.getImageUrl());
					userDetailToUpdate.get().getUserDetail().setLastName(subAdminDto.getLastName());
					userDetailToUpdate.get().getUserDetail().setPhoneNo(subAdminDto.getPhoneNo());
					userDetailToUpdate.get().getUserDetail().setZipCode(subAdminDto.getZipCode());
					userDetailToUpdate.get().getUserDetail().setPnWithoutCountryCode(null);
					userDetailToUpdate.get().getUserDetail().setSecretKey(null);
					userDetailToUpdate.get().getUserDetail().setSocialId(null);
					userDetailToUpdate.get().getUserDetail().setSocialType(null);
					userDetailToUpdate.get().getUserDetail().setState(subAdminDto.getState());
					userDetailToUpdate.get().getUserDetail().setTwoFaType(TwoFaType.NONE);
					userDetailToUpdate.get().setRole(existRole.get());
					userDetailToUpdate.get().setUserDetail(userDetailToUpdate.get().getUserDetail());
					userDetailToUpdate.get().setUserStatus(UserStatus.ACTIVE);
					if (existRole.get().getRole() != RoleStatus.ADMIN && userDetails.isPresent()) {
						AdminActionDetails actionDetails = new AdminActionDetails();
						actionDetails.setUser(userDetails.get());
						actionDetails.setAdminAction(AdminAction.UPDATE_SUBADMIN);
						actionDetails.setMessage("update the SUBADMIN");
						adminActionDao.save(actionDetails);
					}
					userDetailToUpdate.get().setPrevilage(subAdminDto.getPrevilage());
					User savedUser = userDao.save(userDetailToUpdate.get());
					if (isActivityEnabled) {
						ActivityLogDto activityLogDto = new ActivityLogDto("UpdateSubAdmin", savedUser.getUserId(),
								userDetails.get().getUserId(), subAdminDto.getIpAddress(), subAdminDto.getLocation());
						ActivityLogDto activityLogDtos = new ActivityLogDto(LogType.UPDATED,
								"Update SubAdmin Successfully", userDetails.get().getEmail(),
								userDetails.get().getRole().getRole().name());
						activityUtil.isActivitySaved(new ActivityMainClass(activityLogDto, activityLogDtos));
					}
					return new Response<>(200, messageSource.getMessage("usermanagement.edit.staff.successfully",
							new Object[0], Locale.US));
				} else {
					return new Response<>(205,
							messageSource.getMessage("usermanagement.role.not.found", new Object[0], Locale.US));
				}
			} else {
				return new Response<>(205,
						messageSource.getMessage("usermanagement.you.cannot.update.email", new Object[0], Locale.US));
			}
		} else {
			throw new UserNotFoundException(
					String.format("No user found for the userId : %d", subAdminDto.getUserIdToUpdate()));
		}
	}

	@Override
	public Response<Object> addAdmin(Long userId, String username, String role, @Valid AddAdminDto addAdminDto) {
		Optional<User> userDetails = userDao.findById(userId);
		if (userDetails.isPresent()) {
			Optional<Role> roles = roleDao.findById(userDetails.get().getRole().getRoleId());
			if (roles.isPresent() && roles.get().getRole() == RoleStatus.SUBADMIN) {
				return new Response<>(205, messageSource.getMessage(
						"usermanagement.you.dont.have.permission.for.this.action", new Object[0], Locale.US));
			}
		}
		boolean existByEmail = userDao.existsByEmail(addAdminDto.getEmail());
		boolean existingUserWithPhoneNo = userDetailsDao.existsByPhoneNo(addAdminDto.getPhoneNo());
		Optional<Role> existRole = roleDao.findByRole(RoleStatus.ADMIN);
		if (existRole.isPresent() && !existByEmail && !existingUserWithPhoneNo) {
			User user = new User();
			user.setCreateTime(new Date());
			user.setEmail(addAdminDto.getEmail());
			user.setUpdateTime(new Date());
			UserDetail userDetail2 = new UserDetail();
			userDetail2.setCreateTime(new Date());
			userDetail2.setFirstName(addAdminDto.getFirstName());
			userDetail2.setGender(addAdminDto.getGender());
			userDetail2.setLastName(addAdminDto.getLastName());
			userDetail2.setPhoneNo(addAdminDto.getPhoneNo());
			userDetail2.setPnWithoutCountryCode(null);
			userDetail2.setSecretKey(null);
			userDetail2.setSocialId(null);
			userDetail2.setSocialType(null);
			userDetail2.setTwoFaType(TwoFaType.NONE);
			userDetail2.setUpdateTime(new Date());
			user.setRole(existRole.get());
			user.setUserDetail(userDetail2);
			user.setUserStatus(UserStatus.ACTIVE);
			if (existRole.get().getRole() != RoleStatus.ADMIN && userDetails.isPresent()) {
				AdminActionDetails actionDetails = new AdminActionDetails();
				actionDetails.setUser(userDetails.get());
				actionDetails.setAdminAction(AdminAction.CREATE_ADMIN);
				actionDetails.setMessage("create the ADMIN");
				adminActionDao.save(actionDetails);
			}
			user.setPrevilage(addAdminDto.getPrevilage());
			User savedUser = userDao.save(user);
			if (isActivityEnabled) {
				ActivityLogDto activityLogDto = new ActivityLogDto("AddAdmin", savedUser.getUserId(),
						userDetails.get().getUserId(), addAdminDto.getIpAddress(), addAdminDto.getLocation());
				ActivityLogDto activityLogDtos = new ActivityLogDto(LogType.CREATED, "Add Admin Successfully",
						userDetails.get().getEmail(), userDetails.get().getRole().getRole().name());
				activityUtil.isActivitySaved(new ActivityMainClass(activityLogDto, activityLogDtos));
			}
			Response<Object> success = userServiceImpl.forgetPassword(savedUser.getEmail(), addAdminDto.getWebUrl(), "",
					"");
			if (success.getStatus() == 200) {
				return new Response<>(200,
						messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
			} else {
				return new Response<>(201,
						messageSource.getMessage("usermanagement.signup.email.failure", new Object[0], Locale.US));
			}

		} else if (existByEmail) {
			return new Response<>(205,
					messageSource.getMessage("usermanagement.signup.email.exist", new Object[0], Locale.US));
		} else if (existingUserWithPhoneNo) {
			return new Response<>(205,
					messageSource.getMessage("usermanagement.signup.phone.exist", new Object[0], Locale.US));
		} else {
			return new Response<>(400, messageSource.getMessage("usermanagement.failure", new Object[0], Locale.US));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> filterUserDetails(Long userId, UserStatus status, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize, RoleStatus roleStatus, String country, Long newUserId) {
		Optional<User> adminData = userDao.findById(userId);
		Optional<Role> adminRole = roleDao.findById(adminData.get().getRole().getRoleId());
		StringBuilder query = new StringBuilder(
				"select u.userId, u.createTime, u.email, u.userDetail.firstName as firstName,u.userDetail.lastName as lastName,u.userStatus, u.userDetail.phoneNo, u.userDetail.country,u.userDetail.twoFaType, u.randomId,u.userDetail.p2pStatus,u.userDetail.userDetailId from User u");
		if (adminRole.isPresent() && adminRole.get().getRole().equals(RoleStatus.ADMIN)) {
			if (roleStatus != null) {
				LOGGER.debug("Adding role Status", roleStatus);
				query.append(" where u.role.role='SUBADMIN'");
			} else {
				query.append(" where u.role.role='USER'");
			}
		} else if (adminRole.isPresent() && adminRole.get().getRole().equals(RoleStatus.SUBADMIN)
				&& roleStatus != null) {
			return new Response<>(200, "You Don't Have Permission.");
		} else {
			query.append(" where u.role.role='USER'");
		}
		if (status != null) {
			LOGGER.debug("filtering with user status {}", status);
			query.append(" and u.userStatus=:status");
		}
		if (country != null) {
			LOGGER.debug("filtering with user country {}", country);
			query.append(" and u.userDetail.country=:country");
		}
		if (fromDate != null) {
			LOGGER.debug("filtering with from date {}", new Date(fromDate));
			query.append(" and u.createTime > :fromDate");
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date(toDate));
			query.append(" and u.createTime < :toDate");
		}
		if (newUserId != null) {
			LOGGER.debug("filtering with userDetailId", newUserId);
			query.append(" and u.userDetail.userDetailId=:userDetailId");
		}
		if (search != null) {
			LOGGER.debug("filtering with name {}", search);
			query.append(
					" and ((u.userDetail.firstName like :search) or (u.userDetail.lastName like :search) or (u.email like :search) or (u.userDetail.phoneNo like :search) or (u.randomId like :search) or (u.userDetail.p2pStatus like :search))");
		}
		query.append(" order by u.createTime desc");
		Query createQuery = em.createQuery(query.toString());
		if (status != null)
			createQuery.setParameter("status", status);
		if (country != null)
			createQuery.setParameter("country", country);
		if (fromDate != null)
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null)
			createQuery.setParameter("toDate", new Date(toDate));
		if (newUserId != null)
			createQuery.setParameter("userDetailId", newUserId);
		if (search != null)
			createQuery.setParameter("search", '%' + search + '%');
		LOGGER.debug("Total Results {}", search);
		int filteredResultCount = createQuery.getResultList().size();
		LOGGER.debug("Total Results {}", filteredResultCount);
		if (page != null && pageSize != null) {
			createQuery.setFirstResult(page * pageSize);
			createQuery.setMaxResults(pageSize);
		}
		List<Object[]> list = createQuery.getResultList();
		List<UserListDto> response = list.parallelStream().map(o -> {
			UserListDto dto = new UserListDto();
			dto.setUserId((Long) o[0]);
			dto.setCreateTime((Date) o[1]);
			dto.setEmail((String) o[2]);
			dto.setCustomerName((String) o[3] + " " + (String) o[4]);
			dto.setFirstName((String) o[3]);
			dto.setLastName((String) o[4]);
			dto.setUserStatus((UserStatus) o[5]);
			dto.setPhoneNo((String) o[6]);
			dto.setCountry((String) o[7]);
			dto.setTwoFaType((TwoFaType) o[8]);
			dto.setRandomId((String) o[9]);
			dto.setP2pStatus((P2pStatus) o[10]);
			dto.setUserDetailId((Long) o[11]);
			Optional<UserLoginDetail> lastLoginDetails = userLoginDetailsDao
					.findTopByUserUserIdOrderByUserLoginIdDesc(Long.parseLong(String.valueOf(o[0])));
			if (lastLoginDetails.isPresent()) {
				dto.setLastLoginTime(lastLoginDetails.get().getCreateTime());
			}
			return dto;
		}).collect(Collectors.toList());
		Collections.sort(response, (a, b) -> b.getUserId().compareTo(a.getUserId()));
		Map<String, Object> data = new HashMap<>();
		data.put("list", response);
		data.put("totalCount", filteredResultCount);
		return new Response<>(200, "Details fetched successfully", data);

	}

	@Override
	@Transactional
	public Response<Object> updateUserKycLimit(UpdateUserKycDto updateUserKycDto) {
		List<UserKycLimit> checkUser = userKycLimitDao.findByUserId(updateUserKycDto.getUserId());
		if (!checkUser.isEmpty()) {
			for (UserKycLimit u : checkUser) {
				if (u.getCoinName().equalsIgnoreCase("USD")) {
					u.setLimitValue(updateUserKycDto.getUsdAmount());
				}
			}
			userKycLimitDao.saveAll(checkUser);
			return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US));
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US));
		}

	}

	@Override
	public Response<Object> getUserKycLimit(Long userId) {
		List<UserKycLimit> kycLimit = userKycLimitDao.findByUserId(userId);
		if (!kycLimit.isEmpty()) {
			return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US),
					kycLimit);
		} else
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US),
					new ArrayList<>());
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> searchAndFilterStaff(Long loggedInUserId, SearchAndFilterDto searchAndFiltersDto) {
		// try {
		Map<String, Object> map = new HashMap<>();
		StringBuilder query = new StringBuilder(
				"select u.email as email, d.firstName as firstname, d.phoneNo, u.createTime, u.updateTime,u.userStatus "
						+ " as status, r.role as role ,u.userId,d.lastName as lastname ,d.zipCode,d.dob,d.country,d.city,d.state,d.address,d.gender,d.countryCode"
						+ " from User u left join UserDetail d "
						+ "on(u.userDetail=d.userDetailId) left join Role r on(r.roleId=u.role.roleId)"
						+ "where u.role.role in ('SUBADMIN')");
		List<String> conditions = new ArrayList<>();
		if (searchAndFiltersDto.getSearch() != null) {
			conditions.add(
					" ((d.firstName like :search) or (d.lastName like :search) or (u.email like :search) or (r.role like :search)or(d.gender like :search)or(d.zipCode like :search)or(d.dob like :search)or(d.city like :search)or(d.country like :search)or(d.state like :search)or(d.address like :search)or(d.phoneNo like :search)or(d.countryCode like :search))");
		}
		StringBuilder updatedQuery = insertQueryConditions(searchAndFiltersDto, conditions, query);
		updatedQuery.append(ORDER_BY_DESC);
		Query createQuery = em.createQuery(String.valueOf(updatedQuery));
		if (searchAndFiltersDto.getSearch() != null)
			createQuery.setParameter(SEARCH, '%' + searchAndFiltersDto.getSearch() + '%');
		if (searchAndFiltersDto.getStatus() != null)
			createQuery.setParameter(STATUS, UserStatus.valueOf(searchAndFiltersDto.getStatus()));
		if (searchAndFiltersDto.getRole() != null)
			createQuery.setParameter("role", RoleStatus.valueOf(searchAndFiltersDto.getRole()));
		if (searchAndFiltersDto.getFromDate() != null)
			createQuery.setParameter(FROM_DATE, new Date(Long.parseLong(searchAndFiltersDto.getFromDate())));
		if (searchAndFiltersDto.getToDate() != null)
			createQuery.setParameter(TO_DATE, new Date(Long.parseLong(searchAndFiltersDto.getToDate())));
		int filteredResultCount = createQuery.getResultList().size();
		createQuery.setFirstResult(
				Integer.parseInt(searchAndFiltersDto.getPage()) * Integer.parseInt(searchAndFiltersDto.getPageSize()));
		createQuery.setMaxResults(Integer.parseInt(searchAndFiltersDto.getPageSize()));
		List<Object[]> list = createQuery.getResultList();
		List<AdminDetailsDto> response = returnAdminDetailList(list);
		if (!response.isEmpty()) {
			map.put("size", filteredResultCount);
			map.put("list", response);
			return new Response<>(569,
					messageSource.getMessage("usermanagement.search.staff.successfully", new Object[0], Locale.US),
					map);
		} else {
			return new Response<>(568, messageSource.getMessage(NO_DATA_FOUND, new Object[0], Locale.US));
		}

	}

	private List<AdminDetailsDto> returnAdminDetailList(List<Object[]> list) {
		return list.parallelStream().map(o -> {
			AdminDetailsDto dto = new AdminDetailsDto();
			dto.setEmail((String) o[0]);
			dto.setFirstName((String) o[1]);
			dto.setPhoneNo(String.valueOf(o[2]));
			dto.setCreatedTime((Date) o[3]);
			dto.setUpdatedTime((Date) o[4]);
			if (o[5] != null) {
				dto.setUserStatus(UserStatus.valueOf(String.valueOf(o[5])));
			}
			dto.setRole(RoleStatus.valueOf(String.valueOf(o[6])));
			dto.setUserId((String.valueOf(o[7])));
			dto.setLastName((String) o[8]);
			dto.setZipCode((String) o[9]);
			dto.setDob((String) o[10]);
			dto.setCountry((String) o[11]);
			dto.setCity((String) o[12]);
			dto.setState((String) o[13]);
			dto.setAddress((String) o[14]);
			dto.setGender((String) o[15]);
			dto.setCountryCode((String) o[16]);
			Optional<UserLoginDetail> lastLoginDetails = userLoginDetailsDao
					.findTopByUserUserIdOrderByUserLoginIdDesc(Long.parseLong(String.valueOf(o[7])));
			if (lastLoginDetails.isPresent()) {
				dto.setLastLoginTime(lastLoginDetails.get().getCreateTime());
			}
			return dto;
		}).collect(Collectors.toList());

	}

	private StringBuilder insertQueryConditions(SearchAndFilterDto searchAndFiltersDto, List<String> conditions,
			StringBuilder query) {
		if (searchAndFiltersDto.getStatus() != null) {
			conditions.add(USER_STATUS_AND_STATUS);
		}
		if (searchAndFiltersDto.getRole() != null) {
			conditions.add(" (r.role =:role) ");
		}
		if (searchAndFiltersDto.getFromDate() != null) {
			conditions.add(" (u.createTime >=:fromDate) ");
		}
		if (searchAndFiltersDto.getToDate() != null) {
			conditions.add(" (u.createTime <=:toDate) ");
		}
		if (!conditions.isEmpty()) {
			query.append(AND);
			query.append(String.join(AND, conditions.toArray(new String[0])));
		}

		return query;
	}

	@Override
	public Response<Object> getStaffUserProfile(Long userId, CommonDto commonDto) {
		try {
			Optional<User> staffUserDetails = userDao.findById(commonDto.getPrimaryIdCommonPerRequest());
			if (staffUserDetails.isPresent()) {
				Map<String, Object> response = new HashMap<>();
				response.put("staffPrivileges", staffUserDetails.get().getPrevilage());
				AdminDto adminDto = new AdminDto(staffUserDetails.get().getUserId(), staffUserDetails.get().getEmail(),
						staffUserDetails.get().getUserDetail().getFirstName(),
						staffUserDetails.get().getUserDetail().getAddress(),
						staffUserDetails.get().getUserDetail().getZipCode(),
						staffUserDetails.get().getUserDetail().getDob(),
						staffUserDetails.get().getUserDetail().getCountry(),
						staffUserDetails.get().getUserDetail().getCity(),
						staffUserDetails.get().getUserDetail().getState(),
						staffUserDetails.get().getUserDetail().getLastName(),
						staffUserDetails.get().getUserDetail().getPhoneNo(),
						staffUserDetails.get().getUserDetail().getGender(),
						staffUserDetails.get().getUserDetail().getImageUrl(),
						String.valueOf(staffUserDetails.get().getRole().getRole()),
						staffUserDetails.get().getUserDetail().getCountryCode(),
						staffUserDetails.get().getRole().getRoleId(), staffUserDetails.get().getUserStatus());
				response.put("staffDetails", adminDto);
				return new Response<>(561, messageSource.getMessage("usermanagement.fetch.staff.details.successfully",
						new Object[0], Locale.US), response);

			} else {
				return new Response<>(560, messageSource.getMessage(NO_DATA_FOUND, new Object[0], Locale.US));
			}
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	@Transactional
	public Response<Object> deteleStaff(Long loggedInUserId, String loggedInRole, String loggedInEmail,
			CommonDto commonDto) {
		Optional<User> staffUserDetails = userDao.findById(commonDto.getPrimaryIdCommonPerRequest());
		if (staffUserDetails.isPresent()) {
			staffUserDetails.get().setUserStatus(UserStatus.DELETED);
			staffUserDetails.get().setUpdateTime(new Date());
			User userData = userDao.save(staffUserDetails.get());
			if (isActivityEnabled) {
				ActivityLogDto activityLogDto = new ActivityLogDto("Staff", userData.getUserId(), loggedInUserId,
						commonDto.getIpAddress(), commonDto.getLocation());
				ActivityLogDto activityLogDtos = new ActivityLogDto(LogType.DELETED, "Delete Staff Successfully",
						loggedInEmail, loggedInRole);
				activityUtil.isActivitySaved(new ActivityMainClass(activityLogDto, activityLogDtos));
			}
			return new Response<>(660,
					messageSource.getMessage("usermanagement.staff.deleted.successfully", new Object[0], Locale.US));
		} else {
			throw new UserNotFoundException(USER_NOT_FOUND);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> searchAndFilterAdmin(Long userId, SearchAndFilterDto searchAndFiltersDto) {
		Map<String, Object> map = new HashMap<>();
		StringBuilder query = new StringBuilder(
				"select u.email as email, d.firstName as firstname, d.phoneNo as phoneno, u.createTime, u.updateTime,u.userStatus "
						+ " as status, r.role as role ,u.userId,d.lastName as lastname from User u left join UserDetail d "
						+ "on(u.userDetail=d.userDetailId) left join Role r on(r.roleId=u.role.roleId)"
						+ "where u.role.role in ('ADMIN')");
		List<String> conditions = new ArrayList<>();
		if (searchAndFiltersDto.getSearch() != null) {
			conditions.add(
					" ((d.firstName like :search) or (d.lastName like :search) or (u.email like :search) or (r.role like :search)) ");
		}
		StringBuilder updatedQuery = insertQueryConditions(searchAndFiltersDto, conditions, query);
		updatedQuery.append(ORDER_BY_DESC);
		Query createQuery = em.createQuery(String.valueOf(updatedQuery));
		if (searchAndFiltersDto.getSearch() != null)
			createQuery.setParameter(SEARCH, '%' + searchAndFiltersDto.getSearch() + '%');
		if (searchAndFiltersDto.getStatus() != null)
			createQuery.setParameter(STATUS, UserStatus.valueOf(searchAndFiltersDto.getStatus()));
		if (searchAndFiltersDto.getRole() != null)
			createQuery.setParameter("role", RoleStatus.valueOf(searchAndFiltersDto.getRole()));
		if (searchAndFiltersDto.getFromDate() != null)
			createQuery.setParameter(FROM_DATE, new Date(Long.parseLong(searchAndFiltersDto.getFromDate())));
		if (searchAndFiltersDto.getToDate() != null)
			createQuery.setParameter(TO_DATE, new Date(Long.parseLong(searchAndFiltersDto.getToDate())));
		int filteredResultCount = createQuery.getResultList().size();
		createQuery.setFirstResult(
				Integer.parseInt(searchAndFiltersDto.getPage()) * Integer.parseInt(searchAndFiltersDto.getPageSize()));
		createQuery.setMaxResults(Integer.parseInt(searchAndFiltersDto.getPageSize()));
		List<Object[]> list = createQuery.getResultList();
		List<AdminDetailsDto> response = returnAdminDetailList(list);
		if (!response.isEmpty()) {
			map.put("size", filteredResultCount);
			map.put("list", response);
			return new Response<>(569,
					messageSource.getMessage("usermanagement.search.admin.successfully", new Object[0], Locale.US),
					map);
		} else {
			return new Response<>(568, messageSource.getMessage(NO_DATA_FOUND, new Object[0], Locale.US));
		}
	}

	@Override
	public Response<Object> suspendUser(Long userId, SuspendUserDto suspendUserDto) {
		try {
			Optional<User> userExist = userDao.findById(suspendUserDto.getSuspendUserId());
			if (userExist.isPresent()) {
				User user = userExist.get();
				user.setUserStatus(UserStatus.SUSPENDED);
				user.getUserDetail().setSuspendReason(suspendUserDto.getReason());
				Map<String, Object> sendMailData = suspend(userExist.get().getEmail());
				mailSender.susspenedUser(sendMailData, "en");
				Map<String, String> setData = new HashMap<>();
				setData.put(EMAIL_TOKEN, userExist.get().getEmail());
				EmailDto emailDto = new EmailDto(userExist.get().getUserId(), SUSPEND, userExist.get().getEmail(),
						setData);
				notificationClient.sendNotification(emailDto);
				userDao.save(user);
				return new Response<>(200, messageSource.getMessage("usermanagement.user.suspended.successfully",
						new Object[0], Locale.US));
			} else {
				return new Response<>(201,
						messageSource.getMessage("usermanagement.user.not.exist", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(203,
					messageSource.getMessage("usermanagement.user.not.suspended", new Object[0], Locale.US));

		}
	}

	private Map<String, Object> suspend(String email) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, email);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.USERSUSPENED);
		return sendMailData;
	}

	private Map<String, Object> unsuspend(String email) {
		Map<String, Object> sendMailData = new HashMap<>();
		sendMailData.put(EmailConstants.EMAIL_TO, email);
		sendMailData.put(EmailConstants.SUBJECT_OF, EmailConstants.USERUNSUSPEND);
		return sendMailData;
	}

	@Override
	public Response<Object> unSuspendUser(Long userId, SuspendUserDto suspendUserDto) {
		try {
			Optional<User> userExist = userDao.findById(suspendUserDto.getSuspendUserId());
			if (userExist.isPresent()) {
				User user = userExist.get();
				user.setUserStatus(UserStatus.ACTIVE);
				user.getUserDetail().setSuspendReason(suspendUserDto.getReason());
				Map<String, Object> sendMailData = unsuspend(userExist.get().getEmail());
				mailSender.unsuspend(sendMailData, "en");
				Map<String, String> setData = new HashMap<>();
				setData.put(EMAIL_TOKEN, userExist.get().getEmail());
				EmailDto emailDto = new EmailDto(userExist.get().getUserId(), UNSUSPEND, userExist.get().getEmail(),
						setData);
				notificationClient.sendNotification(emailDto);
				userDao.save(user);
				return new Response<>(200, messageSource.getMessage("usermanagement.user.unsuspended.successfully",
						new Object[0], Locale.US));
			} else {
				return new Response<>(201,
						messageSource.getMessage("usermanagement.user.not.exist", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			return new Response<>(203,
					messageSource.getMessage("usermanagement.user.not.unsuspended", new Object[0], Locale.US));

		}
	}

	@Override
	public Response<Object> activateAccount(Long userId, Long userIdForStatusUpdate) {
		try {
			Optional<User> userExist = userDao.findByUserId(userIdForStatusUpdate);
			if (userExist.isPresent()) {
				User user = userExist.get();
				user.setUserStatus(UserStatus.ACTIVE);
				userDao.save(user);
				return new Response<>(200, "User Activated Successfully");
			} else {
				return new Response<>(201, "USER_DOES_NOT_EXIST");
			}
		} catch (Exception e) {
			return new Response<>(203, messageSource.getMessage(SOMETHING_WENT_WRONG, new Object[0], Locale.US));

		}

	}

	@Override
	public Response<Object> notificationSend(String country, NotificationDto description, NotiType notiType) {
		List<User> countryList = userDao.findByUserDetailCountry(country);
		if (!countryList.isEmpty()) {
			for (User data : countryList) {
				Map<String, String> setData = new HashMap<>();
				setData.put("[:description]", description.getDescription());
				EmailDto emailDto = new EmailDto(data.getUserId(), "admin_advertisement", notiType, setData);
				notificationClient.sendNotification(emailDto);
			}
			return new Response<>(200, "Notification Published");
		}
		return new Response<>(205, "Country not present");
	}

	@Override
	public Response<Object> adminReferral(Long userId, ReferalDto referalDto) {
		Optional<AdminReferal> findData = adminreferalDao.findByUserId(userId);
		if (!findData.isPresent()) {
			AdminReferal adminReferal = new AdminReferal();
			adminReferal.setReferalAmountRegister(referalDto.getReferalAmountRegister());
			adminReferal.setAvailablefund(BigDecimal.ZERO);
			adminReferal.setDistributedFund(BigDecimal.ZERO);
			adminReferal.setTotalReferal(new Long(0));
			adminReferal.setUserId(userId);
			adminReferal.setLimit(referalDto.getLimit());
			adminreferalDao.save(adminReferal);
			return new Response<>(200, "Amount Added Successfully");
		}
		if (findData.isPresent()) {
			if (referalDto.getReferalAmountRegister() != null && referalDto.getLimit() == null) {
				findData.get().setReferalAmountRegister(referalDto.getReferalAmountRegister());
				adminreferalDao.save(findData.get());
				return new Response<>(200, "Data updated successfully");
			}
			if (referalDto.getLimit() != null && referalDto.getReferalAmountRegister() == null) {
				findData.get().setLimit(referalDto.getLimit());
				adminreferalDao.save(findData.get());
				return new Response<>(200, "Data updated successfully");
			}
			if (referalDto.getReferalAmountRegister() != null && referalDto.getLimit() != null) {
				findData.get().setLimit(referalDto.getLimit());
				findData.get().setReferalAmountRegister(referalDto.getReferalAmountRegister());
				adminreferalDao.save(findData.get());
				return new Response<>(200, "Data updated successfully");
			}
		}
		return new Response<>(205, "No data Found");
	}

	@SuppressWarnings("unused")
	@Override
	public Response<Object> adminReferralUpdate(Long userId, BigDecimal referalAmountRegister, BigDecimal limit) {
		Optional<AdminReferal> findData = adminreferalDao.findByUserId(userId);
		if (findData.isPresent()) {
			if (referalAmountRegister != null && limit == null) {
				findData.get().setReferalAmountRegister(referalAmountRegister);
				adminreferalDao.save(findData.get());
				return new Response<>(200, "Data updated successfully");
			}
			if (limit != null && referalAmountRegister == null) {
				findData.get().setLimit(limit);
				adminreferalDao.save(findData.get());
				return new Response<>(200, "Data updated successfully");
			}
			if (referalAmountRegister != null && limit != null) {
				findData.get().setLimit(limit);
				findData.get().setReferalAmountRegister(referalAmountRegister);
				adminreferalDao.save(findData.get());
				return new Response<>(200, "Data updated successfully");
			}

		}
		return new Response<>(205, "No data found");
	}

	@Override
	public Response<Object> adminreferallist(Long userId) {

		Optional<AdminReferal> exists = adminreferalDao.findByUserId(userId);
		List<UserDetail> data = userDetailsDao.findAll();
		if (exists.isPresent()) {
			if (!data.isEmpty()) {
				for (UserDetail userDetail : data) {
					Long new1 = Long.valueOf(userDetail.getDirectReferCount());
					Long new2 = Long.valueOf(userDetail.getIndirectrefer());
					Long count = Long.sum(new1, new2);
					if (exists.isPresent()) {
						exists.get().setTotalReferal(Long.sum(exists.get().getTotalReferal(), count));
						adminreferalDao.save(exists.get());
					}
				}
				Map<String, Object> map = new HashMap<>();
				map.put("Available_Fund", exists.get().getAvailablefund());
				map.put("Distributed_Fund", exists.get().getDistributedFund());
				map.put("Limit", exists.get().getLimit());
				map.put("register_amount", exists.get().getReferalAmountRegister());
				map.put("Total_Referal", exists.get().getTotalReferal());

				return new Response<>(200, "data Found", map);
			}
		}
		return new Response<>(205, "No Data Found");
	}

	@Override
	public Response<Object> adminonboard(Long userId) {
		Optional<AdminReferal> exists = adminreferalDao.findByUserId(userId);
		if (exists.isPresent()) {
			BigDecimal data = exists.get().getLimit().subtract(exists.get().getAvailablefund());
			return new Response<>(200, "data fetched", data);
		}
		return new Response<>(205, "no data found");
	}

	@Override
	public Response<Object> changeP2pStatus(Long userId, P2pStatus p2pStatus) {
		Optional<UserDetail> data = detailsDao.findByUserDetailId(userId);
		if (data.isPresent()) {
			data.get().setP2pStatus(p2pStatus);
			detailsDao.save(data.get());
			return new Response<>(200, "p2p status changed");
		}
		return new Response<>(205, "no data found");
	}

}
