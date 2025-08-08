package com.pentagon.app.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pentagon.app.entity.MockTest;

public interface MockTestRepository extends JpaRepository<MockTest, String> {

	@Query("SELECT t FROM MockTest t WHERE (:batchId IS NULL OR t.batch.batchId = :batchId) AND (:techId IS NULL OR t.technology.techId = :techId) AND (:trainerId IS NULL OR t.trainer.trainerId = :trainerId)")
    List<MockTest> findMockTest(
        @Param("batchId") String batchId,
        @Param("techId") String techId,
        @Param("trainerId") String trainerId
    );

    @Query("SELECT DISTINCT t FROM MockTest t JOIN t.mockRatings r WHERE (:batchId IS NULL OR t.batch.batchId = :batchId) AND (:techId IS NULL OR t.technology.techId = :techId) AND (r.student.studentId = :studentId)")
    List<MockTest> findMockTestOfStudent(
        @Param("studentId") String studentId,
        @Param("batchId") String batchId,
        @Param("techId") String techId
    );
    
    @Query("SELECT m From MockTest m WHERE m.mockName =:name AND m.technology.techId =:techId")
    public MockTest findByNameAndTechnology(String name,String techId);
	
	
}
