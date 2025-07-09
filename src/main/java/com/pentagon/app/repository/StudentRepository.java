package com.pentagon.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
	
	@Query("SELECT s FROM Student s " +
		       "WHERE (:q IS NULL OR s.name LIKE CONCAT( :q, '%') OR s.email LIKE CONCAT( :q, '%') OR s.studentId LIKE CONCAT(:q, '%')) " +
		       "AND (:batchId IS NULL OR s.batch.batchId = :batchId) " +
		       "AND (:stackId IS NULL OR s.stack.stackId = :stackId)")
		Page<Student> findStudents(@Param("q") String q, @Param("batchId") String batchId, @Param("stackId") String stackId, Pageable pageable);
	
	@Query("SELECT s.status, COUNT(s) FROM Student s " +
		       "WHERE (:batchId IS NULL OR s.batch.batchId = :batchId) " +
		       "AND (:stackId IS NULL OR s.stack.stackId = :stackId) GROUP BY s.status")
		List<Object[]> countStudentsByStatus(@Param("batchId") String batchId, @Param("stackId") String stackId);
	
	public Student findByPasswordResetToken(String token);
}
