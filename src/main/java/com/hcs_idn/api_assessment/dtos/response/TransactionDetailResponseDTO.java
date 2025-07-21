package com.hcs_idn.api_assessment.dtos.response;

import java.math.BigDecimal;

public class TransactionDetailResponseDTO {
    private ProductSummaryDTO product;

    private Integer quantity;

    private BigDecimal pricePerUnit;
    private BigDecimal lineItemNetAmount;
    private BigDecimal lineItemTaxAmount;
    private BigDecimal lineItemTotalAmount;
}
