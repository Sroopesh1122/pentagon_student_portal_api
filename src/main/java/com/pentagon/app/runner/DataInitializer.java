package com.pentagon.app.runner;

import java.time.LocalDateTime;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pentagon.app.entity.Admin;
import com.pentagon.app.repository.AdminRepository;

@Configuration
public class DataInitializer {

	@Bean
    CommandLineRunner initAdmin(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (adminRepository.findByEmail("shivuroopesh6362@gmail.com").isEmpty()) {
                Admin admin = new Admin();
                admin.setAdminId("ADM001");
                admin.setName("Shivuroopesh");
                admin.setEmail("shivuroopesh6362@gmail.com");
                admin.setMobile("6363636363");
                admin.setPassword(passwordEncoder.encode("Admin123")); // Raw password
                admin.setCreatedAt(LocalDateTime.now());
                adminRepository.save(admin);
                System.out.println("✅ Default admin created.");
            } else {
                System.out.println("ℹ️ Default admin already exists.");
            }
        };
    }
}
