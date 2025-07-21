package com.hcs_idn.api_assessment.repositories;

import com.hcs_idn.api_assessment.entities.Tax;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TaxRepository extends JpaRepository<Tax, UUID>, JpaSpecificationExecutor<Tax> {
    Optional<Tax> findByName(String name);
}
