package com.pentagon.app.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.MockRatings;
import com.pentagon.app.repository.MockRatingsRepository;
import com.pentagon.app.service.MockRatingsService;

@Service
public class MockRatingsServiceImpl implements MockRatingsService {

    @Autowired
    private MockRatingsRepository mockRatingsRepository;

    @Override
    public MockRatings addRating(MockRatings rating) {
        return mockRatingsRepository.save(rating);
    }

    @Override
    public Page<MockRatings> getAllRatings(Pageable pageable) {
        return mockRatingsRepository.findAll(pageable);
    }

    @Override
    public Optional<MockRatings> getRatingById(String id) {
        return mockRatingsRepository.findById(id);
    }

    @Override
    public MockRatings updateRating(MockRatings rating) {
        return mockRatingsRepository.save(rating);
    }

    @Override
    public void deleteRating(String id) {
        mockRatingsRepository.deleteById(id);
    }
}