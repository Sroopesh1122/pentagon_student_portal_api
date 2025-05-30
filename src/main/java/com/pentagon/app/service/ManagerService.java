package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Manager;
import com.pentagon.app.request.ManagerLoginRequest;
import com.pentagon.app.response.ProfileResponse;

public interface ManagerService {

	public Manager addManager( Manager manager);
	
	public Manager updateManager(Manager manager);
	
	public void disableManagerByUniqueId(String managerId);
	
	public boolean getManagerByEmail(String email);

	String loginWithPassword(ManagerLoginRequest managerLoginRequest);

	public ProfileResponse getProfile(Manager manager);
	
	public Page<Manager> findAll(String q ,Pageable pageable);



}
