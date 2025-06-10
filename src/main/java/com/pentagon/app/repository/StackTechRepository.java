package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.StackTech;

@Repository
public interface StackTechRepository  extends JpaRepository<StackTech, Integer> {
	
	@Query("SELECT  s  from Stack s WHERE s.stackId = :stackId")
	List<StackTech> getAllTechnologiesByStackId(String stackId);

}
