package com.hcs_idn.api_assessment.controllers;

import com.hcs_idn.api_assessment.dtos.request.LoginRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.LoginResponseDTO;
import com.hcs_idn.api_assessment.entities.Account;
import com.hcs_idn.api_assessment.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<BaseResponse<LoginResponseDTO>> authenticateUser(@RequestBody LoginRequestDTO loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getLogin(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtil.generateToken(authentication);
        Account userDetails = (Account) authentication.getPrincipal();

        LoginResponseDTO loginResponseData = new LoginResponseDTO();
        loginResponseData.setToken(jwt);
        loginResponseData.setId(userDetails.getId());
        loginResponseData.setUsername(userDetails.getUsername());
        loginResponseData.setEmail(userDetails.getEmail());
        loginResponseData.setRoles(userDetails.getAuthorities());

        BaseResponse<LoginResponseDTO> response = BaseResponse.<LoginResponseDTO>builder()
                .message("User authenticated successfully.")
                .code(HttpStatus.OK.value())
                .data(loginResponseData)
                .pagination(null)
                .build();

        return ResponseEntity.ok(response);
    }
}
