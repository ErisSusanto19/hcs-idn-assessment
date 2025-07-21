package com.hcs_idn.api_assessment.specification;

import com.hcs_idn.api_assessment.entities.Transaction;
import com.hcs_idn.api_assessment.enums.PaymentStatus;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class TransactionSpecification {
    public static Specification<Transaction> customerNameLike(String name) {
        return (root, query, builder) ->
                builder.like(builder.lower(root.join("customer", JoinType.INNER).get("name")), "%" + name.toLowerCase() + "%");
    }

    public static Specification<Transaction> hasDateGreaterThanOrEqual(LocalDateTime date) {
        return (root, query, builder) -> builder.greaterThanOrEqualTo(root.get("transactionTime"), date);
    }

    public static Specification<Transaction> hasDateLessThanOrEqual(LocalDateTime date) {
        return (root, query, builder) -> builder.lessThanOrEqualTo(root.get("transactionTime"), date);
    }

    public static Specification<Transaction> statusIn(List<PaymentStatus> statuses) {
        return (root, query, builder) -> root.get("paymentStatus").in(statuses);
    }

    public static Specification<Transaction> paymentMethodLike(String method) {
        return (root, query, builder) -> builder.like(builder.lower(root.get("paymentMethod")), "%" + method.toLowerCase() + "%");
    }

    public static Specification<Transaction> createdBy(UUID staffId) {
        return (root, query, builder) -> builder.equal(root.join("createdBy", JoinType.INNER).get("id"), staffId);
    }
}
