package com.hcs_idn.api_assessment.services.implementation;

import com.hcs_idn.api_assessment.dtos.response.AmountPerEntityResponseDTO;
import com.hcs_idn.api_assessment.repositories.TransactionRepository;
import com.hcs_idn.api_assessment.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final TransactionRepository transactionRepository;

    @Override
    public BigDecimal getTotalSpendingByCustomer(UUID customerId, LocalDateTime startDate, LocalDateTime endDate) {
        BigDecimal total;
        if (startDate != null && endDate != null) {
            total = transactionRepository.findTotalSpendingByCustomerBetweenDates(customerId, startDate, endDate);
        } else {
            total = transactionRepository.findTotalSpendingByCustomer(customerId);
        }
        return total != null ? total : BigDecimal.ZERO;
    }

    @Override
    public List<AmountPerEntityResponseDTO> getTotalSpendingPerTax() {
        return transactionRepository.findTotalSpendingPerTax().stream()
                .map(result -> {
                    AmountPerEntityResponseDTO amountPerEntityResponseDTO = new AmountPerEntityResponseDTO();
                    amountPerEntityResponseDTO.setEntityName((String) result[0]);
                    amountPerEntityResponseDTO.setTotalAmount((BigDecimal) result[1]);

                    return amountPerEntityResponseDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AmountPerEntityResponseDTO> getTotalSpendingPerProduct() {
        return transactionRepository.findTotalSpendingPerProduct().stream()
                .map(result -> {
                    AmountPerEntityResponseDTO amountPerEntityResponseDTO = new AmountPerEntityResponseDTO();
                    amountPerEntityResponseDTO.setEntityName((String) result[0]);
                    amountPerEntityResponseDTO.setTotalAmount((BigDecimal) result[1]);

                    return amountPerEntityResponseDTO;
                })
                .collect(Collectors.toList());
    }
}
