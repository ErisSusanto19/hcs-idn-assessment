package com.hcs_idn.api_assessment.services.implementation;

import com.hcs_idn.api_assessment.dtos.request.TransactionCreateRequestDTO;
import com.hcs_idn.api_assessment.dtos.request.TransactionItemRequestDTO;
import com.hcs_idn.api_assessment.dtos.response.*;
import com.hcs_idn.api_assessment.entities.*;
import com.hcs_idn.api_assessment.enums.PaymentStatus;
import com.hcs_idn.api_assessment.exceptions.customs.NotFound;
import com.hcs_idn.api_assessment.repositories.*;
import com.hcs_idn.api_assessment.services.TransactionService;
import com.hcs_idn.api_assessment.specification.TransactionSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public TransactionResponseDTO createTransaction(TransactionCreateRequestDTO createRequestDTO) {

        Customer customer = customerRepository.findById(createRequestDTO.getCustomerId())
                .orElseThrow(() -> new NotFound("Customer not found"));

        User createdByStaff = getCurrentLoggedInStaff();

        Set<UUID> productIds = createRequestDTO.getItems().stream()
                .map(TransactionItemRequestDTO::getProductId).collect(Collectors.toSet());
        Map<UUID, Product> productMap = productRepository.findAllById(productIds).stream()
                .collect(Collectors.toMap(Product::getId, Function.identity()));

        BigDecimal totalNetAmount = BigDecimal.ZERO;
        BigDecimal totalTaxAmount = BigDecimal.ZERO;
        Set<TransactionDetail> transactionDetails = new HashSet<>();

        for (TransactionItemRequestDTO itemDTO : createRequestDTO.getItems()) {
            Product product = productMap.get(itemDTO.getProductId());
            if (product == null) {
                throw new NotFound("Product not found with id: " + itemDTO.getProductId());
            }

            BigDecimal quantity = new BigDecimal(itemDTO.getQuantity());
            BigDecimal price = product.getPrice();
            BigDecimal lineItemNetAmount = price.multiply(quantity);

            BigDecimal lineItemTaxAmount = BigDecimal.ZERO;
            for (Tax tax : product.getTaxes()) {
                BigDecimal taxRate = tax.getRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                lineItemTaxAmount = lineItemTaxAmount.add(lineItemNetAmount.multiply(taxRate));
            }

            totalNetAmount = totalNetAmount.add(lineItemNetAmount);
            totalTaxAmount = totalTaxAmount.add(lineItemTaxAmount);

            TransactionDetail detail = new TransactionDetail();
            detail.setProduct(product);
            detail.setQuantity(itemDTO.getQuantity());
            detail.setPriceAtTransaction(price);
            detail.setTaxAmountAtTransaction(lineItemTaxAmount.setScale(2, RoundingMode.HALF_UP));
            transactionDetails.add(detail);
        }

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setCreatedBy(createdByStaff);
        transaction.setTransactionTime(createRequestDTO.getTransactionTime());
        transaction.setPaymentStatus(createRequestDTO.getPaymentStatus());
        transaction.setPaymentMethod(createRequestDTO.getPaymentMethod());
        transaction.setNetAmount(totalNetAmount.setScale(2, RoundingMode.HALF_UP));
        transaction.setTotalTax(totalTaxAmount.setScale(2, RoundingMode.HALF_UP));
        transaction.setTotalAmountPaid(totalNetAmount.add(totalTaxAmount).setScale(2, RoundingMode.HALF_UP));

        for (TransactionDetail detail : transactionDetails) {
            detail.setTransaction(transaction);
        }
        transaction.setTransactionDetails(transactionDetails);

        Transaction savedTransaction = transactionRepository.save(transaction);
        return mapToResponseDTO(savedTransaction);
    }

    @Override
    @Transactional(readOnly = true)
    public BaseResponse<List<TransactionResponseDTO>> getAllTransactions(
            Pageable pageable, LocalDateTime startDate, LocalDateTime endDate, String customerName,
            List<PaymentStatus> statuses, String paymentMethod, UUID staffId) {

        Specification<Transaction> spec = Specification.where(null);
        if(startDate != null) spec = spec.and(TransactionSpecification.hasDateGreaterThanOrEqual(startDate));
        if(endDate != null) spec = spec.and(TransactionSpecification.hasDateLessThanOrEqual(endDate));
        if(customerName != null && !customerName.isEmpty()) spec = spec.and(TransactionSpecification.customerNameLike(customerName));
        if(statuses != null && !statuses.isEmpty()) spec = spec.and(TransactionSpecification.statusIn(statuses));
        if(paymentMethod != null && !paymentMethod.isEmpty()) spec = spec.and(TransactionSpecification.paymentMethodLike(paymentMethod));
        if(staffId != null) spec = spec.and(TransactionSpecification.createdBy(staffId));

        Page<Transaction> transactionPage = transactionRepository.findAll(spec, pageable);

        List<TransactionResponseDTO> dtos = transactionPage.getContent().stream().map(this::mapToResponseDTO).collect(Collectors.toList());
        PaginationResponse pagination = PaginationResponse.builder()
                .currentPage(transactionPage.getNumber())
                .pageSize(transactionPage.getSize())
                .totalPages(transactionPage.getTotalPages())
                .totalElements((int) transactionPage.getTotalElements())
                .build();

        return BaseResponse.<List<TransactionResponseDTO>>builder()
                .message("Successfully retrieved transactions.")
                .code(HttpStatus.OK.value())
                .data(dtos)
                .pagination(pagination)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDTO getTransactionById(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NotFound("Transaction not found with id: " + transactionId));
        return mapToResponseDTO(transaction);
    }

    // --- Helper Methods ---
    private User getCurrentLoggedInStaff() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new NotFound("Logged in user not found."))
                .getUser();
    }

    private TransactionResponseDTO mapToResponseDTO(Transaction transaction) {
        if (transaction == null) {
            return null;
        }

        TransactionResponseDTO responseDTO = new TransactionResponseDTO();
        responseDTO.setId(transaction.getId());
        responseDTO.setTransactionTime(transaction.getTransactionTime());
        responseDTO.setPaymentStatus(transaction.getPaymentStatus());
        responseDTO.setPaymentMethod(transaction.getPaymentMethod());
        responseDTO.setNetAmount(transaction.getNetAmount());
        responseDTO.setTotalTax(transaction.getTotalTax());
        responseDTO.setTotalAmountPaid(transaction.getTotalAmountPaid());

        if (transaction.getCustomer() != null) {
            CustomerSummaryDTO customerSummary = new CustomerSummaryDTO();
            customerSummary.setId(transaction.getCustomer().getId());
            customerSummary.setName(transaction.getCustomer().getName());
            customerSummary.setEmail(transaction.getCustomer().getAccount().getEmail());
            responseDTO.setCustomer(customerSummary);
        }

        if (transaction.getCreatedBy() != null) {
            UserSummaryDTO userSummary = new UserSummaryDTO();
            userSummary.setId(transaction.getCreatedBy().getId());
            userSummary.setFullName(transaction.getCreatedBy().getFullName());
            responseDTO.setCreatedBy(userSummary);
        }

        if (transaction.getTransactionDetails() != null) {
            Set<TransactionDetailResponseDTO> detailDTOs = transaction.getTransactionDetails().stream()
                    .map(this::mapDetailToResponseDTO)
                    .collect(Collectors.toSet());
            responseDTO.setDetails(detailDTOs);
        }

        return responseDTO;
    }

    private TransactionDetailResponseDTO mapDetailToResponseDTO(TransactionDetail detail) {
        TransactionDetailResponseDTO detailDTO = new TransactionDetailResponseDTO();
        detailDTO.setQuantity(detail.getQuantity());

        detailDTO.setPricePerUnit(detail.getPriceAtTransaction());

        BigDecimal quantity = new BigDecimal(detail.getQuantity());
        BigDecimal lineItemNetAmount = detail.getPriceAtTransaction().multiply(quantity);
        BigDecimal lineItemTaxAmount = detail.getTaxAmountAtTransaction();

        detailDTO.setLineItemNetAmount(lineItemNetAmount.setScale(2, RoundingMode.HALF_UP));
        detailDTO.setLineItemTaxAmount(lineItemTaxAmount.setScale(2, RoundingMode.HALF_UP));
        detailDTO.setLineItemTotalAmount(lineItemNetAmount.add(lineItemTaxAmount).setScale(2, RoundingMode.HALF_UP));

        if (detail.getProduct() != null) {
            ProductSummaryDTO productSummary = new ProductSummaryDTO();
            productSummary.setId(detail.getProduct().getId());
            productSummary.setName(detail.getProduct().getName());
            detailDTO.setProduct(productSummary);
        }

        return detailDTO;
    }
}
