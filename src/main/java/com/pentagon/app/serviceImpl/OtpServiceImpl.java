package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.entity.Otp;
import com.pentagon.app.exception.AdminException;
import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.OtpRepository;
import com.pentagon.app.requestDTO.OtpVerificationRequest;
import com.pentagon.app.service.OtpService;

import jakarta.transaction.Transactional;

@Service
public class OtpServiceImpl implements OtpService {

	@Autowired
	private OtpRepository otpRepository;

	@Autowired
	private AdminRepository adminRepository;
	
	@Autowired
	private MailService mailService;

	@Override
	public String generateOtpAndStore(String email) {
		// 6-digit OTP
		String otpCode = String.valueOf(new Random().nextInt(899999) + 100000);

		Otp otp = new Otp();
		otp.setEmail(email);
		otp.setOtp(otpCode);
		otpRepository.save(otp);

		return otpCode;

	}

	@Override
	public void sendOtpToEmail(String email, String otp) {
		// write otp sending logic or store in database
//		mailService.sendSimpleEmail(email, "Otp From Penatagon Sapce", otp);

	}

	@Override
	public boolean verifyOtp(OtpVerificationRequest request) {
		Optional<Otp> storedOtpData=  otpRepository.findByEmail(request.getEmail());
		if (!storedOtpData.get().getOtp().equals(request.getOtp())) {
			return false;
		}
		deleteOtpByEmail(request.getEmail());
		return true;
	}
	
	public void deleteOtpByEmail(String email) {
        otpRepository.deleteByEmail(email);
    }

}
