package com.hcs_idn.api_assessment.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
public class TaxResponseDTO {
    private UUID id;
    private String name;
    private BigDecimal rate;
}
