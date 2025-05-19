package com.pentagon.app.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;

import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.repository.StudentRepository;
import com.pentagon.app.repository.TrainerRepository;

public class IdGeneration {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired private AdminRepository adminRepository;
	@Autowired private ExecutiveRepository executiveRepository;
	@Autowired private TrainerRepository trainerRepository;
	@Autowired private ManagerRepository managerRepository;
	
	public String generateStudentId(String stack) {
	    LocalDate now = LocalDate.now();
	    int month = now.getMonthValue();
	    int year = now.getYear();

	    int count = studentRepository.countByCourseAndMonthYear(stack, month, year);
	    int next = count + 1;

	    String paddedNumber = String.format("%03d", next); // 001
	    String datePart = now.format(DateTimeFormatter.ofPattern("ddMMMyy")).toUpperCase(); // e.g., 19MAY25

	    return "PS" + datePart + "OF" + stack.toUpperCase() + paddedNumber;
	    //PS19MAY25OFXXX001
	}
	
	public String generateId(String userType) {
	    String prefix;
	    int count = 0;

	    switch (userType.toUpperCase()) {
	        case "ADMIN" -> {
	            prefix = "ADM";
	            count = adminRepository.getAdminCount(); //ADM0001
	        }
	        case "MANAGER" -> {
	            prefix = "MGR";
	            count = managerRepository.getManagerCount(); //MGR0001
	        }
	        case "EXECUTIVE" -> {
	            prefix = "EXE";
	            count = executiveRepository.getExecutiveCount(); //EXE0001
	        }
	        case "TRAINER" -> {
	        	prefix = "TRN";
	        	count = trainerRepository.getTrainerCount(); //TRN0001
	        }
	        default -> throw new IllegalArgumentException("Invalid user type");
	    }

	    String padded = String.format("%04d", count + 1);
	    return prefix + padded;
	}


}
