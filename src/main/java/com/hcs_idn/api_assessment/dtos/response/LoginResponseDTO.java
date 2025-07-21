package com.hcs_idn.api_assessment.dtos.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
public class LoginResponseDTO {
    private String token;
    private UUID id;
    private String username;
    private String email;
    private Collection<? extends GrantedAuthority> roles;
}
