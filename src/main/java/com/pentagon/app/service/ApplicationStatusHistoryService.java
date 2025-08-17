package com.pentagon.app.service;

import com.pentagon.app.entity.ApplicationStatusHistory;

public interface ApplicationStatusHistoryService {

	public ApplicationStatusHistory create(ApplicationStatusHistory applicationStatusHistory);
	
	public ApplicationStatusHistory findByRound(String round,String applicationId);
	
	public ApplicationStatusHistory update(ApplicationStatusHistory applicationStatusHistory);
	
}
