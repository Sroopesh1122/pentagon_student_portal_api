package com.pentagon.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.ProgramHead;
import com.pentagon.app.repository.ProgramHeadRepository;
import com.pentagon.app.service.ProgramHeadService;

@Service
public class ProgramHeadServiceImpl implements ProgramHeadService {

	
	@Autowired
	private ProgramHeadRepository programHeadRepository;
	
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
		return programHeadRepository.findByEmail(email);
	}
	
	@Override
	public Page<ProgramHead> getAll(String q, Pageable pageable) {
		return programHeadRepository.getAll(q, pageable);
	}

}
