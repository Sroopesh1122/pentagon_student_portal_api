package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.TrainerTech;

public interface TrainerTechService{
   
	public List<TrainerTech> getAllTechnologiesOfTrainer(String tainerId);
	
	
}
