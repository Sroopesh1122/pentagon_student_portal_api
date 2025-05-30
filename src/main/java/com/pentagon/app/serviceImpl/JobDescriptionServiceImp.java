package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.service.JobDescriptionService;


@Service
public class JobDescriptionServiceImp implements JobDescriptionService {
	
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	
	@Override
	public Optional<JobDescription> findByJobDescriptionId(String jobDescriptionId) {
	    return jobDescriptionRepository.findByJobDescriptionId(jobDescriptionId);
	}
	
	public boolean addJobDescription(JobDescription jobDescription) {
		// TODO Auto-generated method stub
		try {
			jobDescription.setCreatedAt(LocalDateTime.now());
			jobDescriptionRepository.save(jobDescription);
			return true;
		}
		catch(Exception e) {
			throw new JobDescriptionException("Failed to Add Job Description: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	
	@Override
	public JobDescription updateJobDescription(JobDescription jobDescription) {
		try {
			jobDescription.setUpdatedAt(LocalDateTime.now());
			return jobDescriptionRepository.save(jobDescription);
		}
		catch(Exception e) {
			throw new JobDescriptionException("Failed to Update Job Description: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Page<JobDescription> findAllJobDescriptions(
			String companyName, 
			String stack, 
			String role, 
			Boolean isClosed,
	        Integer minYearOfPassing, 
	        Integer maxYearOfPassing, 
	        String qualification, 
	        String stream, 
	        Double percentage,
	        String executiveId,
	        String status,
	        Pageable pageable) {
	    
	    try {
	    	
	    	String stackRegex = stack!=null ? Arrays.asList(stack.split(",")).stream().map(w->Pattern.quote(w)).collect(Collectors.joining("|")) : "";
	    	String qualificationRegex = qualification!=null ?  Arrays.asList(qualification.split(",")).stream().map(w->Pattern.quote(w)).collect(Collectors.joining("|")) : "";
	    	String streamRegex = stream!=null ?  Arrays.asList(stream.split(",")).stream().map(w->Pattern.quote(w)).collect(Collectors.joining("|")) : "";
	    		
	        return jobDescriptionRepository.findWithFiltersUsingRegex(
	                companyName, stackRegex, role, isClosed,
	                minYearOfPassing, maxYearOfPassing,
	                qualificationRegex, streamRegex,
	                percentage, executiveId,status, pageable);
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new JobDescriptionException("Failed to fetch Job Descriptions", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@Override
	public List<JobDescription> viewAllJobDescriptions() {
		// TODO Auto-generated method
		try {
			List<JobDescription> jobDescriptions = jobDescriptionRepository.findAll();
			return jobDescriptions;
		} catch (Exception e) {
			throw new JobDescriptionException("Failed to Job Descriptions", HttpStatus.NOT_FOUND);
		}

	}


	@Override
	public List<JobDescription> viewJobDescriptionBasedOnStack(String stack) {
		// TODO Auto-generated method stub
		try {
		  return jobDescriptionRepository.findByStack(stack);
	    }
		catch(Exception e) {
			 throw new JobDescriptionException("Failed to Fetch Job Descriptions : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}

