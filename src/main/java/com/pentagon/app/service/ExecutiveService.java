package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Executive;

import com.pentagon.app.request.ExecutiveLoginRequest;
import com.pentagon.app.response.ProfileResponse;


public interface ExecutiveService {
	
	public boolean updateExecutive(Executive executive);
	
	public Executive addExecutive(Executive executive);
	
	String loginWithPassword(ExecutiveLoginRequest executiveLoginRequest);

	public ProfileResponse getProfile(Executive executive);
	
	public Page<Executive> getAllExecutives(String q , Pageable pageable);
	
	public void disableExecutiveByUniqueId(String executiveId);
	
	public boolean getExecutiveByEmail(String email);

}