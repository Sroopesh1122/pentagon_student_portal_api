package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Manager;
import com.pentagon.app.request.AddExecutiveRequest;
import com.pentagon.app.request.AddTrainerRequest;
import com.pentagon.app.request.ManagerLoginRequest;
import com.pentagon.app.request.UpdateManagerRequest;
import com.pentagon.app.response.ProfileResponse;

import jakarta.validation.Valid;

public interface ManagerService {

	public Manager addManager( Manager manager);
	
	void updateManagerDetails(CustomUserDetails managerDetails, UpdateManagerRequest request);
//	public Manager updateManager(Manager manager);
	
	public void addExecutive(CustomUserDetails managerDetails, @Valid AddExecutiveRequest newExecutive);
	
	public void addTrainer(CustomUserDetails managerDetails, @Valid AddTrainerRequest newTrainerRequest);
	
	public void disableManagerByUniqueId(String managerId);
	
	public boolean getManagerByEmail(String email);

	String loginWithPassword(ManagerLoginRequest managerLoginRequest);

	public ProfileResponse getProfile(Manager manager);
	
	public Page<Manager> findAll(String q ,Pageable pageable);






}
