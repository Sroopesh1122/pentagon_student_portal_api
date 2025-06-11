package com.pentagon.app.service;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Trainer;
import com.pentagon.app.request.TrainerLoginRequest;
import com.pentagon.app.response.ProfileResponse;


public interface TrainerService {
	
	public Trainer addTrainer(Trainer trainer);

	public Trainer updateTrainer(Trainer trainer);
	
	Page<Trainer> viewAllTrainers(String stack, String name, String trainerId, Pageable pageable);

	String loginWithPassword(TrainerLoginRequest trainerLoginRequest);

	public ProfileResponse getProfile(Trainer trainer);

	boolean checkExistsByEmail(String email);
	
	public Trainer getById(String tainerId);
}