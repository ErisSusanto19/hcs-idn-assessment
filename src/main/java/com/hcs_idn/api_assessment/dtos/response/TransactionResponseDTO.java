package com.hcs_idn.api_assessment.dtos.response;

import com.hcs_idn.api_assessment.enums.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class TransactionResponseDTO {
    private UUID id;
    private LocalDateTime transactionTime;
    private PaymentStatus paymentStatus;
    private String paymentMethod;

    private BigDecimal netAmount;
    private BigDecimal totalTax;
    private BigDecimal totalAmountPaid;

    private CustomerSummaryDTO customer;
    private UserSummaryDTO createdBy;

    private Set<TransactionDetailResponseDTO> details;
}
