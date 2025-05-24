package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.pentagon.app.request.AddManagerRequest;
import com.pentagon.app.request.AdminLoginRequest;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.request.TrainerLoginRequest;
import com.pentagon.app.response.ProfileResponceDto;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.AdminService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.utils.PasswordGenration;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

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

	@Autowired
	private PasswordGenration passwordGenration;

	@Autowired
	private MailService mailService;

	@Autowired
	private IdGeneration idGeneration;

	@Autowired
	private ActivityLogService activityLogService;

	@Transactional
	@Override
	public void addManager(CustomUserDetails adminDetails, @Valid AddManagerRequest newManager) {
		try {
			Manager manager = new Manager();
			manager.setManagerId(idGeneration.generateId("MANAGER"));
			manager.setName(newManager.getName());
			manager.setEmail(newManager.getEmail());
			manager.setMobile(newManager.getMobile());
			manager.setActive(true);
			String password = passwordGenration.generateRandomPassword();
			manager.setPassword(passwordEncoder.encode(password));
			manager.setCreatedAt(LocalDateTime.now());
			managerRepository.save(manager);

			String htmlContent = "<!DOCTYPE html>" + "<html>" + "<body style='font-family: Arial, sans-serif;'>"
					+ "<h2 style='color: #2e6c80;'>Welcome to Pentagon Portal</h2>" + "<p>Hi <strong>"
					+ manager.getName() + "</strong>,</p>"
					+ "<p>Your account has been created by the Admin. Please find your login details below:</p>"
					+ "<ul>" + "<li><strong>Email:</strong> " + manager.getEmail() + "</li>"
					+ "<li><strong>Temporary Password:</strong> " + password + "</li>" + "</ul>"
					+ "<p>Please log in and change your password immediately for security purposes.</p>"
					+ "<br><p>Regards,<br/>Pentagon Team</p>" + "</body>" + "</html>";

			mailService.sendPasswordEmail(manager.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
					htmlContent);
			
			activityLogService.log(adminDetails.getAdmin().getEmail(), adminDetails.getAdmin().getAdminId(), "ADMIN",
					"Manager with Unique Id " + manager.getManagerId() + " Added Successfully");
		} catch (Exception e) {
			throw new ManagerException("Failed to add Manager: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
	public boolean addExecutive(Executive executive) {
		// TODO Auto-generated method stub
		try {

			executive.setCreatedAt(LocalDateTime.now());
			executiveRepository.save(executive);
			return true;
		} catch (Exception e) {
			throw new ExecutiveException("Failed to Add Executive: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<Executive> viewAllExecutives() {
		try {
			List<Executive> executives = executiveRepository.findAll();
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

}
