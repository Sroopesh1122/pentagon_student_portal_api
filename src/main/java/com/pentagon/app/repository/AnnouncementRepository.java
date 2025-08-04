package com.pentagon.app.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Announcement;

@Repository
public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {


	
//	@Query("SELECT DISTINCT a FROM Announcement a  JOIN batches b WHERE b IS NULL OR b.batchId = :batchId ORDER BY a.createdAt desc")
//	public List<Announcement> getAllAnnouncementByBatch(String batchId);
	
	@Query("SELECT DISTINCT a FROM Announcement a JOIN batches b WHERE (b IS NULL OR b.batchId = :batchId) AND a.createdAt >= :startDateTime ORDER BY a.createdAt desc")
	public List<Announcement> getAnnouncementsFromLast7DaysByBatch(@Param("batchId") String batchId, @Param("startDateTime") LocalDateTime startDateTime);
	
}
