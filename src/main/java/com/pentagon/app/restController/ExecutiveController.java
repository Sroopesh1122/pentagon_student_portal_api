package com.pentagon.app.restController;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.JdStatsDTO;
import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.request.AddJobDescriptionRequest;
import com.pentagon.app.request.UpdateClosuresRequest;
import com.pentagon.app.request.UpdateJobDescriptionRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.ExecutiveDetails;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ExecutiveService;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.ManagerService;
import com.pentagon.app.utils.IdGeneration;

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

	// not working
	@PostMapping("/secure/addJobDescription")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> addJobDescription(@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@Valid @RequestBody AddJobDescriptionRequest newJd, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
		    List<String> errorMessages = bindingResult.getFieldErrors().stream()
		        .map(error -> error.getField() + ": " + error.getDefaultMessage())
		        .collect(Collectors.toList());

		    throw new ExecutiveException("Invalid input data: " + String.join(", ", errorMessages), HttpStatus.BAD_REQUEST);
		}
		if (executiveDetails.getExecutive() == null) {
			throw new ExecutiveException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
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
		jd.setStack(newJd.getStack());
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
		if(newJd.getBondDetails()!=null)
		{
			jd.setBondDetails(newJd.getBondDetails());
		}
		if(newJd.getSalaryDetails()!=null)
		{
			jd.setSalaryDetails(newJd.getSalaryDetails());
		}
		
		jobDescriptionService.addJobDescription(jd);
		activityLogService.log(executiveDetails.getExecutive().getEmail(),
				executiveDetails.getExecutive().getExecutiveId(), "EXECUTIVE", "Executive with ID "
						+ executiveDetails.getExecutive().getExecutiveId() + " Added new job Description ");
		return ResponseEntity.ok(new ApiResponse<>("status", "JobDescription added successfully", null));
	}

	@PostMapping("/secure/updateJobDescription")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> updateJobDescriptionBasedOnRegistrations(
			@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@Valid @RequestBody UpdateJobDescriptionRequest request, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ExecutiveException("Invaid Input Data", HttpStatus.BAD_REQUEST);
		}

		if (executiveDetails == null) {
			throw new ExecutiveException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}

		JobDescription jobDescription = jobDescriptionService.findByJobDescriptionId(request.getJobDescriptionId())
				.orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));

		// both update and auto-closing - not working
		jobDescription.updateCurrentRegistrations(request.getCurrentRegistrations());

		jobDescriptionService.updateJobDescription(jobDescription);

		String message = jobDescription.isClosed() ? "Registrations updated and JD auto-closed"
				: "Registrations updated successfully";

		return ResponseEntity.ok(new ApiResponse<>("success", message, null));
	}

	@PostMapping("/secure/updateClosures")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> updateClosures(@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@Valid @RequestBody UpdateClosuresRequest request, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			throw new ExecutiveException("Invaid Input Data", HttpStatus.BAD_REQUEST);
		}

		if (executiveDetails == null) {
			throw new ExecutiveException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}

		JobDescription jobDescription = jobDescriptionService.findByJobDescriptionId(request.getJobDescriptionId())
				.orElseThrow(() -> new JobDescriptionException("Job Description not found", HttpStatus.NOT_FOUND));

		if (!jobDescription.isClosed()) {
			throw new IllegalStateException("Job Description must be closed first");
		}

		if (request.getNumberOfClosures() < 0
				|| request.getNumberOfClosures() > jobDescription.getCurrentRegistrations()) {
			throw new IllegalArgumentException("Invalid closure count");
		}

		// Update closure data
		jobDescription.setNumberOfClosures(request.getNumberOfClosures());
//	    jobDescription.setJdStatus(request.getNumberOfClosures() >= 0); // true if any closures

		jobDescriptionService.updateJobDescription(jobDescription);

		return ResponseEntity.ok(new ApiResponse<>("success", "Closures updated successfully", null));
	}

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
		jobDescriptionDTO.setAcardemicGap(jobDescription.getAcardemicGap());
		jobDescriptionDTO.setBacklogs(jobDescription.getBacklogs());
		jobDescriptionDTO.setBondDetails(jobDescription.getBondDetails());
		jobDescriptionDTO.setSalaryDetails(jobDescription.getSalaryDetails());

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
			jobDescriptionDTO.setSkills(jobDescription.getSkills());
			jobDescriptionDTO.setPostedBy(jobDescription.getPostedBy());
			jobDescriptionDTO.setJdStatus(jobDescription.getJdStatus());
			jobDescriptionDTO.setJdActionReason(jobDescription.getJdActionReason());

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
		ProfileResponse details = executiveService.getProfile(executive);
		return ResponseEntity.ok(new ApiResponse<>("success", "Executive Profile", details));
	}
	
	@GetMapping("/secure/jd/stats")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> getJdStats(
			@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@RequestParam("timeUnit") String timeUnit,
			@RequestParam("range") int range) {
		
		String executiveId = executiveDetails.getExecutive().getExecutiveId(); 
		
		List<JdStatsDTO> jdStats = executiveService.getExecutiveJdStats(executiveId, timeUnit, range);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "JD Stats", jdStats));
	}
	
	
	@GetMapping("/secure/jd/status/stats")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> getJdStats(
			@AuthenticationPrincipal CustomUserDetails executiveDetails) {
		
		String executiveId = executiveDetails.getExecutive().getExecutiveId(); 
		
		Map<String,Long> jdStatusStats = (Map) executiveService.getExecutiveJdDetails(executiveId);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "JD Stats", jdStatusStats));
	}
	
	@GetMapping("/secure/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','EXECUTIVE')")
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
	
	
	@GetMapping("/secure/{id}/recentJd")
	@PreAuthorize("hasAnyRole('ADMIN','MANAGER','EXECUTIVE')")
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
	
	
	@GetMapping("/secure/{id}/jd/stats")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getJdStatsOfExecutive(
			@PathVariable("id") String executiveId, 
			@RequestParam("timeUnit") String timeUnit,
			@RequestParam("range") int range) {
		
		List<JdStatsDTO> jdStats =  executiveService.getExecutiveJdStats(executiveId, timeUnit,range);
		
		return ResponseEntity.ok(new ApiResponse<>("success", "JD Stats", jdStats));
	}
	
	//vieW ALL JDS BY EXECU ID
	//after jd closed, fetch count of clousers
	//for particular executive fetch their total number of JDs,openings,closures completed based on 

}
