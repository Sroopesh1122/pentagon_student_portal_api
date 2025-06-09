package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.ProgramHead;

@Repository
public interface ProgramHeadRepository extends JpaRepository<ProgramHead, String> {
	
	public ProgramHead findByEmail(String email);

}
