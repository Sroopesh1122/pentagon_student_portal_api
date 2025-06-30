package com.pentagon.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.StudentJdApplication;

@Repository
public interface StudentJdApplcationRepository extends JpaRepository<StudentJdApplication, String> {

	// Return List of applied candidates for specific JD with current status and
	// round
	@Query("SELECT s FROM StudentJdApplication s WHERE s.jobDescription.jobDescriptionId =:jdId AND ( :status is NULL OR s.currentStatus =:status ) AND (:round is NULL OR s.currentRound =:round)")
	public Page<StudentJdApplication> findAllByJd(String jdId, String round, String status, Pageable pageable);

	// Return List of applied candidates for specific student 
	@Query("SELECT s FROM StudentJdApplication s WHERE s.student.studentId =:studentId")
	public Page<StudentJdApplication> findAllByStudent(String studentId, Pageable pageable);
	
	@Query("SELECT COUNT(s) FROM StudentJdApplication s WHERE s.jobDescription.jobDescriptionId =:jdId")
	public Long countAplicationByJd(String jdId);
	
	@Query("SELECT COUNT(s) FROM StudentJdApplication s WHERE s.student.studentId =:studentId")
	public Long countAplicationByStudent(String studentId);
	
	
	@Query("SELECT s FROM StudentJdApplication s WHERE s.student.studentId =:studentId AND s.jobDescription.jobDescriptionId =:jdId")
	public StudentJdApplication findByStudentAndJd(String studentId , String jdId);
	
	
	
	

}
