package com.mobiloitte.usermanagement.serviceimpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.constants.MessageConstant;
import com.mobiloitte.usermanagement.dao.MasterPermissionListDao;
import com.mobiloitte.usermanagement.dao.RoleDao;
import com.mobiloitte.usermanagement.dao.RolePermissionDao;
import com.mobiloitte.usermanagement.dao.UserDao;
import com.mobiloitte.usermanagement.dao.UserPermissionsDao;
import com.mobiloitte.usermanagement.dto.PermissionResponseDto;
import com.mobiloitte.usermanagement.exception.RunTimeException;
import com.mobiloitte.usermanagement.exception.UserNotFoundException;
import com.mobiloitte.usermanagement.model.MasterPermissionList;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.Role;
import com.mobiloitte.usermanagement.model.RolePermission;
import com.mobiloitte.usermanagement.model.User;
import com.mobiloitte.usermanagement.model.UserPermissions;
import com.mobiloitte.usermanagement.service.PermissionService;

@Service
public class PermissionServiceImpl extends MessageConstant implements PermissionService {

	@Autowired
	private UserDao userDao;

	@Autowired
	private RoleDao rolesDao;

	@Autowired
	private MessageSource messageSource;

	ModelMapper modelMapper = new ModelMapper();

	@Autowired
	private MasterPermissionListDao masterPermissionListDao;

	@Autowired
	private UserPermissionsDao userPermissionsDao;

	@Autowired
	private RolePermissionDao rolePermissionDao;

	private static final Logger LOGGER = LogManager.getLogger(PermissionServiceImpl.class);

	@Override
	public Response<Object> getMasterPermissionList() {
		try {
			List<MasterPermissionList> masterPermissionLists = masterPermissionListDao.findAll();
			if (!masterPermissionLists.isEmpty()) {
				return new Response<>(2001, messageSource
						.getMessage("usermanagement.get.master.permission.list.successfully", new Object[0], Locale.US),
						masterPermissionLists);
			} else {
				return new Response<>(2004,
						messageSource.getMessage(USERMANAGEMENT_NO_PERMISSIONS_FOUND, new Object[0], Locale.US));
			}
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getAllRolePermission() {
		try {
			List<RolePermission> permissionList = rolePermissionDao.findAll();
			if (!permissionList.isEmpty()) {
				return new Response<>(2002, messageSource
						.getMessage("usermanagement.get.all.role.permission.successfully", new Object[0], Locale.US),
						permissionList);
			} else {
				return new Response<>(2004,
						messageSource.getMessage(USERMANAGEMENT_NO_PERMISSIONS_FOUND, new Object[0], Locale.US));
			}
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getEmailWiseRolePermission(String email) throws IOException, URISyntaxException {
		try {
			Optional<User> userDbDetails = userDao.findByEmail(email);
			if (!userDbDetails.isPresent()) {
				throw new UserNotFoundException(USER_NOT_FOUND);
			} else {
				List<RolePermission> listOfAdminRolePermissions = rolePermissionDao
						.findByRolesRoleId(userDbDetails.get().getRole().getRoleId());
				if (!listOfAdminRolePermissions.isEmpty()) {
					List<PermissionResponseDto> response = new ArrayList<>();
					for (int i = 0; i < listOfAdminRolePermissions.size(); i++) {
						PermissionResponseDto dto = new PermissionResponseDto();
						if (listOfAdminRolePermissions.get(i).getMasterPermissionList().getIsMenu()) {
							dto.setPermissionNames(listOfAdminRolePermissions.get(i).getMasterPermissionList()
									.getMenuPermission().getMenuName());
							dto.setIsMenu(Boolean.TRUE);
						} else {
							dto.setPermissionNames(listOfAdminRolePermissions.get(i).getMasterPermissionList()
									.getMenuPermission().getSubMenuName());
							dto.setIsMenu(Boolean.FALSE);
						}
						dto.setRolesId(userDbDetails.get().getRole().getRoleId());
						response.add(dto);
					}
					return new Response<>(2003,
							messageSource.getMessage("usermanagement.get.all.role.permissions.email.wise.successfully",
									new Object[0], Locale.US),
							response);
				} else {
					return new Response<>(2004,
							messageSource.getMessage(USERMANAGEMENT_NO_PERMISSIONS_FOUND, new Object[0], Locale.US));
				}
			}
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getRoleWiseRolePermission(String rolesId) {
		try {
			Optional<Role> roleCheck = rolesDao.findById(Long.parseLong(rolesId));
			if (roleCheck.isPresent()) {
				List<RolePermission> rolePermissions = rolePermissionDao.findByRolesRoleId(Long.parseLong(rolesId));
				if (!rolePermissions.isEmpty()) {

					return new Response<>(2005,
							messageSource.getMessage("usermanagement.get.all.role.permissions.role.wise.successfully",
									new Object[0], Locale.US),
							rolePermissions);
				} else {
					return new Response<>(2004,
							messageSource.getMessage(USERMANAGEMENT_NO_PERMISSIONS_FOUND, new Object[0], Locale.US));
				}
			} else {
				return new Response<>(2006,
						messageSource.getMessage("usermanagement.role.not.found", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getRolesAndPermissionData(Long userId) {
		try {
			Map<String, Object> reponse = new HashMap<>();
			List<Map<String, Object>> menuList = new ArrayList<>();
			List<Map<String, Object>> subMenuList = new ArrayList<>();
			if (userId != null) {
				List<MasterPermissionList> masterListData = masterPermissionListDao.findAll();
				List<Role> rolesCheck = rolesDao.findAll();
				List<RolePermission> responseData = rolePermissionDao.findByIsDeletedFalse();
				if (!masterListData.isEmpty()) {
					masterListData.parallelStream()
							.sorted(Comparator.comparing(MasterPermissionList::getMasterPermissionListId))
							.forEachOrdered(a -> {
								Map<String, Object> subMenuNameListss = new HashMap<>();
								Map<String, Object> menuNameList = new HashMap<>();
								Long parentId = a.getMenuPermission().getParentId();
								if (parentId == null) {
									String menuName = a.getMenuPermission().getMenuName();
									menuNameList.put("menuname", menuName);
									List<String> subMenuNameLists = new ArrayList<>();
									masterListData.parallelStream().forEachOrdered(t -> {
										if (a.getMenuPermission().getMenuPermissionId()
												.equals(t.getMenuPermission().getParentId())) {
											subMenuNameLists.add(t.getMenuPermission().getSubMenuName());
											menuNameList.put("subMenuName", subMenuNameLists);
										}
									});
									menuList.add(menuNameList);
								} else {
									String subMenuName = a.getMenuPermission().getSubMenuName();
									subMenuNameListss.put("subMenuname", subMenuName);
									subMenuList.add(subMenuNameListss);
								}
							});
					reponse.put("menuList", menuList);
					reponse.put("subMenuList", subMenuList);
					reponse.put("rolePermission", responseData);
					reponse.put("masterPermissionList", masterListData);
					reponse.put("roles", rolesCheck);
					return new Response<>(2007,
							messageSource.getMessage("usermanagement.get.all.master.permission.list.data.successfully",
									new Object[0], Locale.US),
							reponse);
				} else {
					return new Response<>(2004,
							messageSource.getMessage(USERMANAGEMENT_NO_PERMISSIONS_FOUND, new Object[0], Locale.US));
				}
			} else {
				return new Response<>(2006,
						messageSource.getMessage("usermanagement.role.not.found", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getUsersRolePermission(Long userId) {
		try {
			List<UserPermissions> userPermissions = userPermissionsDao.findByUserUserIdAndIsDeleted(userId,
					Boolean.FALSE);
			Map<String, Object> response = new HashMap<>();
			if (!userPermissions.isEmpty()) {
				List<Map<String, Object>> menuList = new ArrayList<>();
				List<Map<String, Object>> subMenuList = new ArrayList<>();
				userPermissions.parallelStream().sorted(Comparator.comparing(UserPermissions::getUserPermissionsId))
						.forEachOrdered(a -> {
							Map<String, Object> subMenuNameListss = new HashMap<>();
							Map<String, Object> menuNameList = new HashMap<>();
							Long parentId = a.getMasterPermissionList().getMenuPermission().getParentId();
							if (parentId == null) {
								String menuName = a.getMasterPermissionList().getMenuPermission().getMenuName();
								menuNameList.put("menuname", menuName);
								List<String> subMenuNameLists = new ArrayList<>();
								userPermissions.parallelStream().forEachOrdered(t -> {
									if (a.getMasterPermissionList().getMenuPermission().getMenuPermissionId()
											.equals(t.getMasterPermissionList().getParentId())) {
										subMenuNameLists
												.add(t.getMasterPermissionList().getMenuPermission().getSubMenuName());
										menuNameList.put("subMenuName", subMenuNameLists);
									}
								});
								menuList.add(menuNameList);
							} else {
								String subMenuName = a.getMasterPermissionList().getMenuPermission().getSubMenuName();
								subMenuNameListss.put("subMenuname", subMenuName);
								subMenuList.add(subMenuNameListss);
							}
						});
				response.put("menuList", menuList);
				response.put("subMenuList", subMenuList);

				return new Response<>(2011,
						messageSource.getMessage("usermanagement.fetch.users.role.permissions.successfully",
								new Object[0], Locale.US),
						response);
			} else {
				return new Response<>(2004,
						messageSource.getMessage("usermanagement.no.permissions.found", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getUsersRolePermissionForPermissionMetric(String userName) {
		try {
			Optional<User> user = userDao.findByEmail(userName);
			Long userId = 0L;
			if (user.isPresent()) {
				userId = user.get().getUserId();
			}
			List<UserPermissions> userPermissions = userPermissionsDao.findByUserUserIdAndIsDeletedFalse(userId);
			List<Object> response = new ArrayList<>();
			if (!userPermissions.isEmpty()) {
				userPermissions.parallelStream().forEachOrdered(a -> {
					Long parentId = a.getMasterPermissionList().getMenuPermission().getParentId();
					if (parentId == null) {
						String menuName = a.getMasterPermissionList().getMenuPermission().getMenuName();
						response.add(menuName);
					} else {
						String subMenuName = a.getMasterPermissionList().getMenuPermission().getSubMenuName();
						response.add(subMenuName);
					}
				});
				return new Response<>(2012,
						messageSource.getMessage(
								"usermanagement.get.users.role.permissions.for.gateway.service.successfully",
								new Object[0], Locale.US),
						response);
			} else {
				return new Response<>(2004,
						messageSource.getMessage("usermanagement.no.permissions.found", new Object[0], Locale.US));
			}
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}
}