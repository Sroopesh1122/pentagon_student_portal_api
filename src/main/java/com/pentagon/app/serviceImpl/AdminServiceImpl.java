package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import com.pentagon.app.request.AdminLoginRequest;
import com.pentagon.app.response.ProfileResponceDto;
import com.pentagon.app.service.AdminService;
import com.pentagon.app.service.OtpService;

import jakarta.transaction.Transactional;

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

	@Autowired
	private OtpService otpService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	@Override
	public Manager addManager(Manager newManager) {
		
		try {
			newManager.setCreatedAt(LocalDateTime.now());
			Manager manager=managerRepository.save(newManager);
			return manager;

			} catch (Exception e) {
			throw new AdminException("Failed to add Manager: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public boolean updateAdmin(Admin admin) {
		// TODO Auto-generated method stub
		try {
			admin.setUpdatedAt(LocalDateTime.now());
			adminRepository.save(admin);
			return true;
		} catch (Exception e) {
			throw new AdminException("Failed to update Admin: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Executive addExecutive(Executive newExecutive) {
		try {
			
			return executiveRepository.save(newExecutive);
			
		} catch (Exception e) {
			throw new ExecutiveException("Failed to Add Executive: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Page<Executive> viewAllExecutives(String name,String number,String email,String managerId,Pageable pageable) {
		try {
			Page<Executive> executives = executiveRepository.findByFilters(name,number,email,managerId,pageable);
			return executives;
		} catch (Exception e) {
			throw new ExecutiveException("Failed to Fetch Executives : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void disableExecutiveByUniqueId(String executiveId) {
		// TODO Auto-generated method stub
		Executive executive = executiveRepository.findByExecutiveId(executiveId).orElseThrow(
				() -> new ExecutiveException("Executive not found with ID: " + executiveId, HttpStatus.NOT_FOUND));

		executive.setActive(!executive.isActive());
		executive.setUpdatedAt(LocalDateTime.now());
		executiveRepository.save(executive);
	}

	@Override
	public Page<Manager> viewAllManagers(String name,String number,String email,String managerId,Pageable pageable) {
		// TODO Auto-generated method stub
		Page<Manager> managers = managerRepository.findByFilters(name,number,email,managerId,pageable);
		if (managers.isEmpty()) {
			throw new ManagerException("No Managers Found", HttpStatus.NOT_FOUND);
		}
		return managers;
	}

	@Override
	public void disableManagerByUniqueId(String managerId) {
		// TODO Auto-generated method stub
		Manager manager = managerRepository.findByManagerId(managerId).orElseThrow(
				() -> new ExecutiveException("Manager not found with ID: " + managerId, HttpStatus.NOT_FOUND));

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
		} catch (Exception e) {
			throw new StudentException("Failed to Fetch Students : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<JobDescription> viewAllJobDescriptions() {
		// TODO Auto-generated method
		try {
			List<JobDescription> jobDescriptions = jobDescriptionRepository.findAll();
			return jobDescriptions;
		} catch (Exception e) {
			throw new JobDescriptionException("Failed to Job Descriptions", HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public String loginWithPassword(AdminLoginRequest request) {
		// TODO Auto-generated method stub
		Admin admin = adminRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new AdminException("Admin not found", HttpStatus.NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
			throw new AdminException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		String otp = otpService.generateOtpAndStore(admin.getEmail());
		otpService.sendOtpToEmail(admin.getEmail(), otp);

		return "OTP sent to registered email";

	}

	@Override
	public ProfileResponceDto getProfile(Admin admin) {
		ProfileResponceDto result = new ProfileResponceDto();
		result.setUniqueId(admin.getAdminId());
		result.setName(admin.getName());
		result.setEmail(admin.getEmail());
		result.setMobile(admin.getMobile());
		return result;
	}

	@Override
	public boolean getManagerByEmail(String email) {
		Optional<Manager> manager=managerRepository.findByEmail(email);
		if(manager.isPresent())
			return false;
		return true;
	}

	@Override
	public boolean getExecutiveByEmail(String email) {
		Optional<Executive> executive=executiveRepository.findByEmail(email);
		if(executive.isPresent())
			throw new ExecutiveException("Email is already exists", HttpStatus.CONFLICT);
		return true;
	}

}
