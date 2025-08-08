package com.pentagon.app.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.JobDescription;

@Repository
public interface JobDescriptionRepository extends JpaRepository<JobDescription, String> {

	public Optional<JobDescription> findByJobDescriptionId(String jobDescriptionId);

	@Query("SELECT jd FROM JobDescription jd WHERE jd.jdStack.stackId= :stack")
	public List<JobDescription> findByStackId(String stack);

	// If stauts is null return all Jds , if there is value then return the count of
	// that specific jd
	@Query("SELECT COUNT(jd) FROM JobDescription jd WHERE jd.managerId = :managerId AND (:status IS NULL OR jd.jdStatus = :status)")
	public Long managerTotalJdCount(@Param("managerId") String managerId, @Param("status") String status);

	@Query("SELECT COUNT(jd) FROM JobDescription jd WHERE jd.postedBy = :executiveId AND (:status IS NULL OR jd.jdStatus = :status)")
	public Long executiveTotalJdCount(@Param("executiveId") String executiveId, @Param("status") String status);

	@Query(value = "SELECT COUNT(*) FROM job_description jd WHERE jd.manager_id = :managerId AND DATE(jd.created_at) = :date", nativeQuery = true)
	public Long countJdsByManagerAndDate(@Param("managerId") String managerId, @Param("date") String date);

	@Query("SELECT COUNT(j) FROM JobDescription j WHERE j.executive.executiveId = :executiveId")
	int countTotalJDsByExecutive(@Param("executiveId") String executiveId);

	@Query(value = """
		    SELECT * FROM job_description jd
		    WHERE (
		        (:companyName IS NULL OR :companyName = '' OR jd.role LIKE CONCAT( :role, '%') OR  jd.company_name LIKE CONCAT('%', :companyName, '%'))
		        AND (:stackRegex = '' OR jd.stack_id IS NULL OR jd.stack_id REGEXP :stackRegex)
		        AND (:isClosed IS NULL OR jd.jd_closed = :isClosed)
		        AND (:minYearOfPassing IS NULL OR jd.min_year_of_passing <= :minYearOfPassing)
		        AND (:maxYearOfPassing IS NULL OR jd.max_year_of_passing >= :maxYearOfPassing)
		        AND (:qualificationRegex IS NULL OR :qualificationRegex = '' OR jd.qualification REGEXP :qualificationRegex)
		        AND (:streamRegex IS NULL OR :streamRegex = '' OR jd.stream REGEXP :streamRegex)
		        AND (:percentage IS NULL OR jd.percentage >= :percentage)
		        AND (:executiveId IS NULL OR :executiveId = '' OR jd.posted_by = :executiveId)
		        AND (:managerId IS NULL OR :managerId = '' OR jd.manager_id = :managerId)
		        AND (:status IS NULL OR :status = '' OR jd.jd_status = :status)
		        AND (:startDate IS NULL OR DATE(jd.created_at) >= DATE(:startDate))
		        AND (:endDate IS NULL OR DATE(jd.created_at) <= DATE(:endDate))
		    )
		    """, 
		    countQuery = """
		    SELECT COUNT(*) FROM job_description jd
		    WHERE (
		        (:companyName IS NULL OR :companyName = '' OR jd.role LIKE CONCAT( :role, '%') OR  jd.company_name LIKE CONCAT('%', :companyName, '%'))
		        AND (:stackRegex ='' OR jd.stack_id IS NULL  OR jd.stack_id REGEXP :stackRegex)
		        AND (:role IS NULL OR :role = '' OR jd.role LIKE CONCAT( :role, '%'))
		        AND (:isClosed IS NULL OR jd.jd_closed = :isClosed)
		        AND (:minYearOfPassing IS NULL OR jd.min_year_of_passing <= :minYearOfPassing)
		        AND (:maxYearOfPassing IS NULL OR jd.max_year_of_passing >= :maxYearOfPassing)
		        AND (:qualificationRegex IS NULL OR :qualificationRegex = '' OR jd.qualification REGEXP :qualificationRegex)
		        AND (:streamRegex IS NULL OR :streamRegex = '' OR jd.stream REGEXP :streamRegex)
		        AND (:percentage IS NULL OR jd.percentage >= :percentage)
		        AND (:executiveId IS NULL OR :executiveId = '' OR jd.posted_by = :executiveId)
		        AND (:managerId IS NULL OR :managerId = '' OR jd.manager_id = :managerId)
		        AND (:status IS NULL OR :status = '' OR jd.jd_status = :status)
		        AND (:startDate IS NULL OR DATE(jd.created_at) >= DATE(:startDate))
		        AND (:endDate IS NULL OR DATE(jd.created_at) <= DATE(:endDate))
		    )
		    """, 
		    nativeQuery = true)
		public Page<JobDescription> findWithFiltersUsingRegex(
		    @Param("companyName") String companyName,
		    @Param("stackRegex") String stackRegex, 
		    @Param("role") String role, 
		    @Param("isClosed") Boolean isClosed,
		    @Param("minYearOfPassing") Integer minYearOfPassing, 
		    @Param("maxYearOfPassing") Integer maxYearOfPassing,
		    @Param("qualificationRegex") String qualificationRegex, 
		    @Param("streamRegex") String streamRegex,
		    @Param("percentage") Double percentage, 
		    @Param("executiveId") String executiveId,
		    @Param("managerId") String managerId, 
		    @Param("status") String status, 
		    @Param("startDate") String startDate,
		    @Param("endDate") String endDate, 
		    Pageable pageable);
	
	
	@Query("SELECT jd FROM JobDescription jd WHERE jd.postedBy = :executiveId")
	public Page<JobDescription> getRecentJd(String executiveId,Pageable pageable);
	
	
	@Query(value = """
		    SELECT * FROM job_description jd
		    WHERE (
		        (:companyName IS NULL OR :companyName = '' OR jd.role LIKE CONCAT( :role, '%') OR  jd.company_name LIKE CONCAT('%', :companyName, '%'))
		        AND (:stackRegex ='' OR jd.stack_id IS NULL OR jd.stack_id REGEXP :stackRegex)
		        AND (:minYearOfPassing IS NULL OR jd.min_year_of_passing <= :minYearOfPassing)
		        AND (:maxYearOfPassing IS NULL OR jd.max_year_of_passing >= :maxYearOfPassing)
		        AND (:qualificationRegex IS NULL OR :qualificationRegex = '' OR jd.qualification REGEXP :qualificationRegex)
		        AND (:streamRegex IS NULL OR :streamRegex = '' OR jd.stream REGEXP :streamRegex)
		        AND (:percentage IS NULL OR jd.percentage <= :percentage)
		        AND (jd.jd_status = 'approved' OR jd.jd_status = 'closed')
		        AND (:startDate IS NULL OR DATE(jd.created_at) >= DATE(:startDate))
		        AND (:endDate IS NULL OR DATE(jd.created_at) <= DATE(:endDate))
		    )
		    """, 
		    countQuery = """
		    SELECT COUNT(*) FROM job_description jd
		    WHERE (
		        (:companyName IS NULL OR :companyName = '' OR jd.role LIKE CONCAT( :role, '%') OR  jd.company_name LIKE CONCAT('%', :companyName, '%'))
		        AND (:stackRegex ='' OR jd.stack_id IS NULL  OR jd.stack_id REGEXP :stackRegex)
		        AND (:role IS NULL OR :role = '' OR jd.role LIKE CONCAT( :role, '%'))
		        AND (:minYearOfPassing IS NULL OR jd.min_year_of_passing <= :minYearOfPassing)
		        AND (:maxYearOfPassing IS NULL OR jd.max_year_of_passing >= :maxYearOfPassing)
		        AND (:qualificationRegex IS NULL OR :qualificationRegex = '' OR jd.qualification REGEXP :qualificationRegex)
		        AND (:streamRegex IS NULL OR :streamRegex = '' OR jd.stream REGEXP :streamRegex)
		        AND (:percentage IS NULL OR jd.percentage <= :percentage)
		        AND (jd.jd_status = 'approved' OR jd.jd_status = 'closed')
		        AND (:startDate IS NULL OR DATE(jd.created_at) >= DATE(:startDate))
		        AND (:endDate IS NULL OR DATE(jd.created_at) <= DATE(:endDate))
		    )
		    """, 
		    nativeQuery = true)
		public Page<JobDescription> findJdForStudent(
		    @Param("companyName") String companyName,
		    @Param("stackRegex") String stackRegex, 
		    @Param("role") String role, 
		    @Param("minYearOfPassing") Integer minYearOfPassing, 
		    @Param("maxYearOfPassing") Integer maxYearOfPassing,
		    @Param("qualificationRegex") String qualificationRegex, 
		    @Param("streamRegex") String streamRegex,
		    @Param("percentage") Double percentage,
		    @Param("startDate") String startDate,
		    @Param("endDate") String endDate, 
		    Pageable pageable);
	
	
	

	@Query(value = "SELECT DATE_FORMAT(created_at, :format) AS label, COUNT(*) AS count FROM job_description WHERE created_at >= :startDate GROUP BY label ORDER BY label", nativeQuery = true)
	public List<Object[]> getJdStats(@Param("format") String format, @Param("startDate") String startDate);

	@Query(value = "SELECT DATE_FORMAT(created_at, :format) AS label, COUNT(*) AS count FROM job_description WHERE created_at >= :startDate AND manager_id= :managerId GROUP BY label ORDER BY label", nativeQuery = true)
	public List<Object[]> getJdStatsOfManager(@Param("managerId") String managerId, @Param("format") String format,
			@Param("startDate") String startDate);

	@Query(value = "SELECT DATE_FORMAT(created_at, :format) AS label, COUNT(*) AS count FROM job_description WHERE created_at >= :startDate AND posted_by= :executiveId GROUP BY label ORDER BY label", nativeQuery = true)
	public List<Object[]> getJdStatsOfExecutive(@Param("executiveId") String executiveId,
			@Param("format") String format, @Param("startDate") String startDate);

	@Query(value = "SELECT COUNT(jd) from JobDescription jd WHERE jd.postedBy = :executiveId")
	public Long getTotalJdCountByExecutive(String executiveId);

	@Query(value = "SELECT COUNT(jd) from JobDescription jd WHERE jd.managerId = :managerId")
	public Long getTotalJdCountByManager(String managerId);
	
	
	@Query(value = "SELECT COUNT(jd.numberOfClosures) from JobDescription jd WHERE jd.managerId = :managerId")
	public Long getTotalJdClosureByManager(String managerId);

	// total jd which are closed
	@Query("SELECT COUNT(j) FROM JobDescription j WHERE j.executive.executiveId = :executiveId AND j.isClosed = false")
	int countOpeningsByExecutive(@Param("executiveId") String executiveId);

	// total jd which are not closed and still processing
	@Query("SELECT COUNT(j) FROM JobDescription j WHERE j.executive.executiveId = :executiveId AND j.isClosed = true")
	int countClosuresByExecutive(@Param("executiveId") String executiveId);
	
	
	@Query(value = "SELECT SUM(jd.numberOfClosures) from JobDescription jd")
	public Long getTotalClosureCount();

	@Query(value = "SELECT SUM(jd.numberOfClosures) from JobDescription jd WHERE jd.postedBy = :executiveId")
	public Long getTotalClosureCountByExecutiveId(String executiveId);
	
	
	@Query("SELECT SUM(jd.numberOfClosures) FROM JobDescription jd " +
		       "WHERE jd.postedBy = :executiveId " +
		       "AND jd.approvedDate BETWEEN :startDate AND :endDate")
		Long getTotalClosureCountByExecutiveIdAndDateRange(
		    @Param("executiveId") String executiveId,
		    @Param("startDate") LocalDateTime startDate,
		    @Param("endDate") LocalDateTime endDate
		);


	@Query(value = "SELECT SUM(jd.numberOfClosures) from JobDescription jd WHERE jd.managerId = :managerId")
	public Long getTotalClosureCountByManagerId(String managerId);

	@Query(value = "SELECT DATE_FORMAT(created_at, :format) AS label, COUNT(*) AS count , SUM(number_of_closures) FROM job_description WHERE created_at >= :startDate GROUP BY label ORDER BY label", nativeQuery = true)
	public List<Object[]> getJdVsClosureStats(@Param("format") String format, @Param("startDate") String startDate);
	
	
	@Query("SELECT jd FROM JobDescription jd WHERE jd.managerId = :managerId")
	public Page<JobDescription> getJdOfManager(String managerId,Pageable pageable);
	
	
	@Query("SELECT jd FROM JobDescription jd WHERE jd.postedBy = :executiveId")
	public Page<JobDescription> getJdOfExecutive(String executiveId ,Pageable pageable);
	


}
