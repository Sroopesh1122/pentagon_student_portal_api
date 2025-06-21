package com.pentagon.app.serviceImpl;

import com.pentagon.app.entity.Batch;
import com.pentagon.app.repository.BatchRepository;
import com.pentagon.app.service.BatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class BatchServiceImpl implements BatchService {

    @Autowired
    private BatchRepository batchRepository;

    @Override
    public Batch addBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    @Override
    public Page<Batch> getAllBatches(String q, String mode, String stackId, Pageable pageable) {
    	return batchRepository.findAll(q, mode, stackId, pageable);
    }

    @Override
    public Optional<Batch> getBatchById(String batchId) {
        return batchRepository.findById(batchId);
    }

    @Override
    public Batch updateBatch(Batch batch) {
        return batchRepository.save(batch);
    }

    @Override
    public void deleteBatch(String batchId) {
        batchRepository.deleteById(batchId);
    }
}