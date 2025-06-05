package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pentagon.app.entity.Executive;

public interface ExecutiveRepository extends JpaRepository<Executive, Integer>{

	Optional<Executive> findByEmail(String email);
	
	public Optional<Executive> findByExecutiveId(String executiveId);

	@Query("SELECT COUNT(E) FROM Executive E")
	public int getExecutiveCount();
	
	@Query("SELECT e FROM Executive e WHERE "
		     + "(:q IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :q, '%'))"
		     + "OR LOWER(e.name) LIKE LOWER(CONCAT('%', :q, '%')) "
		     + "OR CAST(e.executiveId AS string) LIKE CONCAT('%', :q, '%') "
		     + "OR e.mobile LIKE CONCAT('%', :q, '%'))")
		Page<Executive> findAll(@Param("q") String q, Pageable pageable);


	boolean existsByEmail(String email);
	
	@Query("SELECT COUNT(e) FROM Executive e WHERE e.managerId = :managerId")
	public Long getTotalExecutiveCountByManagerId(@Param("managerId") String managerId);
	
	@Query("SELECT e FROM Executive e WHERE e.managerId = :managerId")
	public Page<Executive> getAllExecutives(@Param("managerId") String managerId , Pageable pageable);
	
	@Query("SELECT e FROM Executive e WHERE "
		     + "e.managerId = :managerId AND ("
		     + ":q IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :q, '%')) "
		     + "OR LOWER(e.name) LIKE LOWER(CONCAT('%', :q, '%')) "
		     + "OR CAST(e.executiveId AS string) LIKE CONCAT('%', :q, '%') "
		     + "OR e.mobile LIKE CONCAT('%', :q, '%'))")
		Page<Executive> findByManagerIdWithSearchQuery(@Param("managerId") String managerId,
		                                          @Param("q") String q,
		                                          Pageable pageable);

}
