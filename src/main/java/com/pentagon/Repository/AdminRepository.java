package com.pentagon.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.entity.Admin;

public interface AdminRepository extends JpaRepository<Integer, Admin> {

}
