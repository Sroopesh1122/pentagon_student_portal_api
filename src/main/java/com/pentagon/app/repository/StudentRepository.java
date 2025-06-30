package com.pentagon.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Student;
@Repository
public interface StudentRepository extends JpaRepository<Student, String> {

	public Optional<Student> findByStudentId(String studentId);
	
	@Query("SELECT s FROM Student s WHERE s.stack.stackId = :stackId")
	public List<Student> findByStack(String stackId);
	
	@Query("SELECT COUNT(s) FROM Student s WHERE s.stack.stackId = :stack AND FUNCTION('MONTH', s.createdAt) = :month AND FUNCTION('YEAR', s.createdAt) = :year")
	public int countByCourseAndMonthYear( String stack, int month, int year);

	public Optional<Student> findByEmail(String email);
	
	
	public Student findByPasswordResetToken(String token);
}
