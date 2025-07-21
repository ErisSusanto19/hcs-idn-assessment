package com.hcs_idn.api_assessment.dtos.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ProductResponseDTO {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Set<TaxResponseDTO> taxes;
}
