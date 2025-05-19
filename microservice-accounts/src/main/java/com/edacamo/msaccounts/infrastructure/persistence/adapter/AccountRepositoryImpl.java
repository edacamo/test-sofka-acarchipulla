package com.edacamo.msaccounts.infrastructure.persistence.adapter;

import com.edacamo.msaccounts.domain.model.Account;
import com.edacamo.msaccounts.domain.repositories.AccountRepository;
import com.edacamo.msaccounts.domain.repositories.ClientSnapshotRepository;
import com.edacamo.msaccounts.infrastructure.persistence.entity.AccountEntity;
import com.edacamo.msaccounts.infrastructure.persistence.entity.ClientSnapshotEntity;
import com.edacamo.msaccounts.infrastructure.persistence.jpa.JpaAccountRepository;
import com.edacamo.msaccounts.infrastructure.persistence.jpa.JpaClientSnapshotRepository;
import com.edacamo.msaccounts.infrastructure.persistence.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class AccountRepositoryImpl implements AccountRepository {

    private final JpaAccountRepository jpaAccountRepository;
    private final JpaClientSnapshotRepository clientSnapshotRepository;

    @Override
    public Account save(Account account) {
        // Cargar el cliente desde la base de datos
        ClientSnapshotEntity clientEntity = clientSnapshotRepository.findByClienteId(account.getClient().getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente con ID " + account.getClient().getClienteId() + " no encontrado"));

        AccountEntity entity = AccountMapper.toEntity(account, clientEntity);

        AccountEntity saved = jpaAccountRepository.save(entity);

        return AccountMapper.toDomain(saved);
    }

    @Override
    public boolean existsByNumeroCuenta(String accountNumber) {
        return this.jpaAccountRepository.existsByNumeroCuenta(accountNumber);
    }

    @Override
    public Optional<Account> findByNumeroCuenta(String accountNumber) {
        return jpaAccountRepository.findByNumeroCuenta(accountNumber)
                .map(AccountMapper::toDomain);
    }

    @Override
    public Optional<Account> findById(Long id) {
        return jpaAccountRepository.findById(id).map(AccountMapper::toDomain);
    }

    @Override
    public List<Account> findAll() {
        return jpaAccountRepository.findAll()
                .stream()
                .map(AccountMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int deleteById(Long id) {
        if (!jpaAccountRepository.existsById(id)) return 0;
        jpaAccountRepository.deleteById(id);
        return 1;
    }

    @Override
    public boolean existsById(Long id) {
        return jpaAccountRepository.existsById(id);
    }
}
