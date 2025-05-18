package com.pentagon.app.service;

import com.pentagon.app.requestDTO.OtpVerificationRequest;

public interface OtpService {
	
	public String generateOtpAndStore(String email);
	public void sendOtpToEmail(String email, String otp);
	public boolean verifyOtp(OtpVerificationRequest request);
	public void deleteOtpByEmail(String email);
	

}
