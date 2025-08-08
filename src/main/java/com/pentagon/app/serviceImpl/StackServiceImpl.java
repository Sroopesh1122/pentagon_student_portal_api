package com.pentagon.app.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Stack;
import com.pentagon.app.repository.StackRepository;
import com.pentagon.app.service.StackService;

@Service
public class StackServiceImpl implements StackService {

    @Autowired
    private StackRepository stackRepository;

    @Override
    public Stack addStack(Stack stack) {
        return stackRepository.save(stack);
    }

    @Override
    public Page<Stack> getAllStacks(Pageable pageable) {
        return stackRepository.findAll(pageable);
    }

    @Override
    public Optional<Stack> getStackById(String stackId) {
        return stackRepository.findById(stackId);
    }

    @Override
    public Stack updateStack(Stack stack) {
        return stackRepository.save(stack);
    }

    @Override
    public void deleteStack(String stackId) {
        stackRepository.deleteById(stackId);
    }
    
    @Override
    public List<Stack> getAll() {
    	return stackRepository.findAll();
    } 
    @Override
    public Stack getByName(String stackName) {
    	return stackRepository.findByName(stackName);
    }
    
}