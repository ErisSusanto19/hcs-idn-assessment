package com.hcs_idn.api_assessment.services.implementation;

import com.hcs_idn.api_assessment.entities.Role;
import com.hcs_idn.api_assessment.enums.UserRole;
import com.hcs_idn.api_assessment.repositories.RoleRepository;
import com.hcs_idn.api_assessment.services.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    @Transactional
    public Role getOrCreateRole(UserRole name){
        return roleRepository.findByName(name)
                .orElseGet(() -> {
                    Role newRole = Role.builder().name(name).build();
                    return roleRepository.save(newRole);
                });
    }
}
