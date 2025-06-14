package com.pentagon.app.utils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pentagon.app.exception.SessionExpiredException;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.CustomUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final CustomUserDetailsService customUserDetailsService;

	public JwtFilter(JwtUtil jwtUtil, CustomUserDetailsService customUserDetailsService) {
		this.jwtUtil = jwtUtil;
		this.customUserDetailsService = customUserDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		final String authorizationHeader = request.getHeader("Authorization"); // form header

		if (authorizationHeader != null) {

			String email = null;
			String jwt = null;
			String role = null;
			// Extracting subject, role and token from authorizationHeader
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				email = jwtUtil.extractSubject(jwt);
				role = jwtUtil.extractClaims(jwt).get("role").toString().toUpperCase();

			}
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				CustomUserDetails userDetails =null;
				if(role.equals("ADMIN"))
				{
					userDetails= (CustomUserDetails) customUserDetailsService.loadAdmin(email);
				}
				else if(role.equals("EXECUTIVE"))
				{
					userDetails =  (CustomUserDetails) customUserDetailsService.loadExecutive(email);
				}
				else if(role.equals("MANAGER"))
				{
					userDetails =  (CustomUserDetails) customUserDetailsService.loadManager(email);
				}
				else if(role.equals("PROGRAMHEAD"))
				{
					userDetails =  (CustomUserDetails) customUserDetailsService.loadProgramHead(email);
				}
				else if(role.equals("STUDENTADMIN"))
				{
					userDetails =  (CustomUserDetails) customUserDetailsService.loadStudentAdmin(email);
				}
	            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
	                    userDetails,
	                    null,
	                    userDetails.getAuthorities()
	            );
	            System.out.println(authentication);
	            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		chain.doFilter(request, response);
	}

	private void handleCustomException(HttpServletResponse response, String message) throws IOException {
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.setContentType("application/json");

		Map<String, Object> errorDetails = new HashMap<>();
		errorDetails.put("status", "failure");
		errorDetails.put("type", "Session Expired");
		errorDetails.put("error", message);
		errorDetails.put("localTime", LocalDateTime.now());
		errorDetails.put("relogin", true);

		new ObjectMapper().writeValue(response.getOutputStream(), errorDetails);
	}
}
