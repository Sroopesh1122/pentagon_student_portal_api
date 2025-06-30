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
import com.pentagon.app.entity.ProgramHead;
import com.pentagon.app.entity.Student;
import com.pentagon.app.entity.StudentAdmin;
import com.pentagon.app.entity.Trainer;
import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.repository.ProgramHeadRepository;
import com.pentagon.app.repository.StudentAdminRepository;
import com.pentagon.app.repository.StudentRepository;
import com.pentagon.app.repository.TrainerRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private ManagerRepository managerRepository;

	@Autowired
	private ExecutiveRepository executiveRepository;

	@Autowired
	private TrainerRepository trainerRepository;

	@Autowired
	private StudentAdminRepository studentAdminRepository;

	@Autowired
	private ProgramHeadRepository programHeadRepository;
	
	
	@Autowired
	private StudentRepository studentRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

		return null;
	}

	public UserDetails loadAdmin(String email) {
		Optional<Admin> admin = adminRepository.findByEmail(email);

		if (admin.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}

		return new CustomUserDetails(admin.get());
	}

	public UserDetails loadManager(String email) {
		Optional<Manager> manager = managerRepository.findByEmail(email);

		if (manager.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}

		return new CustomUserDetails(manager.get());
	}

	public UserDetails loadExecutive(String email) {
		Optional<Executive> executive = executiveRepository.findByEmail(email);

		if (executive.isEmpty()) {
			throw new UsernameNotFoundException("User not found");
		}

		return new CustomUserDetails(executive.get());
	}

	public UserDetails loadStudentAdmin(String email) {
		Optional<StudentAdmin> studentAdmin = studentAdminRepository.findByEmail(email);

		if (studentAdmin.isEmpty()) {

			throw new UsernameNotFoundException("User Not found");
		}
		return new CustomUserDetails(studentAdmin.get());
	}

	public UserDetails loadTrainer(String email) {
		Optional<Trainer> trainer = trainerRepository.findByEmail(email);

		if (trainer.isEmpty()) {
			throw new UsernameNotFoundException("User Not Found");
		}
		return new CustomUserDetails(trainer.get());
	}

	public UserDetails loadProgramHead(String email) {
		Optional<ProgramHead> programHead = programHeadRepository.findByEmail(email);

		if (programHead.isEmpty()) {
			throw new UsernameNotFoundException("User Not Found");
		}
		return new CustomUserDetails(programHead.get());
	}
	
	
	public UserDetails loadStudent(String email) {
		Optional<Student> student= studentRepository.findByEmail(email);

		if (student.isEmpty()) {
			throw new UsernameNotFoundException("User Not Found");
		}
		return new CustomUserDetails(student.get());
	}

}