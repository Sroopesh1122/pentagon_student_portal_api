package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.StudentJdApplication;
import com.pentagon.app.repository.StudentJdApplcationRepository;
import com.pentagon.app.service.StudentJdApplicationService;

@Service
public class StudentJdApplicationServiceImpl implements StudentJdApplicationService {

	@Autowired
	private StudentJdApplcationRepository studentJdApplcationRepository;
	
	

	@Override
	public Page<StudentJdApplication> getAllAppliedJdForStudent(String stduentId,Pageable pageable) {
		return studentJdApplcationRepository.findAllByStudent(stduentId, pageable);
	}

	@Override
	public Long totalApplicationByJd(String jdId) {
		return studentJdApplcationRepository.countAplicationByJd(jdId);
	}


	@Override
	public StudentJdApplication create(StudentJdApplication studentJdApplication) {
		studentJdApplication.setAppliedAt(LocalDateTime.now());
		return studentJdApplcationRepository.save(studentJdApplication);
	}

	@Override
	public StudentJdApplication update(StudentJdApplication studentJdApplication) {
		studentJdApplication.setUpdatedAt(LocalDateTime.now());
		return studentJdApplcationRepository.save(studentJdApplication);
	}

	@Override
	public StudentJdApplication findByStudentAndJd(String studentId, String jdId) {
		return studentJdApplcationRepository.findByStudentAndJd(studentId, jdId);
	}

	@Override
	public StudentJdApplication findByApplicationId(String applicationId) {
		return studentJdApplcationRepository.findById(applicationId).orElse(null);
	}

	@Override
	public Page<StudentJdApplication> getAllStudentAppliedForJd(String jobId,String round,String status ,Pageable pageable) {
		return studentJdApplcationRepository.findAllByJd(jobId, round, status, pageable);
	}
	
	@Override
	public List<Student> getAllStudentForJd(String jdId, String round) {
		return studentJdApplcationRepository.getAllStudentAppliedForJd(jdId, round);
	}

	@Override
	public Map<String, Long> totalApplicationStatsByStuent(String studentId) {
		
		Map<String, Long> statsDetails = new HashMap<>();
		statsDetails.put("totalAppliedJd", studentJdApplcationRepository.countOfApplicationByStudent(studentId));
		statsDetails.put("totalShortlistedJd", studentJdApplcationRepository.countScheduledApplicationByStudent(studentId));
		statsDetails.put("totalRejectedJd", studentJdApplcationRepository.countRejectApplicationByStudent(studentId));
		return statsDetails;
	}

	
}
