package com.edacamo.msaccounts.infrastructure.persistence.jpa;

import com.edacamo.msaccounts.infrastructure.persistence.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaAccountRepository extends JpaRepository<AccountEntity, Long> {
    boolean existsByNumeroCuenta(String numeroCuenta);
    Optional<AccountEntity> findByNumeroCuenta(String numeroCuenta);
}
