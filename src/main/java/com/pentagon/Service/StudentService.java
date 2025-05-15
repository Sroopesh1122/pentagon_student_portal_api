package com.pentagon.Service;

import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Student;

public interface StudentService {

	public boolean changePassword(String password, String confirmPassword);
	
	public boolean updateStudent(Student s);
	
	public JobDescription viewJobDescriptionBasedOnStack(String stack);
	
	
//	public JobDescription viewJobDescriptionBasedOnStackandMockRating(String stack, String mockRating);
	
}
