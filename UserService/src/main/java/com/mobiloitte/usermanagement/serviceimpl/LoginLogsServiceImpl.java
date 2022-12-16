package com.mobiloitte.usermanagement.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.mobiloitte.usermanagement.dao.UserLoginDetailsDao;
import com.mobiloitte.usermanagement.dto.LoginLogsDto;
import com.mobiloitte.usermanagement.enums.RoleStatus;
import com.mobiloitte.usermanagement.model.Response;
import com.mobiloitte.usermanagement.model.UserLoginDetail;
import com.mobiloitte.usermanagement.service.LoginLogsService;

@Service
public class LoginLogsServiceImpl implements LoginLogsService {

	private static final Logger LOGGER = LogManager.getLogger(AdminServiceImpl.class);

	@Autowired
	private EntityManager em;

	@Autowired
	private UserLoginDetailsDao userLoginDetailsDao;

	@Autowired
	private MessageSource messageSource;

	@Override
	public Response<Object> getAllAdminLoginDetails(Long userId) {
		List<UserLoginDetail> adminLoginDetailsList = userLoginDetailsDao.findAll();
		if (!adminLoginDetailsList.isEmpty()) {
			List<Map<String, Object>> response = new ArrayList<>();
			adminLoginDetailsList.parallelStream().forEachOrdered(a -> {
				if (a.getUser().getRole().getRole().equals(RoleStatus.ADMIN)) {
					Map<String, Object> userMap = new HashMap<>();
					userMap.put("browserPrint", a.getBrowserPrint());
					userMap.put("createTime", a.getCreateTime());
					userMap.put("ipAddress", a.getIpAddress());
					userMap.put("userAgent", a.getUserAgent());
					userMap.put("userLoginId", a.getUserLoginId());
					userMap.put("email", a.getUser().getEmail());
					response.add(userMap);
				}
			});
			return new Response<>(200, messageSource.getMessage(
					"usermanagement.get.all.admin.login.details.successfully", new Object[0], Locale.US), response);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US),
					Collections.emptyList());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> searchAndFilterAllAdminLoginDetails(Long userId, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize) {
		StringBuilder query = new StringBuilder(
				"select u.userId, l.createTime, u.email, l.ipAddress, l.userAgent, l.browserPrint from UserLoginDetail l left join User u on (l.user=u.userId) ");

		query.append(" where u.role.role='ADMIN'");

		if (fromDate != null) {
			LOGGER.debug("filtering with from date {}", new Date(fromDate));
			query.append(" and l.createTime >= :fromDate");
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date(toDate));
			query.append(" and l.createTime <= :toDate");
		}
		if (search != null) {
			LOGGER.debug("filtering with name {}", search);
			query.append(" and (u.email like :search)");
		}
		Query createQuery = em.createQuery(query.toString());
		if (fromDate != null)
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null)
			createQuery.setParameter("toDate", new Date(toDate));
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
		List<LoginLogsDto> response = list.parallelStream().map(o -> {
			LoginLogsDto dto = new LoginLogsDto();
			dto.setUserId((Long) o[0]);
			dto.setCreateTime((Date) o[1]);
			dto.setEmail((String) o[2]);
			dto.setIpAddress((String) o[3]);
			dto.setUserAgent((String) o[4]);
			dto.setBrowserPrint((String) o[5]);
			return dto;
		}).collect(Collectors.toList());
		if (!response.isEmpty()) {
			Collections.sort(response, (a, b) -> b.getUserId().compareTo(a.getUserId()));
			Map<String, Object> data = new HashMap<>();
			data.put("list", response);
			data.put("totalCount", filteredResultCount);
			return new Response<>(200, messageSource.getMessage(
					"usermanagement.get.all.admin.login.details.successfully", new Object[0], Locale.US), data);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US),
					Collections.emptyList());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> searchAndFilterAllStaffLoginDetails(Long userId, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize) {
		StringBuilder query = new StringBuilder(
				"select u.userId, l.createTime, u.email, l.ipAddress, l.userAgent, l.browserPrint from UserLoginDetail l left join User u on (l.user=u.userId) ");

		query.append(" where u.role.role='SUBADMIN'");

		if (fromDate != null) {
			LOGGER.debug("filtering with from date {}", new Date(fromDate));
			query.append(" and l.createTime >= :fromDate");
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date(toDate));
			query.append(" and l.createTime <= :toDate");
		}
		if (search != null) {
			LOGGER.debug("filtering with name {}", search);
			query.append(" and (u.email like :search)");
		}
		Query createQuery = em.createQuery(query.toString());
		if (fromDate != null)
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null)
			createQuery.setParameter("toDate", new Date(toDate));
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
		List<LoginLogsDto> response = list.parallelStream().map(o -> {
			LoginLogsDto dto = new LoginLogsDto();
			dto.setUserId((Long) o[0]);
			dto.setCreateTime((Date) o[1]);
			dto.setEmail((String) o[2]);
			dto.setIpAddress((String) o[3]);
			dto.setUserAgent((String) o[4]);
			dto.setBrowserPrint((String) o[5]);
			return dto;
		}).collect(Collectors.toList());
		if (!response.isEmpty()) {
			Collections.sort(response, (a, b) -> b.getUserId().compareTo(a.getUserId()));
			Map<String, Object> data = new HashMap<>();
			data.put("list", response);
			data.put("totalCount", filteredResultCount);
			return new Response<>(200, messageSource.getMessage(
					"usermanagement.get.all.staff.login.details.successfully", new Object[0], Locale.US), data);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US),
					Collections.emptyList());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> searchAndFilterAllUserLoginDetails(Long userId, Long fromDate, Long toDate, String search,
			Integer page, Integer pageSize) {
		StringBuilder query = new StringBuilder(
				"select u.userId, l.createTime, u.email, l.ipAddress, l.userAgent, l.browserPrint from UserLoginDetail l left join User u on (l.user=u.userId) ");
		query.append(" where u.role.role='USER'");

		if (fromDate != null) {
			LOGGER.debug("filtering with from date {}", new Date(fromDate));
			query.append(" and l.createTime >= :fromDate");
		}
		if (toDate != null) {
			LOGGER.debug("filtering with to date {}", new Date(toDate));
			query.append(" and l.createTime <= :toDate");
		}
		if (search != null) {
			LOGGER.debug("filtering with name {}", search);
			query.append(" and (u.email like :search)");
		}
		Query createQuery = em.createQuery(query.toString());
		if (fromDate != null)
			createQuery.setParameter("fromDate", new Date(fromDate));
		if (toDate != null)
			createQuery.setParameter("toDate", new Date(toDate));
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
		List<LoginLogsDto> response = list.parallelStream().map(o -> {
			LoginLogsDto dto = new LoginLogsDto();
			dto.setUserId((Long) o[0]);
			dto.setCreateTime((Date) o[1]);
			dto.setEmail((String) o[2]);
			dto.setIpAddress((String) o[3]);
			dto.setUserAgent((String) o[4]);
			dto.setBrowserPrint((String) o[5]);
			return dto;
		}).collect(Collectors.toList());
		if (!response.isEmpty()) {
			Collections.sort(response, (a, b) -> b.getUserId().compareTo(a.getUserId()));
			Map<String, Object> data = new HashMap<>();
			data.put("list", response);
			data.put("totalCount", filteredResultCount);
			return new Response<>(200, "Fetch User Login Details Successfully", data);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US),
					Collections.emptyList());
		}
	}

	@Override
	public Response<Object> getUserLoginDetails(Long userIdForLoginDetails, Integer page, Integer pageSize) {
		List<UserLoginDetail> userLoginDetailsList = userLoginDetailsDao.findByUserUserId(userIdForLoginDetails,
				PageRequest.of(page, pageSize, Direction.DESC, "userLoginId"));
		if (!userLoginDetailsList.isEmpty()) {
			return new Response<>(200, messageSource.getMessage("usermanagement.get.user.login.details.successfully",
					new Object[0], Locale.US), userLoginDetailsList);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US),
					Collections.emptyList());
		}
	}

	@Override
	public Response<Object> getUserLoginDetailsWithoutPagination(Long userIdForLoginDetails) {
		List<UserLoginDetail> userLoginDetailsList = userLoginDetailsDao
				.findByUserUserIdOrderByUserLoginIdDesc(userIdForLoginDetails);
		if (!userLoginDetailsList.isEmpty()) {
			return new Response<>(200, messageSource.getMessage("usermanagement.get.user.login.details.successfully",
					new Object[0], Locale.US), userLoginDetailsList);
		} else {
			return new Response<>(201,
					messageSource.getMessage("usermanagement.no.data.found", new Object[0], Locale.US),
					Collections.emptyList());
		}
	}
}
