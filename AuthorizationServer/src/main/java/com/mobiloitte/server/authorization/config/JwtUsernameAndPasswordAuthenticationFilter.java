package com.mobiloitte.server.authorization.config;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobiloitte.server.authorization.enums.Role;
import com.mobiloitte.server.authorization.model.ErrorResponse;
import com.mobiloitte.server.authorization.model.LoginDetail;
import com.mobiloitte.server.authorization.model.Response;
import com.mobiloitte.server.authorization.model.User;
import com.mobiloitte.server.authorization.service.UserDetailsServiceImpl;

import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	// We use auth manager to validate the user credentials
	private AuthenticationManager authManager;

	private final JwtConfig jwtConfig;

	private UserDetailsServiceImpl userService;

	private static final UserAgentAnalyzer ANALYZER = UserAgentAnalyzer.newBuilder()
			.withFields("DeviceClass", "DeviceName", "OperatingSystemClass", "OperatingSystemName",
					"OperatingSystemVersion", "AgentName", "AgentVersionMajor")
			.withCache(1000).build();

	public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig,
			UserDetailsServiceImpl userService) {
		this.authManager = authManager;
		this.jwtConfig = jwtConfig;
		this.userService = userService;
		// By default, UsernamePasswordAuthenticationFilter listens to "/login" path.
		this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/", "POST"));
	}

	@Override
	@SuppressWarnings("unchecked")
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) {
		try {
			// 1. Get credentials from request
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			Map<String, Object> map = mapper.readValue(request.getInputStream(), Map.class);
			User user = new User();
			user.setEmail(map.get("email").toString());
			user.setIpAdress(map.get("ipAddress").toString());
			user.setUserAgent(map.get("userAgent").toString());
			user.setLocation(map.get("location").toString());
			user.setPassword(map.get("password").toString());

			if (map.get("deviceToken") != null && map.get("deviceType") != null) {
				user.setDeviceToken(map.get("deviceToken").toString());
				user.setDeviceType(map.get("deviceType").toString());

				request.setAttribute("deviceToken", map.get("deviceToken"));
				request.setAttribute("deviceType", map.get("deviceType"));
			}
			request.setAttribute("userAgent", map.get("userAgent").toString());
			request.setAttribute("ipAddress", map.get("ipAddress").toString());
			request.setAttribute("location", map.get("location").toString());
			Object responseUser = userService.userDetail(map.get("email").toString());

			if (responseUser.equals(205)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("", "",
						Collections.emptyList());
				return authManager.authenticate(authToken);
			}
			String role = responseUser.toString();
			if (map.get("deviceType") != null) {
 
				if (role.equalsIgnoreCase(Role.ADMIN.name())
						&& !map.get("deviceType").toString().equalsIgnoreCase("WEB")) {
					UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken("", "",
							Collections.emptyList());
					return authManager.authenticate(authToken);
				}
			}
			if (map.get("deviceToken") != null && map.get("deviceType") != null) {
				user.setDeviceToken(map.get("deviceToken").toString());
				user.setDeviceType(map.get("deviceType").toString());
				request.setAttribute("deviceToken", map.get("deviceToken"));
				request.setAttribute("deviceType", map.get("deviceType"));

			}

			// request.setAttribute("userPresent", Boolean.TRUE);
			// request.setAttribute("webUrl", map.get("webUrl").toString());
			// 2. Create auth object (contains credentials) which will be used by auth
			// manager
			if (map.get("deviceType") == null && role.equalsIgnoreCase(Role.ADMIN.name())) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
						user.getUsername(), user.getPassword(), Collections.emptyList());
				return authManager.authenticate(authToken);
			}
			// request.setAttribute("webUrl", map.get("webUrl").toString());
			// 2. Create auth object (contains credentials) which will be used by auth
			// manager
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(user.getUsername(),
					user.getPassword(), Collections.emptyList());

			// 3. Authentication manager authenticate the user, and use
			// UserDetialsServiceImpl::loadUserByUsername() method to load the user.
			return authManager.authenticate(authToken);

		} catch (IOException e) {
			throw new AuthenticationServiceException(e.getMessage());
		}
	}

	// Upon successful authentication, generate a token.
	// The 'auth' passed to successfulAuthentication() is the current authenticated
	// user.
	@Override
	@SuppressWarnings("unused")
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication auth) throws IOException, ServletException {
		Object principal = auth.getPrincipal();
		if (principal instanceof User) {
			String ipAddress = request.getHeader("x-forwarded-for");
			String ipAddress2 = String.valueOf(request.getAttribute("ipAddress"));

			String userAgent = String.valueOf(request.getAttribute("userAgent"));
			String location = String.valueOf(request.getAttribute("location"));
			if (ipAddress == null)
				ipAddress = request.getRemoteAddr();
			User user = (User) principal;
			logger.debug("Principal is :" + user);
			// user.getIpAdress() != null && !user.getIpAdress().equals(ipAddress)
			if (false) {
				response.getWriter().write(new ObjectMapper().writeValueAsString(new ErrorResponse(401,
						"Different Ip address detected. Please check your email", "Authentication Error")));
				ObjectMapper mapper = new ObjectMapper();
				mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
				userService.sendVerifyIpMail(ipAddress, user.getUserId(), request.getAttribute("webUrl").toString());
			} else {
				String token = userService.generateToken(user);
				Map<String, Object> body = new HashMap<>();
				body.put("token", token);
				body.put("TwoFa", user.getTwoFaType());
				body.put("Role", user.getRole());
				logger.debug("Request received Locale : " + request.getLocale());
				logger.debug("Request received IP : " + request.getRemoteAddr());
				String userAgentString = request.getHeader("User-Agent");
				logger.debug("Request received User-Agent : " + userAgentString);
				UserAgent userAgent2 = ANALYZER.parse(userAgentString);
				logger.debug("User-Agent " + userAgent.toString());
				Object deviceToken = request.getAttribute("deviceToken");
				Object deviceType = request.getAttribute("deviceType");
				user.setDeviceToken(String.valueOf(deviceToken));
				user.setDeviceType(String.valueOf(deviceType));
				user.setUserAgent(userAgent.toString());
				user.setIpAdress(ipAddress2);
				user.setLocation(request.getAttribute("location").toString());

				userService.saveLoginDetail(user.getUserId(),
						new LoginDetail(user.getIpAdress(), user.getUserAgent(), user.getLocation()));
				userService.saveDeviceTokenDetail(user.getUserId(), user.getDeviceType(), user.getDeviceToken());
				userService.checkForNewWallet(user.getUserId(), user.getRandomId());
				response.getWriter().write(new ObjectMapper().writeValueAsString(new Response<>(body)));
				response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
			}
			response.setContentType(MediaType.APPLICATION_JSON.toString());
		} else {
			logger.debug("principal is not user object");
		}
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException failed) throws IOException, ServletException {
		Response<Object> res;
		if (failed instanceof LockedException)
			res = new Response<>(555, "Your account has been blocked by the admin");
		else if (failed instanceof DisabledException)
			res = new Response<>(203, "Please verify your email address");
		else if (failed instanceof ProviderNotFoundException)
			res = new Response<>(203, "You are not registerd with us.Please sign up");
		else
			res = new Response<>(401, failed.getMessage());
		response.getWriter().write(new ObjectMapper().writeValueAsString(res));
		response.setStatus(401);
		response.setContentType(MediaType.APPLICATION_JSON.toString());
	}
}