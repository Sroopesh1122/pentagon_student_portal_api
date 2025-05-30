package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.Student;
import com.pentagon.app.request.StudentLoginRequest;


public interface StudentService {

	public Student addStudent(Student student);
	
	public List<Student> viewStudentsBasedOnStack(String stack);
	
	public boolean changePassword(String password, String studentId);
	
	public boolean updateStudent(Student student);
	
	public void disableStudentByUniqueId(String studentId);
	
	public boolean addMockRating(String studentId, Double mockRating);

	public String loginWithPassword(StudentLoginRequest studentLoginRequest);
	
	public List<Student> viewAllStudents();
	
}
