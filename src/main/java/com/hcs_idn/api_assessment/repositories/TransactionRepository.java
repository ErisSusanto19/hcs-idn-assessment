package com.hcs_idn.api_assessment.repositories;

import com.hcs_idn.api_assessment.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    @Query("SELECT SUM(t.totalAmountPaid) FROM Transaction t " +
            "WHERE t.customer.id = :customerId AND t.transactionTime BETWEEN :startDate AND :endDate")
    BigDecimal findTotalSpendingByCustomerBetweenDates(
            @Param("customerId") UUID customerId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    @Query("SELECT SUM(t.totalAmountPaid) FROM Transaction t WHERE t.customer.id = :customerId")
    BigDecimal findTotalSpendingByCustomer(@Param("customerId") UUID customerId);

    @Query("SELECT tx.name, SUM(td.taxAmountAtTransaction) " +
            "FROM TransactionDetail td " +
            "JOIN td.product p " +
            "JOIN p.taxes tx " +
            "GROUP BY tx.name")
    List<Object[]> findTotalSpendingPerTax();

    @Query("SELECT td.product.name, SUM(td.quantity * td.priceAtTransaction + td.taxAmountAtTransaction) " +
            "FROM TransactionDetail td " +
            "GROUP BY td.product.name")
    List<Object[]> findTotalSpendingPerProduct();
}
