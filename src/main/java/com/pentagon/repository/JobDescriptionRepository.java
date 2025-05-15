package com.pentagon.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.entity.JobDescription;

public interface JobDescriptionRepository extends JpaRepository<Integer, JobDescription> {

}
