package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pentagon.app.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {

	Optional<Admin> findByEmail(String email);

	@Query("SELECT COUNT(A) FROM Admin A")
	int getAdminCount();
}
