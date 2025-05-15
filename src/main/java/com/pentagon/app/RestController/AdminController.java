package com.pentagon.app.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/pentagon/admin")
public class AdminController {

	@GetMapping("/example")
	@PreAuthorize("hasRole('Admin')")
	public String getMethodName() {
		return "confirmed in Admin ";
	}
	
}
