package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.Student.EnrollmentStatus;

@Repository
public interface BatchRepository extends JpaRepository<Batch, String> {

	@Query("""
			    SELECT b FROM Batch b
			    WHERE (:q IS NULL OR b.batchId LIKE CONCAT(:q, '%') OR b.name LIKE CONCAT(:q, '%'))
			      AND (:mode IS NULL OR b.mode = :mode)
			      AND (:stackId IS NULL OR b.stack.stackId = :stackId)
			""")
	public Page<Batch> findAll(String q, String mode, String stackId ,Pageable pageable);
	
	
	@Query("SELECT COUNT(DISTINCT b) FROM Batch b JOIN b.students s WHERE s.status =:status AND b.batchId=:batchId")
	public Long countBatchStudents(EnrollmentStatus status,String batchId);
	
	@Query("SELECT COUNT(b) FROM Batch b WHERE b.completed = :isCompleted")
	public Long countBatch(Boolean isCompleted);
	
	
	@Query("SELECT COUNT(b) FROM Batch b WHERE b.stack.stackId =:stackId AND b.completed = :isCompleted")
	public Long countBatchByStack(String stackId ,Boolean isCompleted);
	
	

}
