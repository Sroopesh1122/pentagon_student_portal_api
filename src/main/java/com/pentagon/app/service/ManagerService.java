package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.request.ManagerLoginRequest;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.response.ProfileResponse;

public interface ManagerService {

    public Manager login(String email, String otp);
	
	public Manager updateManager(Manager manager);
	
	public Executive addExecutive(Executive executive);
	
	public Trainer addTrainer(Trainer trainer);
	
	public JobDescription updateJobDescription(JobDescription jobDescription);

	String loginWithPassword(ManagerLoginRequest managerLoginRequest);
	
	Page<Trainer> viewAllTrainers(String stack, String name, String trainerId, Pageable pageable);

	public ProfileResponse getProfile(Manager manager);
	
	public Page<Manager> findAll(String q ,Pageable pageable);



}
