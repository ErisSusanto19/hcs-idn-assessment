package com.hcs_idn.api_assessment.services;

import com.hcs_idn.api_assessment.dtos.request.TransactionCreateRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.TransactionResponseDTO;
import com.hcs_idn.api_assessment.enums.PaymentStatus;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionService {

    TransactionResponseDTO createTransaction(TransactionCreateRequestDTO createRequestDTO);

    TransactionResponseDTO getTransactionById(UUID transactionId);

    BaseResponse<List<TransactionResponseDTO>> getAllTransactions(
            Pageable pageable,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String customerName,
            List<PaymentStatus> statuses,
            String paymentMethod,
            UUID staffId
    );
}