
package com.pentagon.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.Dto.ExecutiveJDStatusDTO;
import com.pentagon.app.entity.JobDescription;

public interface JobDescriptionService {

	Optional<JobDescription> findByJobDescriptionId(String jobDescriptionId);

	Page<JobDescription> findAllJobDescriptions(String companyName, String stack, String role, Boolean isClosed,
			Integer minYearOfPassing, Integer maxYearOfPassing, String qualification, String stream, Double percentage,
			String executiveId,
			String status,
			 String startDate,
		        String endDate,
			Pageable pageable);

	public JobDescription addJobDescription(JobDescription jobDescription);

	public JobDescription updateJobDescription(JobDescription jobDescription);
	

	public List<JobDescription> viewAllJobDescriptions();
	
	public List<JobDescription> viewJobDescriptionBasedOnStack(String stack);

	public ExecutiveJDStatusDTO getExecutiveJobDescriptionStats(String executiveId);
	

	

}