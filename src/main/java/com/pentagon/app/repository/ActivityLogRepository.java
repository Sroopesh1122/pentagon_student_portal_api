package com.pentagon.app.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pentagon.app.entity.ActivityLog;

public interface ActivityLogRepository extends JpaRepository<ActivityLog, Long> {
	List<ActivityLog> findByEmail(String email);

	List<ActivityLog> findByRole(String role);

	@Query("SELECT a FROM ActivityLog a WHERE a.timestamp BETWEEN :start AND :end")
	List<ActivityLog> findByDateRange(@Param("start") LocalDateTime start, @Param("end") LocalDateTime end);

}