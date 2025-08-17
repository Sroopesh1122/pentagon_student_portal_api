package com.pentagon.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pentagon.app.entity.Enquiry;

public interface EnquiryRepository extends JpaRepository<Enquiry, Long> {
	
	@Query("SELECT e FROM Enquiry e WHERE closed = :closed")
	public Page<Enquiry> findAll( boolean closed ,Pageable pageable);
	
}
