package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Student;
import com.pentagon.app.requestDTO.AdminLoginRequest;
import com.pentagon.app.requestDTO.OtpVerificationRequest;

public interface AdminService {

	public boolean updateAdmin(Admin admin);
	
	//Excutive
	public boolean addExecutive(Executive executive);
	
	public List<Executive> viewAllExecutives();
	
	public void disableExecutiveByUniqueId(String executiveId);
	
	//Manager
    public boolean addManager(Manager manager);
	
	public List<Manager> viewAllManagers();
	
	public void disableManagerByUniqueId(String managerId);
	
	//Student
	public List<Student> viewAllStudents();
	
	//JD
	public List<JobDescription> viewAllJobDescriptions();

	boolean verifyOtp(OtpVerificationRequest request);

	String loginWithPassword(AdminLoginRequest request);
	
	
	
}
