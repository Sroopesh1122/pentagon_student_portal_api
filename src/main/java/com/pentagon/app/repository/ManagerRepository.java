package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.Trainer;
@Repository
public interface ManagerRepository extends JpaRepository<Manager, String> {

	Optional<Manager> findByEmail(String email);
	
	public Optional<Manager> findByManagerId(String managerId);

	@Query("SELECT COUNT(M) FROM Manager M")
	public int getManagerCount();
	
	@Query("SELECT m FROM Manager m WHERE ( :q IS NULL OR :q = '' OR m.name LIKE CONCAT(:q,'%') OR  m.email LIKE CONCAT(:q,'%')  OR  m.managerId LIKE CONCAT(:q,'%')) AND (:active IS NULL OR m.active = :active)")
    public	Page<Manager> findAll(@Param("q") String q,Boolean active,Pageable pageable);
	
	public Manager findByPasswordResetToken(String token);

}
