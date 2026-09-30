package com.thanhdat.jwtnimbus.configs;

import com.thanhdat.jwtnimbus.entities.Role;
import com.thanhdat.jwtnimbus.entities.User;
import com.thanhdat.jwtnimbus.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeUsers(
        UserRepository userRepository,
        PasswordEncoder passwordEncoder,
        @Value("${app.admin.email}") String adminEmail,
        @Value("${app.admin.password}") String adminPassword,
        @Value("${app.admin.full-name}") String adminFullName
    ) {
        return arguments -> {
            String normalizedAdminEmail =
                adminEmail.trim().toLowerCase();

            if (
                !userRepository.existsByEmailIgnoreCase(
                    normalizedAdminEmail
                )
            ) {
                User administrator = new User(
                    adminFullName,
                    normalizedAdminEmail,
                    passwordEncoder.encode(adminPassword),
                    Role.ADMIN,
                    true,
                    true
                );

                userRepository.save(administrator);
            }

            String sampleEmail = "user@example.com";

            if (
                !userRepository.existsByEmailIgnoreCase(
                    sampleEmail
                )
            ) {
                User sampleUser = new User(
                    "Nguyễn Văn User",
                    sampleEmail,
                    passwordEncoder.encode("123456"),
                    Role.USER,
                    true,
                    true
                );

                userRepository.save(sampleUser);
            }

            System.out.println(
                "JWT database users: " + userRepository.count()
            );

            System.out.println(
                "JWT seed administrator: " + normalizedAdminEmail
            );
        };
    }
}
