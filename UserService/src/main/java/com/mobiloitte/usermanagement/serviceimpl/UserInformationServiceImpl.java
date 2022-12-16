package com.mobiloitte.usermanagement.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.constants.MessageConstant;
import com.mobiloitte.usermanagement.dao.RoleDao;
import com.mobiloitte.usermanagement.dao.UserSecurityDetailsDao;
import com.mobiloitte.usermanagement.dto.RoleListDto;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.exception.RunTimeException;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.Role;
import com.mobiloitte.usermanagement.model.UserSecurityDetails;
import com.mobiloitte.usermanagement.service.UserInformationService;

@Service
public class UserInformationServiceImpl extends MessageConstant implements UserInformationService {
	private static final Logger LOGGER = LogManager.getLogger(UserInformationServiceImpl.class);

	@Autowired
	private RoleDao roleDao;

	@Autowired
	private UserSecurityDetailsDao userSecurityDetailsDao;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Response<List<RoleListDto>> getRolesDataForNotification(List<Long> rolesId) {
		try {
			if (!rolesId.isEmpty()) {
				Iterable<Long> ids = rolesId;
				List<Role> roleList = roleDao.findAllById(ids);
				List<RoleListDto> roleListDto = new ArrayList<>();
				roleList.parallelStream().forEachOrdered(a -> {
					List<Long> userId = new ArrayList<>();
					RoleListDto roleLists = new RoleListDto();
					a.getUsers().parallelStream().forEachOrdered(b -> userId.add(b.getUserId()));
					roleLists.setRoleID(a.getRoleId());
					roleLists.setUserId(userId);
					roleListDto.add(roleLists);
				});
				return new Response<>(200, "success", roleListDto);
			}
			return new Response<>(201, "failure", Collections.emptyList());
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Long> getRoleIdByName(String role) {
		try {
			Optional<Role> roleData = roleDao.findByRole(RoleStatus.valueOf(role));
			if (roleData.isPresent()) {
				return new Response<>(200, MessageConstant.SUCCESS, roleData.get().getRoleId());
			} else {
				return new Response<>(200, MessageConstant.SUCCESS);
			}
		} catch (Exception e) {
			LOGGER.error(LOGS_ARE, e);
			throw new RunTimeException(SOMETHING_WENT_WRONG);
		}
	}

	@Override
	public Response<Object> getUserSecurityHistoryDetails(Long userIdForSecurityDetails, Integer page,
			Integer pageSize) {
		List<UserSecurityDetails> userSecurityDetailsList = userSecurityDetailsDao.findByUserUserId(
				userIdForSecurityDetails, PageRequest.of(page, pageSize, Direction.DESC, "userSecurityDetailsId"));
		if (!userSecurityDetailsList.isEmpty()) {
			return new Response<>(200, messageSource.getMessage("usermanagement.get.user.security.details.successfully",
					new Object[0], Locale.US), userSecurityDetailsList);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US),
					Collections.emptyList());
		}
	}

	@Override
	public Response<Object> getUserSecurityHistoryDetailsWithoutPagination(Long userIdForSecurityDetails) {
		List<UserSecurityDetails> userSecurityDetailsList = userSecurityDetailsDao
				.findByUserUserIdOrderByUserSecurityDetailsIdDesc(userIdForSecurityDetails);
		if (!userSecurityDetailsList.isEmpty()) {
			return new Response<>(200, messageSource.getMessage("usermanagement.get.user.security.details.successfully",
					new Object[0], Locale.US), userSecurityDetailsList);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US),
					Collections.emptyList());
		}
	}

}