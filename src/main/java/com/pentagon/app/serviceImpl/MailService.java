package com.pentagon.app.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;


@Service
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;


	// Send HTML Email
	public void send(String toEmail, String subject, String htmlContent) throws Exception {
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
	
	public void sendWithBcc(String toEmail, String subject, String htmlContent, List<String> bccList) throws Exception {
	    try {
	        MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);

	        if (toEmail != null && !toEmail.trim().isEmpty()) {
	            helper.setTo(toEmail);
	        }
	        if (bccList != null && !bccList.isEmpty()) {
	            helper.setBcc(bccList.toArray(new String[0]));
	        }

	        helper.setSubject(subject);
	        helper.setText(htmlContent, true); // true = isHtml
	        javaMailSender.send(message);
	    } catch (Exception e) {
	        throw new RuntimeException("Failed to send email with BCC", e);
	    }
	}
	
	@Async
    public void sendAsync(String toEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = isHtml
            javaMailSender.send(message);
        } catch (Exception e) {
            // You may want to log this exception instead of throwing it
            throw new RuntimeException("Failed to send email asynchronously", e);
        }
    }

    @Async
    public void sendWithBccAsync(String toEmail, String subject, String htmlContent, List<String> bccList) {
        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            if (toEmail != null && !toEmail.trim().isEmpty()) {
                helper.setTo(toEmail);
            }
            if (bccList != null && !bccList.isEmpty()) {
                helper.setBcc(bccList.toArray(new String[0]));
            }

            helper.setSubject(subject);
            helper.setText(htmlContent, true); // true = isHtml
            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email with BCC asynchronously", e);
        }
    }
}
