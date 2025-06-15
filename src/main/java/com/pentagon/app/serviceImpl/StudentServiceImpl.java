package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
	public boolean addMockRating(String studentId, Double mockRating) {
		Student student = studentRepository.findByStudentId(studentId)
           .orElseThrow(()-> new StudentException("Student not found with id: " + studentId, HttpStatus.NOT_FOUND));	
		
		student.setMockRating(mockRating);
		student.setUpdatedAt(LocalDateTime.now());
		
		try {
	        studentRepository.save(student);
	        return true;
	    } catch (Exception e) {
	        throw new StudentException("Failed to update mock rating: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
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
	public Student findByStudentId(String studentId) {
		Optional<Student> student = studentRepository.findById(studentId);
		if (student == null) {
			throw new StudentException("Stduent not found", HttpStatus.NOT_FOUND); 
		}
		return student.get();
	}


}