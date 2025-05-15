package com.pentagon.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.Repository.JobDescriptionRepository;
import com.pentagon.entity.JobDescription;
import com.pentagon.service.JobDescriptionService;

@Service
public class JobDescriptionServiceImpl implements JobDescriptionService{

	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	@Override
	public JobDescription createJD(JobDescription jd) {
		// TODO Auto-generated method stub
	    return null;
	}

}
