package com.pentagon.app.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.entity.Stack;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.StackService;

@RestController
@RequestMapping("/api/stack")
public class StackController {
	
	
	@Autowired
	private StackService stackService;
	
	@GetMapping("/public/all")
	public ResponseEntity<?> getAllSatcks()
	{
		List<Stack> allStacks = stackService.getAll();
		
		return ResponseEntity.ok(new ApiResponse<>("success","Stack Data", allStacks));
	}

}
