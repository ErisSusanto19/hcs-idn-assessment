package com.hcs_idn.api_assessment.dtos.response;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class ProductResponseDTO {
    private UUID id;
    private String name;
    private BigDecimal price;
    private Set<TaxResponseDTO> taxes;
}
