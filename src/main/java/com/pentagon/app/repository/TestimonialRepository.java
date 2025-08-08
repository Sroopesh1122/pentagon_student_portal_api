package com.pentagon.app.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pentagon.app.entity.Testimonials;

public interface TestimonialRepository extends JpaRepository<Testimonials, String> {
	
	@Query("SELECT t FROM Testimonials t WHERE (:name IS NULL OR t.studentName LIKE %:name%) AND (:passingYear IS NULL OR t.yearOfPassing = :passingYear) AND (:ctc IS NULL OR t.ctc >= :ctc)")
	Page<Testimonials> findByFilters(@Param("name") String name, @Param("passingYear") Integer passingYear, @Param("ctc") Double ctc,Pageable pageable);
	
	
	Testimonials findByPentagonId(String pentagonId);

}
