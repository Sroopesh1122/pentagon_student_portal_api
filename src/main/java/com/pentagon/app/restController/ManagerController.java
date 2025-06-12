package com.pentagon.app.restController;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.ExecutiveJDStatusDTO;
import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.request.AddExecutiveRequest;
import com.pentagon.app.request.AddTrainerRequest;
import com.pentagon.app.request.MangerJdStatusUpdateRequest;
import com.pentagon.app.request.UpdateManagerRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ExecutiveDetails;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.TrainerService;
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
	private ExecutiveService executiveService;

	@Autowired
	private PasswordGenration passwordGenration;

	@Autowired
	private HtmlContent htmlContentService;

	@Autowired
	private MailService mailService;

	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private TrainerService trainerService;

	@PostMapping("/secure/updateManager")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateManager(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@Valid @RequestBody UpdateManagerRequest request, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
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

		activityLogService.log(managerDetails.getManager().getEmail(), managerDetails.getManager().getManagerId(),
				"MANAGER",
				"Manager with ID " + managerDetails.getManager().getManagerId() + " updated their own profile details");

		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Updated Successfully", null));
	}

	// not working
	@PostMapping("secure/addExecutive")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> addExecutive(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@Valid @RequestBody AddExecutiveRequest request, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ExecutiveException("Invalid Input Data", HttpStatus.BAD_REQUEST);
		}

		Executive findExecutive = executiveService.getExecutiveByEmail(request.getEmail());

		if (findExecutive != null) {
			throw new ManagerException("Email Already Exists", HttpStatus.CONFLICT);
		}

		Executive executive = new Executive();
		executive.setExecutiveId(idGeneration.generateId("EXECUTIVE"));
		executive.setName(request.getName());
		executive.setEmail(request.getEmail());
		executive.setMobile(request.getMobile());
		executive.setActive(true);
		executive.setManagerId(managerDetails.getManager().getManagerId());
		String password = passwordGenration.generateRandomPassword();
		executive.setPassword(passwordEncoder.encode(password));

		Executive newExecutive = executiveService.addExecutive(executive);

		String htmlContent = htmlContentService.getHtmlContent(executive.getName(), executive.getEmail(), password);

		try {
			mailService.sendPasswordEmail(executive.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
					htmlContent);
		} catch (Exception e) {
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		activityLogService.log(managerDetails.getManager().getEmail(), managerDetails.getManager().getManagerId(),
				"MANAGER", "Manager with ID " + managerDetails.getManager().getManagerId()
						+ " added a new Executive with ID " + newExecutive.getExecutiveId());

		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Added Successfully", null));
	}

	@PostMapping("secure/addTrainer")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> addTrainer(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@Valid @RequestBody AddTrainerRequest request, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ManagerException("Invalid Input Data", HttpStatus.BAD_REQUEST);
		}

		if (trainerService.checkExistsByEmail(request.getEmail())) {
			throw new ExecutiveException("Email already in use by another trainer", HttpStatus.CONFLICT);
		}

		Trainer trainer = new Trainer();
		trainer.setTrainerId(idGeneration.generateId("TRAINER"));
		trainer.setName(request.getName());
		trainer.setEmail(request.getEmail());
		trainer.setMobile(request.getMobile());
		trainer.setYearOfExperiences(request.getYearOfExperiences());
		trainer.setQualification(request.getQualification());
		trainer.setAcitve(true);

		String password = passwordGenration.generateRandomPassword();
		trainer.setPassword(passwordEncoder.encode(password));

		Trainer newTrainer = trainerService.addTrainer(trainer);

		String htmlContent = htmlContentService.getHtmlContent(trainer.getName(), trainer.getEmail(), password);

		try {
			mailService.sendPasswordEmail(trainer.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
					htmlContent);
		} catch (Exception e) {
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		activityLogService.log(managerDetails.getManager().getEmail(), managerDetails.getManager().getManagerId(),
				"MANAGER", "Manager with ID " + managerDetails.getManager().getManagerId()
						+ " added a new Trainer with ID " + newTrainer.getTrainerId());

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Added Successfully", null));
	}

	@GetMapping("/secure/viewAllTrainers")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> viewAllTrainers(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String stack, @RequestParam(required = false) String name,
			@RequestParam(required = false) String trainerId) {
		if (managerDetails.getManager() == null) {
			throw new ManagerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());

		Page<Trainer> trainers = trainerService.viewAllTrainers(stack, name, trainerId, pageable);

		Page<TrainerDTO> TrainerDTOResponse = trainers.map(trainer -> {
			TrainerDTO Trainerdto = new TrainerDTO();
			Trainerdto.setTrainerId(trainer.getTrainerId());
			Trainerdto.setName(trainer.getName());
			Trainerdto.setEmail(trainer.getEmail());
			Trainerdto.setMobile(trainer.getMobile());
			Trainerdto.setQualification(trainer.getQualification());
			Trainerdto.setYearOfExperiences(trainer.getYearOfExperiences());
			Trainerdto.setActive(trainer.isAcitve());
			Trainerdto.setCreatedAt(trainer.getCreatedAt());
			Trainerdto.setUpdatedAt(trainer.getUpdatedAt());
			return Trainerdto;
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers fetched successfully", TrainerDTOResponse));
	}

	@PostMapping("/secure/jd/status")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateJdStatus(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestBody MangerJdStatusUpdateRequest request) {

		JobDescription findJobDescription = jobDescriptionService.findByJobDescriptionId(request.getJdId())
				.orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));

		findJobDescription.setJdStatus(request.getStatus());
		findJobDescription.setManagerApproval(request.getStatus().toLowerCase().equals("approved") ? true : false);
		String jdActionReason;

		if ("approved".equalsIgnoreCase(request.getStatus())) {
			jdActionReason = "JD approved by " + managerDetails.getManager().getName() + ", on "
					+ LocalDateTime.now().toString();
			findJobDescription.setApprovedDate(LocalDateTime.now());
		} else {
			jdActionReason = request.getActionReason();
		}

		findJobDescription.setJdActionReason(jdActionReason);
		findJobDescription = jobDescriptionService.updateJobDescription(findJobDescription);
		return ResponseEntity.ok(new ApiResponse<>("success", "JD stauts updated", null));
	}

	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getManagerProfile(@AuthenticationPrincipal CustomUserDetails managerDetails) {
		if (managerDetails.getManager() == null) {
			throw new ManagerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		Manager manager = managerDetails.getManager();
		ProfileResponse details = managerService.getProfile(manager);
		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Profile", details));
	}

	@GetMapping("/secure/jd/{jobDescriptionId}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getJobDescriptionById(@PathVariable String jobDescriptionId) {

		Optional<JobDescription> jobDescriptionOtp = jobDescriptionService.findByJobDescriptionId(jobDescriptionId);

		if (jobDescriptionOtp.isEmpty()) {
			throw new JobDescriptionException("Job Description Not Found", HttpStatus.NOT_FOUND);
		}

		JobDescription jobDescription = jobDescriptionOtp.get();
		JobDescriptionDTO jobDescriptionDTO = new JobDescriptionDTO();
		jobDescriptionDTO.setJobDescriptionId(jobDescription.getJobDescriptionId());
		jobDescriptionDTO.setCompanyName(jobDescription.getCompanyName());
		jobDescriptionDTO.setWebsite(jobDescription.getWebsite());
		jobDescriptionDTO.setRole(jobDescription.getRole());
		jobDescriptionDTO.setStack(jobDescription.getStack());
		jobDescriptionDTO.setQualification(jobDescription.getQualification());
		jobDescriptionDTO.setStream(jobDescription.getStream());
		jobDescriptionDTO.setPercentage(jobDescription.getPercentage());
		jobDescriptionDTO.setMinYearOfPassing(jobDescription.getMinYearOfPassing());
		jobDescriptionDTO.setMaxYearOfPassing(jobDescription.getMaxYearOfPassing());
		jobDescriptionDTO.setSalaryPackage(jobDescription.getSalaryPackage());
		jobDescriptionDTO.setNumberOfRegistrations(jobDescription.getNumberOfRegistrations());
		jobDescriptionDTO.setCurrentRegistrations(jobDescription.getCurrentRegistrations());
		jobDescriptionDTO.setMockRating(jobDescription.getMockRating());
		jobDescriptionDTO.setJdStatus(jobDescription.getJdStatus());
		jobDescriptionDTO.setManagerApproval(jobDescription.isManagerApproval());
		jobDescriptionDTO.setNumberOfClosures(jobDescription.getNumberOfClosures());
		jobDescriptionDTO.setClosed(jobDescription.isClosed());
		jobDescriptionDTO.setCreatedAt(jobDescription.getCreatedAt());
		jobDescriptionDTO.setUpdatedAt(jobDescription.getUpdatedAt());
		jobDescriptionDTO.setLocation(jobDescription.getLocation());
		jobDescriptionDTO.setExecutive(jobDescription.getExecutive());
		jobDescriptionDTO.setPostedBy(jobDescription.getPostedBy());
		jobDescriptionDTO.setDescription(jobDescription.getDescription());
		jobDescriptionDTO.setSkills(jobDescription.getSkills());
		jobDescriptionDTO.setJdActionReason(jobDescription.getJdActionReason());
		//jobDescriptionDTO.setManagerId(jobDescription.getManagerId());
		return ResponseEntity.ok(new ApiResponse<>("success", "Job Description Fetched", jobDescriptionDTO));

	}

	@GetMapping("/secure/jd")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getAllJds(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String companyName,
			@RequestParam(required = false, defaultValue = "") String stack,
			@RequestParam(required = false) String role, @RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false, defaultValue = "") String qualification,
			@RequestParam(required = false, defaultValue = "") String stream,
			@RequestParam(required = false) Double percentage, @RequestParam(required = false) String status,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate)

	{

		String managerId = managerDetails.getManager().getManagerId();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());

		Page<JobDescription> jobDescriptions = jobDescriptionService.findAllJobDescriptions(companyName, stack, role,
				isClosed, minYearOfPassing, maxYearOfPassing, qualification, stream, percentage, null, managerId,
				status, startDate, endDate, pageable);

		Page<JobDescriptionDTO> JobDescriptionDTOResponse = jobDescriptions.map(jobDescription -> {
			JobDescriptionDTO jobDescriptionDTO = new JobDescriptionDTO();
			jobDescriptionDTO.setJobDescriptionId(jobDescription.getJobDescriptionId());
			jobDescriptionDTO.setCompanyName(jobDescription.getCompanyName());
			jobDescriptionDTO.setWebsite(jobDescription.getWebsite());
			jobDescriptionDTO.setRole(jobDescription.getRole());
			jobDescriptionDTO.setStack(jobDescription.getStack());
			jobDescriptionDTO.setQualification(jobDescription.getQualification());
			jobDescriptionDTO.setStream(jobDescription.getStream());
			jobDescriptionDTO.setPercentage(jobDescription.getPercentage());
			jobDescriptionDTO.setMinYearOfPassing(jobDescription.getMinYearOfPassing());
			jobDescriptionDTO.setMaxYearOfPassing(jobDescription.getMaxYearOfPassing());
			jobDescriptionDTO.setSalaryPackage(jobDescription.getSalaryPackage());
			jobDescriptionDTO.setNumberOfRegistrations(jobDescription.getNumberOfRegistrations());
			jobDescriptionDTO.setCurrentRegistrations(jobDescription.getCurrentRegistrations());
			jobDescriptionDTO.setMockRating(jobDescription.getMockRating());
			jobDescriptionDTO.setJdStatus(jobDescription.getJdStatus());
			jobDescriptionDTO.setManagerApproval(jobDescription.isManagerApproval());
			jobDescriptionDTO.setNumberOfClosures(jobDescription.getNumberOfClosures());
			jobDescriptionDTO.setClosed(jobDescription.isClosed());
			jobDescriptionDTO.setCreatedAt(jobDescription.getCreatedAt());
			jobDescriptionDTO.setUpdatedAt(jobDescription.getUpdatedAt());
			jobDescriptionDTO.setLocation(jobDescription.getLocation());
			return jobDescriptionDTO;
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Profile", JobDescriptionDTOResponse));
	}

	
	@GetMapping("/secure/executives")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getAllExecutives(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String q) {
		Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

		Page<Executive> executives = managerService.getAllExecutives(managerDetails.getManager().getManagerId(),q,
				pageable);

		return ResponseEntity.ok(new ApiResponse<>("success", "Executives data", executives));
	}

	//view all executives based on manger id and also search parameter
//	@GetMapping("/secure/executives")
//	@PreAuthorize("hasRole('MANAGER')")
//	public ResponseEntity<?> getExecutives(
//	        @AuthenticationPrincipal CustomUserDetails managerDetails,
//	        @RequestParam(required = false) String q,
//	        @RequestParam(defaultValue = "0") int initial,
//	        @RequestParam(defaultValue = "10") int limit) {
//
//	    Pageable pageable = PageRequest.of(initial, limit, Sort.by("createdAt").descending());
//	    String managerId = managerDetails.getManager().getManagerId();
//
//	    Page<Executive> executives = executiveService.getExecutivesByManagerIdAndSearchQuery(managerId, q, pageable);
//	    return ResponseEntity.ok(new ApiResponse<>("success", "Executives data", executives));
//	}
	
	//based on ex id - number of jds posted , and number of openings and number of closures
	@GetMapping("/secure/executive/jd-stats/{executiveId}")
	@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
	public ResponseEntity<?> getjobDescriptionStatusByExecutive(@PathVariable String executiveId) {
	    ExecutiveJDStatusDTO stats = jobDescriptionService.getExecutiveJobDescriptionStats(executiveId);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Executive JD stats", stats));
	}
	
	@GetMapping("/secure/executive/{id}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getExecutiveById(@PathVariable String id) {
		
		Executive findExecutive = executiveService.getExecutiveById(id);
		if(findExecutive ==null)
		{
			throw new AdminException("Executive not found", HttpStatus.NOT_FOUND);
		}
		
		ExecutiveDetails executiveDetails =  new ExecutiveDetails();
		executiveDetails.setActive(findExecutive.isActive());
		executiveDetails.setCreatedAt(findExecutive.getCreatedAt());
		executiveDetails.setEmail(findExecutive.getEmail());
		executiveDetails.setExecutiveId(findExecutive.getExecutiveId());
		executiveDetails.setId(null);
		executiveDetails.setManagerId(findExecutive.getManagerId());
		executiveDetails.setMobile(findExecutive.getMobile());
		executiveDetails.setName(findExecutive.getName());
		Map<String, Long> jdDetails = (Map) executiveService.getExecutiveJdDetails(findExecutive.getExecutiveId());
		executiveDetails.setJdsCount(jdDetails);
		Manager manager = managerService.getManagerById(findExecutive.getManagerId());
		executiveDetails.setManagerEmail(manager.getEmail());
		executiveDetails.setManagerName(manager.getName());
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Data", executiveDetails));
	}

}
