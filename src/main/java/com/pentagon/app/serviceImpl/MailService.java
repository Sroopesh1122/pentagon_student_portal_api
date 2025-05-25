package com.pentagon.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;


@Service
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	// Send Plain Text Email
	public void sendOtpToEmail(String to, String subject, String htmlContent) throws Exception {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(htmlContent, true); // true = isHtml
			javaMailSender.send(message);
		} catch (Exception e) {
			throw new RuntimeException("Failed to send email", e);
		}
	}


	// Send HTML Email
	public void sendPasswordEmail(String toEmail, String subject, String htmlContent) throws Exception {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setTo(toEmail);
			helper.setSubject(subject);
			helper.setText(htmlContent, true); // true = isHtml
			javaMailSender.send(message);
		} catch (Exception e) {
			throw new RuntimeException("Failed to send email", e);
		}

	}
}
