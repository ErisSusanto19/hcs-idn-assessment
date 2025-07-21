package com.hcs_idn.api_assessment.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class CustomerSummaryDTO {
    private UUID id;
    private String name;
    private String email;
}
