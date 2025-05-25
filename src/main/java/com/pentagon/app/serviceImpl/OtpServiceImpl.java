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
import com.pentagon.app.exception.OtpException;
import com.pentagon.app.repository.AdminRepository;
import com.pentagon.app.repository.OtpRepository;
import com.pentagon.app.request.OtpVerificationRequest;
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
		Optional<Otp> existingOtpOpt = otpRepository.findByEmail(email);
		String otpValue = generateRandomOtp();

		if (existingOtpOpt.isPresent()) {
			Otp otp = existingOtpOpt.get();

			if (otp.getOtpCount() >= 3) {
				throw new OtpException("Maximum 3 OTP resend attempts reached. Please verify the OTP or wait until it expires.", HttpStatus.TOO_MANY_REQUESTS);
			}

			otp.setOtpCount(otp.getOtpCount() + 1);
			otp.setOtp(otpValue);
			otpRepository.save(otp);

		} else {
			Otp otp = new Otp();
			otp.setEmail(email);
			otp.setOtp(otpValue);
			otp.setOtpCount(1);
			otpRepository.save(otp);
		}

		return otpValue;
	}

	private String generateRandomOtp() {
		return String.valueOf(new Random().nextInt(900000) + 100000); // 6-digit OTP
	}

	@Override
	public void sendOtpToEmail(String email, String otp) {
	    String subject = "Your OTP for Pentagon Space Login";

	    String htmlContent = "<html>" +
	        "<body style='font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px;'>" +
	        "<div style='max-width: 500px; margin: auto; background-color: #ffffff; border-radius: 10px; overflow: hidden; box-shadow: 0 0 10px rgba(0,0,0,0.1);'>" +
	        "<div style='background-color: #3f51b5; padding: 20px; text-align: center;'>" +
	        "<h2 style='color: #ffffff; margin: 0;'>Email OTP</h2>" +
	        "</div>" +
	        "<div style='padding: 30px; text-align: center;'>" +
	        "<p style='font-size: 16px; color: #333;'>Your One-Time Password (OTP) is:</p>" +
	        "<div style='font-size: 32px; font-weight: bold; color: #3f51b5; background-color: #f0f0f0; padding: 15px 30px; border-radius: 8px; display: inline-block; letter-spacing: 5px;'>" + otp + "</div>" +
	        "<p style='font-size: 14px; color: #777; margin-top: 20px;'>Please use this OTP to complete your login process.It is valid only for 5 minutes. Do not share this code with anyone.</p>" +
	        "</div>" +
	        "<div style='padding: 10px; text-align: center; background-color: #fafafa; font-size: 12px; color: #aaa;'>" +
	        "© <a href='https://online.pentagonspace.in/' style='color: #3f51b5; text-decoration: none;'>www.pentagonspace.in</a>. All rights reserved." +
	        "</div>" +
	        "</div>" +
	        "</body>" +
	        "</html>";

	    // Send HTML email
	    mailService.sendSimpleEmail(email, subject, htmlContent);
	}


	@Override
	public boolean verifyOtp(OtpVerificationRequest request) {
        Optional<Otp> otpOpt = otpRepository.findByEmail(request.getEmail());
        if (otpOpt.isEmpty()) {
            throw new OtpException("OTP not found or expired.", HttpStatus.NOT_FOUND);
        }

        Otp otp = otpOpt.get();

        // Check if blocked due to too many wrong attempts
        if (otp.getBlockUntil() != null && otp.getBlockUntil().isAfter(LocalDateTime.now())) {
            throw new OtpException("Too many wrong attempts. Try again after " + otp.getBlockUntil(),HttpStatus.FORBIDDEN);
        }

        if (otp.getOtp().equals(request.getOtp())) {
            otpRepository.deleteByEmail(request.getEmail()); // Verified: delete immediately
            return true;
        } else {
            // Increment wrong attempt count
            int wrongAttempts = otp.getWrongAttemptCount() + 1;
            otp.setWrongAttemptCount(wrongAttempts);

            if (wrongAttempts >= MAX_WRONG_ATTEMPTS) {
                // Block for 30 minutes
                otp.setBlockUntil(LocalDateTime.now().plusMinutes(BLOCK_DURATION_MINUTES));
            }

            otpRepository.save(otp);
            throw new OtpException("Invalid OTP. Attempt " + wrongAttempts + " of " + MAX_WRONG_ATTEMPTS, HttpStatus.UNAUTHORIZED);
        }
    }
}
