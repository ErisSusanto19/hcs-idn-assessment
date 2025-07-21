package com.hcs_idn.api_assessment.repositories;

import com.hcs_idn.api_assessment.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUsernameOrEmail(String username, String email);
    Optional<Account> findByUsername(String username);
    Optional<Account> findByEmail(String email);
}
