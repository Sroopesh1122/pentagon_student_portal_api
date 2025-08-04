package com.pentagon.app.restController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.entity.Announcement;
import com.pentagon.app.entity.Batch;
import com.pentagon.app.exception.AnnouncementExpection;
import com.pentagon.app.exception.InvalidDataException;
import com.pentagon.app.request.CreateAnnouncementRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.AnnouncementService;
import com.pentagon.app.service.BatchService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlTemplates;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/announcement")
public class AnnouncementController {

	@Autowired
	private BatchService batchService;

	@Autowired
	private AnnouncementService announcementService;
	
	@Autowired
	private StudentService studentService;
	
	
	@Autowired
	private HtmlTemplates htmlTemplates;
	
	
	@Autowired
	private MailService mailService;

	@PostMapping("/secure/create")
	public ResponseEntity<?> createAnnouncement(@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@Valid @RequestBody CreateAnnouncementRequest request, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException("Invalid Input Data", HttpStatus.BAD_REQUEST);
		}
		
		String employeeId = getEmployeeId(customUserDetails);
		String employeeName = getEmployeeName(customUserDetails);
		

		Announcement announcement = new Announcement();
		announcement.setDescription(request.getDescription());
		announcement.setTitle(request.getTitle());

		if (request.getLink() != null) {
			announcement.setLink(request.getLink());
		}

		List<Batch> batches = batchService.findAllById(request.getBatchId());
		
		List<String> studentEmails = new ArrayList<>();
		
		batches.forEach(batch->{
			studentEmails.addAll(studentService.getEmailByBatch(batch.getBatchId()));
		});
		

		announcement.setBatches(batches);
		announcement.setEmployeeId(employeeId);
		announcement.setAnnouncedBy(employeeName);
		announcement = announcementService.createAnnouncement(announcement);
		
		String mailMessage = htmlTemplates.getAnnouncementEmail(announcement.getTitle(), announcement.getDescription(), employeeName, null);
		
		
		mailService.sendWithBccAsync(null,"New Announcement - Pentagon Space", mailMessage, studentEmails);
		
		

		return ResponseEntity.ok(new ApiResponse<>("success", "New Announcement Created", null));
	}

	@DeleteMapping("/secure/delete/{id}")
	public ResponseEntity<?> deleteAnnouncement(@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Integer id) {
		
		String employeeId = getEmployeeId(customUserDetails);

		Announcement announcement = announcementService.findById(id);

		if(!employeeId.equals(announcement.getEmployeeId()))
		{
			throw new AnnouncementExpection("You cannot able to delete this announcemnt", HttpStatus.BAD_REQUEST);
		}
		
		
		if (announcement != null) 
		{
			announcementService.deleteById(id);
		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Announcement Deleted SUccessfully", null));

	}

	@GetMapping("/secure/batch/{batchId}")
	public ResponseEntity<?> getAnnouncement(@PathVariable String batchId) {

		List<Announcement> announcements = announcementService.getAnnouncementByBatch(batchId);

		return ResponseEntity.ok(new ApiResponse<>("success", "Batch Announcement", announcements));

	}

	private String getEmployeeId(CustomUserDetails customUserDetails) {
		String employeeId = null;

		if (customUserDetails.getAdmin() != null) {
			employeeId = customUserDetails.getAdmin().getAdminId();
		} else if (customUserDetails.getExecutive() != null) {
			employeeId = customUserDetails.getExecutive().getExecutiveId();
		} else if (customUserDetails.getManager() != null) {
			employeeId = customUserDetails.getManager().getManagerId();
		} else if (customUserDetails.getProgramHead() != null) {
			employeeId = customUserDetails.getProgramHead().getId();
		} else if (customUserDetails.getStudentAdmin() != null) {
			employeeId = customUserDetails.getStudentAdmin().getId();
		} else if (customUserDetails.getTrainer() != null) {
			employeeId = customUserDetails.getTrainer().getTrainerId();
		}
		
		return employeeId;
	}
	
	private String getEmployeeName(CustomUserDetails customUserDetails) {
		String employeeName = null;

		if (customUserDetails.getAdmin() != null) {
			employeeName = customUserDetails.getAdmin().getName();
		} else if (customUserDetails.getExecutive() != null) {
			employeeName = customUserDetails.getExecutive().getName();
		} else if (customUserDetails.getManager() != null) {
			employeeName = customUserDetails.getManager().getName();
		} else if (customUserDetails.getProgramHead() != null) {
			employeeName = customUserDetails.getProgramHead().getName();
		} else if (customUserDetails.getStudentAdmin() != null) {
			employeeName = customUserDetails.getStudentAdmin().getName();
		} else if (customUserDetails.getTrainer() != null) {
			employeeName = customUserDetails.getTrainer().getName();
		}
		
		return employeeName;
	}

}
