package com.pentagon.app.utils;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.pentagon.app.Service.CustomUserDetails;
import com.pentagon.app.Service.CustomUserDetailsService;

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
			//Extracting subject, role and token from authorizationHeader
			if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
				jwt = authorizationHeader.substring(7);
				email = jwtUtil.extractSubject(jwt);
				role = jwtUtil.extractClaims(jwt).get("role").toString();
			}
			if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				
	            CustomUserDetails userDetails = (CustomUserDetails) customUserDetailsService.loadUserByUsername(email);
	            System.out.println(userDetails);
	            
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
}
