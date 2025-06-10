package com.pentagon.app.service;

import java.util.List;

import com.pentagon.app.entity.StackTech;

public interface StackTechService {
	
	public List<StackTech>  getAllTechnologiesByStack(String stackId);
	

}
