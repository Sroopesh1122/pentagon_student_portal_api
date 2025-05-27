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
	int getExecutiveCount();
	
	@Query("SELECT e FROM Executive e WHERE "
		     + "(:email IS NULL OR LOWER(e.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND "
		     + "(:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
		     + "(:executiveId IS NULL OR e.executiveId = :executiveId) AND "
		     + "(:mobile IS NULL OR e.mobile = :mobile)")
	Page<Executive> findByFilters(@Param("name") String name,
		                          @Param("mobile") String mobile,
		                          @Param("email") String email,
		                          @Param("executiveId") String executiveId,
		                          Pageable pageable);

}
