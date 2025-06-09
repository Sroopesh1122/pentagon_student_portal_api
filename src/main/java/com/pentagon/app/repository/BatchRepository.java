package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Batch;
@Repository
public interface BatchRepository extends JpaRepository<Batch,String> {

}
