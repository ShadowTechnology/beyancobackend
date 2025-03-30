package com.beyanco.api.config;

import com.beyanco.api.model.ERole;
import com.beyanco.api.model.Role;
import com.beyanco.api.model.User;
import com.beyanco.api.repository.RoleRepository;
import com.beyanco.api.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * Database initializer for development with H2 database.
 * Automatically disabled when using MySQL with Flyway migrations.
 */
@Component
@ConditionalOnProperty(name = "spring.flyway.enabled", havingValue = "false")
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Running DatabaseInitializer for H2 database...");

        // Initialize roles if they don't exist
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_USER));
            roleRepository.save(new Role(ERole.ROLE_MODERATOR));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));

            System.out.println("Roles initialized");
        }

        // Initialize admin user if no users exist
        if (userRepository.count() == 0) {
            User admin = new User(
                "admin",
                "admin@beyanco.com",
                passwordEncoder.encode("password123")
            );

            admin.setFirstName("Admin");
            admin.setLastName("User");
            admin.setCompanyName("Beyanco");
            admin.setSubscriptionType("premium");
            admin.setSubscriptionStatus("active");
            admin.setCreditsRemaining(999);

            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Admin Role not found."));
            roles.add(adminRole);

            admin.setRoles(roles);

            userRepository.save(admin);

            System.out.println("Admin user initialized");
        }
    }
}
