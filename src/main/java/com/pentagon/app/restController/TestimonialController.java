package com.pentagon.app.restController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pentagon.app.Dto.TestimonialDTO;
import com.pentagon.app.entity.Testimonials;
import com.pentagon.app.exception.TestimonialException;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.TestimonialService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;
import com.pentagon.app.utils.IdGeneration;

@RestController
@RequestMapping("/api/testimonial")
public class TestimonialController {
	
	
	@Autowired
	private TestimonialService testimonialService;
	
	@Autowired
	private IdGeneration idGeneration;
	
	@Autowired
	private CloudinaryServiceImp cloudinaryService;
	
	@PostMapping("/secure/create")
	public ResponseEntity<?> createTestimonial(
			    @RequestParam String studentName,
			    @RequestParam String pentagonId,
		        @RequestParam(required = false) MultipartFile profileImg,
		        @RequestParam String videoLink,
		        @RequestParam(required = false) Double ctc,
		        @RequestParam(required = false) String role,
		        @RequestParam(required = false) String collageName,
		        @RequestParam(required = false) String stack,
		        @RequestParam(required = false) String branch,
		        @RequestParam(required = false) String companyId,
		        @RequestParam(required = false) Integer yearOfPassing,
		        @RequestParam(required = false) String address,
		        @RequestParam(required = false) String about
			)
	{
		
		Testimonials testimonial = testimonialService.finByPentagonId(pentagonId);
		if(testimonial !=null)
		{
			throw new TestimonialException("Testimonial Already Exists", HttpStatus.CONFLICT);
		}
		
		Testimonials newTestimonials = new Testimonials();
		newTestimonials.setTestimonialId(idGeneration.generateRandomString());
		newTestimonials.setBranch(branch);
		newTestimonials.setCollageName(collageName);
		newTestimonials.setCompanyId(companyId);
		newTestimonials.setAddress(address);
		newTestimonials.setCtc(ctc);
		newTestimonials.setPentagonId(pentagonId);
		newTestimonials.setRole(role);
		newTestimonials.setStack(stack);
		newTestimonials.setStudentName(studentName);
		newTestimonials.setVedioLink(videoLink);
		newTestimonials.setYearOfPassing(yearOfPassing);
		newTestimonials.setAbout(about);
		
		if(profileImg!=null)
		{
			Map<String, Object>	uploadResponse  = cloudinaryService.uploadImage(profileImg);
			  newTestimonials.setProfilePublicId(uploadResponse.get("public_id").toString());
			  newTestimonials.setProfileImgUrl(uploadResponse.get("secure_url").toString());
		}
		
		testimonialService.create(newTestimonials);
		return ResponseEntity.ok(new ApiResponse<>("success","New Testimonial Created Successfully",null));
	}
	
	
	
	
	
	@GetMapping("/public")
	public ResponseEntity<?> getTestimonials(
			@RequestParam(required = false ,defaultValue = "0") Integer page,
			@RequestParam(required = false, defaultValue = "10") Integer limit,
			@RequestParam(required = false) String name,
			@RequestParam(required = false) Integer passingYear,
			@RequestParam(required = false) Double ctc)
	{
		Pageable pageable = PageRequest.of(page, limit,Sort.by("createdAt").descending());		
		Page<Testimonials> testimonials = testimonialService.getTestimonil(name, passingYear, ctc, pageable);

		return ResponseEntity.ok(new ApiResponse<>("success","Testimonial Data", testimonials));
		
	}
	
	
	
	@DeleteMapping("/secure/{id}")
	public ResponseEntity<?> deleteTestimonial(@PathVariable String id)
	{
		testimonialService.delete(id);
		return ResponseEntity.ok(new ApiResponse<>("success","Testimonial Deleted successfully", null));
	}
	

}
