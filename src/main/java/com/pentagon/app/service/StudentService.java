package com.pentagon.app.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Student.EnrollmentStatus;
import com.pentagon.app.request.StudentLoginRequest;


public interface StudentService {

	public Student addStudent(Student student);
	
	public Student findByEmail(String email);
	
	public Student findById(String studentId);
	
	public List<Student> viewStudentsBasedOnStack(String stack);
	
	public boolean changePassword(String password, String studentId);
	
	public Student updateStudent(Student student);
	
	public void disableStudentByUniqueId(String studentId);
	

	public String loginWithPassword(StudentLoginRequest studentLoginRequest);
	
	public List<Student> viewAllStudents();
	
	
	public Student findByPasswordResetToken(String token);
	
	public Page<Student> findStudent(String q,String batchId,String stackId,EnrollmentStatus status,String branchId ,Pageable pageable);
	
	public Map<String, Long> countStudent(String batchId,String stackId);
	
	public Map<String, Object> countStudentByStack(String stackId,String branchId);
	
	public List<Student> findByBatch(String batchId);
	
	public List<String> getEmailByBatch(String batchId);
	
	public Long countStudents(EnrollmentStatus status,String branchId);
	
	public Map<String, Long> getStudentCountsForPastMonths(int noOfMonths);
	
	public List<String> getNotPlacedStudentEmails();
	
}
