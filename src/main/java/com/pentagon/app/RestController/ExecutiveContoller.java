package com.pentagon.app.RestController;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/pentagon/executive")
@RestController
public class ExecutiveContoller {
	
	@PreAuthorize("hasRole('Executive')")
	@GetMapping("example")
	public String getMethodName() {
		return "confirmed in the executive";
	}
	

}
