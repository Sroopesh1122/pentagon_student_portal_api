package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pentagon.app.entity.TrainerTech;

public interface TrainerTechRepository extends JpaRepository<TrainerTech, Integer> {

	@Query("SELECT t FROM TrainerTech t WHERE t.trainerId = :trainerId")
	List<TrainerTech> getAllTEchnologiesByTrainer(String trainerId);
	
}
