package com.hcs_idn.api_assessment.dtos.response;

import com.hcs_idn.api_assessment.enums.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserResponseDTO {
    private UUID id;
    private String fullName;
    private String username;
    private String email;
    private Set<UserRole> roles;
    private LocalDateTime createdAt;
}
