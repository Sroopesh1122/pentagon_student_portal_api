package com.pentagon.app.service;

import java.util.List;
import java.util.UUID;

import com.pentagon.app.entity.MockRating;
import com.pentagon.app.entity.MockTest;

public interface MockTestService 
{
  public MockTest createMockTest(MockTest mockTest);
  
  public void createMockRating(List<MockRating> mockRatings);
  
  public MockRating updateMockRating(MockRating mockRating);
  
  public List<MockRating> updateMockRatings(List<MockRating> mockRatings);
  
  public List<MockTest> findMockTest(String batchId,String techId,String trainerId);
  
  public List<MockTest> findMockTestOfStudent(String studentId,String batchId,String techId);
  
  public List<MockRating> findMockRating(String studentId,String techId);
  
  public List<MockRating> findMockRating(String studentId);
  
  public MockRating findMockRatingById(String mockRatingId);
  
  public MockTest findMockTestById(String mockTestId);
  
  public MockTest findByNameAndTechnology(String name,String techId);
  
  public List<MockRating> findMockRatingByMockTest(String mockTestId);
  
  
  public List<Double> getRatingOfStudent(String studentId,String techId);
  
 
}
