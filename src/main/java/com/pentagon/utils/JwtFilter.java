package com.pentagon.utils;

import java.io.IOException;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pentagon.Service.CustomUserDetailsService;

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

		final String authorizationHeader = request.getHeader("Authorization");  // form header
		
		if (authorizationHeader != null) {

			String email = null;
			String jwt = null;
			String role = null;
			//Extracting subject, role and token from it
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				email = jwtUtil.extractSubject(jwt);
				role = jwtUtil.extractClaims(jwt).get("role").toString();
			}
			
		}

		chain.doFilter(request, response);
	}
}
