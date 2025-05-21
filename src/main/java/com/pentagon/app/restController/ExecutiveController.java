package com.pentagon.app.restController;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.requestDTO.AddJobDescriptionRequest;
import com.pentagon.app.service.CustomUserDetails;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/executive")
public class ExecutiveController {
	
	public boolean addJobDescription(@AuthenticationPrincipal CustomUserDetails executiveDetails,
			@Valid @RequestBody AddJobDescriptionRequest addId,
			BindingResult bindingResult)
	{
		if(bindingResult.hasErrors())
		{
			
	            throw new TrainerException("Invalid input data", HttpStatus.BAD_REQUEST);
	        
		}
		return true;
	}

}
