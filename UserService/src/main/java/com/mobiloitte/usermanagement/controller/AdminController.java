package com.mobiloitte.usermanagement.controller;

import java.math.BigDecimal;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mobiloitte.usermanagement.dto.AddAdminDto;
import com.mobiloitte.usermanagement.dto.CommonDto;
import com.mobiloitte.usermanagement.dto.NotificationDto;
import com.mobiloitte.usermanagement.dto.ReferalDto;
import com.mobiloitte.usermanagement.dto.SearchAndFilterDto;
import com.mobiloitte.usermanagement.dto.SubAdminDto;
import com.mobiloitte.usermanagement.dto.SuspendUserDto;
import com.mobiloitte.usermanagement.dto.UpdateUserKycDto;
import com.mobiloitte.usermanagement.dto.UserProfileDto;
import com.mobiloitte.usermanagement.enums.NotiType;
import com.mobiloitte.usermanagement.enums.P2pStatus;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.enums.UserStatus;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.service.AdminService;
import com.mobiloitte.usermanagement.service.UserService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("admin/user-management")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@Autowired
	private UserService userService;

	@Value("${tokenSecretKey}")
	private String tokenSecretKey;
	@ApiOperation(value = "API to get all users details using filter")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/filter-user-details")
	public Response<Object> filterUserDetails(@RequestHeader Long userId,
			@RequestParam(required = false) UserStatus status, @RequestParam(required = false) Long fromDate,
			@RequestParam(required = false) Long toDate, @RequestParam(required = false) String search,
			@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer pageSize,
			@RequestParam(required = false) RoleStatus roleStatus, @RequestParam(required = false) String country,@RequestParam(required = false) Long newUserId) {

		return adminService.filterUserDetails(userId, status, fromDate, toDate, search, page, pageSize, roleStatus,
				country,newUserId);

	}

	@ApiOperation(value = "API to get user details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/user-details")
	public Response<UserProfileDto> getUserDetailsByUserId(@RequestParam Long userId) {
		return userService.getUserByUserId(userId);
	}

	@ApiOperation(value = "API for delete-user-detail")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/delete-user-detail")
	public Response<User> deletUserDetail(@RequestHeader Long userId, @RequestHeader String role,
			@RequestHeader String username, @RequestParam Long userIdToDelete, @RequestParam String ipAddress,
			@RequestParam String location) {
		return adminService.deleteUserDetail(userId, role, username, userIdToDelete, ipAddress, location);
	}

	@ApiOperation(value = "API of save user status")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/user-status")
	public Response<Object> userStatus(@RequestHeader Long userId, @RequestHeader String role,
			@RequestHeader String username, @RequestParam String userStatus, @RequestParam Long userIdForStatusUpdate,
			@RequestParam String ipAddress, @RequestParam String location) {
		return adminService.userStatus(userId, role, username, userStatus, userIdForStatusUpdate, ipAddress, location);
	}

	@ApiOperation(value = "API to create sub admin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/create-sub-admin")
	public Response<Object> createSubAdmin(@RequestHeader Long userId, @Valid @RequestBody SubAdminDto subAdminDto) {
		return adminService.createSubAdmin(userId, subAdminDto);
	}

	@ApiOperation(value = "API to edit sub admin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/edit-sub-admin")
	public Response<Object> editSubAdmin(@RequestHeader Long userId, @RequestHeader String username,
			@RequestHeader String role, @Valid @RequestBody SubAdminDto subAdminDto) {
		return adminService.editSubAdmin(userId, username, role, subAdminDto);
	}

	@ApiOperation(value = "API to add admin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/add-admin")
	public Response<Object> addAdmin(@RequestHeader Long userId, @RequestHeader String username,
			@RequestHeader String role, @Valid @RequestBody AddAdminDto addAdminDto) {
		return adminService.addAdmin(userId, username, role, addAdminDto);
	}

	@ApiOperation(value = "API for update user kyc limit")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/update-user-kyc-limit")
	public Response<Object> updateUserKycLimit(@RequestBody UpdateUserKycDto updateUserKycDto) {
		return adminService.updateUserKycLimit(updateUserKycDto);

	}

	@ApiOperation(value = "API for get user kyc limit")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@GetMapping("/get-user-kyc-limit")
	public Response<Object> getUserKycLimit(@RequestParam Long userId) {
		return adminService.getUserKycLimit(userId);
	}

	@ApiOperation(value = "API to get-staff-user-profile")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/get-staff-user-profile")
	public Response<Object> getStaffUserProfile(@RequestHeader Long userId, @RequestBody CommonDto commonDto) {
		return adminService.getStaffUserProfile(userId, commonDto);
	}

	@ApiOperation(value = "API to search-and-filter-staff-profiles")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/search-and-filter-staff")
	public Response<Object> searchAndFilterStaffApi(@RequestHeader Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) {
		return adminService.searchAndFilterStaff(userId, searchAndFilterDto);
	}

	@ApiOperation(value = "API to search-and-filter-admin")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/search-and-filter-admin")
	public Response<Object> searchAndFilterAdminApi(@RequestHeader Long userId,
			@RequestBody SearchAndFilterDto searchAndFilterDto) {
		return adminService.searchAndFilterAdmin(userId, searchAndFilterDto);
	}

	@ApiOperation(value = "API to delete-staff")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Success"),
			@ApiResponse(code = 205, message = "Invalid code"), })
	@PostMapping("/detele-staff")
	public Response<Object> deteleStaffApi(@RequestHeader Long userId, @RequestHeader String role,
			@RequestHeader String username, @RequestBody CommonDto commonDto) {
		return adminService.deteleStaff(userId, role, username, commonDto);
	}

	@ApiOperation(value = "API for suspend user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/suspend-user")
	public Response<Object> suspendUser(@RequestHeader Long userId, @RequestBody SuspendUserDto suspendUserDto) {
		return adminService.suspendUser(userId, suspendUserDto);
	}

	@ApiOperation(value = "API for unsuspend user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "success"), })
	@PostMapping("/unsuspend-user")
	public Response<Object> unSuspendUser(@RequestHeader Long userId, @RequestBody SuspendUserDto suspendUserDto) {
		return adminService.unSuspendUser(userId, suspendUserDto);
	}

	@PostMapping("/Activate-Account")
	public Response<Object> activateAccount(@RequestHeader Long userId, @RequestParam Long userIdForStatusUpdate) {
		return adminService.activateAccount(userId, userIdForStatusUpdate);
	}

	@PostMapping("/notification-send-all-user")
	public Response<Object> notificationSend(@RequestParam String country, @RequestBody NotificationDto description,
			@RequestParam NotiType notiType) {
		return adminService.notificationSend(country, description, notiType);
	}

	@PostMapping("/admin-referal-add")
	public Response<Object> adminReferral(@RequestHeader Long userId, @RequestBody ReferalDto referalDto) {
		return adminService.adminReferral(userId, referalDto);
	}

	@PostMapping("/admin-referal-update")
	public Response<Object> adminReferralUpdate(@RequestHeader Long userId,
			@RequestParam(required = false) BigDecimal referalAmountRegister,
			@RequestParam(required = false) BigDecimal limit) {
		return adminService.adminReferralUpdate(userId, referalAmountRegister, limit);
	}

	@GetMapping("/get-admin-referal")
	public Response<Object> adminreferallist(@RequestHeader Long userId) {
		return adminService.adminreferallist(userId);
	}
	
	@GetMapping("/total-onborad-distribute")
	public Response<Object> adminonboard(@RequestHeader Long userId){
		return adminService.adminonboard(userId);
	}
	
	@PostMapping("/p2p-status-change")
	public Response<Object> changeP2pStatus(@RequestParam Long userId,@RequestParam P2pStatus p2pStatus ){
		return adminService.changeP2pStatus(userId,p2pStatus);
	}
	
	
}
