package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Student;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.exception.StudentException;
import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.repository.StudentRepository;
import com.pentagon.app.service.AdminService;

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
		try {
			List<Executive> executives = executiveRepository.findAll();
			 return executives;
		}
	    catch(Exception e) {
	    	 throw new ExecutiveException("Failed to Fetch Executives : "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
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
		try {
			List<Student> students = studentRepository.findAll();
			return students;
		}
		catch(Exception e) {
			 throw new StudentException("Failed to Fetch Students : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<JobDescription> viewAllJobDescriptions() {
		// TODO Auto-generated method 
        try {
        	List<JobDescription> jobDescriptions = jobDescriptionRepository.findAll();
        	return jobDescriptions;
        }
        catch(Exception e) {
        	throw new JobDescriptionException("Failed to Job Descriptions", HttpStatus.NOT_FOUND);
        }
		
	}
	
}
