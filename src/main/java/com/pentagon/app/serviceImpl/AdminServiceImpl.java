package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.repository.AdminRepository;
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

	@Override
	public boolean updateAdmin(Admin admin) {
		// TODO Auto-generated method stub
		try {
			admin.setUpdatedAt(LocalDateTime.now());
			adminRepository.save(admin);
			return true;
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

}
