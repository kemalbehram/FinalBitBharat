package com.mobiloitte.usermanagement.serviceimpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.dao.AdminDao;
import com.mobiloitte.usermanagement.dao.KycDao;
import com.mobiloitte.usermanagement.dao.NomineeDao;
import com.mobiloitte.usermanagement.enums.KycStatus;
import com.mobiloitte.usermanagement.enums.NomineeStatus;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.Nominee;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.Role;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

	@Autowired
	private AdminDao adminUserDao;
	@Autowired
	private NomineeDao nomineeDao;
	@Autowired
	private KycDao kycDao;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Response<Object> getTotalUserCount() {
		Role role = new Role();
		role.setRole(RoleStatus.USER);
		long count = adminUserDao.countByRoleRole(RoleStatus.USER);
		return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US), count);
	}

	@Override
	public Response<Object> getTotalUserCountByStatus() {
		Map<String, Long> userCountByStatus = new HashMap<>();
		long activeUserCount = adminUserDao.countByUserStatusAndRoleRole(UserStatus.ACTIVE, RoleStatus.USER);
		long blockUserCount = adminUserDao.countByUserStatusAndRoleRole(UserStatus.BLOCK, RoleStatus.USER);
		long pendingUserCount = adminUserDao.countByUserStatusAndRoleRole(UserStatus.UNVERIFIED, RoleStatus.USER);
		userCountByStatus.put("activeUsersCount", activeUserCount);
		userCountByStatus.put("blockedUsersCount", blockUserCount);
		userCountByStatus.put("pendingUserCount", pendingUserCount);
		return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US),
				userCountByStatus);
	}

	@Override
	public Response<Object> getPendingKycCuont() {
		int countByKycStatus = kycDao.countByKycStatus(KycStatus.PENDING);
		return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US),
				countByKycStatus);
	}

	@Override
	public Response<Object> dashboardCountApi() {
		List<Map<String, Object>> response = new ArrayList<>();
		List<User> allUser = adminUserDao.findAll();
		if (allUser.isEmpty()) {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US));
		}
		long activeUserCount = adminUserDao.countByUserStatusAndRoleRole(UserStatus.ACTIVE, RoleStatus.USER);
		long blockUserCount = adminUserDao.countByUserStatusAndRoleRole(UserStatus.BLOCK, RoleStatus.USER);
		long pendingUserCount = adminUserDao.countByUserStatusAndRoleRole(UserStatus.UNVERIFIED, RoleStatus.USER);
		long totalUserCount = adminUserDao.countByRoleRole(RoleStatus.USER);
		List<Nominee> exists = nomineeDao.findByNomineeStatus(NomineeStatus.PENDING);
		List<Nominee> exists1 = nomineeDao.findByNomineeStatus(NomineeStatus.REJECTED);
		List<Nominee> exists4 = nomineeDao.findByNomineeStatus(NomineeStatus.ACTIVE);
		List<Nominee> exists2 = nomineeDao.findAll();
		int countByKycStatus = kycDao.countByKycStatus(KycStatus.PENDING);
		Map<String, Object> map = new HashMap<>();
		map.put("activeUserCount", activeUserCount);
		map.put("blockUserCount", blockUserCount);
		map.put("pendingUserCount", pendingUserCount);
		map.put("totalUserCount", totalUserCount);
		map.put("countByKycStatus", countByKycStatus);
		map.put("nomineeCountPending", exists.size());
		map.put("nomineeRejectedCount", exists1.size());
		map.put("nomineeCount", exists2.size());
		map.put("nomineeCountAccepted", exists4.size());
		response.add(map);
		return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US),
				response);
	}

	@Override
	public Response<Object> getTotalKycCount() {
		long countByKycStatus = kycDao.countByKycStatus(KycStatus.PENDING);
		long countByKycStatus1 = kycDao.countByKycStatus(KycStatus.REJECTED);
		long countByKycStatus2 = kycDao.countByKycStatus(KycStatus.ACCEPTED);
		Map<String, Object> map = new HashMap<>();
		map.put("Accepted_Kyc_Count", countByKycStatus2);
		map.put("Pending_Kyc_Count", countByKycStatus);
		map.put("Rejected_Kyc_Count", countByKycStatus1);
		return new Response<>(200, messageSource.getMessage("usermanagement.success", new Object[0], Locale.US), map);
	}

}