package com.pentagon.app.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.StudentJdApplication;

@Repository
public interface StudentJdApplcationRepository extends JpaRepository<StudentJdApplication, String> {

	// Return List of applied candidates for specific JD with current status and
	// round
	@Query("""
		    SELECT DISTINCT s 
		    FROM StudentJdApplication s 
		    JOIN s.status h 
		    WHERE s.jobDescription.jobDescriptionId = :jdId 
		      AND (:status IS NULL OR h.status = :status)
		      AND (:round IS NULL OR h.round = :round)
		""")
		Page<StudentJdApplication> findAllByJd(
		    String jdId,
		    String round,
		    String status,
		    Pageable pageable
		);


	// Return List of applied candidates for specific student 
	@Query("SELECT s FROM StudentJdApplication s WHERE s.student.studentId =:studentId")
	public Page<StudentJdApplication> findAllByStudent(String studentId, Pageable pageable);
	
	@Query("SELECT COUNT(s) FROM StudentJdApplication s WHERE s.jobDescription.jobDescriptionId =:jdId")
	public Long countAplicationByJd(String jdId);
	
	@Query("SELECT COUNT(s) FROM StudentJdApplication s WHERE s.student.studentId =:studentId")
	public Long countOfApplicationByStudent(String studentId);
	
	@Query("SELECT COUNT(s) FROM StudentJdApplication s WHERE s.student.studentId = :studentId AND LOWER(s.currentStatus) = 'rejected'")
	public Long countRejectApplicationByStudent(String studentId);

	@Query("SELECT COUNT(s) FROM StudentJdApplication s WHERE s.student.studentId = :studentId AND LOWER(s.currentStatus) != 'rejected'")
	public Long countScheduledApplicationByStudent(String studentId);
	
	
	@Query("SELECT s FROM StudentJdApplication s WHERE s.student.studentId =:studentId AND s.jobDescription.jobDescriptionId =:jdId")
	public StudentJdApplication findByStudentAndJd(String studentId , String jdId);
	
	@Query("SELECT s.student FROM StudentJdApplication s JOIN s.status h WHERE s.jobDescription.jobDescriptionId =:jdId AND h.round =:round")
	public List<Student> getAllStudentAppliedForJd(String jdId,String round);
	
	@Query("SELECT DISTINCT a FROM StudentJdApplication a " +
		       "JOIN a.jobDescription jd " +
		       "JOIN jd.roundHistory rh " +
		       "WHERE a.student.studentId = :studentId " +
		       "AND a.currentStatus != :rejected " +
		       "AND rh.scheduleDate > :now")
		List<StudentJdApplication> getUpcomingJdRound(@Param("studentId") String studentId,
		                                              @Param("rejected") String rejected,
		                                              @Param("now") LocalDateTime now);
	

}
