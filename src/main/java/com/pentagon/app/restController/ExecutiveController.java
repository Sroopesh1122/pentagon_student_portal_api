package com.pentagon.app.restController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.pentagon.app.Dto.JdStatsDTO;
import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JdStatusHistory;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Stack;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.BlockedException;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.request.AddJobDescriptionRequest;
import com.pentagon.app.request.BlockUserRequest;
import com.pentagon.app.request.CloseJdRequest;
import com.pentagon.app.request.UnblockUserRequest;
import com.pentagon.app.request.UpdateClosuresRequest;
import com.pentagon.app.request.UpdateExecutiveManager;
import com.pentagon.app.request.UpdateJobDescriptionRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ExecutiveDetails;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.JdStatusRoundHistoryService;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.service.StackService;
import com.pentagon.app.service.TechnologyService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlTemplates;
import com.pentagon.app.utils.IdGeneration;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/executive")
public class ExecutiveController {

	@Autowired
	private ExecutiveService executiveService;
	@Autowired
	private JobDescriptionService jobDescriptionService;

	@Autowired
	private ActivityLogService activityLogService;

	@Autowired
	private ManagerService managerService;

	@Autowired
	private IdGeneration idGenerator;

	@Autowired
	private StackService stackService;

	@Autowired
	private CloudinaryServiceImp cloudinaryService;

	@Autowired
	private TechnologyService technologyService;

	@Autowired
	private JdStatusRoundHistoryService jdStatusRoundHistoryService;

	@Autowired
	private HtmlTemplates htmlTemplates;

	@Autowired
	private MailService mailService;

	@Value("${FRONTEND_URL}")
	private String FRONTEND_URL;

	// not working
	@PostMapping("/secure/addJobDescription")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> addJobDescription(@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@Valid @RequestBody AddJobDescriptionRequest newJd, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			List<String> errorMessages = bindingResult.getFieldErrors().stream()
					.map(error -> error.getField() + ": " + error.getDefaultMessage()).collect(Collectors.toList());

			throw new ExecutiveException("Invalid input data: " + String.join(", ", errorMessages),
					HttpStatus.BAD_REQUEST);
		}
		Executive executive = executiveDetails.getExecutive();
		if (executive == null) {
			throw new ExecutiveException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}

		if (!executive.isActive()) {
			throw new BlockedException("Your Account is Blocked", HttpStatus.UNAUTHORIZED);
		}

		Stack findStack = null;

		if (newJd.getStack() != null) {
			findStack = stackService.getStackById(newJd.getStack()).orElse(null);

			if (findStack == null) {
				throw new JobDescriptionException("Stack Not Found", HttpStatus.NOT_FOUND);
			}
		}

		JobDescription jd = new JobDescription();
		jd.setJobDescriptionId(idGenerator.generateId("JD"));
		jd.setCompanyLogo(newJd.getCompanyLogoUrl());
		jd.setCompanyName(newJd.getCompanyName());
		jd.setWebsite(newJd.getWebsite());
		jd.setDescription(newJd.getDescription());
		jd.setRole(newJd.getRole());
		jd.setQualification(newJd.getQualification());
		jd.setStream(newJd.getStream());
		jd.setPercentage(newJd.getPercentage());
		jd.setMinYearOfPassing(newJd.getMinYearOfPassing());
		jd.setMaxYearOfPassing(newJd.getMaxYearOfPassing());
		jd.setJdStack(findStack);
		jd.setSalaryPackage(newJd.getSalaryPackage());
		jd.setNumberOfRegistrations(newJd.getNoOfRegistrations());
		jd.setMockRating(newJd.getMockRating());
		jd.setManagerApproval(false);
		jd.setCurrentRegistrations(0);
		jd.setLocation(newJd.getLocation());
		jd.setExecutive(executiveDetails.getExecutive());
		jd.setPostedBy(executiveDetails.getExecutive().getExecutiveId());
		jd.setJdStatus("pending");
		jd.setManagerId(executiveDetails.getExecutive().getManagerId());
		jd.setSkills(newJd.getSkills());
		jd.setAcardemicGap(newJd.getAcardemicGap());
		jd.setBacklogs(newJd.getBacklogs());
		if (newJd.getBondDetails() != null) {
			jd.setBondDetails(newJd.getBondDetails());
		}
		if (newJd.getSalaryDetails() != null) {
			jd.setSalaryDetails(newJd.getSalaryDetails());
		}

		List<Technology> technologiesToCheckMockRating = new ArrayList<>();

		if (newJd.getTechnologies() != null && newJd.getTechnologies().size() > 0) {
			for (String techId : newJd.getTechnologies()) {
				Technology technology = technologyService.getTechnologyById(techId).orElse(null);
				if (technology != null) {
					technologiesToCheckMockRating.add(technology);
				}
			}
		}

		jd.setMockRatingTechnologies(technologiesToCheckMockRating);

		jd.setGenderPreference(newJd.getGenderPreference());
		jd.setAboutCompany(newJd.getAboutCompany());
		jd.setInterviewDate(newJd.getInterviewDate());
		jd.setRolesAndResponsibility(newJd.getRolesAndResponsibility());
		jd = jobDescriptionService.addJobDescription(jd);

		JdStatusHistory jdStatusHistory = new JdStatusHistory();
		jdStatusHistory.setJobDescription(jd);
		jdStatusHistory.setStatus("Pending");

		// updated Status here
		jdStatusHistory.setDescription(null);
		jdStatusRoundHistoryService.addStatus(jdStatusHistory);

		Manager manager = managerService.getManagerById(jd.getManagerId());

		String jdApplicationLink = FRONTEND_URL + "/manager/jd?id=" + jd.getJobDescriptionId();

		String emailTemplate = htmlTemplates.generateJDApprovalEmail(manager.getName(),
				executiveDetails.getExecutive().getName(), executiveDetails.getExecutive().getEmail(),
				executiveDetails.getExecutive().getMobile(), jd.getRole(), jd.getCompanyName(), jd.getCompanyLogo(),
				jdApplicationLink);

		try {
			mailService.send(manager.getEmail(), "Jd Approval Pending", emailTemplate);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	
		String logDetails = "<strong>New Job Description Added</strong><br>" +
                "• <strong>Company:</strong> " + jd.getCompanyName() + "<br>" +
                "• <strong>Role:</strong> " + jd.getRole() + "<br>" +
                "• <strong>Location:</strong> " + jd.getLocation() + "<br>" +
                "• <strong>Qualification:</strong> " + jd.getQualification() + "<br>" +
                "• <strong>Posted By:</strong> " + executiveDetails.getExecutive().getName() + " (" + executiveDetails.getExecutive().getEmail() + ")";

		
		activityLogService.log(executive.getExecutiveId(),"New JD addeed", logDetails);
		return ResponseEntity.ok(new ApiResponse<>("status", "JobDescription added successfully", null));
	}

	@PutMapping("/secure/{id}/manager/change")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> changeExecutiveManager(
			@PathVariable String id,
			@RequestBody @Valid UpdateExecutiveManager request, 
			BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ExecutiveException("Invalid data Input", HttpStatus.BAD_REQUEST);
		}

		Executive executive = executiveService.getExecutiveById(id);

		if (executive == null) {
			throw new ExecutiveException("Executive Not Found", HttpStatus.NOT_FOUND);
		}

		if (!executive.isActive()) {
			throw new ExecutiveException("This Executive Account is Blocked", HttpStatus.BAD_REQUEST);
		}

		Manager manager = managerService.getManagerById(request.getManagerId());

		if (manager == null) {
			throw new ExecutiveException("Manager Not Found", HttpStatus.NOT_FOUND);
		}

		if (!manager.isActive()) {
			throw new ExecutiveException("Manager account is blocked", HttpStatus.BAD_REQUEST);
		}

		executive.setManagerId(manager.getManagerId());

		executiveService.updateExecutive(executive);

		String emailMessage = htmlTemplates.getManagerChangeNotificationEmail(executive.getName(), executive.getEmail(),
				manager.getName(), manager.getEmail());

		List<String> emails = new ArrayList<>();
		emails.add(executive.getEmail());
		emails.add(manager.getEmail());
		mailService.sendWithBccAsync(null, "Pentagon Space - Manager Change Notification", emailMessage, emails);

		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Chnaged Successfully", null));
	}

	@PutMapping("/secure/all/manager/{oldManagerId}/change")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> changeAllExecutiveManager(@PathVariable String oldManagerId,
			@RequestBody @Valid UpdateExecutiveManager request, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ExecutiveException("Invalid data Input", HttpStatus.BAD_REQUEST);
		}

		Manager newManager = managerService.getManagerById(request.getManagerId());
		Manager oldManager = managerService.getManagerById(oldManagerId);

		if (newManager == null || oldManager == null) {
			throw new ExecutiveException("Manager Not Found", HttpStatus.NOT_FOUND);
		}

		if (!newManager.isActive()) {
			throw new ExecutiveException("Manager account is blocked", HttpStatus.BAD_REQUEST);
		}

		List<String> emails = new ArrayList<>();
		emails.add(newManager.getEmail());
		List<String> executiveNames = new ArrayList<>();

		executiveService.getAllManagerExecutive(oldManagerId).forEach(executive -> {
			if (executive.isActive()) {
				emails.add(executive.getEmail());
				executiveNames.add(executive.getName());
				executive.setManagerId(newManager.getManagerId());
				executiveService.updateExecutive(executive);
			}
		});

		if (emails.size() > 0) {
			String emailMessage = htmlTemplates.getBulkManagerAssignmentEmail(newManager.getName(), executiveNames);
			mailService.sendWithBccAsync(null, "Manager Assignment Notification - Pentagon Space", emailMessage,
					emails);

		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Manager Chnaged Successfully", null));
	}

	@PutMapping("/secure")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> updateJobDescription(@AuthenticationPrincipal CustomUserDetails userDetails,
			@Valid @RequestBody UpdateJobDescriptionRequest request, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ExecutiveException("Invaid Input Data", HttpStatus.BAD_REQUEST);
		}

		Executive executive = userDetails.getExecutive();

		if (executive == null) {
			throw new ExecutiveException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}

		if (!executive.isActive()) {
			throw new BlockedException("Your Account is Blocked", HttpStatus.UNAUTHORIZED);
		}

		JobDescription jobDescription = jobDescriptionService.findByJobDescriptionId(request.getJobDescriptionId())
				.orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));

		if (!jobDescription.getPostedBy().equals(executive.getExecutiveId())) {
			throw new JobDescriptionException("You are not allowed to update this jd", HttpStatus.BAD_REQUEST);
		}

		if (request.getNoOfRegistration() != null) {
			jobDescription.setNumberOfRegistrations(request.getNoOfRegistration());
		}
		jobDescriptionService.updateJobDescription(jobDescription);
		return ResponseEntity.ok(new ApiResponse<>("success", "Jd Updated Successfully", null));
	}

	@PutMapping("/secure/jd/{id}/close")
	@PreAuthorize("hasRole('EXECUTIVE')")
	@Transactional
	public ResponseEntity<?> closeJd(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable String id,
			@Valid @RequestBody CloseJdRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new JobDescriptionException("Invalid Input Data", HttpStatus.BAD_REQUEST);
		}

		Executive executive = userDetails.getExecutive();

		if (executive == null) {
			throw new ExecutiveException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}

		if (!executive.isActive()) {
			throw new BlockedException("Your Account is Blocked", HttpStatus.UNAUTHORIZED);
		}

		JobDescription jobDescription = jobDescriptionService.findByJobDescriptionId(id)
				.orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));

		if (!jobDescription.getPostedBy().equals(executive.getExecutiveId())) {
			throw new JobDescriptionException("You are not allowed to update this jd", HttpStatus.BAD_REQUEST);
		}

		jobDescription.setNumberOfClosures(request.getNoOfClosure());
		jobDescription.setClosed(true);
		jobDescription.setJdStatus("Closed");

		JdStatusHistory jdStatusHistory = new JdStatusHistory();
		jdStatusHistory.setStatus("Closed");
		jdStatusHistory.setJobDescription(jobDescription);
		jdStatusHistory
				.setDescription("Jd is closed by " + executive.getName() + " on" + LocalDateTime.now().toString());

		jdStatusRoundHistoryService.addStatus(jdStatusHistory);

		jobDescriptionService.updateJobDescription(jobDescription);

		return ResponseEntity.ok(new ApiResponse<>("success", "JdClosed Successfully", null));

	}

//	@PostMapping("/secure/updateClosures")
//	@PreAuthorize("hasRole('EXECUTIVE')")
//	public ResponseEntity<?> updateClosures(@AuthenticationPrincipal CustomUserDetails executiveDetails,
//			@Valid @RequestBody UpdateClosuresRequest request, BindingResult bindingResult) {
//
//		if (bindingResult.hasErrors()) {
//			throw new ExecutiveException("Invaid Input Data", HttpStatus.BAD_REQUEST);
//		}
//
//		if (executiveDetails == null) {
//			throw new ExecutiveException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
//		}
//
//		JobDescription jobDescription = jobDescriptionService.findByJobDescriptionId(request.getJobDescriptionId())
//				.orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));
//
//		if (!jobDescription.isClosed()) {
//			throw new IllegalStateException("Job Description must be closed first");
//		}
//
//		if (request.getNumberOfClosures() < 0
//				|| request.getNumberOfClosures() > jobDescription.getCurrentRegistrations()) {
//			throw new IllegalArgumentException("Invalid closure count");
//		}
//
//		// Update closure data
//		jobDescription.setNumberOfClosures(request.getNumberOfClosures());
////	    jobDescription.setJdStatus(request.getNumberOfClosures() >= 0); // true if any closures
//
//		jobDescriptionService.updateJobDescription(jobDescription);
//
//		return ResponseEntity.ok(new ApiResponse<>("success", "Closures updated successfully", null));
//	}

	@GetMapping("/secure/jd/{jobDescriptionId}")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> getJobDescriptionById(@PathVariable String jobDescriptionId) {

		Optional<JobDescription> jobDescriptionOtp = jobDescriptionService.findByJobDescriptionId(jobDescriptionId);

		if (jobDescriptionOtp.isEmpty()) {
			throw new JobDescriptionException("Job Description Not Found", HttpStatus.NOT_FOUND);
		}

		JobDescription jobDescription = jobDescriptionOtp.get();
		JobDescriptionDTO jobDescriptionDTO = new JobDescriptionDTO();
		jobDescriptionDTO.setCompanyLogo(jobDescription.getCompanyLogo());
		jobDescriptionDTO.setJobDescriptionId(jobDescription.getJobDescriptionId());
		jobDescriptionDTO.setCompanyName(jobDescription.getCompanyName());
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
		jobDescriptionDTO.setSkills(jobDescription.getSkills());
		jobDescriptionDTO.setJdActionReason(jobDescription.getJdActionReason());
		jobDescriptionDTO.setAcardemicGap(jobDescription.getAcardemicGap());
		jobDescriptionDTO.setBacklogs(jobDescription.getBacklogs());
		jobDescriptionDTO.setBondDetails(jobDescription.getBondDetails());
		jobDescriptionDTO.setSalaryDetails(jobDescription.getSalaryDetails());
		jobDescriptionDTO.setCurrentRound(jobDescription.getCurrentRound());
		jobDescriptionDTO.setStautsHistory(jobDescription.getStautsHistory());
		jobDescriptionDTO.setRoundHistory(jobDescription.getRoundHistory());
		jobDescriptionDTO.setAboutCompany(jobDescription.getAboutCompany());
		jobDescriptionDTO.setInterviewDate(jobDescription.getInterviewDate());
		jobDescriptionDTO.setGenderPreference(jobDescription.getGenderPreference());
		jobDescriptionDTO.setRolesAndResponsibility(jobDescription.getRolesAndResponsibility());
//		jobDescriptionDTO.setGeneric(jobDescription.getGeneric());

		Manager manager = managerService.getManagerById(jobDescription.getManagerId());
		if (manager != null) {
			jobDescriptionDTO.setManagerId(jobDescription.getManagerId());
			jobDescriptionDTO.setManagerName(manager.getName());
		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Job Description Fetched", jobDescriptionDTO));

	}

	@GetMapping("/secure/jd")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> viewAllJobDescriptions(@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String companyName,
			@RequestParam(required = false, defaultValue = "") String stack,
			@RequestParam(required = false) String role, @RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false, defaultValue = "") String qualification,
			@RequestParam(required = false, defaultValue = "") String stream,
			@RequestParam(required = false) Double percentage,
			@RequestParam(required = false, defaultValue = "") String status,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate) {

		if (executiveDetails == null) {
			throw new ExecutiveException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}

		String executiveId = executiveDetails.getExecutive().getExecutiveId();

		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").descending());

		Page<JobDescription> jobDescriptions = jobDescriptionService.findAllJobDescriptions(companyName, stack, role,
				isClosed, minYearOfPassing, maxYearOfPassing, qualification, stream, percentage, executiveId, null,
				status, startDate, endDate, pageable);

		Page<JobDescriptionDTO> JobDescriptionDTOResponse = jobDescriptions.map(jobDescription -> {
			JobDescriptionDTO jobDescriptionDTO = new JobDescriptionDTO();
			jobDescriptionDTO.setCompanyLogo(jobDescription.getCompanyLogo());
			jobDescriptionDTO.setJobDescriptionId(jobDescription.getJobDescriptionId());
			jobDescriptionDTO.setCompanyName(jobDescription.getCompanyName());
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
			jobDescriptionDTO.setSkills(jobDescription.getSkills());
			jobDescriptionDTO.setPostedBy(jobDescription.getPostedBy());
			jobDescriptionDTO.setJdStatus(jobDescription.getJdStatus());
			jobDescriptionDTO.setJdActionReason(jobDescription.getJdActionReason());
			jobDescriptionDTO.setStautsHistory(jobDescription.getStautsHistory());
			jobDescriptionDTO.setRoundHistory(jobDescription.getRoundHistory());
			jobDescriptionDTO.setBondDetails(jobDescription.getBondDetails());
			jobDescriptionDTO.setSalaryDetails(jobDescription.getSalaryDetails());
			jobDescriptionDTO.setAboutCompany(jobDescription.getAboutCompany());
			jobDescriptionDTO.setInterviewDate(jobDescription.getInterviewDate());
			jobDescriptionDTO.setGenderPreference(jobDescription.getGenderPreference());
			jobDescriptionDTO.setRolesAndResponsibility(jobDescription.getRolesAndResponsibility());
//			jobDescriptionDTO.setGeneric(jobDescription.getGeneric());

			Manager jdManager = managerService.getManagerById(jobDescription.getManagerId());
			if (jdManager != null) {
				jobDescriptionDTO.setManagerId(jdManager.getManagerId());
				jobDescriptionDTO.setManagerName(jdManager.getName());
			}

			return jobDescriptionDTO;
		});

		return ResponseEntity
				.ok(new ApiResponse<>("success", "Job Descriptions fetched successfully", JobDescriptionDTOResponse));
	}

	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> getExecutiveProfile(@AuthenticationPrincipal CustomUserDetails executiveDetails) {
		if (executiveDetails.getExecutive() == null) {
			throw new ExecutiveException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		Executive executive = executiveDetails.getExecutive();
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Profile", executive));
	}

	@PutMapping("/secure/")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> updateExecutive(@AuthenticationPrincipal CustomUserDetails managerDetails,
			@RequestParam(required = false) String executiveId, @RequestParam(required = false) String mobile,
			@RequestPart(required = false) MultipartFile profileImg) {
		Executive executive = executiveService.getExecutiveById(executiveId);

		if (executive == null) {
			throw new ExecutiveException("Executive Not Found", HttpStatus.NOT_FOUND);
		}

		if (profileImg != null) {
			if (executive.getProfileImgPublicId() != null) {
				cloudinaryService.deleteFile(executive.getProfileImgPublicId(), "image");
			}
			Map<String, Object> uploadResponse = cloudinaryService.uploadImage(profileImg);
			executive.setProfileImgPublicId(uploadResponse.get("public_id").toString());
			executive.setProfileImgUrl(uploadResponse.get("secure_url").toString());
		}

		if (mobile != null) {
			executive.setMobile(mobile);
		}

		executiveService.updateExecutive(executive);
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Profile Updated Successfully", null));

	}

	@GetMapping("/secure/jd/stats")
	@PreAuthorize("hasAnyRole('EXECUTIVE','MANAGER','ADMIN')")
	public ResponseEntity<?> getJdStats(@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@RequestParam("timeUnit") String timeUnit, @RequestParam("range") int range) {

		String executiveId = executiveDetails.getExecutive().getExecutiveId();

		List<JdStatsDTO> jdStats = executiveService.getExecutiveJdStats(executiveId, timeUnit, range);

		return ResponseEntity.ok(new ApiResponse<>("success", "JD Stats", jdStats));
	}

	@GetMapping("/secure/{id}/jd/status/stats")
	@PreAuthorize("hasAnyRole('EXECUTIVE','MANAGER','ADMIN')")
	public ResponseEntity<?> getJdStats(@PathVariable String id) {

		Executive executive = executiveService.getExecutiveById(id);

		if (executive == null) {
			throw new ExecutiveException("User not FOund", HttpStatus.NOT_FOUND);
		}

		Map<String, Long> jdStatusStats = (Map) executiveService.getExecutiveJdDetails(executive.getExecutiveId());

		return ResponseEntity.ok(new ApiResponse<>("success", "JD Stats", jdStatusStats));
	}

	@GetMapping("/secure/jd/closure/stats")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> getJdClosureStats(@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@RequestParam Integer months) {

		String executiveId = executiveDetails.getExecutive().getExecutiveId();

		Map<String, Long> jdStatusStats = (Map) jobDescriptionService.getJdClosureOfExecutiveByMonthRange(months,
				executiveId);

		return ResponseEntity.ok(new ApiResponse<>("success", "JD Closure Stats", jdStatusStats));
	}

	@GetMapping("/secure/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','EXECUTIVE')")
	public ResponseEntity<?> getExecutiveById(@PathVariable String id) {

		Executive findExecutive = executiveService.getExecutiveById(id);
		if (findExecutive == null) {
			throw new AdminException("Executive not found", HttpStatus.NOT_FOUND);
		}

		ExecutiveDetails executiveDetails = new ExecutiveDetails();
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

	@GetMapping("/secure/{id}/recentJd")
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','EXECUTIVE')")
	public ResponseEntity<?> getExecutiveRecentJd(@PathVariable String id) {

		System.out.println("Here ");

		Executive findExecutive = executiveService.getExecutiveById(id);
		if (findExecutive == null) {
			throw new AdminException("Executive not found", HttpStatus.NOT_FOUND);
		}

		Integer RECENT_COUNT = 5;

		System.out.println("Executive id " + id);

		Page<JobDescription> jobDescriptions = executiveService.getRecentJobDescriptions(id, RECENT_COUNT);

		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Data", jobDescriptions));
	}

	@GetMapping("/secure/{id}/jd/stats")
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','EXECUTIVE')")
	public ResponseEntity<?> getJdStatsOfExecutive(@PathVariable("id") String executiveId,
			@RequestParam("timeUnit") String timeUnit, @RequestParam("range") int range) {

		List<JdStatsDTO> jdStats = executiveService.getExecutiveJdStats(executiveId, timeUnit, range);

		return ResponseEntity.ok(new ApiResponse<>("success", "JD Stats", jdStats));
	}

	@PutMapping("/secure/block/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> blockExecutive(@PathVariable String id) {

		String executiveId = id;

		Executive executive = executiveService.getExecutiveById(executiveId);

		if (executive == null) {
			throw new ManagerException("Executive Not Found", HttpStatus.NOT_FOUND);
		}

		executive.setActive(false);
		executiveService.updateExecutive(executive);

		String mailMessage = htmlTemplates.getAccountBlockedEmail(executive.getName());

		mailService.sendAsync(executive.getEmail(), "Account Blocked - Pentagon Space", mailMessage);

		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Successfully Blocked", null));

	}

	@PutMapping("/secure/unblock/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> UnBlockExecutive(@PathVariable String id) {

		String executiveId = id;

		Executive executive = executiveService.getExecutiveById(executiveId);

		if (executive == null) {
			throw new ManagerException("Executive Not Found", HttpStatus.NOT_FOUND);
		}

		executive.setActive(true);
		executiveService.updateExecutive(executive);

		String mailMessage = htmlTemplates.getAccountUnblockedEmail(executive.getName());

		mailService.sendAsync(executive.getEmail(), "Account Unblocked - Pentagon Space", mailMessage);

		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Successfully UnBlocked", null));

	}

	// Return total Jd counts , total jd Pending ,total Jd rejected , total Closures
	// achieved
	// vieW ALL JDS BY EXECU ID
	// after jd closed, fetch count of clousers
	// for particular executive fetch their total number of JDs,openings,closures
	// completed based on

}
