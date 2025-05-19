package com.pentagon.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pentagon.app.entity.Student;

public interface StudentRepository extends JpaRepository<Student, Integer> {

	public Optional<Student> findByStudentId(String studentId);
	
	List<Student> findByStack(String stack);
	
	@Query("SELECT COUNT(s) FROM Student s WHERE s.stack = :stack AND FUNCTION('MONTH', s.createdAt) = :month AND FUNCTION('YEAR', s.createdAt) = :year")
	int countByCourseAndMonthYear( String stack, int month, int year);
}
