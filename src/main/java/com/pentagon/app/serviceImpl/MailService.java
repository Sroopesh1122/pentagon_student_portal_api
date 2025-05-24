package com.pentagon.app.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {

	@Autowired
	private JavaMailSender javaMailSender;

	// Send Plain Text Email
	public void sendSimpleEmail(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		System.out.println(to + " " + text);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		javaMailSender.send(message);
	}

	// Send HTML Email
	public void sendPasswordEmail(String toEmail, String subject, String htmlContent) throws Exception {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		helper.setTo(toEmail);
		helper.setSubject(subject);
		helper.setText(htmlContent, true); // true = isHtml
		System.out.println("sedding email");
		javaMailSender.send(message);

	}
}
