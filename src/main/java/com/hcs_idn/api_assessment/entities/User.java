package com.hcs_idn.api_assessment.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private UUID id;

    @Column(columnDefinition = "varchar(100)", unique = true, nullable = false)
    private String username;

    @Column(columnDefinition = "varchar(100)", unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "varchar(100)", nullable = false)
    private String password;

    @Column(name = "role_id")
    private Role role;

    @Column(name = "is_account_non_expired")
    private boolean isAccountNonExpired;

    @Column(name = "is_account_non_locked")
    private boolean isAccountNonLocked;

    @Column(name = "is_credentials_non_expired")
    private boolean isCredentialsNonExpired;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.getName().name()));
    }

    @Override
    public boolean isAccountNonExpired(){return this.isAccountNonExpired;}

    @Override
    public boolean isAccountNonLocked(){return this.isAccountNonLocked;}

    @Override
    public boolean isCredentialsNonExpired(){return this.isCredentialsNonExpired;}

    @Override
    public boolean isEnabled(){return this.isEnabled;}
}
