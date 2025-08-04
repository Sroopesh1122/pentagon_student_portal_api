package com.pentagon.app.serviceImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.app.Dto.ExecutiveJDStatusDTO;
import com.pentagon.app.Dto.JdStatsDTO;
import com.pentagon.app.entity.JobDescription;
import com.pentagon.app.entity.Stack;
import com.pentagon.app.exception.JobDescriptionException;
import com.pentagon.app.repository.JobDescriptionRepository;
import com.pentagon.app.service.JobDescriptionService;
import com.pentagon.app.service.StackService;

@Service
public class JobDescriptionServiceImp implements JobDescriptionService {

	@Autowired
	private JobDescriptionRepository jobDescriptionRepository;

	@Autowired
	private StackService stackService;

	@Override
	public Optional<JobDescription> findByJobDescriptionId(String jobDescriptionId) {
		return jobDescriptionRepository.findByJobDescriptionId(jobDescriptionId);
	}

	public JobDescription addJobDescription(JobDescription jobDescription) {
		// TODO Auto-generated method stub
		try {
			jobDescription.setCreatedAt(LocalDateTime.now());
			jobDescription = jobDescriptionRepository.save(jobDescription);
			return jobDescription;
		} catch (Exception e) {
			throw new JobDescriptionException("Failed to Add Job Description: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public JobDescription updateJobDescription(JobDescription jobDescription) {
		try {
			jobDescription.setUpdatedAt(LocalDateTime.now());
			return jobDescriptionRepository.save(jobDescription);
		} catch (Exception e) {
			throw new JobDescriptionException("Failed to Update Job Description: " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Page<JobDescription> findAllJobDescriptions(String companyName, String stack, String role, Boolean isClosed,
			Integer minYearOfPassing, Integer maxYearOfPassing, String qualification, String stream, Double percentage,
			String executiveId, String managerId, String status, String startDate, String endDate, Pageable pageable) {

		try {

			String stackRegex = null;

			if (stack == null || stack.isEmpty()) {
				stackRegex = "";
			} else if (stack.toLowerCase().equals("any")) {
				stackRegex = null;
			} else {
				Stack findStack = stackService.getByName(stack);
				stackRegex = findStack != null ? findStack.getStackId() : "";
			}

			String qualificationRegex = qualification != null ? Arrays.asList(qualification.split(",")).stream()
					.map(w -> Pattern.quote(w)).collect(Collectors.joining("|")) : "";
			String streamRegex = stream != null ? Arrays.asList(stream.split(",")).stream().map(w -> Pattern.quote(w))
					.collect(Collectors.joining("|")) : "";

			return jobDescriptionRepository.findWithFiltersUsingRegex(companyName, stackRegex, role, isClosed,
					minYearOfPassing, maxYearOfPassing, qualificationRegex, streamRegex, percentage, executiveId,
					managerId, status, startDate, endDate, pageable);

		} catch (Exception e) {
			e.printStackTrace();
			throw new JobDescriptionException("Failed to fetch Job Descriptions", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public List<JobDescription> viewAllJobDescriptions() {
		// TODO Auto-generated method
		try {
			List<JobDescription> jobDescriptions = jobDescriptionRepository.findAll();
			return jobDescriptions;
		} catch (Exception e) {
			throw new JobDescriptionException("Failed to Job Descriptions", HttpStatus.NOT_FOUND);
		}

	}

	@Override
	public List<JobDescription> viewJobDescriptionBasedOnStack(String stack) {
		// TODO Auto-generated method stub
		try {
			return jobDescriptionRepository.findByStackId(stack);
		} catch (Exception e) {
			throw new JobDescriptionException("Failed to Fetch Job Descriptions : " + e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public ExecutiveJDStatusDTO getExecutiveJobDescriptionStats(String executiveId) {
		int totalJDs = jobDescriptionRepository.countTotalJDsByExecutive(executiveId);
		int openings = jobDescriptionRepository.countOpeningsByExecutive(executiveId);
		int closures = jobDescriptionRepository.countClosuresByExecutive(executiveId);

		return new ExecutiveJDStatusDTO(totalJDs, openings, closures);
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

		return fullMap.entrySet().stream().map(e -> new JdStatsDTO(e.getKey(), e.getValue()))
				.collect(Collectors.toList());
	}

	@Override
	public Long totalCount() {
		return jobDescriptionRepository.count();
	}

	@Override
	public Long totalClosureCount() {
		return jobDescriptionRepository.getTotalClosureCount();
	}

	@Override
	public JobDescription finById(String jdId) {
		return jobDescriptionRepository.findById(jdId).orElse(null);
	}

	@Override
	public Page<JobDescription> findJdForStudent(String companyName, String stack, String role,
			Integer minYearOfPassing, Integer maxYearOfPassing, String qualification, String stream, Double percentage,
			String startDate, String endDate, Pageable pageable) {
		try {

			String stackRegex = null;

			if (stack == null || stack.isEmpty()) {
				stackRegex = "";
			} else if (stack.toLowerCase().equals("any")) {
				stackRegex = null;
			} else {
				Stack findStack = stackService.getByName(stack);
				stackRegex = findStack != null ? findStack.getStackId() : "";
			}

			String qualificationRegex = qualification != null ? Arrays.asList(qualification.split(",")).stream()
					.map(w -> Pattern.quote(w)).collect(Collectors.joining("|")) : "";
			String streamRegex = stream != null ? Arrays.asList(stream.split(",")).stream().map(w -> Pattern.quote(w))
					.collect(Collectors.joining("|")) : "";

			return jobDescriptionRepository.findJdForStudent(companyName, stackRegex, role, minYearOfPassing,
					maxYearOfPassing, qualificationRegex, streamRegex, percentage, startDate, endDate, pageable);

		} catch (Exception e) {
			e.printStackTrace();
			throw new JobDescriptionException("Failed to fetch Job Descriptions", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	public Map<String, Object> getJdClosureOfExecutiveByMonthRange(
	        Integer noOfMonths,
	        String executiveId) {

	    Map<String, Long> closureStats = new LinkedHashMap<>(); // Use LinkedHashMap to keep order

	    LocalDate today = LocalDate.now();

	    for (int i = 0; i < noOfMonths; i++) {
	        // Calculate the year-month we want
	        LocalDate firstDayOfMonth = today.minusMonths(i).withDayOfMonth(1);
	        LocalDate lastDayOfMonth = firstDayOfMonth.withDayOfMonth(firstDayOfMonth.lengthOfMonth());

	        LocalDateTime startDateTime = firstDayOfMonth.atStartOfDay(); // 1st day 00:00:00
	        LocalDateTime endDateTime = lastDayOfMonth.atTime(LocalTime.MAX); // Last day 23:59:59.999...

	        Long count = jobDescriptionRepository.getTotalClosureCountByExecutiveIdAndDateRange(
	                executiveId,
	                startDateTime,
	                endDateTime
	        );

	        String monthKey = firstDayOfMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH)
	                + "-" + firstDayOfMonth.getYear();

	        closureStats.put(monthKey, count != null ? count : 0L);
	    }

	    // If you must wrap in Map<String,Object>
	    Map<String, Object> response = new HashMap<>();
	    response.put("closureStats", closureStats);

	    return response;
	}
	
	@Override
	public Page<JobDescription> getExecutiveJd(String executiveId, Pageable pageable) {
		return jobDescriptionRepository.getJdOfExecutive(executiveId, pageable);
	}
	
	@Override
	public Page<JobDescription> getManagerJd(String managerId, Pageable pageable) {
		// TODO Auto-generated method stub
		return jobDescriptionRepository.getJdOfManager(managerId, pageable);
	}

	

}