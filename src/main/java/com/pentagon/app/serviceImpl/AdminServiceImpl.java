package com.pentagon.app.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.Dto.JdStatsDTO;
import com.pentagon.app.Dto.JdVsClosureStatsDTO;
import com.pentagon.app.entity.Admin;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.request.AdminLoginRequest;


import com.pentagon.app.response.ProfileResponse;


import com.pentagon.app.service.AdminService;
import com.pentagon.app.service.OtpService;


@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private AdminRepository adminRepository;

	@Autowired
	private OtpService otpService;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;

	@Override
	public Admin updateAdmin(Admin admin) {
	try {
			admin.setUpdatedAt(LocalDateTime.now());
			admin = adminRepository.save(admin);
			return admin;
		} catch (Exception e) {
			throw new AdminException("Failed to update Admin: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}


	@Override
	public String loginWithPassword(AdminLoginRequest request) {
		// TODO Auto-generated method stub
		Admin admin = adminRepository.findByEmail(request.getEmail())
				.orElseThrow(() -> new AdminException("Admin not found", HttpStatus.NOT_FOUND));

		if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
			throw new AdminException("Invalid password", HttpStatus.UNAUTHORIZED);
		}
		String otp = otpService.generateOtpAndStore(admin.getEmail());
		otpService.sendOtpToEmail(admin.getEmail(), otp);

		return "OTP sent to registered email";

	}
	

	@Override
	public ProfileResponse getProfile(Admin admin) {
		ProfileResponse result = new ProfileResponse();
		result.setUniqueId(admin.getAdminId());
		result.setName(admin.getName());
		result.setEmail(admin.getEmail());
		result.setMobile(admin.getMobile());
		return result;
	}
	
	@Override
	public List<JdStatsDTO> getJdStats(String timeUnit, int range) {
		DateTimeFormatter formatter;
	    ChronoUnit chronoUnit;
	    String sqlFormat;

	    switch (timeUnit.toLowerCase()) {
	        case "day":
	            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            sqlFormat = "%Y-%m-%d";
	            chronoUnit = ChronoUnit.DAYS;
	            
	            break;
	        case "week":
	            formatter = DateTimeFormatter.ofPattern("YYYY-'W'ww");
	            sqlFormat = "%x-W%v"; // MySQL: ISO year and week
	            chronoUnit = ChronoUnit.WEEKS;
	           
	            break;
	        case "month":
	            formatter = DateTimeFormatter.ofPattern("yyyy-MM");
	            sqlFormat = "%Y-%m";
	            chronoUnit = ChronoUnit.MONTHS;
	          
	            break;
	        case "year":
	            formatter = DateTimeFormatter.ofPattern("yyyy");
	            sqlFormat = "%Y";
	            chronoUnit = ChronoUnit.YEARS;
	           
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid time unit: " + timeUnit);
	    }

	    // Generate time labels in Java
	    LocalDate now = LocalDate.now();
	    Map<String, Long> fullMap = new LinkedHashMap<>();
	    for (int i = range - 1; i >= 0; i--) {
	        String label = now.minus(i, chronoUnit).format(formatter);
	        fullMap.put(label, 0L);
	    }

	    // Fetch grouped count from DB
	    String startDate = now.minus(range - 1, chronoUnit).toString();
	    List<Object[]> results = jobDescriptionRepository.getJdStats(sqlFormat, startDate);

	    for (Object[] obj : results) {
	        String label = (String) obj[0];
	        Long count = ((Number) obj[1]).longValue();
	        fullMap.put(label, count);
	    }

	    return fullMap.entrySet().stream()
	            .map(e -> new JdStatsDTO(e.getKey(), e.getValue()))
	            .collect(Collectors.toList());
	}
	
	@Override
	public List<JdVsClosureStatsDTO> getJdVsClosureStats(String timeUnit, int range) {
	    DateTimeFormatter formatter;
	    ChronoUnit chronoUnit;
	    String sqlFormat;

	    switch (timeUnit.toLowerCase()) {
	        case "day":
	            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	            sqlFormat = "%Y-%m-%d";
	            chronoUnit = ChronoUnit.DAYS;
	            break;
	        case "week":
	            formatter = DateTimeFormatter.ofPattern("YYYY-'W'ww");
	            sqlFormat = "%x-W%v"; // ISO year-week format in MySQL
	            chronoUnit = ChronoUnit.WEEKS;
	            break;
	        case "month":
	            formatter = DateTimeFormatter.ofPattern("yyyy-MM");
	            sqlFormat = "%Y-%m";
	            chronoUnit = ChronoUnit.MONTHS;
	            break;
	        case "year":
	            formatter = DateTimeFormatter.ofPattern("yyyy");
	            sqlFormat = "%Y";
	            chronoUnit = ChronoUnit.YEARS;
	            break;
	        default:
	            throw new IllegalArgumentException("Invalid time unit: " + timeUnit);
	    }

	    // Generate empty default map with zeroes
	    LocalDate now = LocalDate.now();
	    Map<String, JdVsClosureStatsDTO> fullMap = new LinkedHashMap<>();
	    for (int i = range - 1; i >= 0; i--) {
	        String label = now.minus(i, chronoUnit).format(formatter);
	        fullMap.put(label, new JdVsClosureStatsDTO(label, 0L, 0L));
	    }

	    // Fetch DB data
	    String startDate = now.minus(range - 1, chronoUnit).toString();
	    List<Object[]> results = jobDescriptionRepository.getJdVsClosureStats(sqlFormat, startDate);

	    for (Object[] obj : results) {
	        String label = (String) obj[0];
	        Long jdCount = obj[1] != null ? ((Number) obj[1]).longValue() : 0L;
	        Long closureCount = obj[2] != null ? ((Number) obj[2]).longValue() : 0L;
	        fullMap.put(label, new JdVsClosureStatsDTO(label, jdCount, closureCount));
	    }

	    return new ArrayList(fullMap.values());
	}


	@Override
	public Admin findByEmail(String email) {
		return adminRepository.findByEmail(email).orElse(null);
	}


	@Override
	public Admin findByPasswordResetToken(String token) {
		return adminRepository.findByPasswordResetToken(token);
	}

	
}
