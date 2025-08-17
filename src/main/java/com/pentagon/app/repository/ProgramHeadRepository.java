package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.ProgramHead;

@Repository
public interface ProgramHeadRepository extends JpaRepository<ProgramHead, String> {
	
	public Optional<ProgramHead> findByEmail(String email);
	
	@Query("SELECT ph FROM ProgramHead ph WHERE (:q IS NULL OR :q= '' OR ph.id LIKE CONCAT(:q,'%') OR ph.email LIKE CONCAT(:q,'%' ) OR ph.name LIKE CONCAT(:q,'%') )")
	public Page<ProgramHead> getAll(String q ,Pageable pageable);

	public ProgramHead findByPasswordResetToken(String token);
	
	
}
