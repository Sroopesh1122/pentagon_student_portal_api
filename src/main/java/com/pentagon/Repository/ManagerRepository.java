package com.pentagon.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.entity.Manager;

public interface ManagerRepository extends JpaRepository<Integer, Manager> {

}
