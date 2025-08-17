package com.pentagon.app.service;

import com.pentagon.app.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BatchService {
    public Batch addBatch(Batch batch);
    public Page<Batch> getAllBatches(String q,String mode,String stackId,String branchId ,Pageable pageable);
    public Optional<Batch> getBatchById(String batchId);
    public Batch updateBatch(Batch batch);
    public void deleteBatch(String batchId);
    public List<Batch> findAllById(List<String> ids);
    
    public Long countCompletedBatch(String branchId);
    
    public Long countOnGoingBatch(String branchId);
    
    public Long countCompletedBatchByStack(String stackId,String branchId);
    public Long countOnGOingBatchByStack(String stackId,String branchId);
    
    
}