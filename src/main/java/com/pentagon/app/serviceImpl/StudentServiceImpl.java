package com.pentagon.app.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

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
		
		if (student.getStatus() == EnrollmentStatus.BLOCKED) {
	        throw new StudentException("Student is already disabled", HttpStatus.CONFLICT);
	    }
		
		student.setStatus(EnrollmentStatus.BLOCKED);
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
		return studentRepository.findByStudentId(studentId).orElse(null);
	}
	
	@Override
	public Page<Student> findStudent(String q, String batchId, String stackId,EnrollmentStatus status, Pageable pageable) {
		return studentRepository.findStudents(q, batchId, stackId,status, pageable);
	}

	
	@Override
	public Map<String, Long> countStudent(String batchId, String stackId) {
		
		Map<String, Long> studentCounts = new HashMap<>();
		studentCounts.put(EnrollmentStatus.ACTIVE.toString(), 0l);
		studentCounts.put(EnrollmentStatus.COMPLETED.toString(), 0l);
		studentCounts.put(EnrollmentStatus.BLOCKED.toString(),0l);
		studentCounts.put(EnrollmentStatus.PLACED.toString(), 0l);
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
	public List<String> getEmailByBatch(String batchId) {
		return studentRepository.getEmailsByBatch(batchId);
	}

	@Override
	public Long countStudents(EnrollmentStatus status) {
		return studentRepository.countStudents(status);
	}

	@Override
	public Map<String, Object> countStudentByStack(String stackId) {
		
		Map<String, Object> studentCounts = new HashMap<>();
		studentCounts.put(EnrollmentStatus.ACTIVE.toString() ,studentRepository.countStudentsByBatch(EnrollmentStatus.ACTIVE,stackId));
		studentCounts.put(EnrollmentStatus.COMPLETED.toString(), studentRepository.countStudentsByBatch(EnrollmentStatus.COMPLETED,stackId));
		studentCounts.put(EnrollmentStatus.BLOCKED.toString(),studentRepository.countStudentsByBatch(EnrollmentStatus.BLOCKED,stackId));
		studentCounts.put(EnrollmentStatus.PLACED.toString(),studentRepository.countStudentsByBatch(EnrollmentStatus.PLACED,stackId));
		
		
		return studentCounts;
	}

	public Map<String, Long> getStudentCountsForPastMonths(int noOfMonths) {
		Map<String, Long> monthCountMap = new LinkedHashMap<>();

		LocalDate currentDate = LocalDate.now();

		for (int i = 0; i < noOfMonths; i++) {
		    LocalDate date = currentDate.minusMonths(i);
		    int month = date.getMonthValue();
		    int year = date.getYear();

		    long count = studentRepository.countByCreatedAtMonthAndYear(month, year);

		    String monthYear = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH) + " " + year;

		    monthCountMap.put(monthYear, count);
		}

		return monthCountMap;

    }

   

}