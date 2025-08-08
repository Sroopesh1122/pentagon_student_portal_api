package com.pentagon.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableScheduling
@EnableAsync
@EnableMethodSecurity
public class PengtagonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PengtagonApplication.class, args);
	}

}
