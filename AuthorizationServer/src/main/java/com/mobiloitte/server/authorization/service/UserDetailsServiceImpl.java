package com.mobiloitte.server.authorization.service;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mobiloitte.server.authorization.config.JwtConfig;
import com.mobiloitte.server.authorization.dto.UserDto;
import com.mobiloitte.server.authorization.dto.VerifyIpDTO;
import com.mobiloitte.server.authorization.enums.Role;
import com.mobiloitte.server.authorization.enums.UserStatus;
import com.mobiloitte.server.authorization.feign.UserClient;
import com.mobiloitte.server.authorization.feign.WalletClient;
import com.mobiloitte.server.authorization.model.DeviceTokenDetail;
import com.mobiloitte.server.authorization.model.LoginDetail;
import com.mobiloitte.server.authorization.model.Response;
import com.mobiloitte.server.authorization.model.TwoFaType;
import com.mobiloitte.server.authorization.model.User;

import feign.FeignException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service 
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserClient userClient;

	@Autowired
	private WalletClient walletClient;
	
	@Autowired
	private JwtConfig jwtConfig;

	private static final Logger LOGGER = LogManager.getLogger(UserDetailsServiceImpl.class);

	@Override
	public User loadUserByUsername(String username) {
		try {
			Response<UserDto> response = userClient.getUserByEmail(username);
			LOGGER.debug("Response recieved from user service :{}", response);
			if (response.getData() != null) {
				UserDto userDto = response.getData();
				User user = new User();
				user.setUserId(userDto.getUserId());
				user.setPassword(userDto.getPassword());
				user.setEmail(userDto.getUsername());
				user.setTwoFaType(userDto.getTwoFaType());
				user.setRole(userDto.getRole());
				user.setRandomId(userDto.getRandomId());
				user.setIpAdress(userDto.getIpAdress());
				if (userDto.getTwoFaType() == TwoFaType.NONE || userDto.getTwoFaType() == TwoFaType.SKIP)
					user.setAuthenticated(true);
				if (userDto.getRole() == Role.SUBADMIN) {
					List<SimpleGrantedAuthority> list = userDto.getPrevilage().parallelStream()
							.map(SimpleGrantedAuthority::new).collect(Collectors.toList());
					list.add(new SimpleGrantedAuthority("ROLE_SUBADMIN"));
					user.setAuthorities(list);
				} else {
					String role;
					if (userDto.getRole() == Role.ADMIN)
						role = "ROLE_ADMIN";
					else if (userDto.getRole() == Role.USER)
						role = "ROLE_USER";
					else if (userDto.getRole() == Role.DEVELOPER)
						role = "ROLE_DEVELOPER";
					else
						role = "ROLE_GUEST";
					user.setAuthorities(Arrays.asList(new SimpleGrantedAuthority(role)));
				}
				if (userDto.getUserStatus() == UserStatus.BLOCK) {
					user.setEnabled(true);
					user.setBlocked(false);
				} else if (userDto.getUserStatus() == UserStatus.UNVERIFIED) {
					user.setEnabled(false);
				} else if (userDto.getUserStatus() == UserStatus.ACTIVE) {
					user.setEnabled(true);
				}
				else if (userDto.getUserStatus()==UserStatus.DEACTIVATE) {
					user.setEnabled(false);
					user.setBlocked(true);
				}
				return user;
			}
		} catch (FeignException e) {
			LOGGER.error(e.getMessage());
			throw new UsernameNotFoundException(e.getMessage());
		}
		// If user not found. Throw this exception.
		throw new UsernameNotFoundException("Username: " + username + " not found");
	}

	public String generateToken(User user) {
		long now = System.currentTimeMillis();
		return Jwts.builder().setSubject(user.getUsername())
				.claim("authorities",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.claim("role", user.getRole()).claim("authenticated", user.isAuthenticated())
				.claim("userId", user.getUserId()).claim("username", user.getUsername()).setIssuedAt(new Date(now))
				.setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes()).compact();
	}

	public String generateToken(String username, String otp) {
		long now = System.currentTimeMillis();
		User user = loadUserByUsername(username);
		return Jwts.builder().setSubject(user.getUsername())
				.claim("authorities",
						user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
				.claim("role", user.getRole()).claim("smsOtp", otp).claim("authenticated", false)
				.claim("userId", user.getUserId()).claim("username", user.getUsername()).setIssuedAt(new Date(now))
				.setExpiration(new Date(now + jwtConfig.getExpiration() * 1000))
				.signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes()).compact();
	}

	public void saveLoginDetail(Long userId, LoginDetail loginDetail) {
		new Thread(() -> {
			try {
				Response<Object> response = userClient.saveLoginDetail(userId,loginDetail);
				LOGGER.debug("Login detail saved : {}", response);
			} catch (Exception e) {
				LOGGER.catching(e);
			}
		}).start();
	}

	public void saveDeviceTokenDetail(Long userId, String deviceType,String deviceToken) {
		new Thread(() -> {
			try {
				DeviceTokenDetail deviceTokenDetail = new DeviceTokenDetail();
				deviceTokenDetail.setDeviceToken(deviceToken);
				deviceTokenDetail.setDeviceType(deviceType);
				Response<Object> response = userClient.saveDeviceTokenDetail(userId,deviceTokenDetail);
				LOGGER.debug("device token saved : {}",response);
			}catch (Exception e) {
				LOGGER.catching(e);
			}
		}).start();
	}
	public void sendVerifyIpMail(String ipAddress, Long userId, String webUrl) {
		new Thread(() -> {
			try {
				Response<Object> response = userClient.sendVerifyIpMail(new VerifyIpDTO(userId, ipAddress, webUrl));
				LOGGER.debug("Verify Email sent: {}", response);
			} catch (Exception e) {
				LOGGER.catching(e);
			}
		}).start();
	}
	
	public void checkForNewWallet(Long userId,String randomId) {
		new Thread(() -> {
			try {
				Response<String> response = walletClient.createWallet(userId,randomId);
				LOGGER.debug("Wallet Checked : {}", response);
			} catch (Exception e) {
				LOGGER.catching(e);
			}
		}).start();
	}

	public Object userDetail(String email) {
		Response<Object> response = new Response<>();
		/*
		 * try {
		 * 
		 * response = userClient.getrole(email)
;
		 * 
		 * if(response.getStatus()==200)
		 * 
		 * return response.getData(); else return response.getStatus();
		 * 
		 * } catch (Exception e) { LOGGER.catching(e); return null; }
		 */
		try {

			response = userClient.getrole(email)
;

			if (response.getStatus() == 200)

				return response.getData();
			else
				return response.getStatus();

		} catch (Exception e) {
			LOGGER.catching(e);
			return null;
		}

	}

}