package com.pentagon.app.restController;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pentagon.app.Dto.AdminDashboardInfoDTO;
import com.pentagon.app.Dto.ExecutiveDTO;
import com.pentagon.app.Dto.JdStatsDTO;
import com.pentagon.app.Dto.JdVsClosureStatsDTO;
import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.Dto.ManagerDTO;
import com.pentagon.app.Dto.ProgramHeadDTO;
import com.pentagon.app.Dto.StudentAdminDTO;
import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.ProgramHead;
import com.pentagon.app.entity.Stack;
import com.pentagon.app.entity.StudentAdmin;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.exception.ProgramHeadException;
import com.pentagon.app.mapper.ProgramHeadMapper;
import com.pentagon.app.mapper.StudentAdminMapper;
import com.pentagon.app.request.AddExecutiveRequest;
import com.pentagon.app.request.AddManagerRequest;
import com.pentagon.app.request.AddProgramHeadRequest;
import com.pentagon.app.request.AddStudentAdminRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ExecutiveDetails;
import com.pentagon.app.response.ManagerDetails;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.AdminService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.utils.HtmlTemplates;
import com.pentagon.app.utils.IdGeneration;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.ProgramHeadService;
import com.pentagon.app.service.StackService;
import com.pentagon.app.service.StudentAdminService;
import com.pentagon.app.service.TechnologyService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;
import com.pentagon.app.serviceImpl.MailService;
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
	private HtmlTemplates htmlContentService;
	@Autowired
	private PasswordGenration passwordGenration;

	@Autowired
	private ExecutiveService executiveService;

	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private TrainerService trainerService;
	
	@Autowired
	private ProgramHeadService programHeadService;
	
	
	@Autowired
	private ProgramHeadMapper programHeadMapper;
	
	@Autowired
	private StudentAdminMapper studentAdminMapper;
	
	@Autowired
	private StackService stackService;
	
	
	@Autowired
	private StudentAdminService studentAdminService;
	
	@Autowired
	private TechnologyService technologyService;
	
	@Autowired
	private CloudinaryServiceImp cloudinaryService;

	@PostMapping("/secure/addManager")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addManagerByAdmin(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@Valid @RequestBody AddManagerRequest newManager, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new AdminException("Invalid data", HttpStatus.BAD_REQUEST);
		}
		if (adminDetails == null) {
			throw new AdminException("Admin details not found", HttpStatus.UNAUTHORIZED);
		}

		Manager finManager = managerService.getManagerByEmail(newManager.getEmail());

		if (finManager != null) {
			throw new AdminException("Email Already exists", HttpStatus.CONFLICT);
		}

		Manager manager = new Manager();
		manager.setManagerId(idGeneration.generateId("MANAGER"));
		manager.setName(newManager.getName());
		manager.setEmail(newManager.getEmail());
		manager.setMobile(newManager.getMobile());
		manager.setActive(true);
		String password = passwordGenration.generateRandomPassword();
		manager.setPassword(passwordEncoder.encode(password));

		manager = managerService.addManager(manager);

		String htmlContent = htmlContentService.getHtmlContent(manager.getName(), manager.getEmail(), password);

		try {
			mailService.send(manager.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
					htmlContent);
		} catch (Exception e) {
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		String logDetails = "Manager Added:<br>" +
                "Name: " + manager.getName() + "<br>" +
                "Email: " + manager.getEmail() + "<br>" +
                "Mobile: " + manager.getMobile();


		activityLogService.log(adminDetails.getAdmin().getAdminId(),"New Manager Added",logDetails);

		return ResponseEntity.ok(new ApiResponse<>("success", "Manager added successfully", null));
	}

	@PostMapping("/secure/addExecutive")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addExecutiveByAdmin(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@Valid @RequestBody AddExecutiveRequest newExecutive, BindingResult bindingResult) {
		if (bindingResult.hasErrors())
			throw new AdminException("Invalid data ", HttpStatus.BAD_REQUEST);
		if (adminDetails == null) {
			throw new AdminException("Admin details not found", HttpStatus.UNAUTHORIZED);
		}

		Executive findExecutive = executiveService.getExecutiveByEmail(newExecutive.getEmail());

		if (findExecutive != null) {
			throw new AdminException("Email Already exists", HttpStatus.CONFLICT);
		}
		
		Manager  manager  = managerService.getManagerById(newExecutive.getManagerId());
		
		if(manager == null)
		{
			throw new AdminException("Manager Not Found", HttpStatus.NOT_FOUND);
		}
		
		if(!manager.isActive())
		{
			throw new AdminException("This Manager is in-active ", HttpStatus.BAD_REQUEST);
		}

		Executive executive = new Executive();
		executive.setExecutiveId(idGeneration.generateId("EXECUTIVE"));
		executive.setName(newExecutive.getName());
		executive.setEmail(newExecutive.getEmail());
		executive.setActive(true);
		executive.setMobile(newExecutive.getMobile());
		String password = passwordGenration.generateRandomPassword();
		executive.setPassword(passwordEncoder.encode(password));
		executive.setCreatedAt(LocalDateTime.now());
		executive.setManagerId(newExecutive.getManagerId());
		executive = executiveService.addExecutive(executive);
		String htmlContent = htmlContentService.getHtmlContent(executive.getName(), executive.getEmail(), password);

		try {
			mailService.send(executive.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
					htmlContent);
		} catch (Exception e) {
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		String logDetails = "Executive Added:<br>" +
                "Name: " + executive.getName() + "<br>" +
                "Email: " + executive.getEmail() + "<br>" +
                "Mobile: " + executive.getMobile();


		activityLogService.log(adminDetails.getAdmin().getAdminId(),"New Executive Added",logDetails);

		return ResponseEntity.ok(new ApiResponse<>("success", "Executive added Successfully", null));
	}
		
	
	@PostMapping("/secure/program-head/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addProgramHead(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@Valid @RequestBody AddProgramHeadRequest request, BindingResult bindingResult) {
         
		
		if(bindingResult.hasErrors())
		{
			throw new AdminException("Invalid Inputs", HttpStatus.BAD_REQUEST);
		}
		
		ProgramHead findProgramHead = programHeadService.getByEmail(request.getEmail());
		
		if(findProgramHead !=null)
		{
			throw new AdminException("Email Already exists", HttpStatus.CONFLICT);
		}
		
		ProgramHead newProgramHead = new ProgramHead();
		newProgramHead.setId(idGeneration.generateId("PG-HEAD"));
		newProgramHead.setEmail(request.getEmail());
		newProgramHead.setName(request.getName());
		String password = passwordGenration.generateRandomPassword();
		newProgramHead.setPassword(passwordEncoder.encode(password));
		newProgramHead.setCreatedAt(LocalDateTime.now());
		
		List<Stack> programHeadStacks = new ArrayList<>();
		
		request.getStackIds().forEach(stackId ->{
			  Stack findStack = stackService.getStackById(stackId).orElse(null);
			  if(findStack ==null)
			  {
				  throw new AdminException("No stack found", HttpStatus.NOT_FOUND);
			  }
			  programHeadStacks.add(findStack);			  
		});
		
		newProgramHead.setStacks(programHeadStacks);

		newProgramHead= programHeadService.add(newProgramHead); 
		
		
		Trainer trainer = new Trainer();
	    trainer.setTrainerId(idGeneration.generateId("TRAINER"));
	    trainer.setName(request.getName());
		trainer.setEmail(request.getEmail());
		trainer.setMobile(request.getMobile());		
		trainer.setActive(true);
		trainer.setProgramHeadId(newProgramHead.getId());
		trainer.setProgramHead(true);
		trainer.setCreatedAt(LocalDateTime.now());
		trainer.setPassword(passwordEncoder.encode(password));
		
		List<Technology> trainerTechnologies = new ArrayList<>();
		
		request.getTechnologies().forEach(technologyId ->{
			  Technology findTechnology = technologyService.getTechnologyById(technologyId).orElse(null);
			  if(findTechnology ==null)
			  {
				  throw new ProgramHeadException("No Technology found", HttpStatus.NOT_FOUND);
			  }
			  trainerTechnologies.add(findTechnology);			  
		});
		
		trainer.setTechnologies(trainerTechnologies);
		
		trainerService.addTrainer(trainer);
		
		String htmlContent = htmlContentService.getLoginEmailHtmlContent(newProgramHead.getName(), newProgramHead.getEmail(), password);

	     mailService.send(newProgramHead.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",htmlContent);
	     
	     
	     String logDetails = "Trainer Added:<br>" +
	                "Name: " + trainer.getName() + "<br>" +
	                "Email: " + trainer.getEmail() + "<br>";
			activityLogService.log(adminDetails.getAdmin().getAdminId(),"New Trainer Added",logDetails);
		
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Program Head added Successfully", null));	
	}
	
	
	@PostMapping("/secure/student-admin/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addStudentAdmin(
			@AuthenticationPrincipal CustomUserDetails adminDetails,
			@Valid @RequestBody AddStudentAdminRequest request, 
			BindingResult bindingResult) {
       
		if(bindingResult.hasErrors())
		{
			throw new AdminException("Invalid Inputs", HttpStatus.BAD_REQUEST);
		}
		
		StudentAdmin findStudentAdmin = studentAdminService.getByEmail(request.getEmail());
		if(findStudentAdmin !=null)
		{
			throw new AdminException("Email Already exists", HttpStatus.CONFLICT);
		}
		StudentAdmin studentAdmin =  new StudentAdmin();
		studentAdmin.setEmail(request.getEmail());
		studentAdmin.setId(idGeneration.generateId("STU-ADMIN"));
		studentAdmin.setName(request.getName());
		String password = passwordGenration.generateRandomPassword();
		studentAdmin.setPassword(passwordEncoder.encode(password));
		studentAdmin.setCreatedAt(LocalDateTime.now());	
		studentAdmin = studentAdminService.add(studentAdmin);
		
		String htmlContent = htmlContentService.getLoginEmailHtmlContent(studentAdmin.getName(), studentAdmin.getEmail(), password);

	   mailService.send(studentAdmin.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",htmlContent);
	   
	   String logDetails = "Student Admin Added:<br>" +
               "Name: " + studentAdmin.getName() + "<br>" +
               "Email: " + studentAdmin.getEmail() + "<br>";
		activityLogService.log(adminDetails.getAdmin().getAdminId(),"New Student Admin Added",logDetails);
		
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Student Admin added Successfully",null));	
	}
	

	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAdminProfile(@AuthenticationPrincipal CustomUserDetails adminDetails) {

		Admin admin = adminDetails.getAdmin();
		return ResponseEntity.ok(new ApiResponse<>("success", "Admin Profile", admin));
	}
	
	
	@PutMapping("/secure/profile/update")
	public ResponseEntity<?> updateProfile(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@RequestParam String adminId,
			@RequestParam String name,
			@RequestParam String email,
			@RequestParam String mobile,
			@RequestParam(required = false) MultipartFile profileImg)
	{
		Admin admin = customUserDetails.getAdmin();
		if(admin == null)
		{
			throw new AdminException("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		if(!admin.getAdminId().toLowerCase().equals(adminId.toLowerCase()))
		{
			throw new AdminException("You can't update this profile", HttpStatus.FORBIDDEN);
		}
		
		if(name!=null)
		{
			admin.setName(name);
		}
		if(email !=null)
		{
			admin.setEmail(email);
		}
		if(mobile!=null)
		{
			admin.setMobile(mobile);
		}
		
		if(profileImg !=null)
		{
			if(admin.getProfilePublicId()!=null)
			{
				cloudinaryService.deleteFile(admin.getProfilePublicId(),"image");
			}
			
			Map<String, Object>	uploadResponse  = cloudinaryService.uploadImage(profileImg);
			  admin.setProfilePublicId(uploadResponse.get("public_id").toString());
			  admin.setProfileImgUrl(uploadResponse.get("secure_url").toString());
		}
		
		adminservice.updateAdmin(admin);
		
		 
		 activityLogService.log(customUserDetails.getAdmin().getAdminId(),"Profile Updated",null);
		
		return ResponseEntity.ok(new ApiResponse<>("success","Admin Profile Updated Successfully", null));
	}
	
	@GetMapping("/secure/program-heads")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> viewAllProgramHeads(
			@AuthenticationPrincipal CustomUserDetails adminDetails,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam String q) {

		Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
         
		Page<ProgramHeadDTO> programHeads =  programHeadService.getAll(q, pageable).map(programHead -> {
			ProgramHeadDTO programHeadDTO = programHeadMapper.toDTO(programHead);
		    Trainer trainer = trainerService.getTrainer(programHead.getId());
		    if(trainer !=null)
		    {
		    	programHeadDTO.setProfileImgUrl(trainer.getProfileImgUrl());
		    	programHeadDTO.setTrainer(trainer);
		    }
			
			return programHeadDTO;
		});
		

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers fetched successfully", programHeads));
	}
	
	
	@GetMapping("/secure/student-admins")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> viewAllStudentAdmins(
			@AuthenticationPrincipal CustomUserDetails adminDetails,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam String q) {

		Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
         
		Page<StudentAdminDTO> studentAdmins =  studentAdminService.getAll(q, pageable).map(studentAdmin -> studentAdminMapper.toDto(studentAdmin));
	
		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers fetched successfully", studentAdmins));
	}

	@GetMapping("/secure/viewAllTrainers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> viewAllTrainers(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String stack, @RequestParam(required = false) String name,
			@RequestParam(required = false) String trainerId) {

		Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("createdAt").descending());

		Page<Trainer> trainers = trainerService.viewAllTrainers(stack, name, trainerId, pageable);

		Page<TrainerDTO> TrainerDTOPage = trainers.map(trainer -> {
			TrainerDTO dto = new TrainerDTO();
			dto.setTrainerId(trainer.getTrainerId());
			dto.setName(trainer.getName());
			dto.setEmail(trainer.getEmail());
			dto.setMobile(trainer.getMobile());
			dto.setQualification(trainer.getQualification());
			dto.setYearOfExperiences(trainer.getYearOfExperiences());
			dto.setActive(trainer.isActive());
			dto.setCreatedAt(trainer.getCreatedAt());
			dto.setUpdatedAt(trainer.getUpdatedAt());
			return dto;
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers fetched successfully", TrainerDTOPage));
	}

	@GetMapping("/secure/managers")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> viewAllManagers(
			@AuthenticationPrincipal CustomUserDetails adminDetails,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String q ,
			@RequestParam(required = false) String status) {

		Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

		Page<Manager> managers = managerService.findAll(q, status,pageable);

		Page<ManagerDTO> managerDTOPage = managers.map(manager -> {
			ManagerDTO dto = new ManagerDTO();
			dto.setManagerId(manager.getManagerId());
			dto.setName(manager.getName());
			dto.setEmail(manager.getEmail());
			dto.setMobile(manager.getMobile());
			dto.setActive(manager.isActive());
			dto.setCreatedAt(manager.getCreatedAt());
			dto.setUpdatedAt(manager.getUpdatedAt());
			dto.setProfileImgUrl(manager.getProfileImgUrl());
			return dto;
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Managers fetched successfully", managerDTOPage));
	}

	@GetMapping("/secure/executives")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> viewAllExecutives(@AuthenticationPrincipal CustomUserDetails adminDetails,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String q) {

		Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

		Page<Executive> executives = executiveService.getAllExecutives(q, pageable);

		Page<ExecutiveDTO> executiveDTOPage = executives.map(executive -> {
			ExecutiveDTO dto = new ExecutiveDTO();
			dto.setExecutiveId(executive.getExecutiveId());
			dto.setName(executive.getName());
			dto.setEmail(executive.getEmail());
			dto.setMobile(executive.getMobile());
			dto.setActive(executive.isActive());
			dto.setCreatedAt(executive.getCreatedAt());
			dto.setUpdatedAt(executive.getUpdatedAt());
			dto.setProfileImgUrl(executive.getProfileImgUrl());
			return dto;
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Executives fetched successfully", executiveDTOPage));
	}

	@GetMapping("/secure/jd/{jobDescriptionId}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getJobDescriptionById(@PathVariable String jobDescriptionId) {

		JobDescription jobDescription = jobDescriptionService.findByJobDescriptionId(jobDescriptionId)
				.orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));

		JobDescriptionDTO jobDescriptionDTO = new JobDescriptionDTO();
		jobDescriptionDTO.setJobDescriptionId(jobDescription.getJobDescriptionId());
		jobDescriptionDTO.setCompanyName(jobDescription.getCompanyName());
		jobDescriptionDTO.setCompanyLogo(jobDescription.getCompanyLogo());
		jobDescriptionDTO.setWebsite(jobDescription.getWebsite());
		jobDescriptionDTO.setRole(jobDescription.getRole());
		jobDescriptionDTO.setStack(jobDescription.getJdStack());
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
		jobDescriptionDTO.setManagerId(jobDescription.getManagerId());
		jobDescriptionDTO.setSkills(jobDescription.getSkills());
		jobDescriptionDTO.setJdActionReason(jobDescription.getJdActionReason());
		jobDescriptionDTO.setBondDetails(jobDescription.getBondDetails());
		jobDescriptionDTO.setSalaryDetails(jobDescription.getSalaryDetails());
		jobDescriptionDTO.setStautsHistory(jobDescription.getStautsHistory());
		jobDescriptionDTO.setRoundHistory(jobDescription.getRoundHistory());
		jobDescriptionDTO.setAboutCompany(jobDescription.getAboutCompany());
		jobDescriptionDTO.setInterviewDate(jobDescription.getInterviewDate());
		jobDescriptionDTO.setGenderPreference(jobDescription.getGenderPreference());
		jobDescriptionDTO.setRolesAndResponsibility(jobDescription.getRolesAndResponsibility());
		jobDescriptionDTO.setBacklogs(jobDescription.getBacklogs());
		jobDescriptionDTO.setAcardemicGap(jobDescription.getAcardemicGap());
		jobDescriptionDTO.setMockRatingTechnologies(jobDescription.getMockRatingTechnologies());
		Manager manager = managerService.getManagerById(jobDescription.getManagerId());
		if(manager !=null)
		{
			jobDescriptionDTO.setManagerName(manager.getName());
		}
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Job Description Fetched", jobDescriptionDTO));

	}

	@GetMapping("/secure/jd")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAllJds(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String companyName, 
			@RequestParam(required = false) String stack,
			@RequestParam(required = false) String role,
			@RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false) String qualification, 
			@RequestParam(required = false) String stream,
			@RequestParam(required = false) Double percentage, 
			@RequestParam(required = false) String status,
			@RequestParam(required = false) String startDate,
			@RequestParam(required = false) String endDate) {
		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());
		Page<JobDescription> jobDescriptions = jobDescriptionService.findAllJobDescriptions(companyName, stack, role,
				isClosed, minYearOfPassing, maxYearOfPassing, qualification, stream, percentage, null,null, status,
				startDate, endDate, pageable);
		return ResponseEntity.ok(new ApiResponse<>("success", "Jd results", jobDescriptions));
	}

	@GetMapping("/secure/manager/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getMangerById(@PathVariable String id) {
		Manager findManager = managerService.getManagerById(id);
		if (findManager == null) {
			throw new AdminException("Manader Not Found", HttpStatus.NOT_FOUND);
		}

		ManagerDetails managerDetails = new ManagerDetails();
		managerDetails.setActive(findManager.isActive());
		managerDetails.setCreatedAt(findManager.getCreatedAt());
		managerDetails.setEmail(findManager.getEmail());
		managerDetails.setId(null);
		managerDetails.setManagerId(findManager.getManagerId());
		managerDetails.setMobile(findManager.getMobile());
		managerDetails.setName(findManager.getName());
		managerDetails.setPassword(null);
		managerDetails.setProfileImgUrl(findManager.getProfileImgUrl());
		managerDetails.setTotalExecutives(managerService.getAllExecutivesCount(findManager.getManagerId()));
		
		
		Map<String, Long> jdDetails = (Map) managerService.getManagersJdDetails(findManager.getManagerId());
		managerDetails.setJdsCount(jdDetails);

	    List<JdStatsDTO> jdCounts = managerService.getManagerJdStats(findManager.getManagerId(), "day",7);
		
        managerDetails.setLastWeekJdCount(jdCounts);

		return ResponseEntity.ok(new ApiResponse<>("success", "Managers Data", managerDetails));
	}
	
	
	@GetMapping("/secure/manager/{id}/executives")
	public ResponseEntity<?> getAllExecutivesByManager(@PathVariable("id") String managerId,
			@RequestParam(required = false , defaultValue = "0") Integer page,
			@RequestParam(required = false ,defaultValue = "10") Integer limit)
	{
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());
		
		//null for no search text
		Page<Executive> executiveList =  managerService.getAllExecutives(managerId,null,pageable);
		return ResponseEntity.ok(new ApiResponse<>("success","Executives reporting to this manager",executiveList));
		
	}
	
	@GetMapping("/secure/executive/{id}")
	@PreAuthorize("hasRole('ADMIN')")
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
		executiveDetails.setProfileImgUrl(findExecutive.getProfileImgUrl());
		Map<String, Long> jdDetails = (Map) executiveService.getExecutiveJdDetails(findExecutive.getExecutiveId());
		executiveDetails.setJdsCount(jdDetails);
		Manager manager = managerService.getManagerById(findExecutive.getManagerId());
		executiveDetails.setManagerEmail(manager.getEmail());
		executiveDetails.setManagerName(manager.getName());
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Data", executiveDetails));
	}
	
	
	@GetMapping("/secure/executive/{id}/recentJd")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getExecutiveRecentJd(@PathVariable String id) {
		
		Executive findExecutive = executiveService.getExecutiveById(id);
		if(findExecutive ==null)
		{
			throw new AdminException("Executive not found", HttpStatus.NOT_FOUND);
		}
	 
		Integer RECENT_COUNT = 5;
		Page<JobDescription> jobDescriptions = executiveService.getRecentJobDescriptions(id, RECENT_COUNT);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Data", jobDescriptions));
	}
	

	//Stats
	
	@GetMapping("/secure/executive/{id}/jd/stats")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getJdStatsOfExecutive(
			@PathVariable("id") String executiveId, 
			@RequestParam("timeUnit") String timeUnit,
			@RequestParam("range") int range) {
		
		List<JdStatsDTO> jdStats =  executiveService.getExecutiveJdStats(executiveId, timeUnit,range);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "JD Stats", jdStats));
	}
	
	@GetMapping("/secure/manager/{id}/jd/stats")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getJdStatsOfManager(
			@PathVariable("id") String managerId, 
			@RequestParam("timeUnit") String timeUnit,
			@RequestParam("range") int range) {
		
		List<JdStatsDTO> jdStats =  managerService.getManagerJdStats(managerId, timeUnit, range);
		return ResponseEntity.ok(new ApiResponse<>("success", "JD Stats", jdStats));
	}
	
	
	
	@GetMapping("/secure/jd/stats")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getJdStats(
			@RequestParam("timeUnit") String timeUnit,
			@RequestParam("range") int range) {
		
		List<JdStatsDTO> jdStats = adminservice.getJdStats(timeUnit, range);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "JD Stats", jdStats));
	}
	
	
	@GetMapping("/secure/jd-vs-closure/stats")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getJdvsClosureStats(
			@RequestParam("timeUnit") String timeUnit,
			@RequestParam("range") int range) {
		List<JdVsClosureStatsDTO> jdVsClosureStats = adminservice.getJdVsClosureStats(timeUnit, range);
		return ResponseEntity.ok(new ApiResponse<>("success", "JD vs closure Stats", jdVsClosureStats));
	}
	
	
	@GetMapping("/secure/dash-info")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getAdminDashInfo() {
		
		AdminDashboardInfoDTO dashBoardInfo = new AdminDashboardInfoDTO();
		dashBoardInfo.setTotalClosures(jobDescriptionService.totalClosureCount());
		dashBoardInfo.setTotalExecutives(executiveService.getTotalCount());
		dashBoardInfo.setTotalJobDescription(jobDescriptionService.totalCount());
		dashBoardInfo.setTotalManagers(managerService.getTotalCount());
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Admin Dashboard Info",dashBoardInfo));
	}
}
