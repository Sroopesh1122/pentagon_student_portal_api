package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.StudentJdApplication;

public interface StudentJdApplicationService 
{
   public StudentJdApplication create(StudentJdApplication studentJdApplication);
   
   public StudentJdApplication update(StudentJdApplication studentJdApplication);
	
   public StudentJdApplication findByApplicationId(String applicationId);
	
   public Page<StudentJdApplication> getAllStudentAppliedForJd(String jobId,String round,String status ,Pageable pageable);
   
   public Page<StudentJdApplication> getAllAppliedJdForStudent(String stduentId,Pageable pageable);
   
   public Long totalApplicationByJd(String jobId);
   
   public Long totalApplicationByStuent(String studentId);
   
   public StudentJdApplication findByStudentAndJd(String studentId,String jdId);
   
}
