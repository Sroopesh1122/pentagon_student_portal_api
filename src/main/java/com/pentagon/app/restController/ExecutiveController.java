package com.pentagon.app.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.request.AddJobDescriptionRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.ExecutiveService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/executive")
public class ExecutiveController {
	@Autowired
	private ExecutiveService executiveService;
	
	@Autowired
	private ActivityLogService activityLogService;
	
	@PostMapping("/secure/addJobDescription")
	@PreAuthorize("hasRole('EXECUTIVE')")
	public ResponseEntity<?> addJobDescription(@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@Valid @RequestBody AddJobDescriptionRequest newJd,
			BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
		{
	            throw new ExecutiveException("Invalid input data", HttpStatus.BAD_REQUEST);   
		}
		if(executiveDetails.getExecutive()==null)
		{
			throw new ExecutiveException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		JobDescription jd=new JobDescription();
		jd.setJobDescriptionId(null);
        jd.setCompanyName(newJd.getCompanyName());
        jd.setWebsite(newJd.getWebsite());
        jd.setDescription(newJd.getDescription());
        jd.setQualification(newJd.getQualification());
        jd.setStream(newJd.getStream());
        jd.setPercentage(newJd.getPercentage());
        jd.setMinYearOfPassing(newJd.getMinYearOfPassing());
        jd.setMaxYearOfPassing(newJd.getMaxYearOfPassing());
        jd.setStack(newJd.getStack());
        jd.setSalaryPackage(newJd.getSalaryPackage());
        jd.setNumberOfRegistrations(newJd.getNoOfRegistraions());
        jd.setMockRating(newJd.getMockRating());
        jd.setManagerApproval(false);
        jd.setCurrentRegistrations(0);
        executiveService.addJobDescription(jd);
		activityLogService.log(executiveDetails.getExecutive().getEmail(), 
				executiveDetails.getExecutive().getExecutiveId(), 
				"EXECUTIVE", 
				"Executive with ID " + executiveDetails.getExecutive().getExecutiveId() + " Added new job Description ");
		return ResponseEntity.ok(new ApiResponse<>("status","JobDescription added successfully",null));
	}
	
//	@PostMapping("/secure/upadteJobDescription")
//	@PreAuthorize("hasRole('EXECUTIVE')")
//	public ResponseEntity<?> updateJobDescription(@AuthenticationPrincipal CustomUserDetails executiveDetails,
//			@Valid @RequestBody updateJobDescription updatedJd,
//			BindingResult bindingResult)
//	{
//		if(bindingResult.hasErrors())
//		{
//	            throw new ExecutiveException("Invalid input data", HttpStatus.BAD_REQUEST);   
//		}
//		if(executiveDetails==null)
//		{
//			throw new ExecutiveException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
//		}
//		executiveService.updateJobDescription(updateJd);	
//		return ResponseEntity.ok(new ApiResponse<>("success", "JobDescription updated", null));
//	}
}
