package com.pentagon.app.restController;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.pentagon.app.entity.ActivityLog;
import com.pentagon.app.serviceImpl.ActivityLogServiceImp;

@RestController
@RequestMapping("/api/activity")
public class ActivityLogController {

    @Autowired
    private ActivityLogServiceImp activityLogServiceImp;

    @GetMapping("/all")
	@PreAuthorize("hasRole('ADMIN')")
    public List<ActivityLog> getAllLogs() {
        return activityLogServiceImp.getAllLogs();
    }

    @GetMapping("/by-email")
	@PreAuthorize("hasRole('ADMIN')")
    public List<ActivityLog> getLogsByEmail(@RequestParam String email) {
        return activityLogServiceImp.getLogsByEmail(email);
    }

    @GetMapping("/by-role")	
    @PreAuthorize("hasRole('ADMIN')")
    public List<ActivityLog> getLogsByRole(@RequestParam String role) {
        return activityLogServiceImp.getLogsByRole(role);
    }

    @GetMapping("/by-date")
	@PreAuthorize("hasRole('ADMIN')")
    public List<ActivityLog> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return activityLogServiceImp.getLogsByDateRange(start, end);
    }
}
