package com.hcs_idn.api_assessment.controllers;

import com.hcs_idn.api_assessment.dtos.response.AmountPerEntityResponseDTO;
import com.hcs_idn.api_assessment.dtos.response.BaseResponse;
import com.hcs_idn.api_assessment.dtos.response.TotalSpentResponseDTO;
import com.hcs_idn.api_assessment.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/customer-spending")
    public ResponseEntity<BaseResponse<TotalSpentResponseDTO>> getCustomerSpending(
            @RequestParam UUID customerId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        BigDecimal total = reportService.getTotalSpendingByCustomer(customerId, startDate, endDate);
        TotalSpentResponseDTO data = new TotalSpentResponseDTO();
        data.setTotal(total);

        BaseResponse<TotalSpentResponseDTO> response = BaseResponse.<TotalSpentResponseDTO>builder()
                .message("Customer spending report retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/spending-per-tax")
    public ResponseEntity<BaseResponse<List<AmountPerEntityResponseDTO>>> getSpendingPerTax() {
        List<AmountPerEntityResponseDTO> data = reportService.getTotalSpendingPerTax();
        BaseResponse<List<AmountPerEntityResponseDTO>> response = BaseResponse.<List<AmountPerEntityResponseDTO>>builder()
                .message("Spending per tax report retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/spending-per-product")
    public ResponseEntity<BaseResponse<List<AmountPerEntityResponseDTO>>> getSpendingPerProduct() {
        List<AmountPerEntityResponseDTO> data = reportService.getTotalSpendingPerProduct();
        BaseResponse<List<AmountPerEntityResponseDTO>> response = BaseResponse.<List<AmountPerEntityResponseDTO>>builder()
                .message("Spending per product report retrieved successfully.")
                .code(HttpStatus.OK.value())
                .data(data)
                .build();
        return ResponseEntity.ok(response);
    }
}