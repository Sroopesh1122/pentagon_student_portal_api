package com.pentagon.app.restController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.StackDTO;
import com.pentagon.app.entity.Stack;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.mapper.StackMapper;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.response.StackTechResponse;
import com.pentagon.app.response.TechTrainersResponse;
import com.pentagon.app.service.StackService;
import com.pentagon.app.service.TechnologyService;
import com.pentagon.app.service.TrainerService;

@RestController
@RequestMapping("/api/stack")
public class StackController {
	
	
	@Autowired
	private StackService stackService;
	
	
	
	@Autowired
	private TechnologyService technologyService;
	
	
	
	@Autowired
	private TrainerService trainerService;
	
	
	@Autowired
	private StackMapper stackMapper;
	
	@GetMapping("/public/all")
	public ResponseEntity<?> getAllSatcks()
	{
		List<StackDTO> allStacks = stackService.getAll().stream().map(stack->stackMapper.convertToDTO(stack)).toList();
		
		return ResponseEntity.ok(new ApiResponse<>("success","Stack Data", allStacks));
	}
	
	
	@GetMapping("/public/stack-technologies")
	public ResponseEntity<?> getAllSatcksWithTechnologies()
	{
		List<Stack> allStacks = stackService.getAll();
		
		List<StackTechResponse> listOfstackTechResponse =  new ArrayList<>();	
		return ResponseEntity.ok(new ApiResponse<>("success","Stack - trainer - tech  Data", listOfstackTechResponse));
	}

}
