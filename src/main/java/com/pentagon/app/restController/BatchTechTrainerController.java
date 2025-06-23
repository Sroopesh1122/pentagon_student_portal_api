package com.pentagon.app.restController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.BatchDTO;
import com.pentagon.app.Dto.BatchTechTrainerDTO;
import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.mapper.BatchMapper;
import com.pentagon.app.mapper.BatchTechTrainerMapper;import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.BatchService;
import com.pentagon.app.service.BatchTechTrainerService;
import com.pentagon.app.service.TrainerService;

@RestController
@RequestMapping("/api/batch")
public class BatchTechTrainerController {

	
	@Autowired
	private TrainerService trainerService;
	
	
	@Autowired
	private BatchTechTrainerService batchTechTrainerService;
	
	@Autowired
	private BatchTechTrainerMapper batchTechTrainerMapper;
	
	
	@Autowired
	private BatchMapper batchMapper;
	
	
	
	@Autowired
	private BatchService batchService;
	
	
	@GetMapping("/secure/trainer/{id}/schedule")
	@PreAuthorize("hasAnyRole('STUDENTADMIN', 'PROGRAMHEAD','ADMIN')")
	public ResponseEntity<?> getTrainerScheduleInfo(@PathVariable String id)
	{
		Trainer findTrainer = trainerService.getById(id);
		if(findTrainer == null)
		{
			throw new TrainerException("Trainer Not Found", HttpStatus.NOT_FOUND);
		}
		
		
		List<BatchTechTrainer> scheduleInfo = batchTechTrainerService.getTrainerSchedule(id);
		return ResponseEntity.ok(new ApiResponse<>("success","ScheduleData", scheduleInfo));
		
	}
	
	@GetMapping("/secure/all")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','PROGRAMHEAD','ADMIN')")
	public ResponseEntity<?> getAllBatches(
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String q,
			@RequestParam(required =  false) String mode,
			@RequestParam(required =  false) String stackId)
	{
		
		Pageable pageable = PageRequest.of(page, limit);
		Page<Batch> batches = batchService.getAllBatches(q, mode, stackId, pageable);
		Page<BatchDTO> batchesDTO  = batches.map(batch->{
			return batchMapper.toDTO(batch);
		});
		return ResponseEntity.ok(new ApiResponse<>("success","Batch Data", batchesDTO));
	}
	
	
	
	@GetMapping("/secure/{id}/details")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','PROGRAMHEAD','ADMIN')")
	public ResponseEntity<?> getBatchScheduleInfo(@PathVariable String id)
	{
		List<BatchTechTrainer> batchScheduleInfo  = batchTechTrainerService.getBatchScheduleInfo(id);
		return ResponseEntity.ok(new ApiResponse<>("success","Batch Data", batchScheduleInfo));
	}
	
	
}
