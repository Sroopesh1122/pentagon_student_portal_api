package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.app.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	Optional<Manager> findByEmail(String email);
	
	public Optional<Manager> findByManagerId(String managerId);

}
