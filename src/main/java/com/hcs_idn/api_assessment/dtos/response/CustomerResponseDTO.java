package com.hcs_idn.api_assessment.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CustomerResponseDTO {
    private UUID id;
    private String name;
    private LocalDate birthdate;
    private String birthplace;

    private String username;
    private String email;

    private UserSummaryDTO createdBy;
    private UserSummaryDTO updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
