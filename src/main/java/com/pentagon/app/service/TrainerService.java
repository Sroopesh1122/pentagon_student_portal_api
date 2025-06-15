package com.pentagon.app.service;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.request.TrainerLoginRequest;
import com.pentagon.app.response.ProfileResponse;


public interface TrainerService {
	
	public Trainer addTrainer(Trainer trainer);

	public Trainer updateTrainer(Trainer trainer);
	
	Page<Trainer> viewAllTrainers(String stack, String name, String trainerId, Pageable pageable);

	String loginWithPassword(TrainerLoginRequest trainerLoginRequest);

	public ProfileResponse getProfile(Trainer trainer);

	Trainer checkExistsByEmail(String email);
	
	public Trainer getById(String tainerId);
	
	public Trainer disableTrainerById(String Id);
	
	Page<Trainer> getAllTrainers(String programHeadId, String q, Pageable pageable);

	public void submitMockRating(Trainer trainer, Student student, Technology tech, double mockRateing);

	public List<Student> getStudentsByBatch(String batchId, String trainerId);

	public Student getStudentByIdIfTrainerAuthorized(String trainerId, String studentId);
}