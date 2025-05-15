package com.pentagon.ServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.Repository.AdminRepository;
import com.pentagon.Service.AdminService;
import com.pentagon.entity.Admin;
import com.pentagon.entity.Executive;
import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Manager;
import com.pentagon.entity.Student;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;
	
	@Override
	public boolean updateAdmin(Admin admin) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Executive addExecutive(Executive executive) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Executive> viewAllExecutives() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disableExecutiveByUniqueId(String executiveId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Manager addManager(Manager manager) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Manager> viewAllManagers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void disableManagerByUniqueId(String managerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<Student> viewAllStudents() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<JobDescription> viewAllJobDescriptions() {
		// TODO Auto-generated method stub
		return null;
	}

}
