package com.pentagon.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PengtagonApplication {

	public static void main(String[] args) {
		SpringApplication.run(PengtagonApplication.class, args);
	}

}
