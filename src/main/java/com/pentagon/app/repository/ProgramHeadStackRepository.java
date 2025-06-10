package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pentagon.app.entity.ProgramHeadStack;

public interface ProgramHeadStackRepository extends JpaRepository<ProgramHeadStack, Integer> {

	@Query("SELECT p FROM ProgramHeadStack p WHERE p.programHeadId = :programHeadId")
	public List<ProgramHeadStack> getStackByProgramHead(String programHeadId);
}
