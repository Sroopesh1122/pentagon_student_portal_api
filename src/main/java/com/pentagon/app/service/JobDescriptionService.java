package com.pentagon.app.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.JobDescription;

public interface JobDescriptionService {

	public JobDescription findByJobDescriptionId(String jobDescriptionId);

	Page<JobDescription> findAllJobDescriptions(String companyName, String stack, String role, Boolean isClosed,
			Integer minYearOfPassing, Integer maxYearOfPassing, String qualification, String stream, Double percentage,
			String executiveId,
			String status,
			Pageable pageable);

	public boolean addJobDescription(JobDescription jobDescription);

	public JobDescription updateJobDescription(JobDescription jobDescription);

}
