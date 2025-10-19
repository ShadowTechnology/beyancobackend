package com.beyancoback.beyanco.service;

import com.beyancoback.beyanco.model.*;
import com.beyancoback.beyanco.repo.RoleRepository;
import com.beyancoback.beyanco.repo.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(RoleRepository roleRepository,
                                   UserRepo userRepo,
                                   PasswordEncoder passwordEncoder) {
        return args -> {
            // 1. Insert roles if not present
            for (ERole role : ERole.values()) {
                if (!roleRepository.existsByName(role)) {
                    roleRepository.save(new Role(role));
                }
            }

            // 2. Create default admin user if not present
            if (!userRepo.existsByUsername("admin")) {
                Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                        .orElseThrow(() -> new RuntimeException("Admin role not found"));

                User admin = new User(
                        "admin",
                        "admin@system.com",
                        passwordEncoder.encode("admin123")
                );
                admin.setRoles(Collections.singleton(adminRole));
                admin.setFirstName("System");
                admin.setLastName("Administrator");
                admin.setCompanyName("Beyanco");
                admin.setSubscriptionType("FULL");
                admin.setSubscriptionStatus("ACTIVE");
                admin.setCreditsRemaining(9999);

                userRepo.save(admin);
                System.out.println("✅ Default admin user created: username=admin, password=admin123");
            }
        };
    }
}
