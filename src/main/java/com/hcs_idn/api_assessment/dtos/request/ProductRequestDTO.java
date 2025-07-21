package com.hcs_idn.api_assessment.dtos.request;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public class ProductRequestDTO {
    private String name;
    private BigDecimal price;
    private Set<UUID> taxIds;
}
