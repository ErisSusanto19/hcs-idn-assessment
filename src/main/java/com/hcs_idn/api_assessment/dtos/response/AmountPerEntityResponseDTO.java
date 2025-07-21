package com.hcs_idn.api_assessment.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class AmountPerEntityResponseDTO {
    private String entityName;
    private BigDecimal totalAmount;
}
