package com.pentagon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.entity.Manager;

public interface ManagerRepository extends JpaRepository<Manager, Integer> {

	public Optional<Manager> findByManagerId(String managerId);

}
