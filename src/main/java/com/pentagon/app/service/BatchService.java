package com.pentagon.app.service;

import com.pentagon.app.entity.Batch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;

public interface BatchService {
    public Batch addBatch(Batch batch);
    public Page<Batch> getAllBatches(String q,String mode,String stackId ,Pageable pageable);
    public Optional<Batch> getBatchById(String batchId);
    public Batch updateBatch(Batch batch);
    public void deleteBatch(String batchId);
}