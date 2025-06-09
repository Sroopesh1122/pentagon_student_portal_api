package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.StudentAdmin;

@Repository
public interface StudentAdminRepository extends JpaRepository<StudentAdmin, String> {

}
