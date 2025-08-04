package com.pentagon.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.ActivityLog;
@Repository
public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
	
	List<ActivityLog> findByUserId(String userId);

	@Query("SELECT a FROM ActivityLog a WHERE a.timestamp BETWEEN :start AND :end")
	List<ActivityLog> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}