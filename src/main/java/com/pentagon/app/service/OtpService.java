package com.pentagon.app.service;

import com.pentagon.app.requestDTO.OtpVerificationRequest;

public interface OtpService {
	public int MAX_RESEND = 3;
	public int MAX_WRONG_ATTEMPTS = 5;
	public int BLOCK_DURATION_MINUTES = 30;
	
	public String generateOtpAndStore(String email);
	public void sendOtpToEmail(String email, String otp);
	public boolean verifyOtp(OtpVerificationRequest request);	

}
