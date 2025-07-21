package com.hcs_idn.api_assessment.controllers;

import com.hcs_idn.api_assessment.dtos.request.TransactionCreateRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.TransactionResponseDTO;
import com.hcs_idn.api_assessment.enums.PaymentStatus;
import com.hcs_idn.api_assessment.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<TransactionResponseDTO>> createTransaction(@RequestBody TransactionCreateRequestDTO createRequestDTO) {
        TransactionResponseDTO createdTransaction = transactionService.createTransaction(createRequestDTO);
        BaseResponse<TransactionResponseDTO> response = BaseResponse.<TransactionResponseDTO>builder()
                .message("Transaction created successfully.")
                .code(HttpStatus.CREATED.value())
                .data(createdTransaction)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<List<TransactionResponseDTO>>> getAllTransactions(
            @PageableDefault(sort = "transactionTime", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) List<PaymentStatus> statuses,
            @RequestParam(required = false) String paymentMethod,
            @RequestParam(required = false) UUID staffId
    ) {
        BaseResponse<List<TransactionResponseDTO>> response = transactionService.getAllTransactions(
                pageable, startDate, endDate, customerName, statuses, paymentMethod, staffId
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<BaseResponse<TransactionResponseDTO>> getTransactionById(@PathVariable UUID id) {
        TransactionResponseDTO transaction = transactionService.getTransactionById(id);
        BaseResponse<TransactionResponseDTO> response = BaseResponse.<TransactionResponseDTO>builder()
                .message("Transaction retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(transaction)
                .build();
        return ResponseEntity.ok(response);
    }
}
