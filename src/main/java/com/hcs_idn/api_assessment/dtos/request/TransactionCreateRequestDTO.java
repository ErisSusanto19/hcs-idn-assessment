package com.hcs_idn.api_assessment.dtos.request;

import com.hcs_idn.api_assessment.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class TransactionCreateRequestDTO {
    private UUID customerId;
    private LocalDateTime transactionTime;
    private PaymentStatus paymentStatus; // PAID, NOT_PAID, CANCELLED
    private String paymentMethod;
    private Set<TransactionItemRequestDTO> items;
}
