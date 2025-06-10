package com.pentagon.app.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.TrainerTech;
import com.pentagon.app.repository.TrainerTechRepository;
import com.pentagon.app.service.TrainerTechService;

@Service
public class TrainerTechServiceImpl implements TrainerTechService {

	
	@Autowired
	private TrainerTechRepository trainerTechRepository;
	
	@Override
	public List<TrainerTech> getAllTechnologiesOfTrainer(String tainerId) {
		return trainerTechRepository.getAllTEchnologiesByTrainer(tainerId);
	}

}
