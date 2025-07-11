package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Student.EnrollmentStatus;

import com.pentagon.app.exception.StudentException;
import com.pentagon.app.repository.StudentRepository;
import com.pentagon.app.request.StudentLoginRequest;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.service.StudentService;

import jakarta.transaction.Transactional;


@Service
public class StudentServiceImpl implements StudentService {

	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OtpService otpService;
	
	@Override
	public boolean changePassword(String password, String studentId) {
		
		return false;
	}

	@Override
	public Student updateStudent(Student student) {
		// TODO Auto-generated method stub
		try {
			student.setUpdatedAt(LocalDateTime.now());
			return studentRepository.save(student);
		}
		catch (Exception e) {
	        throw new StudentException("Failed to update Student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}


	@Override
	public String loginWithPassword(StudentLoginRequest studentLoginRequest) 
	{
		Student student= studentRepository.findByEmail(studentLoginRequest.getEmail()).orElse(null);
		
		if(student ==null)
		{
			throw new StudentException("Account Not Found", HttpStatus.NOT_FOUND);
		}
		
		if(!passwordEncoder.matches(studentLoginRequest.getPassword(), student.getPassword())) {
			throw new StudentException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		
		
		String otp= otpService.generateOtpAndStore(student.getEmail());
		otpService.sendOtpToEmail(student.getEmail(), otp);
		
		return "OTP sent to registered email";
	}

	@Override
	@Transactional
	public Student addStudent(Student student) {
		try {
			student.setCreatedAt(LocalDateTime.now());
			return studentRepository.save(student);
			 
		}
		catch (Exception e) {
	        throw new StudentException("Failed to add Student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@Override
	public Student findByEmail(String email) {
		return studentRepository.findByEmail(email).orElse(null);
	}

	@Override
	public List<Student> viewStudentsBasedOnStack(String stack) {
		try {
			return studentRepository.findByStack(stack);
		}
		catch(Exception e) {
			throw new StudentException("Failed to Fetch Students from " + stack + e.getMessage() , HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public void disableStudentByUniqueId(String studentId) {
		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(()->new StudentException("Student not found with id: " + studentId, HttpStatus.NOT_FOUND));
		
		if (student.getStatus() == EnrollmentStatus.DISABLED) {
	        throw new StudentException("Student is already disabled", HttpStatus.CONFLICT);
	    }
		
		student.setStatus(EnrollmentStatus.DISABLED);
		student.setUpdatedAt(LocalDateTime.now());
		studentRepository.save(student);
	}

	

	@Override
	public List<Student> viewAllStudents() {
		// TODO Auto-generated method stub
		try {
			List<Student> students = studentRepository.findAll();
			return students;
		} catch (Exception e) {
			throw new StudentException("Failed to Fetch Students : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	public Student findByPasswordResetToken(String token) {
		return studentRepository.findByPasswordResetToken(token);
	}

	@Override
	public Student findById(String studentId) {
		return studentRepository.findById(studentId).orElse(null);
	}
	
	@Override
	public Page<Student> findStudent(String q, String batchId, String stackId, Pageable pageable) {
		return studentRepository.findStudents(q, batchId, stackId, pageable);
	}

	
	@Override
	public Map<String, Long> countStudent(String batchId, String stackId) {
		
		Map<String, Long> studentCounts = new HashMap<>();
		studentCounts.put(EnrollmentStatus.ACTIVE.toString(), 0l);
		studentCounts.put(EnrollmentStatus.COMPLETED.toString(), 0l);
		studentCounts.put(EnrollmentStatus.DISABLED.toString(),0l);
		studentCounts.put(EnrollmentStatus.DROPPED.toString(), 0l);
		studentCounts.put(EnrollmentStatus.PENDING.toString(), 0l);
		studentCounts.put(EnrollmentStatus.PLACED.toString(), 0l);
		
		
		List<Object[]> result = studentRepository.countStudentsByStatus(batchId, stackId);
		
		for(Object[] row :result)
		{
			EnrollmentStatus status = (EnrollmentStatus) row[0];
			Long count = (Long) row[1];
			studentCounts.put(status.toString(), count);
		}
		
		return studentCounts;
	}

	@Override
	public List<Student> findByBatch(String batchId) {
		return studentRepository.findByBatch(batchId);
	}

	@Override
	public List<String> findEmailByBatch(String batchId) {
		return studentRepository.findEmailsByBatch(batchId);
	}

}