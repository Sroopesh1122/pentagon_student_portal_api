package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	Optional<Manager> findByEmail(String email);
	
	public Optional<Manager> findByManagerId(String managerId);

	@Query("SELECT COUNT(M) FROM Manager M")
	public int getManagerCount();
	
	@Query("SELECT m FROM Manager m WHERE ( :q IS NULL OR :q = '' OR m.name LIKE CONCAT(:q,'%') OR  m.email LIKE CONCAT(:q,'%')  OR  m.managerId LIKE CONCAT(:q,'%'))")
    public	Page<Manager> findAll(@Param("q") String q, Pageable pageable);
	
	

}
