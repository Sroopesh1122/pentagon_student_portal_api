package com.pentagon.app.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Executive;
import com.pentagon.app.entity.Manager;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.repository.TrainerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Autowired
	private TrainerRepository trainerRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Admin> admin=adminRepository.findByEmail(email);
		Optional<Manager> manager=managerRepository.findByEmail(email);
		Optional<Executive> executive=executiveRepository.findByEmail(email);
		Optional<Trainer> trainer=trainerRepository.findByEmail(email);
		
		if (admin.isPresent()) {
			return new CustomUserDetails(admin.get());
		}
		if (manager.isPresent()) {
			return new CustomUserDetails(manager.get());
		}
		if (executive.isPresent()) {
			return new CustomUserDetails(executive.get());
		}
		if (trainer.isPresent()) {
			return new CustomUserDetails(trainer.get());
		}
		throw new RuntimeException("USER NOT FOUND");
	}

}