package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.MockRatings;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.entity.Student.EnrollmentStatus;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.exception.StudentException;
import com.pentagon.app.exception.TrainerException;
import com.pentagon.app.repository.BatchRepository;
import com.pentagon.app.repository.MockRatingsRepository;
import com.pentagon.app.repository.StudentRepository;
import com.pentagon.app.repository.TrainerRepository;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.request.TrainerLoginRequest;
import com.pentagon.app.response.ProfileResponse;
import com.pentagon.app.service.BatchService;
import com.pentagon.app.service.OtpService;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.service.TrainerService;

import jakarta.transaction.Transactional;

@Service
public class TrainerServiceImpl implements TrainerService {

	@Autowired
	private TrainerRepository trainerRepository;

	@Autowired
	private OtpService otpService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private MockRatingsRepository mockRatingsRepository;
	
	@Autowired
	private BatchService batchService;
	
	@Autowired
	private StudentService studentService;

	@Override
	public Trainer updateTrainer(Trainer trainer) {
		try {
			trainer.setUpdatedAt(LocalDateTime.now());
			return trainerRepository.save(trainer);

		} catch (Exception e) {
			throw new TrainerException("Failed to update Trainer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public String loginWithPassword(TrainerLoginRequest trainerLoginRequest) {
		Trainer trainer = trainerRepository.findByEmail(trainerLoginRequest.getEmail())
				.orElseThrow(() -> new TrainerException("Trainer not found", HttpStatus.NOT_FOUND));
		if (!passwordEncoder.matches(trainerLoginRequest.getPassword(), trainer.getPassword())) {
			throw new TrainerException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		String otp = otpService.generateOtpAndStore(trainerLoginRequest.getEmail());
		otpService.sendOtpToEmail(trainerLoginRequest.getEmail(), otp);

		return "OTP sent to registered email";

	}

	@Override
	public ProfileResponse getProfile(Trainer trainer) {
		ProfileResponse result = new ProfileResponse();
		result.setUniqueId(trainer.getTrainerId());
		result.setName(trainer.getName());
		result.setEmail(trainer.getEmail());
		result.setMobile(trainer.getMobile());
		return result;
	}

	@Override
	@Transactional
	public Trainer addTrainer(Trainer trainer) {
		// TODO Auto-generated method stub
		try {
			trainer.setCreatedAt(LocalDateTime.now());
			return trainerRepository.save(trainer);

		} catch (Exception e) {
			throw new TrainerException("Failed to Add Trainer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Page<Trainer> viewAllTrainers(String stack, String name, String trainerId, Pageable pageable) {
		try {
			return trainerRepository.findByFilters(stack, name, trainerId, pageable);
		} catch (Exception e) {
			throw new TrainerException("Failed to fetch trainers", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Trainer checkExistsByEmail(String email) {
		// TODO Auto-generated method stub
		Optional<Trainer> trainer = trainerRepository.findByEmail(email);
		if (trainer.isEmpty()) {
			return null;
		}
		return trainer.get();
	}

	@Override
	public Trainer getById(String tainerId) {
		return trainerRepository.findById(tainerId).orElse(null);
	}

	@Override
	public Trainer disableTrainerById(String Id) {
		// TODO Auto-generated method stub
		Trainer trainer = trainerRepository.findById(Id)
				.orElseThrow(() -> new TrainerException("Trainer not found with ID: " + Id, HttpStatus.NOT_FOUND));

		if (!trainer.isActive()) {
			throw new TrainerException("Trainer is already suspended", HttpStatus.BAD_REQUEST);
		}

		trainer.setActive(false);
		trainer.setUpdatedAt(LocalDateTime.now());
		return trainerRepository.save(trainer);
	}

	@Override
	public Page<Trainer> getAllTrainers(String programHeadId, String q, Pageable pageable) {
		// TODO Auto-generated method stub
		return trainerRepository.getAllTrainers(programHeadId, q, pageable);
	}

	@Override
	public void submitMockRating(Trainer trainer, Student student, Technology tech, double rating) {
		String trainerId = trainer.getTrainerId();
		String studentId = student.getStudentId();
		String techId = tech.getTechId();

		// Checking if the trainer is authorized to give mpckrating for this student's
		// tech
		boolean authorized = student.getBatch().getBatchTechTrainers().stream()
				.anyMatch(bt -> bt.getTrainer().getTrainerId().equals(trainerId)
						&& bt.getTechnology().getTechId().equals(techId));

		if (!authorized) {
			throw new TrainerException("Trainer not authorized to submit rating for this tech",
					HttpStatus.UNAUTHORIZED);
		}

		Optional<MockRatings> existingRating = mockRatingsRepository.findByStudentIdAndTrainerIdAndTechId(studentId,
				trainerId, techId);

		if (existingRating.isPresent()) {
			// Update existing rating
			MockRatings ratingEntity = existingRating.get();
			ratingEntity.setRating(rating);
			mockRatingsRepository.save(ratingEntity);
		} else {
			// Create new rating
			MockRatings newRating = new MockRatings();
			newRating.setStudentId(studentId);
			newRating.setTrainerId(trainerId);
			newRating.setTrachId(techId);
			newRating.setRating(rating);
			mockRatingsRepository.save(newRating);
		}
		// after this is that we need avg the rating and update in student entity or not
	}

	@Override
	public List<Student> getStudentsByBatch(String batchId, String trainerId) {
		 Optional<Batch> batch = batchService.getBatchById(batchId);
		 if (batch == null) {
			 throw new TrainerException("Batch not found", HttpStatus.NOT_FOUND);
		}

		boolean isTrainerMapped = batch.get().getBatchTechTrainers().stream()
				.anyMatch(bt -> bt.getTrainer().getTrainerId().equals(trainerId));

		if (!isTrainerMapped) {
			throw new TrainerException("Trainer not authorized for this batch", HttpStatus.UNAUTHORIZED);
		}

		return batch.get().getStudents(); // returns list of students in the batch
	}
	
	@Override
	public Student getStudentByIdIfTrainerAuthorized(String trainerId, String studentId) {
	    Student student = studentService.findByStudentId(studentId);
	    if (student == null) {
	        throw new TrainerException("Student not found", HttpStatus.NOT_FOUND);
	    }

	    Batch studentBatch = student.getBatch();
	    if (studentBatch == null) {
	        throw new TrainerException("Student not assigned to a batch", HttpStatus.BAD_REQUEST);
	    }

	    boolean isTrainerMapped = studentBatch.getBatchTechTrainers().stream()
	        .anyMatch(btt -> btt.getTrainer().getTrainerId().equals(trainerId));

	    if (!isTrainerMapped) {
	        throw new TrainerException("Trainer not authorized to view this student", HttpStatus.UNAUTHORIZED);
	    }

	    return student;
	}


}