package com.pentagon.app.restController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.ExecutiveDTO;
import com.pentagon.app.Dto.ManagerDTO;
import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.request.AddExecutiveRequest;
import com.pentagon.app.request.AddManagerRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.AdminService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.utils.HtmlContent;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.JwtUtil;
import com.pentagon.app.utils.PasswordGenration;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
	
	
	@Autowired 
	AdminService adminservice;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MailService mailService;
	@Autowired 
	private IdGeneration idGeneration;
	@Autowired
	private ManagerService managerService;
	@Autowired
	private ActivityLogService activityLogService;
	@Autowired
	private OtpService otpService;
	@Autowired
	private HtmlContent htmlContentService;
	@Autowired
	private PasswordGenration passwordGenration;
	
	@PostMapping("/secure/addManager")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addManagerByAdmin(
	        @AuthenticationPrincipal CustomUserDetails adminDetails,
	        @Valid @RequestBody AddManagerRequest newManager,
	        BindingResult bindingResult) {

		try {
	    if (bindingResult.hasErrors()) {
	        throw new AdminException("Invalid data", HttpStatus.BAD_REQUEST);
	    }
	    if(adminDetails==null)
	    {
	    	throw new AdminException("Admin details not found",HttpStatus.UNAUTHORIZED);
	    }
	    boolean checkManagerEmail=adminservice.getManagerByEmail(newManager.getEmail());
	    
	    if(checkManagerEmail)
	    {
	    	Manager manager = new Manager();
			manager.setManagerId(idGeneration.generateId("MANAGER"));
			manager.setName(newManager.getName());
			manager.setEmail(newManager.getEmail());
			manager.setMobile(newManager.getMobile());
			manager.setActive(true);
			String password = passwordGenration.generateRandomPassword();
			manager.setPassword(passwordEncoder.encode(password));
			
			manager=adminservice.addManager(manager); 
			
			String htmlContent=htmlContentService.getHtmlContent(manager.getName(), manager.getEmail(), password);

			mailService.sendPasswordEmail(manager.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
					htmlContent);
			
			activityLogService.log(adminDetails.getAdmin().getEmail(), adminDetails.getAdmin().getAdminId(), "ADMIN",
					"Manager with Unique Id " + manager.getManagerId() + " Added Successfully");

	    }else
		{
			throw new AdminException("Email is already exists", HttpStatus.CONFLICT);
		}
		}catch(Exception e)
		{
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	    return ResponseEntity.ok(new ApiResponse<>("success", "Manager added successfully", null));
	}
	
	
	@PostMapping("/secure/addExecutive")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addExecutiveByAdmin(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@Valid @RequestBody	AddExecutiveRequest newExecutive,
			BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
		  throw new AdminException("Invalid data ", HttpStatus.BAD_REQUEST);
		if(adminDetails==null)
	    {
	    	throw new AdminException("Admin details not found",HttpStatus.UNAUTHORIZED);
	    }
		
		try {
		boolean checkExecutiveEmail=adminservice.getExecutiveByEmail(newExecutive.getEmail());
		if(checkExecutiveEmail) {
		
		Executive executive=new Executive();
		executive.setExecutiveId(idGeneration.generateId("EXECUTIVE"));
		executive.setName(newExecutive.getName());
		executive.setEmail(newExecutive.getEmail());
		executive.setActive(true);
		executive.setMobile(newExecutive.getMobile());
		String password = passwordGenration.generateRandomPassword();
		executive.setPassword(passwordEncoder.encode(password));
		executive.setCreatedAt(LocalDateTime.now());
		executive=adminservice.addExecutive(executive);
		
		String htmlContent=htmlContentService.getHtmlContent(executive.getName(), executive.getEmail(), password);

		mailService.sendPasswordEmail(executive.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
				htmlContent);

		
		activityLogService.log(adminDetails.getAdmin().getEmail(), 
				adminDetails.getAdmin().getAdminId(), 
				"ADMIN", 
				"Executive with Unique Id "+ executive.getExecutiveId()+" Added Successfully");
		}
		}catch(Exception e)
		{
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return ResponseEntity.ok(new ApiResponse<>("success","Executive added Successfully",null));
	}
	

	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAdminProfile(@AuthenticationPrincipal CustomUserDetails adminDetails) {
	    Admin admin= adminDetails.getAdmin();
	    ProfileResponse details = adminservice.getProfile(admin);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Admin Profile", details));
	}

	@GetMapping("/secure/viewAllTrainers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> viewAllTrainers(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int limit,
	        @RequestParam(required = false) String stack,
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String trainerId){
		
		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
		
		Page<Trainer> trainers = managerService.viewAllTrainers(stack, name, trainerId, pageable);
		
		Page<TrainerDTO> TrainerDTOPage = trainers.map(trainer -> {
            TrainerDTO dto = new TrainerDTO();
            dto.setId(trainer.getId());
            dto.setTrainerId(trainer.getTrainerId());
            dto.setName(trainer.getName());
            dto.setEmail(trainer.getEmail());
            dto.setMobile(trainer.getMobile());
            dto.setTrainerStack(trainer.getTrainerStack());
            dto.setQualification(trainer.getQualification());
            dto.setYearOfExperiences(trainer.getYearOfExperiences());
            dto.setTechnologies(trainer.getTechnologies());
            dto.setActive(trainer.isAcitve());
            dto.setCreatedAt(trainer.getCreatedAt());
            dto.setUpdatedAt(trainer.getUpdatedAt());
            return dto;
        });		
		
		return ResponseEntity.ok(
	            new ApiResponse<>("success", "Trainers fetched successfully", TrainerDTOPage)
	        );
	}	
	
	@GetMapping("/secure/viewAllManagers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> viewAllManagers(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int limit,
	        @RequestParam(required = false) String email,
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String managerId,
	        @RequestParam(required = false) String number){
		
		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
		
		Page<Manager> managers = adminservice.viewAllManagers(name,number,email,managerId,pageable);
		
		Page<ManagerDTO> managerDTOPage = managers.map(manager -> {
            ManagerDTO dto=new ManagerDTO();
            dto.setId(manager.getId());
            dto.setManagerId(manager.getManagerId());
            dto.setName(manager.getName());
            dto.setEmail(manager.getEmail());
            dto.setMobile(manager.getMobile());
            dto.setActive(manager.isActive());
            dto.setCreatedAt(manager.getCreatedAt());
            dto.setUpdatedAt(manager.getUpdatedAt());
            return dto;
        });		
		
		return ResponseEntity.ok(
	            new ApiResponse<>("success", "Managers fetched successfully", managerDTOPage)
	        );
	}
	
	@GetMapping("/secure/viewAllExecutive")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> viewAllExecutives(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int limit,
	        @RequestParam(required = false) String email,
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String managerId,
	        @RequestParam(required = false) String number){
		
		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
		
		Page<Executive> executives = adminservice.viewAllExecutives(name,number,email,managerId,pageable);
		
		Page<ExecutiveDTO> executiveDTOPage = executives.map(executive -> {
            ExecutiveDTO dto=new ExecutiveDTO();
            dto.setId(executive.getId());
            dto.setExecutiveId(executive.getExecutiveId());
            dto.setName(executive.getName());
            dto.setEmail(executive.getEmail());
            dto.setMobile(executive.getMobile());
            dto.setActive(executive.isActive());
            dto.setCreatedAt(executive.getCreatedAt());
            dto.setUpdatedAt(executive.getUpdatedAt());
            return dto;
        });		
		
		return ResponseEntity.ok(
	            new ApiResponse<>("success", "Executives fetched successfully", executiveDTOPage)
	        );
	}
}
