package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.JdStatusHistory;

@Repository
public interface JdStatusHistoryRepository extends JpaRepository<JdStatusHistory, Long> {
	

}
