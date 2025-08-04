package com.pentagon.app.serviceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Technology;
import com.pentagon.app.repository.TechnologyRepository;
import com.pentagon.app.service.TechnologyService;

@Service
public class TechnologyServiceImpl implements TechnologyService {

    @Autowired
    private TechnologyRepository technologyRepository;

    @Override
    public Technology addTechnology(Technology technology) {
        return technologyRepository.save(technology);
    }

    @Override
    public Page<Technology> getAllTechnologies(Pageable pageable) {
        return technologyRepository.findAll(pageable);
    }

    @Override
    public Optional<Technology> getTechnologyById(String techId) {
        return technologyRepository.findById(techId);
    }

    @Override
    public Technology updateTechnology(Technology technology) {
        return technologyRepository.save(technology);
    }

    @Override
    public void deleteTechnology(String techId) {
        technologyRepository.deleteById(techId);
    }
    
    @Override
    public List<Technology> getAllTechnologies() {
    	return technologyRepository.findAll();
    }
    
    @Override
    public List<Technology> findTechnologiesByStack(String stackId) {
    	return technologyRepository.getAllTechnologiesByStack(stackId);
    }
    
}