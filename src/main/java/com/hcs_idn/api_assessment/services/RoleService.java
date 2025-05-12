package com.hcs_idn.api_assessment.services;

import com.hcs_idn.api_assessment.entities.Role;
import com.hcs_idn.api_assessment.enums.UserRole;

public interface RoleService {
    Role getOrCreateRole(UserRole name);
}
