package com.hcs_idn.api_assessment.configs;

import com.hcs_idn.api_assessment.entities.Role;
import com.hcs_idn.api_assessment.entities.User;
import com.hcs_idn.api_assessment.enums.UserRole;
import com.hcs_idn.api_assessment.services.RoleService;
import com.hcs_idn.api_assessment.services.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AdminSeeder {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin-username}")
    private String adminUsername;

    @Value("${app.admin-email}")
    private String adminEmail;

    @Value("${app.admin-password}")
    private String adminPassword;

    @PostConstruct
    @Transactional
    public void initAdmin(){
        Role adminRole = roleService.getOrCreateRole(UserRole.ADMIN);

        User adminUser = User.builder()
                .username(adminUsername)
                .email(adminEmail)
                .password(passwordEncoder.encode(adminPassword))
                .role(adminRole)
                .isAccountNonExpired(true)
                .isAccountNonLocked(true)
                .isCredentialsNonExpired(true)
                .isEnabled(true)
                .build();

        userService.addUserAsEntity(adminUser);
    }
}
