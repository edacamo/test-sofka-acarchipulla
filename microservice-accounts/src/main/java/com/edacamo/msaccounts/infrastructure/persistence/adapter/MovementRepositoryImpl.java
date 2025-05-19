package com.edacamo.msaccounts.infrastructure.persistence.adapter;

import com.edacamo.msaccounts.common.exception.InsufficientFundsException;
import com.edacamo.msaccounts.domain.model.Account;
import com.edacamo.msaccounts.domain.model.Movements;
import com.edacamo.msaccounts.domain.repositories.AccountRepository;
import com.edacamo.msaccounts.domain.repositories.MovementsRepository;
import com.edacamo.msaccounts.dto.TransactionReportRequest;
import com.edacamo.msaccounts.dto.TransactionReportResponse;
import com.edacamo.msaccounts.infrastructure.persistence.entity.AccountEntity;
import com.edacamo.msaccounts.infrastructure.persistence.entity.MovementEntity;
import com.edacamo.msaccounts.infrastructure.persistence.jpa.JpaAccountRepository;
import com.edacamo.msaccounts.infrastructure.persistence.jpa.JpaMovementRepository;
import com.edacamo.msaccounts.infrastructure.persistence.mapper.AccountMapper;
import com.edacamo.msaccounts.infrastructure.persistence.mapper.MovementMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class MovementRepositoryImpl implements MovementsRepository {

    public static final String DEPOSIT = "C";
    public static final String WITHDRAWAL = "D";

    private final JpaMovementRepository jpaMovementRepository;
    private final JpaAccountRepository accountRepository;

    @Override
    public Movements create(Movements movement) {
        AccountEntity account = accountRepository.findByNumeroCuenta(movement.getAccount().getNumeroCuenta())
                .orElseThrow(() -> new EmptyResultDataAccessException("Cuenta no encontrada", 1));

        if (!Boolean.TRUE.equals(account.getEstado())) {
            throw new RuntimeException("No se pueden realizar movimientos en una cuenta inactiva.");
        }

        if (!movement.getTipoMovimiento().equalsIgnoreCase(DEPOSIT) &&
                !movement.getTipoMovimiento().equalsIgnoreCase(WITHDRAWAL)) {
            throw new RuntimeException("Tipo de movimiento no válido. Debe ser D (Debito) o C (Credito).");
        }

        BigDecimal saldoInicial = account.getSaldoInicial();

        //Obtiene saldo (Retiro o Deposito)
        BigDecimal balance = getBalance(movement, account);

        account.setSaldoInicial(balance);
        accountRepository.save(account);

        String description;

        if (movement.getTipoMovimiento().equalsIgnoreCase(WITHDRAWAL)) {
            description = "Retiro de " + movement.getValor();
        } else {
            description = "Deposito de " + movement.getValor();
        }

        movement.setDescripcion(description);
        movement.setSaldoInicial(saldoInicial);
        movement.setFecha(movement.getFecha() != null ? movement.getFecha() : LocalDateTime.now());
        movement.setSaldo(balance);

        MovementEntity create = this.jpaMovementRepository.save(MovementMapper.toEntity(movement, account));
        return MovementMapper.toDomain(create);
    }

    @Override
    public List<Movements> getMovimientosByCuenta(Long accountId) {

        return this.jpaMovementRepository.findByCuentaId(accountId)
                .stream()
                .map(MovementMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public int deleteById(Long movementId) {
        MovementEntity movement = this.jpaMovementRepository.findById(movementId)
                .orElseThrow(() -> new EmptyResultDataAccessException("Movimiento no encontrado", 1));

        AccountEntity account = movement.getAccount();

        List<MovementEntity> movements = this.jpaMovementRepository.findByCuentaId(account.getId());

        if (movements.isEmpty()) {
            throw new EmptyResultDataAccessException("No hay movimientos registrados para esta cuenta.", 1);
        }

        MovementEntity lastMovement = movements.get(movements.size() - 1);

        if (!lastMovement.getId().equals(movementId)) {
            throw new RuntimeException("Solo se puede eliminar el último movimiento registrado");
        }

        account.setSaldoInicial(account.getSaldoInicial().subtract(movement.getValor()));
        this.accountRepository.save(account);

        if(!jpaMovementRepository.existsById(movementId)) return 0;
        this.jpaMovementRepository.delete(movement);
        return 1;
    }

    @Override
    public Movements update(Long id, Movements movimientoDetails) {
        MovementEntity movimiento = this.jpaMovementRepository.findById(id)
                .orElseThrow(() -> new EmptyResultDataAccessException("Movimiento no encontrado", 1));

        movimiento.setFecha(movimientoDetails.getFecha());
        movimiento.setTipoMovimiento(movimientoDetails.getTipoMovimiento());
        movimiento.setValor(movimientoDetails.getValor());
        movimiento.setSaldo(movimientoDetails.getSaldo());
        movimiento.setAccount(AccountMapper.toEntity(movimientoDetails.getAccount()));

        MovementEntity update = this.jpaMovementRepository.save(movimiento);

        return MovementMapper.toDomain(update);
    }

    @Override
    public List<TransactionReportResponse> getMovementsReport(TransactionReportRequest request) {
        List<MovementEntity> movs = this.jpaMovementRepository.findByAccountAndDateRange(
                request.getClientId(),
                request.getStartDate(),
                request.getEndDate());

        List<TransactionReportResponse> responses = new ArrayList<>();

        if (!movs.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

            for (MovementEntity mov : movs) {
                TransactionReportResponse r = new TransactionReportResponse(
                        mov.getFecha().format(formatter),
                        mov.getAccount().getClient().getNombre(),
                        mov.getAccount().getNumeroCuenta(),
                        mov.getAccount().getTipo(),
                        mov.getSaldoInicial(),
                        mov.getAccount().getEstado(),
                        mov.getValor(),
                        mov.getSaldo()
                );
                responses.add(r);
            }
        }

        return responses;
    }

    private static BigDecimal getBalance(Movements movement, AccountEntity account) {
        BigDecimal balance;

        if (movement.getTipoMovimiento().equalsIgnoreCase(WITHDRAWAL)) {
            balance = account.getSaldoInicial().subtract(movement.getValor().abs());
        } else {
            balance = account.getSaldoInicial().add(movement.getValor());
        }

        if (movement.getTipoMovimiento().equalsIgnoreCase(WITHDRAWAL) && balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new InsufficientFundsException("Saldo insuficiente para realizar el retiro");
        }
        return balance;
    }
}
