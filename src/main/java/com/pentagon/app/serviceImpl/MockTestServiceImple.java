package com.pentagon.app.serviceImpl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.MockRating;
import com.pentagon.app.entity.MockTest;
import com.pentagon.app.repository.MockRatingRepository;
import com.pentagon.app.repository.MockTestRepository;
import com.pentagon.app.service.MockTestService;
import com.pentagon.app.utils.IdGeneration;

@Service
public class MockTestServiceImple implements MockTestService {
	
	
	
	@Autowired
	private MockTestRepository mockTestRepository;
	
	@Autowired
	private MockRatingRepository mockRatingRepository;

	@Override
	public MockTest createMockTest(MockTest mockTest) {
		return mockTestRepository.save(mockTest);
	}

	@Override
	public void createMockRating(List<MockRating> mockRatings) {
	   mockRatingRepository.saveAll(mockRatings);
	}

	@Override
	public MockRating updateMockRating(MockRating mockRating) {
		return mockRatingRepository.save(mockRating);
	}

	@Override
	public List<MockTest> findMockTest(String batchId, String techId, String trainerId) {
		return mockTestRepository.findMockTest(batchId, techId, trainerId);
	}

	@Override
	public List<MockTest> findMockTestOfStudent(String studentId, String batchId, String techId) {
		return mockTestRepository.findMockTestOfStudent(studentId, batchId, techId);
	}

	@Override
	public List<MockRating> findMockRating(String studentId,String techId) {
		return mockRatingRepository.findMockRating(studentId, techId);
	}
	
	@Override
	public List<MockRating> findMockRating(String studentId) {
		return mockRatingRepository.findMockRating(studentId);
	}

	@Override
	public MockRating findMockRatingById(String mockRatingId) {
		return mockRatingRepository.findById(mockRatingId).orElse(null);
	}

	@Override
	public MockTest findMockTestById(String mockTestId) {
		return mockTestRepository.findById(mockTestId).orElse(null);
	}
	
	
	@Override
	public List<MockRating> findMockRatingByMockTest(String mockTestId) {
		return mockRatingRepository.findMockRatingByMockTest(mockTestId);
	}
	@Override
	public List<MockRating> updateMockRatings(List<MockRating> mockRatings) {
		return mockRatingRepository.saveAll(mockRatings);
	}

	@Override
	public MockTest findByNameAndTechnology(String name,String techId) {
		// TODO Auto-generated method stub
		return mockTestRepository.findByNameAndTechnology(name, techId);
	}
	
	@Override
	public List<Double> getRatingOfStudent(String studentId, String techId) {
		return mockRatingRepository.getRatingByStudentAndTechnology(studentId, techId);
	}

}
