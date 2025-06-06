package com.pentagon.app.service;


import java.util.List;

import com.pentagon.app.Dto.JdStatsDTO;
import com.pentagon.app.Dto.JdVsClosureStatsDTO;
import com.pentagon.app.entity.Admin;
import com.pentagon.app.request.AdminLoginRequest;
import com.pentagon.app.response.ProfileResponse;


public interface AdminService {

	public Admin updateAdmin(Admin admin);

	public String loginWithPassword(AdminLoginRequest request);

	public ProfileResponse getProfile(Admin admin);
	
	public List<JdStatsDTO> getJdStats(String timeUnit , int range);
	
	
	public List<JdVsClosureStatsDTO> getJdVsClosureStats(String timeUnit , int range);
	
}
