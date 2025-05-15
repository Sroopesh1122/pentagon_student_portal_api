package com.pentagon.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.Service.JobDescriptionService;
import com.pentagon.entity.JobDescription;
import com.pentagon.repository.JobDescriptionRepository;

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
