package com.pentagon.app.serviceImpl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.repository.BatchTechTrainerRepository;
import com.pentagon.app.service.BatchTechTrainerService;

@Service
public class BatchTechTrainerServiceImpl implements BatchTechTrainerService {

    @Autowired
    private BatchTechTrainerRepository batchTechTrainerRepository;

    @Override
    public BatchTechTrainer assignTrainer(BatchTechTrainer assignment) {
        return batchTechTrainerRepository.save(assignment);
    }

    @Override
    public Page<BatchTechTrainer> getAllAssignments(Pageable pageable) {
        return batchTechTrainerRepository.findAll(pageable);
    }

    @Override
    public Optional<BatchTechTrainer> getAssignmentById(String id) {
        return batchTechTrainerRepository.findById(id);
    }

    @Override
    public BatchTechTrainer updateAssignment(BatchTechTrainer assignment) {
        return batchTechTrainerRepository.save(assignment);
    }

    @Override
    public void deleteAssignment(String id) {
        batchTechTrainerRepository.deleteById(id);
    }
}