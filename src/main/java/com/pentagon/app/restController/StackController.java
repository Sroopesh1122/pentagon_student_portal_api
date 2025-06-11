package com.pentagon.app.restController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.entity.Stack;
import com.pentagon.app.entity.Trainer;
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
	
	@GetMapping("/public/all")
	public ResponseEntity<?> getAllSatcks()
	{
		List<Stack> allStacks = stackService.getAll();
		
		return ResponseEntity.ok(new ApiResponse<>("success","Stack Data", allStacks));
	}
	
	
	@GetMapping("/public/stack-technologies")
	public ResponseEntity<?> getAllSatcksWithTechnologies()
	{
		List<Stack> allStacks = stackService.getAll();
		
		List<StackTechResponse> listOfstackTechResponse =  new ArrayList<>();
		
		
		//Looping through stacks
//		allStacks.forEach(stack->{
//			
//			StackTechResponse stackTechResponse = new StackTechResponse();
//			
//			System.out.println(stack.getName());
//			
//			stackTechResponse.setStack(stack);
//			
//			//get All techs under stack
//			List<StackTech> stackTechs = stackTechService.getAllTechnologiesByStack(stack.getStackId());
//			
//			List<TechTrainersResponse> techTrainersResponses  = new ArrayList<>();
//			
//			//Get all trainers under technology
//			stackTechs.forEach(tech->{
//				
//				TechTrainersResponse techTrainersResponse = new TechTrainersResponse();
//				techTrainersResponse.setTechnology(technologyService.getTechnologyById(tech.getTechId()).orElse(null));
//				
//				//List of trainer under tech
//				List<Trainer> trainers =  new ArrayList<>();
//				List<TrainerTech> trainerTech = trainerTechService.getAllTrainerByTechnology(tech.getTechId());
//				trainerTech.forEach(trainerTechData -> {
//					trainers.add(trainerService.getById(trainerTechData.getTrainerId()));
//				});
//				techTrainersResponse.setTrainers(trainers);
//				
//				techTrainersResponses.add(techTrainersResponse);
//				
//			});
//			
//			
//			listOfstackTechResponse.add(stackTechResponse);
//			
//			
//		});
		
		
		
		return ResponseEntity.ok(new ApiResponse<>("success","Stack - trainer - tech  Data", listOfstackTechResponse));
	}

}
