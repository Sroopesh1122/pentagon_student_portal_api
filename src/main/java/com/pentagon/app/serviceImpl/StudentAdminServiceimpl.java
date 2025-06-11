package com.pentagon.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.StudentAdmin;
import com.pentagon.app.exception.StudentException;
import com.pentagon.app.repository.StudentAdminRepository;
import com.pentagon.app.request.StudentAdminLoginRequest;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.service.StudentAdminService;

import jakarta.validation.Valid;

@Service
public class StudentAdminServiceimpl implements StudentAdminService {

	@Autowired
	private StudentAdminRepository studentAdminRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OtpService otpService;

	@Override
	public StudentAdmin getByEmail(String email) {
		return studentAdminRepository.findByEmail(email).orElse(null);
	}

	@Override
	public StudentAdmin getById(String id) {
		return studentAdminRepository.findById(id).orElse(null);
	}

	@Override
	public StudentAdmin add(StudentAdmin studentAdmin) {
		return studentAdminRepository.save(studentAdmin);
	}

	@Override
	public StudentAdmin update(StudentAdmin studentAdmin) {
		return studentAdminRepository.save(studentAdmin);
	}

	@Override
	public Page<StudentAdmin> getAll(String q, Pageable pageable) {
		return studentAdminRepository.getAll(q, pageable);
	}

	@Override
	public String loginWithPassword(@Valid StudentAdminLoginRequest request) {
		StudentAdmin studntadmin= studentAdminRepository.findByEmail(request.getEmail())
				.orElseThrow(()-> new StudentException("Student Admin not found", HttpStatus.NOT_FOUND));
		if(!passwordEncoder.matches(request.getPassword(), studntadmin.getPassword())) {
//			throw new StudentException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		String otp = otpService.generateOtpAndStore(request.getEmail());
		otpService.sendOtpToEmail(studntadmin.getEmail(), otp);
		return "OTP sent to registered email";
	}

}
