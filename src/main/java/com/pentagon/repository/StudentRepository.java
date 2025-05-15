package com.pentagon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.entity.Student;

public interface StudentRepository extends JpaRepository<Integer, Student> {

}
