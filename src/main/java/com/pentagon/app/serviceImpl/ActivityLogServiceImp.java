package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.ActivityLog;
import com.pentagon.app.repository.ActivityLogRepository;
import com.pentagon.app.service.ActivityLogService;

@Service
public class ActivityLogServiceImp implements ActivityLogService{

    @Autowired
    private ActivityLogRepository repository;

    @Override
    public void log(String userId,String title,String description) {
        ActivityLog log = new ActivityLog();
        log.setUserId(userId);
        log.setTitle(title);
        log.setDescription(description);
        log.setTimestamp(LocalDateTime.now());
        repository.save(log);
    }

    @Override
    public List<ActivityLog> getAllLogs() {
        return repository.findAll();
    }

    @Override
    public List<ActivityLog> getLogsByUserId(String userId) {
    	return repository.findByUserId(userId);
    }

    @Override

    public List<ActivityLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByDateRange(start, end);
    }
}

