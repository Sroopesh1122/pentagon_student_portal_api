package com.pentagon.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.app.entity.JobDescription;

public interface JobDescriptionRepository extends JpaRepository<JobDescription,Integer> {

	Optional<JobDescription> findByJobDescriptionId(String jobDescriptionId);
	
	List<JobDescription> findByStack(String stack);
}
