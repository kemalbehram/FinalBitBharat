package com.mobiloitte.server.apigateway.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mobiloitte.server.apigateway.config.JwtConfig;
import com.netflix.zuul.context.RequestContext;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

	private static final Logger LOGGER = LogManager.getLogger(JwtTokenAuthenticationFilter.class);
	private final JwtConfig jwtConfig;

	public JwtTokenAuthenticationFilter(JwtConfig jwtConfig) {
		this.jwtConfig = jwtConfig;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		String header = request.getHeader(jwtConfig.getHeader());

		response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		if (header == null || !header.startsWith(jwtConfig.getPrefix())) {
			chain.doFilter(request, response); // If not valid, go to the next filter.
			return;
		}
		String token = header.replace(jwtConfig.getPrefix(), "").trim();
		LOGGER.debug("Token found: {}", token);
		try {
			Claims claims = Jwts.parser().setSigningKey(jwtConfig.getSecret().getBytes()).parseClaimsJws(token)
					.getBody();
			String username = claims.getSubject();
			if (username != null) {
				List<String> authorities;
				RequestContext ctx = RequestContext.getCurrentContext();
				if (claims.get("authenticated", Boolean.class)) {
					authorities = claims.get("authorities", List.class);
				} else {
					authorities = Arrays.asList("ROLE_GUEST");
				}
				ctx.addZuulRequestHeader("smsOtp", claims.get("smsOtp", String.class));
				UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null,
						authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
				SecurityContextHolder.getContext().setAuthentication(auth);
				ctx.addZuulRequestHeader("userId", claims.get("userId", Long.class).toString());
				ctx.addZuulRequestHeader("username", claims.get("username", String.class));
				ctx.addZuulRequestHeader("role", authorities.get(0));
			}

		} catch (Exception e) {
			LOGGER.debug("Exception occured Token invalid: {}", e.getMessage());
			SecurityContextHolder.clearContext();
		}
		// go to the next filter in the filter chain
		chain.doFilter(request, response);
	}

}