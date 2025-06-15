package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.MockRatings;
@Repository
public interface MockRatingsRepository extends JpaRepository<MockRatings,Integer> {

	Optional<MockRatings> findByStudentIdAndTrainerIdAndTechId(String studentId, String trainerId, String techId);

}
