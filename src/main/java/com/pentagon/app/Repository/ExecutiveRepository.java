package com.pentagon.app.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.app.Entity.Executive;

public interface ExecutiveRepository extends JpaRepository<Executive, Integer>{

	Optional<Executive> findByEmail(String email);
}
