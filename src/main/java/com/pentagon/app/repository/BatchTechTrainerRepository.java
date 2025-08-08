package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.BatchTechTrainer;

@Repository
public interface BatchTechTrainerRepository extends JpaRepository<BatchTechTrainer, Integer> {

	@Query("SELECT COUNT(btt) = 0  FROM BatchTechTrainer btt WHERE btt.trainer.id = :trainerId AND  (btt.status ='In Progress') AND (:startTime = 0 OR :endTime = 0 OR (:startTime < btt.endTime AND :endTime > btt.startTime))")
	public boolean isTrainerAvailable(String trainerId, Double startTime, Double endTime);
	
//	@Query("""
//		    SELECT COUNT(btt) = 0
//		    FROM BatchTechTrainer btt
//		    WHERE btt.trainer.id = :trainerId
//		      AND btt.completed = false
//		      AND LOWER(btt.status) = 'started'
//		      AND (
//		            (:startTime IS NULL OR :endTime IS NULL)
//		         OR (:startTime < btt.endTime AND :endTime > btt.startTime)
//		      )
//		""")
//		boolean isTrainerAvailable(
//		    @Param("trainerId") String trainerId,
//		    @Param("startTime") Double startTime,
//		    @Param("endTime") Double endTime
//		);

	@Query("SELECT btt FROM BatchTechTrainer btt WHERE btt.batch.batchId = :batchId")
	public List<BatchTechTrainer> getBatchInfo(String batchId);

	@Query("SELECT btt FROM BatchTechTrainer btt WHERE btt.trainer.trainerId = :trainerId AND btt.completed = false")
	public List<BatchTechTrainer> getTrainerSchedule(String trainerId);
	
	@Query("SELECT btt FROM BatchTechTrainer btt WHERE btt.batch.batchId = :batchId AND btt.technology.techId=:techId")
	public BatchTechTrainer findByBatchTechnology(String batchId,String techId);
	
}
