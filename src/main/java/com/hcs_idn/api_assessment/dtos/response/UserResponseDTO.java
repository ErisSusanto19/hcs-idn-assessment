package com.hcs_idn.api_assessment.dtos.response;

import com.hcs_idn.api_assessment.enums.UserRole;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public class UserResponseDTO {
    private UUID id;
    private String fullName;
    private String username;
    private String email;
    private Set<UserRole> roles;
    private LocalDateTime createdAt;
}
