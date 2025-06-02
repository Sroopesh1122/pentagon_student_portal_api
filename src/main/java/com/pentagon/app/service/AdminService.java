package com.pentagon.app.service;


import com.pentagon.app.entity.Admin;
import com.pentagon.app.request.AdminLoginRequest;
import com.pentagon.app.response.ProfileResponse;


public interface AdminService {

	public Admin updateAdmin(Admin admin);

	public String loginWithPassword(AdminLoginRequest request);

	public ProfileResponse getProfile(Admin admin);
	
}
