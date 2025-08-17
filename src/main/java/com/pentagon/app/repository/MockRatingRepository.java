package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pentagon.app.entity.MockRating;

public interface MockRatingRepository extends JpaRepository<MockRating,String> {
	
	@Query("SELECT r FROM MockRating r WHERE r.student.studentId =:studentId AND r.mockTest.technology.techId=:techId")
	public List<MockRating> findMockRating(String studentId,String tehcId);
	
	@Query("SELECT r FROM MockRating r WHERE r.student.studentId =:studentId ORDER BY r.createdAt desc")
	public List<MockRating> findMockRating(String studentId);
	
	@Query("SELECT r FROM MockRating r WHERE r.mockTest.id =:testId ")
	public List<MockRating> findMockRatingByMockTest(String testId);
	
	@Query("SELECT r.rating FROM MockRating r WHERE r.student.studentId =:studentId AND r.mockTest.technology.techId =:techId  order by createdAt desc")
	public List<Double> getRatingByStudentAndTechnology(String studentId,String techId);
	

}
