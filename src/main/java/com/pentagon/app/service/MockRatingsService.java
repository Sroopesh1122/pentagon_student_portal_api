package com.pentagon.app.service;

import com.pentagon.app.entity.MockRatings;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface MockRatingsService {
    public MockRatings addRating(MockRatings rating);
    public Page<MockRatings> getAllRatings(Pageable pageable);
    public MockRatings updateRating(MockRatings rating);
}