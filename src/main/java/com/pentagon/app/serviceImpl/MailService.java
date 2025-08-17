//package com.pentagon.app.serviceImpl;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.mail.SimpleMailMessage;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
//import jakarta.mail.internet.MimeMessage;
//
//
//@Service
//public class MailService {
//
//	@Autowired
//	private JavaMailSender javaMailSender;
//
//
//	// Send HTML Email
//	public void send(String toEmail, String subject, String htmlContent) throws Exception {
//		try {
//			MimeMessage message = javaMailSender.createMimeMessage();
//			MimeMessageHelper helper = new MimeMessageHelper(message, true);
//			helper.setTo(toEmail);
//			helper.setSubject(subject);
//			helper.setText(htmlContent, true); // true = isHtml
//			javaMailSender.send(message);
//		} catch (Exception e) {
//			throw new RuntimeException("Failed to send email", e);
//		}
//
//	}
//	
//	public void sendWithBcc(String toEmail, String subject, String htmlContent, List<String> bccList) throws Exception {
//	    try {
//	        MimeMessage message = javaMailSender.createMimeMessage();
//	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//	        if (toEmail != null && !toEmail.trim().isEmpty()) {
//	            helper.setTo(toEmail);
//	        }
//	        if (bccList != null && !bccList.isEmpty()) {
//	            helper.setBcc(bccList.toArray(new String[0]));
//	        }
//
//	        helper.setSubject(subject);
//	        helper.setText(htmlContent, true); // true = isHtml
//	        javaMailSender.send(message);
//	    } catch (Exception e) {
//	        throw new RuntimeException("Failed to send email with BCC", e);
//	    }
//	}
//	
//	@Async
//    public void sendAsync(String toEmail, String subject, String htmlContent) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//            helper.setTo(toEmail);
//            helper.setSubject(subject);
//            helper.setText(htmlContent, true); // true = isHtml
//            javaMailSender.send(message);
//        } catch (Exception e) {
//            // You may want to log this exception instead of throwing it
//            throw new RuntimeException("Failed to send email asynchronously", e);
//        }
//    }
//
//    @Async
//    public void sendWithBccAsync(String toEmail, String subject, String htmlContent, List<String> bccList) {
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//            if (toEmail != null && !toEmail.trim().isEmpty()) {
//                helper.setTo(toEmail);
//            }
//            if (bccList != null && !bccList.isEmpty()) {
//                helper.setBcc(bccList.toArray(new String[0]));
//            }
//
//            helper.setSubject(subject);
//            helper.setText(htmlContent, true); // true = isHtml
//            javaMailSender.send(message);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to send email with BCC asynchronously", e);
//        }
//    }
//}


package com.pentagon.app.serviceImpl;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.*;

@Service
public class MailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Value("${sendgrid.from.email}")
    private String fromEmail;

    @Value("${sendgrid.from.name}")
    private String fromName;

    // ----------------------------
    // 1. Synchronous Send
    // ----------------------------
    public void send(String toEmail, String subject, String htmlContent) {
        Email from = new Email(fromEmail, fromName);
        Email to = new Email(toEmail);
        Content content = new Content("text/html", htmlContent);
        Mail mail = new Mail(from, subject, to, content);
        sendMail(mail);
    }

    // ----------------------------
    // 2. Send with BCC (sync)
    // ----------------------------
    public void sendWithBcc(String toEmail, String subject, String htmlContent, List<String> bccList) {
        Email from = new Email(fromEmail, fromName);
        Content content = new Content("text/html", htmlContent);

        Mail mail = new Mail();
        mail.setFrom(from);
        mail.setSubject(subject);
        mail.addContent(content);

        Personalization personalization = new Personalization();

        // If no toEmail is given, use fromEmail as placeholder
        if (toEmail == null || toEmail.trim().isEmpty()) {
            personalization.addTo(new Email(fromEmail, fromName));
        } else {
            personalization.addTo(new Email(toEmail));
        }

        // Add BCC recipients
        if (bccList != null && !bccList.isEmpty()) {
            for (String bccEmail : bccList) {
                personalization.addBcc(new Email(bccEmail));
            }
        }

        mail.addPersonalization(personalization);
        sendMail(mail);
    }

    // ----------------------------
    // 3. Asynchronous Send
    // ----------------------------
    @Async
    public void sendAsync(String toEmail, String subject, String htmlContent) {
        send(toEmail, subject, htmlContent);
    }

    // ----------------------------
    // 4. Asynchronous BCC Send
    // ----------------------------
    @Async
    public void sendWithBccAsync(String toEmail, String subject, String htmlContent, List<String> bccList) {
        sendWithBcc(toEmail, subject, htmlContent, bccList);
    }

    // ----------------------------
    // Common method to send using SendGrid
    // ----------------------------
    private void sendMail(Mail mail) {
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println("Status Code: " + response.getStatusCode());
            System.out.println("Body: " + response.getBody());
            System.out.println("Headers: " + response.getHeaders());

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email via SendGrid", e);
        }
    }
}


