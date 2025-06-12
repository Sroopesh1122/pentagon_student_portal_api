package com.pentagon.app.restController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.JobDescriptionDTO;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.StudentAdminException;
import com.pentagon.app.request.MangerJdStatusUpdateRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.ManagerService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/studentAdmin/")
public class StudentAdminController {
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private JobDescriptionService jobDescriptionService;
	
	@Autowired
	private ManagerService managerService;

	@GetMapping("/secure/viewJobdescription")
	public ResponseEntity<?> getMethodName(@AuthenticationPrincipal CustomUserDetails studnetAdminDetails,
			@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String companyName,
			@RequestParam(required = false) Integer minYearOfPassing,
			@RequestParam(required = false) Integer maxYearOfPassing,
			@RequestParam(required = false, defaultValue = "") String qualification,
			@RequestParam(required = false, defaultValue = "") String stream,
			@RequestParam(required = false, defaultValue = "") String stack,
			@RequestParam(required = false) String role, @RequestParam(required = false) Boolean isClosed,
			@RequestParam(required = false) Double percentage, @RequestParam(required = false) String status,
			@RequestParam(required = false) String startDate, @RequestParam(required = false) String endDate){
		
		if (studnetAdminDetails == null) {
			throw new StudentAdminException("UNAUTORIZED", HttpStatus.UNAUTHORIZED);
		}
		String StduentAdminId= studnetAdminDetails.getStudentAdmin().getId();
		Pageable pageable = PageRequest.of(page, limit, Sort.by("created_at").ascending());

		Page<JobDescription> filterdJobDiscription= jobDescriptionService.findAllJobDescriptions(companyName, stack, role, isClosed, minYearOfPassing, maxYearOfPassing, qualification, stream, percentage, null, null, status, startDate, endDate, pageable);
		Page<JobDescriptionDTO> jobDescriptionDTO = filterdJobDiscription.map(
			    job -> {
			    	JobDescriptionDTO dto = modelMapper.map(job, JobDescriptionDTO.class);
//			    	dto.setManagerName(managerService.getManagerById(job.getManagerId()).getName());
			    	 return dto;
			    });
		
		return ResponseEntity.ok(new ApiResponse<>("success", "Job Description Fetched", jobDescriptionDTO));
	}
	
}
