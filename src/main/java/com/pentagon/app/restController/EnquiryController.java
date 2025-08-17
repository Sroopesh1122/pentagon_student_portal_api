package com.pentagon.app.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.entity.Enquiry;
import com.pentagon.app.exception.EnquiryException;
import com.pentagon.app.request.CreateEnquiryRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.EnquiryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/enquiry")
public class EnquiryController
{
	
	@Autowired
	private EnquiryService enquiryService;
	
	
	@PostMapping("/public/")
	public ResponseEntity<?> createEnquiry(@RequestBody @Valid CreateEnquiryRequest request , BindingResult bindingResult)
	{
		
		if(bindingResult.hasErrors())
		{
			throw new EnquiryException("Invalid Input Data", HttpStatus.BAD_REQUEST);
		}
		
		Enquiry enquiry = new Enquiry();
		enquiry.setEmail(request.getEmail());
		enquiry.setName(request.getName());
		enquiry.setPhoneNo(request.getPhone());
		enquiry.setMessage(request.getMessage());
		
		
		enquiryService.create(enquiry);
		
		return ResponseEntity.ok(
			    new ApiResponse<>("success", 
			        "Thank you for reaching out to us. Your enquiry has been received and our team will get back to you shortly.", 
			        null
			    )
			);
	}
	
	
	
	@GetMapping("/secure/")
	public ResponseEntity<?> getEnquiries(
			@RequestParam(required = false , defaultValue = "0") Integer page,
			@RequestParam(required = false,defaultValue = "12") Integer limit,
			@RequestParam(required = false, defaultValue = "false") boolean closed)
	{
		Pageable pageable = PageRequest.of(page, limit,Sort.by("createdAt").descending());
		
		Page<Enquiry> enquiries = enquiryService.findAll(pageable);
		
		return ResponseEntity.ok(new ApiResponse<>("success","Enquiry Data",enquiries));
		
		
	}
	

}
