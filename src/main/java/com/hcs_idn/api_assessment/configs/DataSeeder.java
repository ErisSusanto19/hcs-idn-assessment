package com.hcs_idn.api_assessment.configs;

import com.hcs_idn.api_assessment.entities.Account;
import com.hcs_idn.api_assessment.entities.Role;
import com.hcs_idn.api_assessment.entities.User;
import com.hcs_idn.api_assessment.enums.UserRole;
import com.hcs_idn.api_assessment.exceptions.customs.NotFound;
import com.hcs_idn.api_assessment.repositories.AccountRepository;
import com.hcs_idn.api_assessment.repositories.RoleRepository;
import com.hcs_idn.api_assessment.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin-username}")
    private String adminUsername;

    @Value("${app.admin-email}")
    private String adminEmail;

    @Value("${app.admin-password}")
    private String adminPassword;

    @PostConstruct
    @Transactional
    public void seedData() {
        seedRoles();
        seedAdmin();
    }

    private void seedRoles() {
        for (UserRole roleName : UserRole.values()) {
            if (roleRepository.findByName(roleName).isEmpty()) {
                Role newRole = new Role();
                newRole.setName(roleName);
                roleRepository.save(newRole);
                log.info("Role {} has been created.", roleName);
            }
        }
    }

    private void seedAdmin() {
        Optional<Account> existingAdminAccount = accountRepository.findByUsername(adminUsername);
        if (existingAdminAccount.isEmpty()) {
            log.info("Admin user not found, creating one...");

            Role adminRole = roleRepository.findByName(UserRole.ADMIN)
                    .orElseThrow(() -> new NotFound("Error: ADMIN role is not found."));

            Account adminAccount = new Account();
            adminAccount.setUsername(adminUsername);
            adminAccount.setEmail(adminEmail);
            adminAccount.setPassword(passwordEncoder.encode(adminPassword));
            adminAccount.setRoles(Collections.singleton(adminRole));

            User adminUser = new User();
            adminUser.setFullName("Default Admin User");

            adminUser.setAccount(adminAccount);

            userRepository.save(adminUser);
            log.info("Admin user has been created successfully.");
        } else {
            log.info("Admin user already exists.");
        }
    }
}
