package com.pentagon.app.serviceImpl;

import com.pentagon.app.entity.Batch;
import com.pentagon.app.repository.BatchRepository;
import com.pentagon.app.service.BatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
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
    public Page<Batch> getAllBatches(String q, String mode, String stackId,String branchId ,Pageable pageable) {
    	return batchRepository.findAll(q, mode, stackId,branchId ,pageable);
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

	@Override
	public List<Batch> findAllById(List<String> ids) {
		return batchRepository.findAllById(ids);
	}

	@Override
	public Long countCompletedBatch(String branchId) {
		return batchRepository.countBatch(true,branchId);
	}

	@Override
	public Long countOnGoingBatch(String branchId) {
		// TODO Auto-generated method stub
		return batchRepository.countBatch(false ,branchId);
	}

	@Override
	public Long countCompletedBatchByStack(String stackId,String branchId) {
		return batchRepository.countBatchByStack(stackId, true,branchId);
	}

	@Override
	public Long countOnGOingBatchByStack(String stackId,String branchId) {
		return batchRepository.countBatchByStack(stackId, false,branchId);
	}

}