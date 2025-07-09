package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {


	
	@Query("SELECT DISTINCT a FROM Announcement a  JOIN batches b WHERE b IS NULL OR b.batchId = :batchId")
	public List<Announcement> getAllAnnouncementByBatch(String batchId);
	
}
