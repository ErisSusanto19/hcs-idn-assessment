package com.hcs_idn.api_assessment.dtos.request;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class TransactionItemRequestDTO {
    private UUID productId;
    private Integer quantity;
}
