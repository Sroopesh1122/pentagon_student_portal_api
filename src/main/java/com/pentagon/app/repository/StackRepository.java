package com.pentagon.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pentagon.app.entity.Stack;
import java.util.List;

@Repository
public interface StackRepository extends JpaRepository<Stack,String> {	

	Stack findByName(String name);
}
