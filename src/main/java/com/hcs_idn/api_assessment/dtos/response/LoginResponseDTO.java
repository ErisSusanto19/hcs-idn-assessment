package com.hcs_idn.api_assessment.dtos.response;

import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class LoginResponseDTO {
    private String token;
    private String username;
    private Collection<? extends GrantedAuthority> roles;
}
