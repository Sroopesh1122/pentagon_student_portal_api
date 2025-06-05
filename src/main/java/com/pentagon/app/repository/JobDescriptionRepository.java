package com.pentagon.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pentagon.app.entity.JobDescription;

public interface JobDescriptionRepository extends JpaRepository<JobDescription, Integer> {

	Optional<JobDescription> findByJobDescriptionId(String jobDescriptionId);

	public List<JobDescription> findByStack(String stack);

	// If stauts is null return all Jds , if there is value then return the count of
	// taht specific jd
	@Query("SELECT COUNT(jd) FROM JobDescription jd WHERE jd.managerId = :managerId AND (:status IS NULL OR jd.jdStatus = :status)")
	public Long managerTotalJdCount(@Param("managerId") String managerId, @Param("status") String status);
	
	
	
	@Query("SELECT COUNT(jd) FROM JobDescription jd WHERE jd.postedBy = :executiveId AND (:status IS NULL OR jd.jdStatus = :status)")
	public Long executiveTotalJdCount(@Param("executiveId") String executiveId, @Param("status") String status);

	
	
	@Query(value = "SELECT COUNT(*) FROM job_description jd WHERE jd.manager_id = :managerId AND DATE(jd.created_at) = :date", nativeQuery = true)
	public Long countJdsByManagerAndDate(@Param("managerId") String managerId, @Param("date") String date);

	@Query(value = """
			SELECT * FROM job_description jd
			WHERE (
			    (:companyName IS NULL OR :companyName = '' OR jd.role LIKE CONCAT( :role, '%') OR  jd.company_name LIKE CONCAT('%', :companyName, '%'))
			    AND (:stackRegex IS NULL OR :stackRegex = '' OR jd.stack REGEXP :stackRegex)
			    AND (:isClosed IS NULL OR jd.jd_closed = :isClosed)
			    AND (:minYearOfPassing IS NULL OR jd.min_year_of_passing = :minYearOfPassing)
			    AND (:maxYearOfPassing IS NULL OR jd.max_year_of_passing = :maxYearOfPassing)
			    AND (:qualificationRegex IS NULL OR :qualificationRegex = '' OR jd.qualification REGEXP :qualificationRegex)
			    AND (:streamRegex IS NULL OR :streamRegex = '' OR jd.stream REGEXP :streamRegex)
			    AND (:percentage IS NULL OR jd.percentage >= :percentage)
			    AND (:executiveId IS NULL OR :executiveId = '' OR jd.posted_by = :executiveId)
			    AND (:status IS NULL OR :status = '' OR jd.jd_status = :status)
			    AND (:startDate IS NULL OR jd.created_at >= :startDate)
			    AND (:endDate IS NULL OR jd.created_at <= :endDate)
			)
			""", countQuery = """
			SELECT COUNT(*) FROM job_description jd
			WHERE (
			    (:companyName IS NULL OR :companyName = '' OR jd.role LIKE CONCAT( :role, '%') OR  jd.company_name LIKE CONCAT('%', :companyName, '%'))
			    AND (:stackRegex IS NULL OR :stackRegex = '' OR jd.stack REGEXP :stackRegex)
			    AND (:role IS NULL OR :role = '' OR jd.role LIKE CONCAT( :role, '%'))
			    AND (:isClosed IS NULL OR jd.jd_closed = :isClosed)
			    AND (:minYearOfPassing IS NULL OR jd.min_year_of_passing = :minYearOfPassing)
			    AND (:maxYearOfPassing IS NULL OR jd.max_year_of_passing = :maxYearOfPassing)
			    AND (:qualificationRegex IS NULL OR :qualificationRegex = '' OR jd.qualification REGEXP :qualificationRegex)
			    AND (:streamRegex IS NULL OR :streamRegex = '' OR jd.stream REGEXP :streamRegex)
			    AND (:percentage IS NULL OR jd.percentage >= :percentage)
			    AND (:executiveId IS NULL OR :executiveId = '' OR jd.posted_by = :executiveId)
			    AND (:status IS NULL OR :status = '' OR jd.jd_status = :status)
			    AND (:startDate IS NULL OR jd.created_at >= :startDate)
			    AND (:endDate IS NULL OR jd.created_at <= :endDate)
			)
			""", nativeQuery = true)
	Page<JobDescription> findWithFiltersUsingRegex(@Param("companyName") String companyName,
			@Param("stackRegex") String stackRegex, 
			@Param("role") String role,
			@Param("isClosed") Boolean isClosed,
			@Param("minYearOfPassing") Integer minYearOfPassing, 
			@Param("maxYearOfPassing") Integer maxYearOfPassing,
			@Param("qualificationRegex") String qualificationRegex, 
			@Param("streamRegex") String streamRegex,
			@Param("percentage") Double percentage, 
			@Param("executiveId") String executiveId,
			@Param("status") String status,
			@Param("startDate") String startDate,
			@Param("endDate") String endDate,
			Pageable pageable);
	
	@Query("SELECT COUNT(j) FROM JobDescription j WHERE j.executive.executiveId = :executiveId")
	int countTotalJDsByExecutive(@Param("executiveId") String executiveId);

	@Query("SELECT COUNT(j) FROM JobDescription j WHERE j.executive.executiveId = :executiveId AND j.isClosed = false")
	int countOpeningsByExecutive(@Param("executiveId") String executiveId);

	@Query("SELECT COUNT(j) FROM JobDescription j WHERE j.executive.executiveId = :executiveId AND j.isClosed = true")
	int countClosuresByExecutive(@Param("executiveId") String executiveId);

}
