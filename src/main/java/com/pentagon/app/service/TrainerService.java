package com.pentagon.app.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.request.TrainerLoginRequest;
import com.pentagon.app.response.ProfileResponse;


public interface TrainerService {
	
	public Trainer addTrainer(Trainer trainer);

	public Trainer updateTrainer(Trainer trainer);
	
	public Page<Trainer> viewAllTrainers(String stack, String name, String trainerId, Pageable pageable);

	public String loginWithPassword(TrainerLoginRequest trainerLoginRequest);

	public ProfileResponse getProfile(Trainer trainer);

	public Trainer getByEmail(String email);
	
	public Trainer getById(String tainerId);
	
	public Trainer disableTrainerById(String Id);
	
	public Page<Trainer> getAllTrainers(String programHeadId, String q, Pageable pageable);
	
	public Trainer findByPasswordResetToken(String token);
	
	
}