package com.pentagon.app.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Student;
import com.pentagon.app.request.AddManagerRequest;
import com.pentagon.app.request.AdminLoginRequest;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.request.TrainerLoginRequest;
import com.pentagon.app.response.ProfileResponse;

import jakarta.validation.Valid;

public interface AdminService {

	public boolean updateAdmin(Admin admin);
	
	//Excutive
	public Executive addExecutive(Executive executive);
	
	
	public void disableExecutiveByUniqueId(String executiveId);//
	
	//Manager
	public Manager addManager( Manager manager);
	
	
	public void disableManagerByUniqueId(String managerId);//
	
	//Student
	public List<Student> viewAllStudents();//
	
	//JD
	public List<JobDescription> viewAllJobDescriptions();//

	public String loginWithPassword(AdminLoginRequest request);

	public ProfileResponse getProfile(Admin admin);
	
	public boolean getManagerByEmail(String email);
	
	public boolean getExecutiveByEmail(String email);


	

	
	
	
}
