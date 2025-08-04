package com.pentagon.app.serviceImpl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pentagon.app.entity.Otp;
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.OtpRepository;
import com.pentagon.app.request.OtpVerificationRequest;
import com.pentagon.app.service.OtpService;


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
	    Optional<Otp> existingOtpOpt = otpRepository.findByEmail(email);
	    String otpValue = generateRandomOtp();
	    LocalDateTime now = LocalDateTime.now();

	    if (existingOtpOpt.isPresent()) {
	        Otp otp = existingOtpOpt.get();

	        // Check if blocked
	        if (otp.getOtpCount() > 3) {
	            if (otp.getBlockUntil() == null) {
	                otp.setBlockUntil(now.plusMinutes(20));
	                otpRepository.save(otp);
	            }

	            if (otp.getBlockUntil().isAfter(now)) {
	                throw new OtpException(
	                    "You have exceeded the maximum OTP resend attempts. Please try again after some time.",
	                    HttpStatus.TOO_MANY_REQUESTS
	                );
	            } else {
	                otp.setOtpCount(1);
	                otp.setBlockUntil(null);
	            }
	        } else {
	            otp.setOtpCount(otp.getOtpCount() + 1);
	        }

	        otp.setOtp(otpValue);
	        otp.setCreatedAt(now);
	        otp.setExpiredAt(now.plusMinutes(5));
	        otpRepository.save(otp);

	    } else {
	        // First-time OTP request
	        Otp otp = new Otp();
	        otp.setEmail(email);
	        otp.setOtp(otpValue);
	        otp.setOtpCount(1);
	        otp.setCreatedAt(now);
	        otp.setExpiredAt(now.plusMinutes(5));
	        otpRepository.save(otp);
	    }

	    return otpValue;
	}

	private String generateRandomOtp() {
		return String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
	}

	@Override
	public void sendOtpToEmail(String email, String otp) {
		String subject = "Your OTP for Pentagon Space Login – Expires in 5 Minutes";

	    String htmlContent = "<!DOCTYPE html>\n" +
	    	    "<html lang='en'>\n" +
	    	    "<head>\n" +
	    	    "  <meta charset='UTF-8'>\n" +
	    	    "  <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n" +
	    	    "  <title>OTP Verification</title>\n" +
	    	    "  <style>\n" +
	    	    "    @media (prefers-color-scheme: dark) {\n" +
	    	    "      body { background-color: #1a1a1a; color: #e0e0e0; }\n" +
	    	    "      .otp-box { background-color: rgba(247, 68, 98, 0.441); color: #ffffff; border: 2px solid #F43F5E; border-radius: 10px; }\n" +
	    	    "      a { color: #F43F5E; }\n" +
	    	    "    }\n" +
	    	    "  </style>\n" +
	    	    "</head>\n" +
	    	    "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; margin: 0; padding: 20px;'>\n" +
	    	    "  <div style='max-width: 600px; margin: auto; background-color: #ffffff; border-radius: 10px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); overflow: hidden;'>\n" +
	    	    "    <div style='background-color: #F43F5E; padding: 20px; text-align: center;'>\n" +
	    	    "      <img src='https://dme2wmiz2suov.cloudfront.net/User(76576463)/1808770-PENTAGON_LOGO_blue.png' alt='Pentagon Logo' style='max-height: 50px; margin-bottom: 10px;' />\n" +
	    	    "      <h1 style='color: #ffffff; margin: 0;'>OTP Verification</h1>\n" +
	    	    "    </div>\n" +
	    	    "    <div style='padding: 30px; text-align: center;'>\n" +
	    	    "      <p style='font-size: 16px; color: #333333;'>Please use the following OTP to proceed with verification:</p>\n" +
	    	    "      <div style='font-size: 36px; font-weight: bold; color: #F43F5E; background-color: #f43f5d12; padding: 15px 25px; border: 2px solid #F43F5E; border-radius: 8px; display: inline-block; letter-spacing: 6px;'>" + otp + "</div>\n" +
	    	    "      <p style='font-size: 14px; color: #666666; margin-top: 25px;'>This OTP is valid for <strong>5 minutes</strong>. Do not share this code with anyone.</p>\n" +
	    	    "    </div>\n" +
	    	    "    <div style='padding: 15px 30px; background-color: #f9f9f9; font-size: 13px; color: #999999; text-align: center;'>\n" +
	    	    "      Need help? Contact our support team at <a href='mailto:support@pentagonspace.in' style='color: #F43F5E; text-decoration: none;'>support@pentagonspace.in</a><br><br>\n" +
	    	    "      &copy; <a href='https://online.pentagonspace.in/' style='color: #F43F5E; text-decoration: none;'>www.pentagonspace.in</a> — All rights reserved.\n" +
	    	    "    </div>\n" +
	    	    "    <div style='padding: 10px 30px; font-size: 12px; color: #888888; background-color: #eeeeee; text-align: center;'>\n" +
	    	    "      <p style='margin: 10px 0;'>You are receiving this email because your email was used to request an OTP.</p>\n" +
	    	    "      <p style='margin: 5px 0;'>If this wasn't you, please ignore this message or contact support.</p>\n" +
	    	    "      <p style='margin: 10px 0;'>For terms and privacy, visit <a href='https://online.pentagonspace.in/terms-and-conditions' style='color: #F43F5E; text-decoration: none;'>Terms & Policies</a>.</p>\n" +
	    	    "    </div>\n" +
	    	    "  </div>\n" +
	    	    "</body>\n" +
	    	    "</html>";

	    try {
	        mailService.send(email, subject, htmlContent);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to send email", e);
	    }
	}


	@Override
	public boolean verifyOtp(OtpVerificationRequest request) {
        Optional<Otp> otpOpt = otpRepository.findByEmail(request.getEmail());
        
        if (otpOpt.isEmpty()) {
            throw new OtpException("OTP not found or expired.", HttpStatus.NOT_FOUND);
        }

        Otp otp = otpOpt.get();
        
        if(LocalDateTime.now().isAfter(otp.getExpiredAt()))
        {
        	otpRepository.deleteByEmail(request.getEmail());
        	throw new OtpException("OTP expired, Please Try Again", HttpStatus.BAD_REQUEST);
        }

        // Check if blocked due to too many wrong attempts
        if (otp.getBlockUntil() != null && otp.getBlockUntil().isAfter(LocalDateTime.now())) {
            throw new OtpException("Too many wrong attempts. Try again after " + otp.getBlockUntil().toLocalDate()+","+otp.getBlockUntil().toLocalTime(),HttpStatus.FORBIDDEN);
        }
        
     

        if (otp.getOtp().equals(request.getOtp())) {
            otpRepository.deleteByEmail(request.getEmail()); // Verified: delete immediately
            return true;
        } else {
            // Increment wrong attempt count
            int wrongAttempts = otp.getWrongAttemptCount() + 1;
            otp.setWrongAttemptCount(wrongAttempts);

            if (wrongAttempts >= MAX_WRONG_ATTEMPTS) {
                otp.setBlockUntil(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));
            }

            otpRepository.save(otp);
            throw new OtpException("Invalid OTP. Attempt " + wrongAttempts + " of " + MAX_WRONG_ATTEMPTS, HttpStatus.UNAUTHORIZED);
        }
    }
}
