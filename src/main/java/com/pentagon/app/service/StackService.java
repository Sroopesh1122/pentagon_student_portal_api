package com.pentagon.app.service;

import com.pentagon.app.entity.Stack;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface StackService {
    public Stack addStack(Stack stack);
    public Page<Stack> getAllStacks(Pageable pageable);
    public Optional<Stack> getStackById(String stackId);
    public Stack updateStack(Stack stack);
    public void deleteStack(String stackId);
    
    public List<Stack> getAll();
    
    public Stack getByName(String stackName);
}