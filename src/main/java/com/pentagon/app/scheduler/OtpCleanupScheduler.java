package com.pentagon.app.scheduler;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.pentagon.app.repository.OtpRepository;

@Component
public class OtpCleanupScheduler {

	@Autowired
    private OtpRepository otpRepository;

//    @Scheduled(fixedRate = 300000) // every 5 minutes
    public void cleanupExpiredOtps() {
        LocalDateTime expiryTime = LocalDateTime.now().minusMinutes(25);
        otpRepository.deleteExpiredOtps(expiryTime);
    }
}
