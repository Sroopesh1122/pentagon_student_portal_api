package com.pentagon.app.service;

import java.time.LocalDateTime;
import java.util.List;

import com.pentagon.app.entity.ActivityLog;

public interface ActivityLogService {

	List<ActivityLog> getAllLogs();


	List<ActivityLog> getLogsByUserId(String userId);

	List<ActivityLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end);

	void log(String userId,String title,String description);

}
