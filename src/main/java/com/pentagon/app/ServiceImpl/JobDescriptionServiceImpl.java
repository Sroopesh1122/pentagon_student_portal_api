package com.pentagon.app.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.app.Entity.JobDescription;
import com.pentagon.app.Repository.JobDescriptionRepository;
import com.pentagon.app.Service.JobDescriptionService;

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
