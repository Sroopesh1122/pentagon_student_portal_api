package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.OtpService;

public class JobDescriptionServiceImp implements JobDescriptionService {
	
	
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	
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
		// TODO Auto-generated method stub
		try {
			jobDescription.setUpdatedAt(LocalDateTime.now());
			return jobDescriptionRepository.save(jobDescription);
		}
		catch(Exception e) {
			throw new JobDescriptionException("Failed to Update Job Description: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	public Page<JobDescription> findAllJobDescriptions(
			String companyName, String stack, String role, Boolean isClosed,
	        Integer minYearOfPassing, Integer maxYearOfPassing, String qualification, String stream, Double percentage,
	        Pageable pageable) {
	    
	    try {
	    	
	    	System.out.println(companyName);
	    	
	    	String stackRegex = Arrays.asList(stack.split(",")).stream().map(w->Pattern.quote(w)).collect(Collectors.joining("|"));
	    	String qualificationRegex = Arrays.asList(qualification.split(",")).stream().map(w->Pattern.quote(w)).collect(Collectors.joining("|"));
	    	String streamRegex = Arrays.asList(stream.split(",")).stream().map(w->Pattern.quote(w)).collect(Collectors.joining("|"));
	    	
	    	companyName  = companyName ==null || companyName.isBlank() ? null : "%"+companyName.trim()+"%";
	    	role  = role ==null || role.isBlank() ? null : "%"+role.trim()+"%";
	        
	        return jobDescriptionRepository.findWithFiltersUsingRegex(
	                companyName, stackRegex, role, isClosed,
	                minYearOfPassing, maxYearOfPassing,
	                qualificationRegex, streamRegex,
	                percentage, pageable);
	        
	    } catch (Exception e) {
	    	e.printStackTrace();
	        throw new JobDescriptionException("Failed to fetch Job Descriptions", HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
}
