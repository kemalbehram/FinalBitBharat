package com.mobiloitte.usermanagement.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

import com.mobiloitte.usermanagement.dto.RoleListDto;
import com.mobiloitte.usermanagement.model.Response;

public interface UserInformationService {

	Response<List<RoleListDto>> getRolesDataForNotification(List<Long> rolesId);

	Response<Long> getRoleIdByName(@Valid @Pattern(regexp = "USER|ADMIN|DEVELOPER|SUBADMIN") String role);

	Response<Object> getUserSecurityHistoryDetails(Long userIdForSecurityDetails, Integer page, Integer pageSize);

	Response<Object> getUserSecurityHistoryDetailsWithoutPagination(Long userIdForSecurityDetails);

}