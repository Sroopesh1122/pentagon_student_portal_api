package com.pentagon.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Student;
import com.pentagon.exception.ExecutiveException;
import com.pentagon.exception.JobDescriptionException;
import com.pentagon.exception.StudentException;
import com.pentagon.repository.JobDescriptionRepository;
import com.pentagon.repository.StudentRepository;
import com.pentagon.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	@Override
	public boolean changePassword(String password, String confirmPassword) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateStudent(Student student) {
		// TODO Auto-generated method stub
		try {
			student.setUpdatedAt(LocalDateTime.now());
			studentRepository.save(student);
			return true;
		}
		catch (Exception e) {
	        throw new StudentException("Failed to update Student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public List<JobDescription> viewJobDescriptionBasedOnStack(String stack) {
		// TODO Auto-generated method stub
		try {
		return jobDescriptionRepository.findByStack(stack);
	    }
		catch(Exception e) {
			 throw new JobDescriptionException("No jobs found for this stack", HttpStatus.NOT_FOUND);
		}
	}

}
