package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Student;
import com.pentagon.app.exception.ExecutiveException;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.exception.ManagerException;
import com.pentagon.app.exception.StudentException;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.repository.StudentRepository;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.request.StudentLoginRequest;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.service.StudentService;

import jakarta.validation.Valid;

@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OtpService otpService;
	
	@Override
	public boolean changePassword(String password, String studentId) {
		
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

//	@Override
//	public List<JobDescription> viewJobDescriptionBasedOnStack(String stack) {
//		// TODO Auto-generated method stub
//		try {
//		  return jobDescriptionRepository.findByStack(stack);
//	    }
//		catch(Exception e) {
//			 throw new JobDescriptionException("Failed to Fetch Job Descriptions : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}

	@Override
	public String loginWithPassword(StudentLoginRequest studentLoginRequest) {
		Student student= studentRepository.findByStudentId(studentLoginRequest.getStudentId())
				.orElseThrow(()-> new StudentException("Manager not found", HttpStatus.NOT_FOUND));
		if(!passwordEncoder.matches(studentLoginRequest.getPassword(), student.getPassword())) {
			throw new StudentException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		String otp= otpService.generateOtpAndStore(student.getEmail());
		otpService.sendOtpToEmail(student.getEmail(), otp);
		
		return "OTP sent to registered email";
	}


}
