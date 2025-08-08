
package com.pentagon.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.Dto.ExecutiveJDStatusDTO;
import com.pentagon.app.Dto.JdStatsDTO;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.StudentJdApplication;

public interface JobDescriptionService {

	
	public JobDescription finById(String jdId);
	
	public Optional<JobDescription> findByJobDescriptionId(String jobDescriptionId);

	public Page<JobDescription> findAllJobDescriptions(String companyName, String stack, String role, Boolean isClosed,
			Integer minYearOfPassing, Integer maxYearOfPassing, String qualification, String stream, Double percentage,
			String executiveId,
			String managerId,
			String status,
			 String startDate,
		        String endDate,
			Pageable pageable);

	public Page<JobDescription> findJdForStudent(String companyName, String stack, String role,
			Integer minYearOfPassing, Integer maxYearOfPassing, String qualification, String stream, Double percentage,
			 String startDate,
		        String endDate,
			Pageable pageable);
	
	public JobDescription addJobDescription(JobDescription jobDescription);

	public JobDescription updateJobDescription(JobDescription jobDescription);
	

	public List<JobDescription> viewAllJobDescriptions();
	
	public List<JobDescription> viewJobDescriptionBasedOnStack(String stack);

	public ExecutiveJDStatusDTO getExecutiveJobDescriptionStats(String executiveId);
	
	
	public List<JdStatsDTO> getJdStats(String timeUnit , int range);
	
	
	public Long totalCount();
	
	public Long totalClosureCount();
	
	
	public Map<String, Object> getJdClosureOfExecutiveByMonthRange(Integer noOfMonths,String executiveId);
	
	
	public Page<JobDescription> getManagerJd(String managerId,Pageable pageable);
	
	public Page<JobDescription> getExecutiveJd(String executiveId,Pageable pageable);
	
	
	

	

}