package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pentagon.app.entity.Executive;

public interface ExecutiveRepository extends JpaRepository<Executive, Integer>{

	Optional<Executive> findByEmail(String email);
	
	public Optional<Executive> findByExecutiveId(String executiveId);

	@Query("SELECT COUNT(E) FROM Executive E")
	int getExecutiveCount();
	
	boolean existsByEmail(String email);

}
