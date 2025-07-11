package com.pentagon.app.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.ProgramHead;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.StudentAdmin;
import java.util.Optional;


@Repository
public interface StudentAdminRepository extends JpaRepository<StudentAdmin, String> {

	public Optional<StudentAdmin>  findByEmail(String email);
	
	@Query("SELECT sa FROM StudentAdmin sa WHERE (:q IS NULL OR :q= '' OR sa.id LIKE CONCAT(:q,'%') OR sa.email LIKE CONCAT(:q,'%' ) OR sa.name LIKE CONCAT(:q,'%') )")
	public Page<StudentAdmin> getAll(String q ,Pageable pageable);
	
	public StudentAdmin findByPasswordResetToken(String token);
}
