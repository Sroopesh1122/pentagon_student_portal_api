package com.pentagon.app.restController;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.pentagon.app.Dto.MockRatingDTO;
import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.BatchTechTrainer;
import com.pentagon.app.entity.MockRating;
import com.pentagon.app.entity.MockTest;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Technology;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.exception.InvalidDataException;
import com.pentagon.app.exception.MockException;
import com.pentagon.app.mapper.MockTestMapper;
import com.pentagon.app.request.CreateMockTestRequest;
import com.pentagon.app.request.UpdateMockRatingRequest;
import com.pentagon.app.response.ApiResponse;
import com.pentagon.app.service.BatchService;
import com.pentagon.app.service.BatchTechTrainerService;
import com.pentagon.app.service.CustomUserDetails;
import com.pentagon.app.service.MockTestService;
import com.pentagon.app.service.StudentService;
import com.pentagon.app.service.TechnologyService;
import com.pentagon.app.service.TrainerService;
import com.pentagon.app.utils.IdGeneration;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/mock-test")
public class MockTestController {

	@Autowired
	private MockTestService mockTestService;

	@Autowired
	private IdGeneration idGeneration;

	@Autowired
	private BatchTechTrainerService batchTechTrainerService;

	@Autowired
	private BatchService batchService;

	@Autowired
	private TechnologyService technologyService;

	@Autowired
	private TrainerService trainerService;

	@Autowired
	private StudentService studentService;
	
	@Autowired
	private MockTestMapper mockTestMapper;

	@PostMapping("/secure/create")
	public ResponseEntity<?> createMockTest(@Valid @RequestBody CreateMockTestRequest request,
			BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			throw new InvalidDataException("Invalid Inputs", HttpStatus.BAD_REQUEST);
		}

		Batch findBatch = batchService.getBatchById(request.getBatchId()).orElse(null);
		Technology findTechnology = technologyService.getTechnologyById(request.getTechId()).orElse(null);
		Trainer findTrainer = trainerService.getById(request.getTrainerId());

		if (findBatch == null) {
			throw new MockException("Batch Not found", HttpStatus.NOT_FOUND);
		}

		if (findTechnology == null) {
			throw new MockException("Technology Not Found", HttpStatus.NOT_FOUND);
		}

		if (findTrainer == null) {
			throw new MockException("Trainer Not Found", HttpStatus.NOT_FOUND);
		}

		BatchTechTrainer batchTechTrainer = batchTechTrainerService.findByBatchTechnology(request.getBatchId(),
				request.getTechId());

		if (batchTechTrainer == null) {
			throw new MockException("Data Not Found", HttpStatus.NOT_FOUND);
		}

		if (batchTechTrainer.getStatus().toLowerCase().equals("not started")) {
			throw new MockException("Class is not Started yet", HttpStatus.BAD_REQUEST);
		}

		if (!request.getTrainerId().equals(batchTechTrainer.getTrainer().getTrainerId())) {
			throw new MockException("Only assigned trainer can able to create test", HttpStatus.BAD_REQUEST);
		}

		MockTest mockTest = mockTestService.findByNameAndTechnology(request.getMockName(),request.getTechId());

		if (mockTest != null) {
			throw new MockException("Mock Test Already Exists", HttpStatus.BAD_REQUEST);
		}

		MockTest createTest = new MockTest();
		createTest.setId(idGeneration.generateRandomString());
		createTest.setBatch(findBatch);
		createTest.setTechnology(findTechnology);
		createTest.setTrainer(findTrainer);
		createTest.setMockDate(request.getMockDate());
		createTest.setTopic(request.getTopic());
		createTest.setMockName(request.getMockName());
		mockTestService.createMockTest(createTest);
		return ResponseEntity.ok(new ApiResponse<>("success", "Mock Test Created Successfully", null));

	}

	@GetMapping("/secure/test/batch-technology")
	public ResponseEntity<?> getMockTestsByBatchAndTechnology(@RequestParam String batchId,
			@RequestParam String techId) {
		List<MockTest> mockTests = mockTestService.findMockTest(batchId, techId, null);

		return ResponseEntity.ok(new ApiResponse<>("success", "Mock Test Data", mockTests));

	}

	@GetMapping("/secure/test/{id}")
	public ResponseEntity<?> getMockTestsById(@PathVariable String id) {
		MockTest mockTest = mockTestService.findMockTestById(id);

		if (mockTest == null) {
			throw new MockException("Test Not Found", HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(new ApiResponse<>("success", "Mock Test Data", mockTest));

	}

	@GetMapping("/secure/test/{id}/ratings")
	public ResponseEntity<?> getMockRatingsOfMockTest(@PathVariable String id) {
		MockTest mockTest = mockTestService.findMockTestById(id);

		if (mockTest == null) {
			throw new MockException("Test Not Found", HttpStatus.NOT_FOUND);
		}

		List<MockRating> mockRatings = mockTestService.findMockRatingByMockTest(id);

		return ResponseEntity.ok(new ApiResponse<>("success", "Mock Rating Data", mockRatings));

	}

	@PostMapping("/secure/test/{id}/create-mock-rating-sheet")
	public ResponseEntity<?> getCreateMockRatingsSheet(@PathVariable String id) {
		MockTest mockTest = mockTestService.findMockTestById(id);

		if (mockTest == null) {
			throw new MockException("Test Not Found", HttpStatus.NOT_FOUND);
		}

		Batch batch = mockTest.getBatch();

		List<Student> batchStudents = studentService.findByBatch(batch.getBatchId());
		List<MockRating> mockRatings = new ArrayList<>();

		for (Student student : batchStudents) {

			MockRating mockRating = new MockRating();
			mockRating.setId(idGeneration.generateRandomString());
			mockRating.setStudent(student);
			mockRating.setMockTest(mockTest);
			mockRatings.add(mockRating);
		}

		mockTestService.createMockRating(mockRatings);

		return ResponseEntity.ok(new ApiResponse<>("success", "Mock Rating Sheet created Successfully", null));

	}

	@PutMapping("/secure/test/{testId}/update/mock-rating-sheet")
	public ResponseEntity<?> getUpdatesMockRatingsSheet(
			@AuthenticationPrincipal CustomUserDetails customUserDetails,
			@PathVariable String testId,
			@RequestBody List<UpdateMockRatingRequest> requests) {
		Trainer trainer = customUserDetails.getTrainer();

		if (trainer == null) {
			throw new MockException("Unauthorized", HttpStatus.UNAUTHORIZED);
		}
		MockTest findMockTest = mockTestService.findMockTestById(testId);

		if (findMockTest == null) {
			throw new MockException("Test Not Found", HttpStatus.NOT_FOUND);
		}
		
		if(!trainer.getTrainerId().equals(findMockTest.getTrainer().getTrainerId()))
		{
			throw new MockException("Your are not authorized to access the sheet",HttpStatus.BAD_REQUEST);
		}
		List<MockRating> mockRatings = new ArrayList<>();
		for (UpdateMockRatingRequest updateRequest : requests) {
			if (updateRequest.isAttendance()) {
				MockRating mockRating = mockTestService.findMockRatingById(updateRequest.getMockRatingId());
				if (mockRating != null) {
					mockRating.setAttendance(updateRequest.isAttendance());
					mockRating.setRating(updateRequest.getRating());
					mockRating.setRemark(updateRequest.getRemark());
					mockRatings.add(mockRating);
				}
			}

		}
		mockTestService.updateMockRatings(mockRatings);
		return ResponseEntity.ok(new ApiResponse<>("success", "Mock Rating updated successfully", null));
	}
	
	
	@GetMapping("/secure/student/mock-ratings")
	public ResponseEntity<?> getMockRatingOfStudent(@AuthenticationPrincipal CustomUserDetails customUserDetails)
	{
		Student student = customUserDetails.getStudent();
		
		if(student ==null)
		{
			throw new MockException("Unthorized", HttpStatus.BAD_REQUEST);
		}
		
		List<MockRatingDTO> mockRatings = mockTestService.findMockRating(student.getStudentId()).stream().map(data->mockTestMapper.toDTO(data)).toList();
		
		return ResponseEntity.ok(new ApiResponse<>("success","Mock Rating Results",  mockRatings));
			
	}

}
