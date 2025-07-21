package com.hcs_idn.api_assessment.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TaxRequestDTO {
    private String name;
    private BigDecimal rate;
}
