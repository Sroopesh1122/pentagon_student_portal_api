package com.pentagon.service;

import java.util.List;

import com.pentagon.entity.Student;
import com.pentagon.entity.Trainer;

public interface TrainerService {

	public boolean updateTrainer(Trainer trainer);
	
	public boolean addStudent(Student student);
	
	public List<Student> viewStudentsBasedOnStack(String stack);
	
	public boolean addMockRating(String studentId, Double mockRating);
	
	public void disableStudentByUniqueId(String studentId);
}
