package com.pentagon.app.utils;


import org.springframework.context.annotation.Bean;

import org.springframework.stereotype.Component;

@Component
public class HtmlContent {

	
	public String getHtmlContent(String name, String email,String password)
	{
		String htmlContent = "<!DOCTYPE html>" + "<html>" + "<body style='font-family: Arial, sans-serif;'>"
				+ "<h2 style='color: #2e6c80;'>Welcome to Pentagon Portal</h2>" + "<p>Hi <strong>"
				+ name + "</strong>,</p>"
				+ "<p>Your account has been created by the Admin. Please find your login details below:</p>"
				+ "<ul>" + "<li><strong>Email:</strong> " + email + "</li>"
				+ "<li><strong>Temporary Password:</strong> " +password  + "</li>" + "</ul>"
				+ "<p>Please log in and change your password immediately for security purposes.</p>"
				+ "<br><p>Regards,<br/>Pentagon Team</p>" + "</body>" + "</html>";
		return htmlContent;
	}
	
	public String getLoginEmailHtmlContent(String name, String email,String password)
	{
		String htmlContent = "<!DOCTYPE html>" + "<html>" + "<body style='font-family: Arial, sans-serif;'>"
				+ "<h2 style='color: #2e6c80;'>Welcome to Pentagon Portal</h2>" + "<p>Hi <strong>"
				+ name + "</strong>,</p>"
				+ "<p>Your account has been created by the Admin. Please find your login details below:</p>"
				+ "<ul>" + "<li><strong>Email:</strong> " + email + "</li>"
				+ "<li><strong>Temporary Password:</strong> " +password  + "</li>" + "</ul>"
				+ "<p>Please log in and change your password immediately for security purposes.</p>"
				+ "<br><p>Regards,<br/>Pentagon Team</p>" + "</body>" + "</html>";
		return htmlContent;
	}
}
