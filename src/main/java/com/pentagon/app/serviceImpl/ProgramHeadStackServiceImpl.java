package com.pentagon.app.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pentagon.app.entity.ProgramHeadStack;
import com.pentagon.app.repository.ProgramHeadStackRepository;
import com.pentagon.app.service.ProgramHeadStackService;


@Service
public class ProgramHeadStackServiceImpl implements ProgramHeadStackService {

	@Autowired
	private ProgramHeadStackRepository programHeadStackRepository;
	
	@Override
	public List<ProgramHeadStack> addAll(List<ProgramHeadStack> programHeadStacks) {
		return programHeadStackRepository.saveAll(programHeadStacks);
	}
	
	@Override
	public ProgramHeadStack add(ProgramHeadStack programHeadStack) {
		return programHeadStackRepository.save(programHeadStack);
	}

	@Override
	public List<ProgramHeadStack> getStacksByProgramHead(String programHeadId) {
		return programHeadStackRepository.getStackByProgramHead(programHeadId);
	}


}
