package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.JdRoundHistory;

@Repository
public interface JdRoundHistoryRepository extends JpaRepository<JdRoundHistory, Long> {
	
	@Query("SELECT h FROM JdRoundHistory h WHERE h.round =:round AND h.jobDescription.jobDescriptionId=:jdId")
	public JdRoundHistory findJdRound(String round , String jdId);

}
