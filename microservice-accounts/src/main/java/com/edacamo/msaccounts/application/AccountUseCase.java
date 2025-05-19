package com.edacamo.msaccounts.application;

import com.edacamo.msaccounts.domain.model.Account;
import com.edacamo.msaccounts.domain.repositories.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AccountUseCase {

    private final AccountRepository repository;

    public Account create(Account account) {
        if (repository.existsByNumeroCuenta(account.getNumeroCuenta())) {
            throw new IllegalArgumentException("Cuenta con número " + account.getNumeroCuenta() + " ya existe");
        }
        return repository.save(account);
    }

    public List<Account> getAll() {
        return repository.findAll();
    }

    public Account getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NoSuchElementException("Cuenta no encontrada"));
    }

    public Account update(Long id, Account account) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Cuenta no encontrada para actualizar");
        }
        account.setId(id); // asegura que se actualice y no cree uno nuevo
        return repository.save(account);
    }

    public void delete(Long id) {
        int deleted = repository.deleteById(id);
        if (deleted == 0) {
            throw new NoSuchElementException("Cuenta no encontrada o ya eliminada");
        }
    }
}
