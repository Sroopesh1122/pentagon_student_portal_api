package com.pentagon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.entity.Trainer;

public interface TrainerRepository extends JpaRepository<Trainer, Integer> {

}
