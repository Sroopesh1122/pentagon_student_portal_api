package com.pentagon.app.restController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pentagon.app.Dto.ExecutiveJDStatusDTO;
import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JdRoundHistory;
import com.pentagon.app.entity.JdStatusHistory;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.mapper.JobDescriptionMapper;
import com.pentagon.app.request.AddExecutiveRequest;
import com.pentagon.app.request.AddTrainerRequest;
import com.pentagon.app.request.BlockUserRequest;
import com.pentagon.app.request.MangerJdStatusUpdateRequest;
import com.pentagon.app.request.UnblockUserRequest;
import com.pentagon.app.request.UpdateManagerRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ExecutiveDetails;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.JdStatusRoundHistoryService;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlTemplates;
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
	private ActivityLogService activityLogService;

	@Autowired
	private ExecutiveService executiveService;

	@Autowired
	private PasswordGenration passwordGenration;

	@Autowired
	private HtmlTemplates htmlContentService;

	@Autowired
	private MailService mailService;

	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private TrainerService trainerService;
	
	@Autowired
	private JdStatusRoundHistoryService jdStatusRoundHistoryService;
	
	
	@Autowired
	private HtmlTemplates htmlTemplates;
	
	@Autowired
	private JobDescriptionMapper jobDescriptionMapper;
	
	@Autowired
	private CloudinaryServiceImp cloudinaryService;
	
	
	@Value("${FRONTEND_URL}")
	private String FRONTEND_URL;

	@PutMapping("/secure/")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateManager(
			@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestParam String managerId,
			@RequestParam(required = false) String mobile,
			@RequestPart(required = false) MultipartFile profileImg) {



		Manager manager = managerService.getManagerById(managerId);
		
		if(manager ==null)
		{
			throw new ManagerException("Manger Not Found", HttpStatus.NOT_FOUND);
		}

		if(profileImg!=null)
		{
			if(manager.getProfileImgPublicId() !=null)
			{
				cloudinaryService.deleteFile(manager.getProfileImgPublicId(), "image");
			}
			Map<String, Object>	uploadResponse  = cloudinaryService.uploadImage(profileImg);
			manager.setProfileImgPublicId(uploadResponse.get("public_id").toString());
			manager.setProfileImgUrl(uploadResponse.get("secure_url").toString());
		}
		
		if(mobile !=null)
		{
			manager.setMobile(mobile);
		}

		Manager updatedManager = managerService.updateManager(manager);


		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Updated Successfully", null));
	}
	
	
	@PostMapping("/secure/executive/block")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> blockExecutive(@AuthenticationPrincipal CustomUserDetails customUserDetails ,@RequestBody BlockUserRequest request)
	{
		Manager manager = customUserDetails.getManager();
		
		String executiveId = request.getId();
		
		if(manager ==null)
		{
			throw new ManagerException("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		
		Executive executive = executiveService.getExecutiveById(executiveId);
		
		if(executive ==null)
		{
			throw new ManagerException("Executive Not Found", HttpStatus.NOT_FOUND);
		}
		
		if(!executive.getManagerId().equals(manager.getManagerId()))
		{
			throw new ManagerException("You are not allowed to block this executive", null);
		}
		
		executive.setActive(false);
		executiveService.updateExecutive(executive);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Successfully Blocked", null));		
		
	}
	
	
	@PostMapping("/secure/executive/unblock")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> UnBlockExecutive(@AuthenticationPrincipal CustomUserDetails customUserDetails ,@RequestBody UnblockUserRequest request)
	{
		Manager manager = customUserDetails.getManager();
		
		String executiveId = request.getId();
		
		if(manager ==null)
		{
			throw new ManagerException("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
	
		Executive executive = executiveService.getExecutiveById(executiveId);
		
		if(executive ==null)
		{
			throw new ManagerException("Executive Not Found", HttpStatus.NOT_FOUND);
		}
		
		if(!executive.getManagerId().equals(manager.getManagerId()))
		{
			throw new ManagerException("You are not allowed to unblock this executive", null);
		}
		
		executive.setActive(true);
		executiveService.updateExecutive(executive);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Successfully UnBlocked", null));		
		
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
			mailService.send(executive.getEmail(), "Welcome to Pentagon – Login Credentials Enclosed",
					htmlContent);
		} catch (Exception e) {
			throw new OtpException("Mail couldn't be sent", HttpStatus.INTERNAL_SERVER_ERROR);
		}


		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Added Successfully", null));
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
			Trainerdto.setActive(trainer.isActive());
			Trainerdto.setCreatedAt(trainer.getCreatedAt());
			Trainerdto.setUpdatedAt(trainer.getUpdatedAt());
			return Trainerdto;
		});

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers fetched successfully", TrainerDTOResponse));
	}
	
	
	
     // To Approved or Hold or Reject JD
	@PostMapping("/secure/jd/status")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> updateJdStatus(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestBody MangerJdStatusUpdateRequest request) {

		JobDescription findJobDescription = jobDescriptionService.findByJobDescriptionId(request.getJdId())
				.orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));
		
		if(!request.getStatus().equals("approved")&& !request.getStatus().equals("hold") && !request.getStatus().equals("rejected") )
		{
			throw new ManagerException("Invalid  Status", HttpStatus.BAD_REQUEST);
		}

		findJobDescription.setJdStatus(request.getStatus());
		findJobDescription.setManagerApproval(request.getStatus().toLowerCase().equals("approved") ? true : false);
		String jdActionReason;

		if ("approved".equalsIgnoreCase(request.getStatus())) {
			jdActionReason = "JD approved by " + managerDetails.getManager().getName() + ", on "
					+ LocalDateTime.now().toLocalDate()+LocalDateTime.now().toLocalTime();
			findJobDescription.setApprovedDate(LocalDateTime.now());
		} else {
			jdActionReason = request.getActionReason();
		}

		findJobDescription.setJdActionReason(jdActionReason);
		
		
		if("approved".equalsIgnoreCase(request.getStatus()))
		{
			findJobDescription.setCurrentRound("Pending Scheduling");
		}
		findJobDescription = jobDescriptionService.updateJobDescription(findJobDescription);
		
		//Adding Status History
		JdStatusHistory jdStatusHistory= new JdStatusHistory();
		
		if("approved".equalsIgnoreCase(request.getStatus()))
		{
			jdStatusHistory.setStatus("Approved");
			jdStatusHistory.setDescription("JD approved by " + managerDetails.getManager().getName() + ", on "
					+ LocalDateTime.now().toLocalDate()+LocalDateTime.now().toLocalTime());
			jdStatusHistory.setJobDescription(findJobDescription);
		}
		else if("hold".equalsIgnoreCase(request.getStatus()))
		{
			jdStatusHistory.setStatus("Hold");
			jdStatusHistory.setDescription("JD holded by " + managerDetails.getManager().getName() + ", on "
					+ LocalDateTime.now().toLocalDate()+LocalDateTime.now().toLocalTime());
			jdStatusHistory.setJobDescription(findJobDescription);
		}
		else if("rejected".equalsIgnoreCase(request.getStatus()))
		{
			jdStatusHistory.setStatus("Hold");
			jdStatusHistory.setDescription("JD rejected by " + managerDetails.getManager().getName() + ", on "
					+ LocalDateTime.now().toLocalDate()+LocalDateTime.now().toLocalTime());
			jdStatusHistory.setJobDescription(findJobDescription);
		}
		
		jdStatusRoundHistoryService.addStatus(jdStatusHistory);
		
		
		//Adding Round History
		if("approved".equalsIgnoreCase(request.getStatus()))
		{
			JdRoundHistory jdRoundHistory = new JdRoundHistory();
			jdRoundHistory.setRound("Pending Scheduling");
			jdRoundHistory.setJobDescription(findJobDescription);
			jdStatusRoundHistoryService.addRound(jdRoundHistory);
		}
		
		Executive executive = executiveService.getExecutiveById(findJobDescription.getPostedBy());
		
		if(executive!=null)
		{
			String applicationLink = FRONTEND_URL+"/executive/jd?id="+findJobDescription.getJobDescriptionId();
			String approvedEmailTemplate = htmlTemplates.generateJDApprovedEmail(executive.getName(), findJobDescription.getRole(),findJobDescription.getCompanyName(), managerDetails.getManager().getName(), findJobDescription.getCompanyLogo(),applicationLink);
			
			try {
				mailService.send(executive.getEmail(),"JD Approved", approvedEmailTemplate);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return ResponseEntity.ok(new ApiResponse<>("success", "JD stauts updated", null));
	}

	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getManagerProfile(@AuthenticationPrincipal CustomUserDetails managerDetails) {
		if (managerDetails.getManager() == null) {
			throw new ManagerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		Manager manager = managerDetails.getManager();
		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Profile", manager));
	}
	
	@GetMapping("/secure/{id}")
	public ResponseEntity<?> getManagerById(@AuthenticationPrincipal CustomUserDetails managerDetails ,@PathVariable("id") String managerId) {
		
		
		Manager manager = managerService.getManagerById(managerId);
		
		if(manager ==null)
		{
			throw new ManagerException("Manager Not Found", HttpStatus.NOT_FOUND);
		}
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Data", manager));
	}

	@GetMapping("/secure/jd/{jobDescriptionId}")
	@PreAuthorize("hasRole('MANAGER')")
	public ResponseEntity<?> getJobDescriptionById(@PathVariable String jobDescriptionId) {

		Optional<JobDescription> jobDescriptionOtp = jobDescriptionService.findByJobDescriptionId(jobDescriptionId);

		if (jobDescriptionOtp.isEmpty()) {
			throw new JobDescriptionException("Job Description Not Found", HttpStatus.NOT_FOUND);
		}

		JobDescription jobDescription = jobDescriptionOtp.get();
		JobDescriptionDTO jobDescriptionDTO = jobDescriptionMapper.toDTO(jobDescription);
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
			JobDescriptionDTO jobDescriptionDTO = jobDescriptionMapper.toDTO(jobDescription);
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

	
	
	@GetMapping("/secure/executive/jd-stats/{executiveId}")
	@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
	public ResponseEntity<?> getjobDescriptionStatusByExecutive(@PathVariable String executiveId) {
	    ExecutiveJDStatusDTO stats = jobDescriptionService.getExecutiveJobDescriptionStats(executiveId);
	    return ResponseEntity.ok(new ApiResponse<>("success", "Executive JD stats", stats));
	}
	
	@GetMapping("/secure/{id}/jd-stats")
	@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
	public ResponseEntity<?> getManagerJdStats(@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable String id) {
		Manager manager = managerService.getManagerById(id);
		if(manager ==null)
		{
			throw new ManagerException("User Not Found", HttpStatus.BAD_REQUEST);
		}
	   Map<String, Long> jdDetails = (Map)managerService.getManagersJdDetails(manager.getManagerId());
	    return ResponseEntity.ok(new ApiResponse<>("success", "Manager JD stats", jdDetails ));
	}
	
	
	
	// return executive name and their js count details 
	
	@GetMapping("/secure/executives/jd-metrics")
	@PreAuthorize("hasAnyRole('MANAGER','ADMIN')")
	public ResponseEntity<?> getJdMetricsByExecutive(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
		Manager manager = customUserDetails.getManager();
		if(manager ==null)
		{
			throw new UsernameNotFoundException("Unauthorized");
		}
		
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE,Sort.by("createdAt").descending());
			
		
		List<Map<String, Object>> executivesJdMetricsByName =
			    executiveService.getExecutivesByManagerIdAndSearchQuery(manager.getManagerId(), null, pageable)
			        .stream()
			        .map(e -> (Executive) e)
			        .map(executive -> {
			            Map<String, Object> map = new HashMap<>();
			            map.put("name", executive.getName());
			            map.put("executiveId", executive.getExecutiveId());
			            map.put("details", executiveService.getExecutiveJdDetails(executive.getExecutiveId()));
			            return map;
			        })
			        .collect(Collectors.toList());
		
	    return ResponseEntity.ok(new ApiResponse<>("success", "Manager JD stats", executivesJdMetricsByName ));
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
	
	
	
	@PutMapping("/secure/block/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> blockManager(@PathVariable String id)
	{
		Manager manager = managerService.getManagerById(id);
		if(manager ==null)
		{
			throw new ManagerException("Manager not FOund", HttpStatus.NOT_FOUND);
		}
		
		if(!manager.isActive())
		{
			throw new ManagerException("Manager is Already blocked", HttpStatus.BAD_REQUEST);
		}
		
		manager.setActive(false);
		
		managerService.updateManager(manager);
		
		String  mailMessage= htmlTemplates.getAccountBlockedEmail(manager.getName());
		
		mailService.sendAsync(manager.getEmail(),"Account Blocked - Pentagon Space", mailMessage);
		
		
		return ResponseEntity.ok(new ApiResponse<>("success","Manager Blocked Successfully", null));
		
	}
	
	@PutMapping("/secure/unblock/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> unBlockManager(@PathVariable String id)
	{
		Manager manager = managerService.getManagerById(id);
		if(manager ==null)
		{
			throw new ManagerException("Manager not FOund", HttpStatus.NOT_FOUND);
		}
		
		if(manager.isActive())
		{
			throw new ManagerException("Manager is Already active", HttpStatus.BAD_REQUEST);
		}
		
		manager.setActive(true);
		
		managerService.updateManager(manager);
		
		String mailMessage = htmlContentService.getAccountUnblockedEmail(manager.getName());
		
		mailService.sendAsync(manager.getEmail(), "Account Unblocked - Pentagon Space", mailMessage);
		
		return ResponseEntity.ok(new ApiResponse<>("success","Manager Un-Blocked Successfully", null));
		
	}
	
	
	
	
	
	

}
