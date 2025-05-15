package com.pentagon.app.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pentagon.app.Entity.JobDescription;

public interface JobDescriptionRepository extends JpaRepository<Integer, JobDescription> {

}
