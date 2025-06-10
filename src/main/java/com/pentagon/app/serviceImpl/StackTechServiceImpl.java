package com.pentagon.app.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.StackTech;
import com.pentagon.app.repository.StackTechRepository;

@Service
public class StackTechServiceImpl implements com.pentagon.app.service.StackTechService {

	
	@Autowired
	private StackTechRepository stackTechRepository;
	
	@Override
	public List<StackTech> getAllTechnologiesByStack(String stackId) {
		return stackTechRepository.getAllTechnologiesByStackId(stackId);
	}

	
}
