package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Batch;
import com.pentagon.app.entity.MockRatings;
import com.pentagon.app.entity.Stack;
@Repository
public interface StackRepository extends JpaRepository<Stack,String> {

}
