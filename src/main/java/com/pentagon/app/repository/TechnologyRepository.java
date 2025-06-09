package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Technology;
@Repository
public interface TechnologyRepository extends JpaRepository<Technology, String> {

}
