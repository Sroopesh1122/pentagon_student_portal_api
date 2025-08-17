package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.ApplicationStatusHistory;

@Repository
public interface ApplicationStatusHistoryRepository extends JpaRepository<ApplicationStatusHistory, Long> {

	@Query("SELECT sh  FROM ApplicationStatusHistory sh WHERE sh.round =:round AND sh.studentJdApplication.applicationId =:applicationId")
	public ApplicationStatusHistory findByRound(String round,String applicationId);
	
	
}
