package com.pentagon.app.serviceImpl;

import java.util.List;
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
    public Optional<BatchTechTrainer> getAssignmentById(Integer id) {
        return batchTechTrainerRepository.findById(id);
    }

    @Override
    public BatchTechTrainer update(BatchTechTrainer assignment) {
        return batchTechTrainerRepository.save(assignment);
    }

    @Override
    public void deleteAssignment(Integer id) {
        batchTechTrainerRepository.deleteById(id);
    }
    @Override
    public boolean checkTrainerAvailabality(String trainerId, Double startTime, Double endTime) {
    	return batchTechTrainerRepository.isTrainerAvailable(trainerId, startTime, endTime);
    }
    
    @Override
    public List<BatchTechTrainer> getBatchScheduleInfo(String batchId) {
    	return batchTechTrainerRepository.getBatchInfo(batchId);
    }
    
    @Override
    public List<BatchTechTrainer> getTrainerSchedule(String trainerId) {
    	return batchTechTrainerRepository.getTrainerSchedule(trainerId);
    }
    
    @Override
    public BatchTechTrainer getById(Integer id) {
    	
    	return batchTechTrainerRepository.findById(id).orElse(null);
    }
}