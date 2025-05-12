package com.hcs_idn.api_assessment.repositories;

import com.hcs_idn.api_assessment.entities.Role;
import com.hcs_idn.api_assessment.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID>, JpaSpecificationExecutor<Role> {
    Optional<Role> findByName(UserRole name);
}
