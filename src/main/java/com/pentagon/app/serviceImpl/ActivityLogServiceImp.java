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
    public void log(String email, String userId, String role, String description) {
        ActivityLog log = new ActivityLog();
        log.setEmail(email);
        log.setUserId(userId);
        log.setRole(role);
        log.setDescription(description);
        repository.save(log);
    }

    @Override
    public List<ActivityLog> getAllLogs() {
        return repository.findAll();
    }

    @Override
    public List<ActivityLog> getLogsByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override

    public List<ActivityLog> getLogsByRole(String role) {
        return repository.findByRole(role);
    }

    @Override

    public List<ActivityLog> getLogsByDateRange(LocalDateTime start, LocalDateTime end) {
        return repository.findByDateRange(start, end);
    }
}

