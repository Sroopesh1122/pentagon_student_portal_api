package com.pentagon.app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.ProgramHead;
import com.pentagon.app.request.ProgramheadLoginRequest;
import com.pentagon.app.response.ProfileResponse;

import jakarta.validation.Valid;

public interface ProgramHeadService {
	public ProgramHead getById(String id);

	public ProgramHead add(ProgramHead programHead);

	public ProgramHead update(ProgramHead programHead);

	public Page<ProgramHead> getAll(Pageable pageable);

	public ProgramHead getByEmail(String email);

	public Page<ProgramHead> getAll(String q, Pageable pageable);

	public String loginwithPassword(@Valid ProgramheadLoginRequest request);
	
	public ProfileResponse getProfile(ProgramHead programHead);
	
	public ProgramHead findByPasswordResetToken(String token);
}
