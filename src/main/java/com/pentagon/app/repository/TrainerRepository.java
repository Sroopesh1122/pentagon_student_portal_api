package com.pentagon.app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.app.entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

	Optional<Trainer> findByEmail(String email);

}
