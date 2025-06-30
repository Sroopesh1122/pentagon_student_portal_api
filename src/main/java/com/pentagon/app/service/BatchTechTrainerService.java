package com.pentagon.app.service;

import com.pentagon.app.entity.BatchTechTrainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BatchTechTrainerService {
	
    public BatchTechTrainer assignTrainer(BatchTechTrainer assignment);
    public Page<BatchTechTrainer> getAllAssignments(Pageable pageable);
    public Optional<BatchTechTrainer> getAssignmentById(Integer id);
    public BatchTechTrainer update(BatchTechTrainer assignment);
    public void deleteAssignment(Integer id);
    public boolean checkTrainerAvailabality(String trainerId , Double startTime,Double endTime);
    public List<BatchTechTrainer> getBatchScheduleInfo(String batchId);
    public List<BatchTechTrainer> getTrainerSchedule(String trainerId);
    public BatchTechTrainer getById(Integer id);
}