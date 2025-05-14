package com.pentagon.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.Entity.Manager;

public interface ManagerRepository extends JpaRepository<Integer, Manager> {

	Optional<Manager> findByEmail(String email);

}
