package com.hcs_idn.api_assessment.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuthEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException{
        log.error("Unauthorized error: {}",  authException.getMessage());

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        BaseResponse<Object> errorResponse = BaseResponse.builder()
                .message("Unauthorized: " + authException.getMessage())
                .code(HttpStatus.UNAUTHORIZED.value())
                .data(null)
                .timestamp(LocalDateTime.now())
                .build();

//        final ObjectMapper mapper = new ObjectMapper();
//        mapper.findAndRegisterModules();
//        mapper.writeValue(response.getOutputStream(), errorResponse);

        response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
    }
}
