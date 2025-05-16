package com.pentagon.service;

import java.util.List;

import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Student;

public interface StudentService {

	public boolean changePassword(String password, String studentId);
	
	public boolean updateStudent(Student student);
	
	public List<JobDescription> viewJobDescriptionBasedOnStack(String stack);
	
	
//	public JobDescription viewJobDescriptionBasedOnStackandMockRating(String stack, String mockRating);
	
}
