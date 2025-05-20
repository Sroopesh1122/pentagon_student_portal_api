package com.pentagon.app.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.repository.StudentRepository;
import com.pentagon.app.repository.TrainerRepository;

@Component
public class IdGeneration {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired private AdminRepository adminRepository;
	@Autowired private ExecutiveRepository executiveRepository;
	@Autowired private TrainerRepository trainerRepository;
	@Autowired private ManagerRepository managerRepository;
	
	// PS19MAY25OFJFS0001 paid-offline
	// PS19MAY25ONJFS0001 paid-online
	// PS19MAY25OFJFS#001 CSR-offline
	// PS19MAY25ONJFS#001 CSR-online

	public String generateStudentId(String stack, String mode, String typeOfAdmission) {
	    Map<String, String> stackCodeMap = Map.of(
	        "java full stack", "JFS",
	        "python full stack", "PFS",
	        "mern full stack", "MFS",
	        "software testing", "ST"
	    );

	    String code = stackCodeMap.get(stack.toLowerCase());
	    if (code == null || mode == null || typeOfAdmission == null) {
	        throw new IllegalArgumentException("Invalid data");
	    }

	    LocalDate now = LocalDate.now();
	    int month = now.getMonthValue();
	    int year = now.getYear();

	    int count = studentRepository.countByCourseAndMonthYear(stack, month, year);
	    int next = count + 1;

	    String paddedNumber = code.equals("ST")
	            ? String.format("%04d", next)  // ST -> 0001
	            : String.format("%03d", next); // Others -> 001

	    String datePart = now.format(DateTimeFormatter.ofPattern("ddMMMyy")).toUpperCase(); // e.g., 19MAY25

	    String modePart = mode.equalsIgnoreCase("online") ? "ON" : "OF";
	    String prefix = "PS" + datePart + modePart + code;

	    if (typeOfAdmission.equalsIgnoreCase("csr")) {
	        return prefix + "#" + paddedNumber; // PS19MAY25OFJFS#0001
	    } else {
	        return prefix + "0" + paddedNumber;       // PS19MAY25OFJFS0001
	    }
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
	// CSR and paid ofline online


}
