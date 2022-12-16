package com.mobiloitte.server.apigateway.config;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.google.common.collect.ImmutableList;
import com.mobiloitte.server.apigateway.filter.JwtTokenAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityTokenConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private JwtConfig jwtConfig;

	@Autowired
	private UrlMappings mappings;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().cors().and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().exceptionHandling()
				.authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED)).and()
				.addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				// add security here
				.antMatchers(mappings.getGuest().toArray(new String[0])).hasRole("GUEST")
				.antMatchers(mappings.getUser().toArray(new String[0])).hasRole("USER")
				.antMatchers("/*/admin/dashboard/**").hasAnyAuthority("ROLE_SUBADMIN", "ROLE_ADMIN", "ROLE_DEVELOPER")
				.antMatchers("/*/admin/user-management/**")
				.hasAnyAuthority("USER_MANAGEMENT", "ROLE_ADMIN", "ROLE_DEVELOPER")
				.antMatchers("/*/admin/hot-cold-storage/**")
				.hasAnyAuthority("HOT_COLD_LIMIT_MANAGEMENT", "ROLE_ADMIN", "ROLE_DEVELOPER")
				.antMatchers("/*/admin/fee-management/**")
				.hasAnyAuthority("FEE_PRICE_MANAGEMENT", "ROLE_ADMIN", "ROLE_DEVELOPER")
				.antMatchers("/*/admin/transaction-history/**")
				.hasAnyAuthority("WALLET_MANAGEMENT", "ROLE_ADMIN", "ROLE_DEVELOPER")
				.antMatchers("/*/admin/trade-history/**")
				.hasAnyAuthority("TRADE_MANAGEMENT", "ROLE_ADMIN", "ROLE_DEVELOPER")
				.antMatchers("/*/admin/kyc-management/**")
				.hasAnyAuthority("KYC_MANAGEMENT", "ROLE_ADMIN", "ROLE_DEVELOPER")
				.antMatchers("/*/admin/static-content/**")
				.hasAnyAuthority("STATIC_CONTENT", "ROLE_ADMIN", "ROLE_DEVELOPER")
				.antMatchers("/reference-service/admin/**")
				.hasAnyAuthority("REFERENCE_MANAGEMENT", "ROLE_ADMIN", "ROLE_DEVELOPER")
				.antMatchers(mappings.getAdmin().toArray(new String[0])).hasRole("ADMIN")
				.antMatchers(mappings.getDeveloper().toArray(new String[0])).hasRole("DEVELOPER")
				.antMatchers(mappings.getNone().toArray(new String[0])).permitAll()
				.antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll().anyRequest().hasRole("USER");
	}

	@Bean
	public RoleHierarchyImpl roleHierarchy() {
		RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
		roleHierarchy.setHierarchy("ROLE_DEVELOPER > ROLE_ADMIN > ROLE_SUBADMIN > ROLE_USER > ROLE_GUEST");
		return roleHierarchy;
	}

	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(ImmutableList.of("*"));
		configuration.setAllowedMethods(ImmutableList.of("*"));
		configuration.setAllowCredentials(true);
		configuration.setAllowedHeaders(ImmutableList.of("*"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
}