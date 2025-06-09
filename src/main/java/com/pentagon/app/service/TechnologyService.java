package com.pentagon.app.service;

import com.pentagon.app.entity.Technology;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface TechnologyService {
    public Technology addTechnology(Technology technology);
    public Page<Technology> getAllTechnologies(Pageable pageable);
    public Optional<Technology> getTechnologyById(String techId);
    public Technology updateTechnology(Technology technology);
    public void deleteTechnology(String techId);
}