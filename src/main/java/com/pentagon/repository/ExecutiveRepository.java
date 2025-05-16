package com.pentagon.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.entity.Executive;

public interface ExecutiveRepository extends JpaRepository<Executive, Integer>{

	public Optional<Executive> findByExecutiveId(String executiveId);
}
