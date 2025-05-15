package com.pentagon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.entity.Manager;

public interface ManagerRepository extends JpaRepository<Integer, Manager> {

}
