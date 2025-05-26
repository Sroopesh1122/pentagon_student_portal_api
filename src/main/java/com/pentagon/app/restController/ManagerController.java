package com.pentagon.app.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.TrainerRepository;
import com.pentagon.app.request.AddExecutiveRequest;
import com.pentagon.app.request.AddTrainerRequest;
import com.pentagon.app.request.UpdateManagerRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlContent;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.utils.JwtUtil;
import com.pentagon.app.utils.PasswordGenration;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/manager")
public class ManagerController {

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private ManagerService managerService;
	
	@Autowired
	private IdGeneration idGeneration;
	
	@Autowired
	private JwtUtil jwtUtil;
	
	@Autowired
	private ActivityLogService activityLogService;
	
	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Autowired
	private PasswordGenration passwordGenration;
	
	@Autowired
	private HtmlContent htmlContentService;
	
	@Autowired
	private MailService mailService;
	
	@Autowired
	private TrainerRepository trainerRepository;
	
	@PostMapping("/secure/updateManager")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateManager(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@Valid @RequestBody UpdateManagerRequest request, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			throw new ManagerException("Ivalid Input Data", HttpStatus.BAD_REQUEST);
		}
		
		Manager manager = managerDetails.getManager();
		manager.setName(request.getName());
		manager.setEmail(request.getEmail());
		manager.setMobile(request.getMobile());
		
		if (request.getPassword() != null && !request.getPassword().isBlank()) {
			
            String hashedPassword = passwordEncoder.encode(request.getPassword());
            manager.setPassword(hashedPassword);
            
        }
		
		Manager updatedManager = managerService.updateManager(manager);
		
		activityLogService.log(managerDetails.getManager().getEmail(), 
				managerDetails.getManager().getManagerId(), 
				"MANAGER", 
				"Manager with ID "+managerDetails.getManager().getManagerId() +" updated their own profile details");
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Updated Successfully", null));
	}
	
	@PostMapping("secure/addExecutive")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> addExecutive(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@Valid @RequestBody AddExecutiveRequest request, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			throw new ManagerException("Invalid Input Data", HttpStatus.BAD_REQUEST );
		}
		
		if(executiveRepository.existsByEmail(request.getEmail())) {
			throw new ExecutiveException("Email already in use by another executive", HttpStatus.CONFLICT);
		}
		
		Executive executive = new Executive();
		executive.setExecutiveId(idGeneration.generateId("EXECUTIVE"));
		executive.setName(request.getName());
		executive.setEmail(request.getEmail());
		executive.setMobile(request.getMobile());
		executive.setActive(true);
		
		String password = passwordGenration.generateRandomPassword();
		executive.setPassword(passwordEncoder.encode(password));
		
		Executive newExecutive = managerService.addExecutive(executive);
		
		String htmlContent=htmlContentService.getHtmlContent(executive.getName(), executive.getEmail(), password);
		
		try {
				mailService.sendPasswordEmail(executive.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
				htmlContent);
		}catch(Exception e) {
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		activityLogService.log(managerDetails.getManager().getEmail(), 
				managerDetails.getManager().getManagerId(), 
				"MANAGER", 
				"Manager with ID " + managerDetails.getManager().getManagerId() + " added a new Executive with ID " + newExecutive.getExecutiveId());
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Added Successfully", null));
	}

	
	@PostMapping("secure/addTrainer")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> addTrainer(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@Valid @RequestBody AddTrainerRequest request, BindingResult bindingResult){
		
		if(bindingResult.hasErrors()) {
			throw new ManagerException("Invalid Input Data", HttpStatus.BAD_REQUEST );
		}
		
		if(trainerRepository.existsByEmail(request.getEmail())) {
			throw new ExecutiveException("Email already in use by another trainer", HttpStatus.CONFLICT);
		}
		
		Trainer trainer = new Trainer();
		trainer.setTrainerId(idGeneration.generateId("TRAINER"));
		trainer.setName(request.getName());
		trainer.setEmail(request.getEmail());
		trainer.setMobile(request.getMobile());
		trainer.setTrainerStack(request.getTrainerStack());
		trainer.setTechnologies(request.getTechnologies());
		trainer.setYearOfExperiences(request.getYearOfExperiences());
		trainer.setQualification(request.getQualification());
		trainer.setAcitve(true);
		
		String password = passwordGenration.generateRandomPassword();
		trainer.setPassword(passwordEncoder.encode(password));
		
		Trainer newTrainer = managerService.addTrainer(trainer);
		
		String htmlContent=htmlContentService.getHtmlContent(trainer.getName(), trainer.getEmail(), password);
		
		try {
				mailService.sendPasswordEmail(trainer.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
				htmlContent);
		}catch(Exception e) {
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		activityLogService.log(managerDetails.getManager().getEmail(), 
				managerDetails.getManager().getManagerId(), 
				"MANAGER", 
				"Manager with ID " + managerDetails.getManager().getManagerId() + " added a new Trainer with ID " + newTrainer.getTrainerId());
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Added Successfully", null));
	}

	
	@GetMapping("/secure/viewAllTrainers")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> viewAllTrainers(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int limit,
	        @RequestParam(required = false) String stack,
	        @RequestParam(required = false) String name,
	        @RequestParam(required = false) String trainerId){
		if (managerDetails.getManager() == null) {
	        throw new ManagerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
	    }
		
		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());
		
		Page<Trainer> trainers = managerService.viewAllTrainers(stack, name, trainerId, pageable);
		
		Page<TrainerDTO> TrainerDTOResponse = trainers.map(trainer -> {
            TrainerDTO Trainerdto = new TrainerDTO();
            Trainerdto.setId(trainer.getId());
            Trainerdto.setTrainerId(trainer.getTrainerId());
            Trainerdto.setName(trainer.getName());
            Trainerdto.setEmail(trainer.getEmail());
            Trainerdto.setMobile(trainer.getMobile());
            Trainerdto.setTrainerStack(trainer.getTrainerStack());
            Trainerdto.setQualification(trainer.getQualification());
            Trainerdto.setYearOfExperiences(trainer.getYearOfExperiences());
            Trainerdto.setTechnologies(trainer.getTechnologies());
            Trainerdto.setActive(trainer.isAcitve());
            Trainerdto.setCreatedAt(trainer.getCreatedAt());
            Trainerdto.setUpdatedAt(trainer.getUpdatedAt());
            return Trainerdto;
        });
		
		return ResponseEntity.ok(
	            new ApiResponse<>("success", "Trainers fetched successfully", TrainerDTOResponse)
	        );
	}
	
	@PostMapping("/secure/acceptJobDescription")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> acceptJobDescription(@AuthenticationPrincipal CustomUserDetails managerDetails,
	        @RequestParam String jobDescriptionId){
		
		if (managerDetails.getManager() == null) {
	        throw new ManagerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
	    }
		
		try {
			JobDescription acceptedJobDescription = managerService.acceptJobDescription(jobDescriptionId);
			activityLogService.log(managerDetails.getManager().getEmail(), 
					managerDetails.getManager().getManagerId(), 
					"MANAGER", 
					"Manager with ID " + managerDetails.getManager().getManagerId() + " Approved the Job Posted, Job Id " + jobDescriptionId);
			return ResponseEntity.ok(new ApiResponse<>("success", "Job Description accepted successfully", null));
		}
		catch(JobDescriptionException e) {
			return ResponseEntity.ok(new ApiResponse<>("failure","Could not accept JobDEscription", null));
		}
	
	}
	
	
	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getManagerProfile(@AuthenticationPrincipal CustomUserDetails managerDetails) {
		if(managerDetails.getManager() == null) {
			throw new ManagerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
	    Manager manager= managerDetails.getManager();
	    ProfileResponse details = managerService.getProfile(manager);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Manager Profile", details));
	}
	
	
	
}
