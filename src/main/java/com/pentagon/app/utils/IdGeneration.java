package com.pentagon.app.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.pentagon.app.entity.Batch;
import com.pentagon.app.exception.IdGenerationException;
import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.ExecutiveRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.repository.ManagerRepository;
import com.pentagon.app.repository.StudentRepository;
import com.pentagon.app.repository.TrainerRepository;
import java.security.SecureRandom;

@Component
public class IdGeneration {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@Autowired 
	private AdminRepository adminRepository;
	@Autowired 
	private ExecutiveRepository executiveRepository;
	@Autowired 
	private TrainerRepository trainerRepository;
	@Autowired 
	private ManagerRepository managerRepository;
	
	@Autowired 
	private JobDescriptionRepository jdRepository;
	
	// PS19MAY25OFJFS0001 paid-offline
	// PS19MAY25ONJFS0001 paid-online
	// PS19MAY25OFJFS#001 CSR-offline
	// PS19MAY25ONJFS#001 CSR-online

	// CSR, paid and offline, online
	public String generateStudentId(String stack, String mode, String typeOfAdmission ,Batch batch) {
	    Map<String, String> stackCodeMap = Map.of(
	        "java full stack", "JFS",
	        "python full stack", "PFS",
	        "mern full stack", "MFS",
	        "software testing", "ST"
	    );

	    String code = stackCodeMap.get(stack.toLowerCase());
	    if (code == null || mode == null || typeOfAdmission == null) {
	        throw new IdGenerationException("Invalid input for ID generation. Please check stack, mode, or admission type.", HttpStatus.BAD_REQUEST);
	    }

	    
	    //Based on batch created date student id will be generated
	    LocalDateTime now = batch.getCreatedAt();
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
	
	public  String generateBatchId(String stack) {
		
		String prefix="";
		
		if(stack.toLowerCase().contains("python"))
		{
			prefix ="PFS";
		}
		else if(stack.toLowerCase().contains("java"))
		{
			prefix ="JFS";
		}
		else if(stack.toLowerCase().contains("mern"))
		{
			prefix ="MERN";
		}else if(stack.toLowerCase().contains("testing"))
		{
			prefix ="TESTING";
		}
		
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        return prefix+"-BATCH-" + today.format(formatter);
    }
	
	public String generateId(String type) {
	    String prefix = switch (type.toUpperCase()) {
	        case "ADMIN" -> "ADM";
	        case "MANAGER" -> "MGR";
	        case "EXECUTIVE" -> "EXE";
	        case "TRAINER" -> "TRN";
	        case "JD" -> "JD";
	        case "PG-HEAD" -> "PG-HEAD";
	        case "STU-ADMIN" -> "STU-ADMIN";
	        default -> throw new IdGenerationException("Invalid type: " + type, HttpStatus.BAD_REQUEST);
	    };
	    
	    SecureRandom random = new SecureRandom();
	    int randomNumber = random.nextInt(100_000_000); // 0-99,999,999
	    String suffix = String.format("%08d", randomNumber); // Pad with leading zeros

	    return prefix + suffix; // e.g., "ADM04192683"
	
	}
    
	public String generateRandomString() {
	    String uuid = UUID.randomUUID().toString().replace("-", "");
	    long timestamp = System.currentTimeMillis();
	    return uuid + timestamp;
	}

}