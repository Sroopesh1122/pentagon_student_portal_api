package com.pentagon.app.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.app.Entity.Manager;

public interface ManagerRepository extends JpaRepository<Integer, Manager> {

	Optional<Manager> findByEmail(String email);

}
