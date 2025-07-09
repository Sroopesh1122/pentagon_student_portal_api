package com.pentagon.app.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.Dto.JdStatsDTO;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.request.ExecutiveLoginRequest;
import com.pentagon.app.response.ProfileResponse;


public interface ExecutiveService {
	
	public Executive updateExecutive(Executive executive);
	
	public Executive addExecutive(Executive executive);
	
	String loginWithPassword(ExecutiveLoginRequest executiveLoginRequest);

	public ProfileResponse getProfile(Executive executive);
	
	public Page<Executive> getAllExecutives(String q , Pageable pageable);

	
	public void disableExecutiveByUniqueId(String executiveId);
	
	public Executive getExecutiveByEmail(String email);

	public Executive getExecutiveById(String executiveId);
	
	public Object getExecutiveJdDetails(String executiveId);
	
	public Page<JobDescription> getRecentJobDescriptions(String executiveId , Integer count);

	public Page<Executive> getExecutivesByManagerIdAndSearchQuery(String managerId, String q, Pageable pageable);
	
	
	public List<JdStatsDTO> getExecutiveJdStats(String executiveId,String timeUnit ,Integer range);
	
	
	public Long getTotalCount();
}