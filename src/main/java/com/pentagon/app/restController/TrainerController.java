package com.pentagon.app.restController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.pentagon.app.Dto.TrainerDTO;
import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.mapper.TrainerMapper;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.ActivityLogService;
import com.pentagon.app.service.BatchTechTrainerService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.serviceImpl.CloudinaryServiceImp;

import jakarta.transaction.Transactional;


@RestController
@RequestMapping("/api/trainer")
public class TrainerController {
	
	@Autowired
	private TrainerService trainerService;
	
	
	@Autowired
	private ActivityLogService activityLogService;
	
	
	
	@Autowired
	private BatchTechTrainerService batchTechTrainerService;
	
	@Autowired
	private TrainerMapper trainerMapper;
	
	@Autowired
	private CloudinaryServiceImp cloudinaryService;
	
	
	@PutMapping("/secure/update")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> updateTrainer( 
			@AuthenticationPrincipal CustomUserDetails trainerDetails,
			@RequestParam(required = false) String name,
		    @RequestParam(required = false) String mobile,
		    @RequestParam(required = false) String qualification,
		    @RequestParam(required = false) Integer yearOfExperiences,
		    @RequestParam(required = false) String gender,
		    @RequestParam(required = false) String bio,
		    @RequestPart(required = false) MultipartFile profileFile,
		    @RequestParam(required=false) LocalDate dob
			){
		
		
		if(trainerDetails.getTrainer() == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		
		Trainer trainer = trainerDetails.getTrainer();
		
		if (trainer == null) {
            throw new TrainerException("Trainer not found", HttpStatus.NOT_FOUND);
        }

		if(name!=null)
		{
			trainer.setName(name);
		}
		if (mobile != null) {
	        trainer.setMobile(mobile);
	    }
		if (qualification != null) {
	        trainer.setQualification(qualification);
	    }
	    if (yearOfExperiences != null) {
	        trainer.setYearOfExperiences(yearOfExperiences);
	    }
	    if (gender != null) {
	        trainer.setGender(gender);
	    }
	    if (bio != null) {
	        trainer.setBio(bio);
	    }
	    if(dob!=null)
	    {
	    	trainer.setDob(dob);
	    }
	    
	    if(profileFile!=null)
	    {
	    	if(trainer.getProfilePublicId()!=null)
	    	{
	    		cloudinaryService.deleteFile(trainer.getProfilePublicId(), "image");
	    	}
	    	Map<String, Object>	uploadResponse  = cloudinaryService.uploadImage(profileFile);
			trainer.setProfilePublicId(uploadResponse.get("public_id").toString());
			trainer.setProfileImgUrl(uploadResponse.get("secure_url").toString());
	    	
	    }
	    
		 
		Trainer updatedTrainer = trainerService.updateTrainer(trainer);
		
		activityLogService.log(trainerDetails.getTrainer().getEmail(), 
				trainerDetails.getTrainer().getTrainerId(), 
				"TRAINER", 
				"Trainer with ID " + trainerDetails.getTrainer().getTrainerId() + "Updated his profile details");
        return  ResponseEntity.ok(new ApiResponse<>("success", "Profile Updated Successfully", null));
		
	}
	
	
	@Transactional
	@GetMapping("/secure/profile")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> getAdminProfile(@AuthenticationPrincipal CustomUserDetails trainerDetails) {
		if(trainerDetails.getTrainer() == null) {
			throw new TrainerException("UNAUTHORIZED", HttpStatus.UNAUTHORIZED);
		}
		Trainer trainer = trainerDetails.getTrainer();
	    
	    return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Profile", trainer));
	}
	
	
	@GetMapping("/public/trainer/{id}/schedule")
	@PreAuthorize("hasRole('TRAINER')")
	public ResponseEntity<?> getTrainerScheduleInfo(@AuthenticationPrincipal CustomUserDetails trainerDetails , @PathVariable String id) {
		
		
		Trainer findTrainer = trainerService.getById(id);
		
		if(findTrainer ==null)
		{
			throw new TrainerException("Trainer Not Found",HttpStatus.NOT_FOUND);
		}
		
		
		List<BatchTechTrainer> trainerScheduleInfo = batchTechTrainerService.getTrainerSchedule(id);
	    
	    return ResponseEntity.ok(new ApiResponse<>("success", "Trainer Profile", trainerScheduleInfo));
	}
	
	
	@GetMapping("/secure/all")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','ADMIN','PROGRAMHEAD')")
	public ResponseEntity<?> getAllTrainers(
			@AuthenticationPrincipal CustomUserDetails programHeadDetails,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int limit,
			@RequestParam(required = false) String q) {
		
		Pageable pageable = PageRequest.of(page, limit, Sort.by("createdAt").descending());

		
		//if we pass null then all trainers are fetched under every program Head
		Page<TrainerDTO> trainers = trainerService.getAllTrainers(null,q,
				pageable).map(trainer -> trainerMapper.toDTO(trainer));

		return ResponseEntity.ok(new ApiResponse<>("success", "Trainers data", trainers));
	}
	
	//individual trainers
	@Transactional
	@GetMapping("/secure/{id}")
	@PreAuthorize("hasAnyRole('STUDENTADMIN','ADMIN','PROGRAMHEAD')")
	public ResponseEntity<?> getTrainerById(@PathVariable String id){
		
		Trainer findTrainer = trainerService.getById(id);
		
		if(findTrainer == null)
		{
			throw new TrainerException("Trainer not found", HttpStatus.NOT_FOUND);
		}
	     
		TrainerDTO trainerDTO=  trainerMapper.toDTO(findTrainer);
	
		return ResponseEntity.ok(new ApiResponse<>("success", "Trainer data", trainerDTO));
	}
	
	

}