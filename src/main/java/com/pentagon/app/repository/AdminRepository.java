package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Student;
@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

	Optional<Admin> findByEmail(String email);

	@Query("SELECT COUNT(A) FROM Admin A")
	int getAdminCount();
	
	
	public Admin findByPasswordResetToken(String token);
}
