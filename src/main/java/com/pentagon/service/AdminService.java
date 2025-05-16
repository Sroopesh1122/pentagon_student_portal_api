package com.pentagon.service;

import java.util.List;

import com.pentagon.entity.Admin;
import com.pentagon.entity.Executive;
import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Manager;
import com.pentagon.entity.Student;

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
	
	
	
}
