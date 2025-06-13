package com.pentagon.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.ProgramHead;
import com.pentagon.app.exception.ProgramHeadException;
import com.pentagon.app.repository.ProgramHeadRepository;
import com.pentagon.app.request.ProgramheadLoginRequest;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.service.ProgramHeadService;

import jakarta.validation.Valid;

@Service
public class ProgramHeadServiceImpl implements ProgramHeadService {

	@Autowired
	private ProgramHeadRepository programHeadRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private OtpService otpService;

	@Override
	public ProgramHead getById(String id) {
		return programHeadRepository.findById(id).orElse(null);
	}

	@Override
	public ProgramHead add(ProgramHead programHead) {
		return programHeadRepository.save(programHead);
	}

	@Override
	public ProgramHead update(ProgramHead programHead) {
		return programHeadRepository.save(programHead);
	}

	@Override
	public Page<ProgramHead> getAll(Pageable pageable) {
		return programHeadRepository.findAll(pageable);
	}

	@Override
	public ProgramHead getByEmail(String email) {
		return programHeadRepository.findByEmail(email).orElse(null);
	}

	@Override
	public Page<ProgramHead> getAll(String q, Pageable pageable) {
		return programHeadRepository.getAll(q, pageable);
	}

	@Override
	public String loginwithPassword(@Valid ProgramheadLoginRequest request) {
		ProgramHead programHead = programHeadRepository.findByEmail(request.getEmail())
				.orElseThrow(()-> new ProgramHeadException("Program Head not found", HttpStatus.NOT_FOUND));
		
		if(!passwordEncoder.matches(request.getPassword(), programHead.getPassword())) {
			throw new ProgramHeadException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		String otp = otpService.generateOtpAndStore(request.getEmail());
		otpService.sendOtpToEmail(programHead.getEmail(), otp);
		return "OTP sent to registered email";
	}

}
