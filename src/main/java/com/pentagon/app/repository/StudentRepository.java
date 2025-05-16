package com.pentagon.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.app.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	public Optional<Student> findByStudentId(String studentId);
	
	List<Student> findByStack(String stack);
}
