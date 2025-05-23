package com.pentagon.app.service;

import java.time.LocalDateTime;
import java.util.List;

import com.pentagon.app.entity.ActivityLog;

public interface ActivityLogService {

	List<ActivityLog> getAllLogs();

	List<ActivityLog> getLogsByEmail(String email);

	List<ActivityLog> getLogsByRole(String role);

	List<ActivityLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end);

}
