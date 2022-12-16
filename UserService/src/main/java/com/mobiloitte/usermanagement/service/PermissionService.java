package com.mobiloitte.usermanagement.service;

import java.io.IOException;
import java.net.URISyntaxException;

import com.mobiloitte.usermanagement.model.Response;

public interface PermissionService {

	Response<Object> getMasterPermissionList();

	Response<Object> getAllRolePermission();

	Response<Object> getEmailWiseRolePermission(String email) throws IOException, URISyntaxException;

	Response<Object> getRolesAndPermissionData(Long userId);

	Response<Object> getUsersRolePermission(Long userId);

	Response<Object> getUsersRolePermissionForPermissionMetric(String userName);

	Response<Object> getRoleWiseRolePermission(String roleId);

}