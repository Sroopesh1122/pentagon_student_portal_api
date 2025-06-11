package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Trainer;
@Repository
public interface TrainerRepository extends JpaRepository<Trainer, String> {

	Optional<Trainer> findByEmail(String email);

	@Query("SELECT t FROM Trainer t WHERE " + "(:stack IS NULL) AND "
			+ "(:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND "
			+ "(:trainerId IS NULL OR t.trainerId = :trainerId)")
	Page<Trainer> findByFilters(@Param("stack") String stack, @Param("name") String name,
			@Param("trainerId") String trainerId, Pageable pageable);

	@Query("SELECT COUNT(T) FROM Trainer T")
	int getTrainerCount();
	
	boolean existsByEmail(String email);
}
