package com.pentagon.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.entity.JobDescription;
import com.pentagon.entity.Student;
import com.pentagon.repository.StudentRepository;
import com.pentagon.service.StudentService;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Override
	public boolean changePassword(String password, String confirmPassword) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean updateStudent(Student s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public JobDescription viewJobDescriptionBasedOnStack(String stack) {
		// TODO Auto-generated method stub
		return null;
	}

}
