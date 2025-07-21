package com.hcs_idn.api_assessment.dtos.request;

import com.hcs_idn.api_assessment.enums.PaymentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionUpdateDTO {
    private PaymentStatus paymentStatus;
    private String paymentMethod;
}
