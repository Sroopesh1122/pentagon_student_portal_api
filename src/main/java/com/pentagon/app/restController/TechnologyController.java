package com.pentagon.app.restController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.entity.Technology;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.TechnologyService;

@RequestMapping("/api/technology")
@RestController
public class TechnologyController {

	
	@Autowired
	private TechnologyService technologyService;
	
	
	@GetMapping("/public/")
	public ResponseEntity<?> getAllTechnologies()
	{
		List<Technology> technologies = technologyService.getAllTechnologies();
		return ResponseEntity.ok(new ApiResponse<>("success","technology data",technologies));
	}
	
}
