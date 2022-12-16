package com.mobiloitte.usermanagement.service;

import java.math.BigDecimal;

import javax.validation.Valid;

import com.mobiloitte.usermanagement.controller.ReferalUpdateDto;
import com.mobiloitte.usermanagement.dto.AddAdminDto;
import com.mobiloitte.usermanagement.dto.CommonDto;
import com.mobiloitte.usermanagement.dto.NotificationDto;
import com.mobiloitte.usermanagement.dto.ReferalDto;
import com.mobiloitte.usermanagement.dto.SearchAndFilterDto;
import com.mobiloitte.usermanagement.dto.SubAdminDto;
import com.mobiloitte.usermanagement.dto.SuspendUserDto;
import com.mobiloitte.usermanagement.dto.UpdateUserKycDto;
import com.mobiloitte.usermanagement.enums.NotiType;
import com.mobiloitte.usermanagement.enums.P2pStatus;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.User;

public interface AdminService {

	Response<Object> userStatus(Long userId, String role, String username, String userStatus,
			Long userIdForStatusUpdate, String ipAddress, String location);

	Response<User> deleteUserDetail(Long userId, String role, String username, Long userIdToDelete, String ipAddress,
			String location);

	Response<Object> createSubAdmin(Long userId, SubAdminDto subAdminDto);

	Response<Object> updateUserKycLimit(UpdateUserKycDto updateUserKycDto);

	Response<Object> getUserKycLimit(Long userId);

	Response<Object> filterUserDetails(Long userId, UserStatus status, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize, RoleStatus roleStatus, String country, Long newUserId);

	Response<Object> getStaffUserProfile(Long userId, CommonDto commonDto);

	Response<Object> searchAndFilterStaff(Long userId, SearchAndFilterDto searchAndFilterDto);

	Response<Object> deteleStaff(Long userId, String role, String username, CommonDto commonDto);

	Response<Object> editSubAdmin(Long userId, String username, String role, @Valid SubAdminDto subAdminDto);

	Response<Object> addAdmin(Long userId, String username, String role, @Valid AddAdminDto addAdminDto);

	Response<Object> searchAndFilterAdmin(Long userId, SearchAndFilterDto searchAndFilterDto);

	Response<Object> suspendUser(Long userId, SuspendUserDto suspendUserDto);

	Response<Object> unSuspendUser(Long userId, SuspendUserDto suspendUserDto);

	Response<Object> activateAccount(Long userId, Long userIdForStatusUpdate);

	Response<Object> notificationSend(String country, NotificationDto description, NotiType notiType);

	Response<Object> adminReferral(Long userId, ReferalDto referalDto);



//	Response<Object> adminReferralUpdate(Long userId, BigDecimal referalAmountRegister, String limit);

	Response<Object> adminreferallist(Long userId);

	Response<Object> adminReferralUpdate(Long userId, BigDecimal referalAmountRegister, BigDecimal limit);

	Response<Object> adminonboard(Long userId);

	Response<Object> changeP2pStatus(Long userId, P2pStatus p2pStatus);

	

}
