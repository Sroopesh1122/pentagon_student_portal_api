package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.app.entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

}
