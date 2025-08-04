package com.pentagon.app.restController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.BatchDTO;
import com.pentagon.app.Dto.BatchTechTrainerDTO;
import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.BatchException;
import com.pentagon.app.exception.BatchTechTrainerException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.mapper.BatchMapper;
import com.pentagon.app.mapper.BatchTechTrainerMapper;
import com.pentagon.app.request.UpdateClassProgress;
import com.pentagon.app.request.UpdateScheduleDetails;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.BatchService;
import com.pentagon.app.service.BatchTechTrainerService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.serviceImpl.MailService;
import com.pentagon.app.utils.HtmlTemplates;

@RestController
@RequestMapping("/api/batch")
public class BatchTechTrainerController {

	
	@Autowired
	private TrainerService trainerService;
	
	
	@Autowired
	private BatchTechTrainerService batchTechTrainerService;
	
	@Autowired
	private BatchTechTrainerMapper batchTechTrainerMapper;
	
	
	@Autowired
	private BatchMapper batchMapper;
	
	
	@Autowired
	private StudentService studentService;
	
	@Autowired
	private HtmlTemplates htmlTemplates;
	
	@Autowired
	private BatchService batchService;
	
	
	@Autowired
	private MailService mailService;
	
	
	@GetMapping("/secure/trainer/{id}/schedule")
	@PreAuthorize("hasAnyRole('STUDENTADMIN', 'PROGRAMHEAD','ADMIN','TRAINER')")
	public ResponseEntity<?> getTrainerScheduleInfo(@PathVariable String id)
	{
		Trainer findTrainer = trainerService.getById(id);
		if(findTrainer == null)
		{
			throw new TrainerException("Trainer Not Found", HttpStatus.NOT_FOUND);
		}
		
		
		List<BatchTechTrainer> scheduleInfo = batchTechTrainerService.getTrainerSchedule(id);
		return ResponseEntity.ok(new ApiResponse<>("success","ScheduleData", scheduleInfo));
		
	}
	
	
	@PutMapping("/secure/{id}/progress")
	@PreAuthorize("hasRole('TRAINER','PROGRAMHEAD')")
	public ResponseEntity<?> updateClassProgress(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable Integer id,
			@RequestBody UpdateClassProgress request)
	{
		Trainer trainer = customUserDetails.getTrainer();
		
		BatchTechTrainer batchTechTrainer = batchTechTrainerService.getById(id);
		
		if(batchTechTrainer ==null)
		{
			throw new BatchTechTrainerException("Class Not Found", HttpStatus.NOT_FOUND);
		}
		
		if(batchTechTrainer.getClassProgress() >=100)
		{
			throw new BatchTechTrainerException("Progress is full", HttpStatus.BAD_REQUEST);
		}
		
		if(!trainer.getTrainerId().equals(batchTechTrainer.getTrainer().getTrainerId()))
		{
			throw new BatchTechTrainerException("Your are not allowed to update class progress", HttpStatus.BAD_REQUEST);
		}
		
		if(request.getProgress() < batchTechTrainer.getClassProgress())
		{
			throw new BatchTechTrainerException("Cannot able to decrease progress", HttpStatus.BAD_REQUEST);
		}
		
		if(batchTechTrainer.isCompleted())
		{
			throw new BatchTechTrainerException("This Batch is Completed", HttpStatus.BAD_REQUEST);
		}
		
		batchTechTrainer.setClassProgress(request.getProgress());
		
		batchTechTrainerService.update(batchTechTrainer);
		
		return ResponseEntity.ok(new ApiResponse<>("success","Class progress updated", null));
	}
	
	
	@GetMapping("/secure/timetabel/send-email")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','PROGRAMHEAD','ADMIN','TRAINER')")
	public ResponseEntity<?> getSendTimeTableNotificationBatches(@RequestParam String batchId)
	{
		
		List<BatchTechTrainer> batchTechTrainers = batchTechTrainerService.getBatchScheduleInfo(batchId)
			    .stream()
			    .filter(data -> !data.getStatus().equalsIgnoreCase("Not Started") && !data.getStatus().equalsIgnoreCase("Completed"))
			    .collect(Collectors.toList());

			List<String> trainerEmails = batchTechTrainers.stream()
			    .map(data -> data.getTrainer().getEmail())
			    .collect(Collectors.toList());

			List<String> studentsEmail = studentService.getEmailByBatch(batchId);

			// Combine trainer and student emails into a new list
			List<String> recipientsEmails = new ArrayList<>(trainerEmails);
			recipientsEmails.addAll(studentsEmail);

			// If you want to build timetable details:
			List<String> timeTableDetails = new ArrayList<>();
			for (BatchTechTrainer batchTechTrainer : batchTechTrainers) {
			    String details = batchTechTrainer.getTechnology().getName() + "," +
			                     batchTechTrainer.getTrainer().getName() + "," +
			                     doubleTimeRangeToReadable(batchTechTrainer.getStartTime(), batchTechTrainer.getEndTime());
			    timeTableDetails.add(details);
			}
			
			
			String trainerEmailTemplate = htmlTemplates.getTrainerTimeTableEmail(timeTableDetails);
			String studentEmailTemplate = htmlTemplates.getStudentTimeTableEmail(timeTableDetails);
			
			try {
				mailService.sendWithBcc(null, "Time Table", studentEmailTemplate, studentsEmail);
				mailService.sendWithBcc(null, "Time Table", trainerEmailTemplate, trainerEmails);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		return ResponseEntity.ok(new ApiResponse<>("success","Emails Sent Successfully",null));
	}
	
	
	@PostMapping("/secure/{id}/complete")
	@PreAuthorize("hasAnyRole('STUDENTADMIN')")
	public ResponseEntity<?> completeBatch(@PathVariable String id)
	{
		Batch findBatch = batchService.getBatchById(id).orElse(null);
		if(findBatch ==null)
		{
			throw new BatchException("Batch Not Found", HttpStatus.NOT_FOUND);
		}
		
		
		if(findBatch.isCompleted())
		{
			throw new BatchException("Batch is already completed", HttpStatus.BAD_REQUEST); 
		}
		
		findBatch.setCompleted(true);
		batchService.updateBatch(findBatch);
		
		return ResponseEntity.ok(new ApiResponse<>("success","Batch Completed Successfully" , null));
	}
	
	
	
	
	@GetMapping("/secure/all")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','PROGRAMHEAD','ADMIN','TRAINER')")
	public ResponseEntity<?> getAllBatches(
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String q,
			@RequestParam(required =  false) String mode,
			@RequestParam(required =  false) String stackId)
	{
		
		Pageable pageable = PageRequest.of(page, limit);
		Page<Batch> batches = batchService.getAllBatches(q, mode, stackId, pageable);
		Page<BatchDTO> batchesDTO  = batches.map(batch->{
			return batchMapper.toDTO(batch);
		});
		return ResponseEntity.ok(new ApiResponse<>("success","Batch Data", batchesDTO));
	}
	
	
	
	@GetMapping("/secure/{id}/details")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','PROGRAMHEAD','ADMIN','TRAINER','STUDENT')")
	public ResponseEntity<?> getBatchScheduleInfo(@PathVariable String id)
	{
		List<BatchTechTrainer> batchScheduleInfo  = batchTechTrainerService.getBatchScheduleInfo(id);
		return ResponseEntity.ok(new ApiResponse<>("success","Batch Data", batchScheduleInfo));
	}
	
	
	@GetMapping("/secure/schedule/details/{id}")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','PROGRAMHEAD','ADMIN','TRAINER','STUDENT')")
	public ResponseEntity<?> getScheduleInfoById(@PathVariable Integer id)
	{
		BatchTechTrainer batchScheduleInfo  = batchTechTrainerService.getById(id);
		return ResponseEntity.ok(new ApiResponse<>("success","Schedule Info", batchScheduleInfo));
	}
	
	
	@PutMapping("/secure/class/complete/{id}")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','PROGRAMHEAD','ADMIN','TRAINER')")
	public ResponseEntity<?> markClassAsComplete(@AuthenticationPrincipal CustomUserDetails userDetails,@PathVariable Integer id)
	{
		BatchTechTrainer batchScheduleInfo  = batchTechTrainerService.getById(id);
		
		Trainer trainer =null;
		
		if(userDetails.getTrainer()== null && userDetails.getProgramHead() ==null )
		{
			throw new BatchException("Unauthorized",HttpStatus.UNAUTHORIZED);
		}
		
		if(userDetails.getTrainer()!=null)
		{
			trainer = userDetails.getTrainer();
		}
		else {
			trainer = trainerService.getTrainer(userDetails.getProgramHead().getId());
		}
		
		
		
		if(batchScheduleInfo==null)
		{
			throw new BatchTechTrainerException("Class Not Found", null);
		}
		
		if(!batchScheduleInfo.getTrainer().getTrainerId().equals(trainer.getTrainerId()))
		{
			throw new BatchTechTrainerException("You are not authorized", null);
		}
		
		batchScheduleInfo.setCompleted(true);
		batchScheduleInfo.setStatus("Completed");
		batchScheduleInfo.setCompletedDate(LocalDate.now());
		batchTechTrainerService.update(batchScheduleInfo);
		return ResponseEntity.ok(new ApiResponse<>("success","Successfully marked as completed", null));
	}
	
	
	
	@PutMapping("/secure/schedule/update")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','PROGRAMHEAD')")
	public ResponseEntity<?> updateBatchScheduleInfo(@RequestBody List<UpdateScheduleDetails> updateScheduleDetails)
	{
		for(UpdateScheduleDetails  updateScheduleDetail :updateScheduleDetails)
		{
			BatchTechTrainer batchTechTrainer =  batchTechTrainerService.getById(updateScheduleDetail.getId());
			if(batchTechTrainer ==null)
			{
				throw new BatchTechTrainerException("Data not Found", HttpStatus.NOT_FOUND);
			}
			
			Trainer findTrainer = trainerService.getById(updateScheduleDetail.getTrainerId());
			if(findTrainer ==null)
			{
				throw new BatchTechTrainerException("Trainer not found", HttpStatus.NOT_FOUND);
			}
			
			boolean available = batchTechTrainerService.checkTrainerAvailabality(findTrainer.getTrainerId(), updateScheduleDetail.getStartTime(), updateScheduleDetail.getEndTime());
			
			if(!available)
			{
				throw new BatchTechTrainerException("Trainer Not Available", HttpStatus.NOT_FOUND);	
			}
			
			batchTechTrainer.setTrainer(findTrainer);
			batchTechTrainer.setStartTime(updateScheduleDetail.getStartTime());
			batchTechTrainer.setEndTime(updateScheduleDetail.getEndTime());
			if(batchTechTrainer.getStatus().toLowerCase().equals("not started"))
			{
				batchTechTrainer.setStatus("In Progress");
				batchTechTrainer.setStartDate(LocalDateTime.now());
			}
			batchTechTrainer = batchTechTrainerService.update(batchTechTrainer);
		}	
		return ResponseEntity.ok(new ApiResponse<>("success", "Batch Info Updated",null));
	}
	
	
	
	
	private String doubleTimeRangeToReadable(double start, double end) {
        return doubleTimeToString(start) + " - " + doubleTimeToString(end);
    }

	private String doubleTimeToString(double time) {
        int hours = (int) time;
        int minutes = (int) Math.round((time - hours) * 60);

        // Handle rounding up to 60 minutes
        if (minutes == 60) {
            hours += 1;
            minutes = 0;
        }

        String ampm = (hours >= 12) ? "PM" : "AM";
        int displayHours = hours % 12;
        if (displayHours == 0) displayHours = 12;

        return String.format("%02d:%02d %s", displayHours, minutes, ampm);
    }
	
	
}
