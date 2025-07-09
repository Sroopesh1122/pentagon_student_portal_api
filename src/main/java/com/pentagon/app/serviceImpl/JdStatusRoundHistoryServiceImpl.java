package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.JdRoundHistory;
import com.pentagon.app.entity.JdStatusHistory;
import com.pentagon.app.repository.JdRoundHistoryRepository;
import com.pentagon.app.repository.JdStatusHistoryRepository;
import com.pentagon.app.service.JdStatusRoundHistoryService;

@Service
public class JdStatusRoundHistoryServiceImpl implements JdStatusRoundHistoryService {

	@Autowired
	private JdStatusHistoryRepository jdStatusHistoryRepository;
	
	@Autowired
	private JdRoundHistoryRepository jdRoundHistoryRepository;
	
	@Override
	public JdStatusHistory addStatus(JdStatusHistory jdStatusHistory) {
		jdStatusHistory.setCreatedAt(LocalDateTime.now());
		return jdStatusHistoryRepository.save(jdStatusHistory);
	}

	@Override
	public JdRoundHistory addRound(JdRoundHistory jdRoundHistory) {
		jdRoundHistory.setCreatedAt(LocalDateTime.now());
		return jdRoundHistoryRepository.save(jdRoundHistory);
	}

	@Override
	public JdRoundHistory findRound(String roundName, String jdId) {
		return jdRoundHistoryRepository.findJdRound(roundName, jdId);
	}

}
