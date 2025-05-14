package com.pentagon.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.pentagon.Entity.Admin;

public interface AdminRepository extends JpaRepository<Integer, Admin> {

	Optional<Admin> findByEmail(String email);

}
