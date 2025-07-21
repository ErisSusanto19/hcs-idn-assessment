package com.hcs_idn.api_assessment.services;

import com.hcs_idn.api_assessment.dtos.response.AmountPerEntityResponseDTO;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ReportService {

    BigDecimal getTotalSpendingByCustomer(UUID customerId, LocalDateTime startDate, LocalDateTime endDate);

    List<AmountPerEntityResponseDTO> getTotalSpendingPerTax();

    List<AmountPerEntityResponseDTO> getTotalSpendingPerProduct();
}