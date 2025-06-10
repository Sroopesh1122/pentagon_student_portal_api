package com.pentagon.app.service;

import com.pentagon.app.entity.BatchTechTrainer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface BatchTechTrainerService {
    public BatchTechTrainer assignTrainer(BatchTechTrainer assignment);
    public Page<BatchTechTrainer> getAllAssignments(Pageable pageable);
    public Optional<BatchTechTrainer> getAssignmentById(Integer id);
    public BatchTechTrainer updateAssignment(BatchTechTrainer assignment);
    public void deleteAssignment(Integer id);
}