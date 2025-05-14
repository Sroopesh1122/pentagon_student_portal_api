package com.pentagon.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pentagon.Entity.Admin;
import com.pentagon.Entity.Executive;
import com.pentagon.Entity.Manager;
import com.pentagon.Repository.AdminRepository;
import com.pentagon.Repository.ExecutiveRepository;
import com.pentagon.Repository.ManagerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private ManagerRepository managerRepository;
	
	@Autowired
	private ExecutiveRepository executiveRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<Admin> admin=adminRepository.findByEmail(email);
		Optional<Manager> manager=managerRepository.findByEmail(email);
		Optional<Executive> executive=executiveRepository.findByEmail(email);
		if (admin.isPresent()) {
			return new CustomUserDetails(admin.get());
		}
		if (manager.isPresent()) {
			return new CustomUserDetails(manager.get());
		}
		if (executive.isPresent()) {
			return new CustomUserDetails(executive.get());
		}
		throw new RuntimeException("USER NOT FOUND");
	}

}
