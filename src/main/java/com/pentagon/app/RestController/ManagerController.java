package com.pentagon.app.RestController;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("penatgon/manager")
public class ManagerController {
	@GetMapping("/example")
	public String getMethodName() {
		return "Confiremd in manager";
	}
	

}
