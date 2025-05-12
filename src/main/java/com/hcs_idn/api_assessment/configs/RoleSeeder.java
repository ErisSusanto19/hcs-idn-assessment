package com.hcs_idn.api_assessment.configs;

import com.hcs_idn.api_assessment.enums.UserRole;
import com.hcs_idn.api_assessment.services.RoleService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class RoleSeeder {
    private final RoleService roleService;

    @PostConstruct
    @Transactional
    public void initRole(){
        for(UserRole userRole: UserRole.values()){
            roleService.getOrCreateRole(userRole);
        }
    }
}
