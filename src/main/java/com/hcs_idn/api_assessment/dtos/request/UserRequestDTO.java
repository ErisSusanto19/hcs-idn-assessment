package com.hcs_idn.api_assessment.dtos.request;

import com.hcs_idn.api_assessment.enums.UserRole;

import java.util.Set;

public class UserRequestDTO {
    private String fullName;
    private String username;
    private String email;
    private String password;
    private Set<UserRole> roles;
}
