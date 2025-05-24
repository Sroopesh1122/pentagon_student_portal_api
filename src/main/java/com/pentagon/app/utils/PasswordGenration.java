package com.pentagon.app.utils;

import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class PasswordGenration {

	public String generateRandomPassword(int length) {
	    String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789@#$!";
	    SecureRandom random = new SecureRandom();
	    StringBuilder password = new StringBuilder(length);
	    for (int i = 0; i < length; i++) {
	        password.append(chars.charAt(random.nextInt(chars.length())));
	    }
	    return password.toString();
	}
}
