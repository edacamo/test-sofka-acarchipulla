package com.edacamo.msaccounts.domain.repositories;

import com.edacamo.msaccounts.domain.model.Account;

import java.util.List;
import java.util.Optional;

public interface AccountRepository {
    Account save(Account account);

    boolean existsByNumeroCuenta(String accountNumber);

    Optional<Account> findByNumeroCuenta(String accountNumber);

    Optional<Account> findById(Long id);

    List<Account> findAll();

    int deleteById(Long id);

    boolean existsById(Long id);
}
