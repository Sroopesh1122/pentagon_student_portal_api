package com.pentagon.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.entity.Admin;
import com.pentagon.entity.Executive;
import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Manager;
import com.pentagon.entity.Student;
import com.pentagon.exception.AdminException;
import com.pentagon.exception.ExecutiveException;
import com.pentagon.exception.JobDescriptionException;
import com.pentagon.exception.ManagerException;
import com.pentagon.exception.StudentException;
import com.pentagon.repository.AdminRepository;
import com.pentagon.repository.ExecutiveRepository;
import com.pentagon.repository.JobDescriptionRepository;
import com.pentagon.repository.ManagerRepository;
import com.pentagon.repository.StudentRepository;
import com.pentagon.service.AdminService;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	@Override
	public boolean updateAdmin(Admin admin) {
		// TODO Auto-generated method stub
		try {
			admin.setUpdatedAt(LocalDateTime.now());
			adminRepository.save(admin);
			return true;
		}
		catch (Exception e) {
	        throw new AdminException("Failed to update Admin: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public boolean addExecutive(Executive executive) {
		// TODO Auto-generated method stub
		try {
			executive.setCreatedAt(LocalDateTime.now());
			executiveRepository.save(executive);
			return true;
		}
		catch(Exception e) {
			throw new ExecutiveException("Failed to Add Executive: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<Executive> viewAllExecutives() {
	    List<Executive> executives = executiveRepository.findAll();
	    if (executives.isEmpty()) {
	        throw new ExecutiveException("No executives found", HttpStatus.NOT_FOUND);
	    }
	    return executives;
	}


	@Override
	public void disableExecutiveByUniqueId(String executiveId) {
		// TODO Auto-generated method stub
		 Executive executive = executiveRepository.findByExecutiveId(executiveId)
		            .orElseThrow(() -> new ExecutiveException("Executive not found with ID: " + executiveId, HttpStatus.NOT_FOUND));
		 
		 executive.setActive(!executive.isActive());
		 executive.setUpdatedAt(LocalDateTime.now());
	     executiveRepository.save(executive);
	}

	@Override
	public boolean addManager(Manager manager) {
		// TODO Auto-generated method stub
		try {
			manager.setCreatedAt(LocalDateTime.now());
			managerRepository.save(manager);
			return true;
		}
		catch(Exception e) {
			throw new ManagerException("Failed to add Manager: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<Manager> viewAllManagers() {
		// TODO Auto-generated method stub
		List<Manager> managers = managerRepository.findAll();
	    if (managers.isEmpty()) {
	        throw new ManagerException("No Managers Found", HttpStatus.NOT_FOUND);
	    }
	    return managers;
	}

	@Override
	public void disableManagerByUniqueId(String managerId) {
		// TODO Auto-generated method stub
		 Manager manager = managerRepository.findByManagerId(managerId)
		            .orElseThrow(() -> new ExecutiveException("Manager not found with ID: " + managerId, HttpStatus.NOT_FOUND));
		 
		
		 manager.setActive(!manager.isActive());
		 manager.setUpdatedAt(LocalDateTime.now());
	     managerRepository.save(manager);
	}

	@Override
	public List<Student> viewAllStudents() {
		// TODO Auto-generated method stub
		List<Student> students = studentRepository.findAll();
	    if (students.isEmpty()) {
	        throw new StudentException("No Students Found", HttpStatus.NOT_FOUND);
	    }
	    return students;
	}

	@Override
	public List<JobDescription> viewAllJobDescriptions() {
		// TODO Auto-generated method stub
		List<JobDescription> jobDescriptions = jobDescriptionRepository.findAll();
	    if (jobDescriptions.isEmpty()) {
	        throw new JobDescriptionException("No Job Descriptions Found", HttpStatus.NOT_FOUND);
	    }
	    return jobDescriptions;
	}

}
