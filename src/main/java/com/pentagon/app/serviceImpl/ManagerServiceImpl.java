package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.repository.TrainerRepository;
import com.pentagon.app.service.ManagerService;

@Service
public class ManagerServiceImpl implements ManagerService {

	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	@Autowired
	private TrainerRepository trainerRepository;
	
	@Override
	public Manager login(String email, String otp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean updateManager(Manager manager) {
		// TODO Auto-generated method stub
		try {
			manager.setUpdatedAt(LocalDateTime.now());
			managerRepository.save(manager);
			return true;
		}
		catch (Exception e) {
	        throw new ManagerException("Failed to update Manager: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public boolean addExecutive(Executive executive) {
		// TODO Auto-generated method stub
		try {
			executive.setCreatedAt(LocalDateTime.now());
			executiveRepository.save(executive);
			return true;
		}
		catch(Exception e) {
			throw new ExecutiveException("Failed to Add Executive: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Override
	public JobDescription acceptJobDescription(String jobDescriptionId) {
		// TODO Auto-generated method stub
		JobDescription jobDescription = jobDescriptionRepository.findByJobDescriptionId(jobDescriptionId)
				.orElseThrow(() -> new JobDescriptionException("Job Description not found with id: " + jobDescriptionId, HttpStatus.NOT_FOUND));
		
		if (jobDescription.isManagerApproval()) {
            throw new JobDescriptionException("Job Description is already approved", HttpStatus.CONFLICT);
        }
		
		jobDescription.setManagerApproval(true);
		jobDescriptionRepository.save(jobDescription);
		
		return jobDescription;
	}

	@Override
	public boolean addTrainer(Trainer trainer) {
		// TODO Auto-generated method stub
		try {
			trainer.setCreatedAt(LocalDateTime.now());
			trainerRepository.save(trainer);
			return true;
		}
		catch(Exception e) {
			throw new TrainerException("Failed to Add Trainer: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
