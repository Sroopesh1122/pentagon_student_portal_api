package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.ApplicationStatusHistory;
import com.pentagon.app.repository.ApplicationStatusHistoryRepository;
import com.pentagon.app.service.ApplicationStatusHistoryService;

@Service
public class ApplicationStatusHistoryServiceImpl implements ApplicationStatusHistoryService {

	@Autowired
	private ApplicationStatusHistoryRepository applicationStatusHistoryRepository;
	
	@Override
	public ApplicationStatusHistory create(ApplicationStatusHistory applicationStatusHistory) {
		applicationStatusHistory.setCreatedAt(LocalDateTime.now());
		return applicationStatusHistoryRepository.save(applicationStatusHistory);
	}

}
