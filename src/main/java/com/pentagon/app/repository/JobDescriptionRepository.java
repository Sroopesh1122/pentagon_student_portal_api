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

	List<JobDescription> findByStack(String stack);

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
			)
			""", nativeQuery = true)
	Page<JobDescription> findWithFiltersUsingRegex(@Param("companyName") String companyName,
			@Param("stackRegex") String stackRegex, @Param("role") String role, @Param("isClosed") Boolean isClosed,
			@Param("minYearOfPassing") Integer minYearOfPassing, @Param("maxYearOfPassing") Integer maxYearOfPassing,
			@Param("qualificationRegex") String qualificationRegex, @Param("streamRegex") String streamRegex,
			@Param("percentage") Double percentage, @Param("executiveId") String executiveId,
			@Param("status") String status, Pageable pageable);

}
