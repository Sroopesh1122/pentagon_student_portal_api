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
		// write otp sending logic or store in database
//		mailService.sendSimpleEmail(email, "Otp From Penatagon Sapce", otp);

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
