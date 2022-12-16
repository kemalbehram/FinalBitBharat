package com.mobiloitte.usermanagement.service;

import com.mobiloitte.usermanagement.model.Response;

public interface LoginLogsService {

	Response<Object> getAllAdminLoginDetails(Long userId);

	Response<Object> searchAndFilterAllAdminLoginDetails(Long userId, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize);

	Response<Object> searchAndFilterAllStaffLoginDetails(Long userId, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize);

	Response<Object> searchAndFilterAllUserLoginDetails(Long userId, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize);

	Response<Object> getUserLoginDetails(Long userId, Integer page, Integer pageSize);

	Response<Object> getUserLoginDetailsWithoutPagination(Long userIdForLoginDetails);

}
