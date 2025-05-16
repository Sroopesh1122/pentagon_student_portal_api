package com.pentagon.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.entity.Student;
import com.pentagon.entity.Student.EnrollmentStatus;
import com.pentagon.entity.Student.EnrollmentStatus;
import com.pentagon.entity.Trainer;
import com.pentagon.exception.StudentException;
import com.pentagon.exception.TrainerException;
import com.pentagon.repository.StudentRepository;
import com.pentagon.repository.TrainerRepository;
import com.pentagon.service.TrainerService;

@Service
public class TrainerServiceImpl implements TrainerService {

	@Autowired
	private TrainerRepository trainerRepository;
	
	@Autowired
	private StudentRepository studentRepository;
	
	
	@Override
	public boolean updateTrainer(Trainer trainer) {
		// TODO Auto-generated method stub
		try {
			trainer.setUpdatedAt(LocalDateTime.now());
			trainerRepository.save(trainer);
			return true;
		}
		catch (Exception e) {
	        throw new TrainerException("Failed to update Trainer: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public boolean addStudent(Student student) {
		// TODO Auto-generated method stub
		try {
			student.setCreatedAt(LocalDateTime.now());
			studentRepository.save(student);
			return true;
		}
		catch (Exception e) {
	        throw new StudentException("Failed to add Student: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public List<Student> viewStudentsBasedOnStack(String stack) {
		// TODO Auto-generated method stub
		try {
			return studentRepository.findByStack(stack);
		}
		catch(Exception e) {
			throw new StudentException("No students found from " + stack , HttpStatus.NOT_FOUND);
		}
	}

	@Override
	public boolean addMockRating(String studentId, Double mockRating) {
		// TODO Auto-generated method stub
		Student student = studentRepository.findByStudentId(studentId)
           .orElseThrow(()-> new StudentException("Student not found with id: " + studentId, HttpStatus.NOT_FOUND));	
		
		student.setMockRating(mockRating);
		student.setUpdatedAt(LocalDateTime.now());
		
		try {
	        studentRepository.save(student);
	        return true;
	    } catch (Exception e) {
	        throw new StudentException("Failed to update mock rating: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

	@Override
	public void disableStudentByUniqueId(String studentId) {
		// TODO Auto-generated method stub
		Student student = studentRepository.findByStudentId(studentId)
				.orElseThrow(()->new StudentException("Student not found with id: " + studentId, HttpStatus.NOT_FOUND));
		
		if (student.getStatus() == EnrollmentStatus.DISABLED) {
	        throw new StudentException("Student is already disabled", HttpStatus.CONFLICT);
	    }
		
		student.setStatus(EnrollmentStatus.DISABLED);
		student.setUpdatedAt(LocalDateTime.now());
		studentRepository.save(student);
	}

}
