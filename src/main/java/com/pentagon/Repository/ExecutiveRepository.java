package com.pentagon.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.Entity.Executive;

public interface ExecutiveRepository extends JpaRepository<Integer, Executive>{

	Optional<Executive> findByEmail(String email);
}
