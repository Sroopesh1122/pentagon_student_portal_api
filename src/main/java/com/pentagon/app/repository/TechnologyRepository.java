package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Technology;
@Repository
public interface TechnologyRepository extends JpaRepository<Technology, String> {

	@Query("SELECT t FROM Technology t JOIN t.stacks s WHERE s.stackId = :stackId")
	public List<Technology> getAllTechnologiesByStack(String stackId);
	
}
