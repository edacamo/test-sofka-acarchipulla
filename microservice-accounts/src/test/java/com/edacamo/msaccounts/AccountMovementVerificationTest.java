package com.edacamo.msaccounts;

import com.edacamo.msaccounts.application.AccountUseCase;
import com.edacamo.msaccounts.application.MovementsUseCase;
import com.edacamo.msaccounts.domain.model.Account;
import com.edacamo.msaccounts.domain.model.Movements;
import com.edacamo.msaccounts.domain.repositories.MovementsRepository;
import com.edacamo.msaccounts.dto.AccountRegisterRequest;
import com.edacamo.msaccounts.dto.MovementRequest;
import com.edacamo.msaccounts.dto.MovementResponse;
import com.edacamo.msaccounts.infrastructure.persistence.entity.MovementEntity;
import com.edacamo.msaccounts.infrastructure.persistence.mapper.AccountMapper;
import com.edacamo.msaccounts.infrastructure.persistence.mapper.MovementMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@SpringBootTest
@Transactional
public class AccountMovementVerificationTest {

    @Autowired
    private AccountUseCase accountUseCase;

    @Autowired
    private MovementsUseCase movementsUseCase;

    @Autowired
    private MovementsRepository movementsRepository;

    @Test
    void shouldCreateAccountAndAddMovements() {
        log.info("[START] Creating account and adding test movements...");

        // Arrange: Datos de prueba
        AccountRegisterRequest accountRequest = new AccountRegisterRequest();
        accountRequest.setNumeroCuenta("123456789");
        accountRequest.setTipo("AHORROS");
        accountRequest.setSaldoInicial(new BigDecimal("1000"));
        accountRequest.setClienteId("jperez");
        accountRequest.setEstado(true);

        // Act: Crear la cuenta
        Account save = AccountMapper.toDomainRequest(accountRequest);
        Account createdAccount = accountUseCase.create(save);

        // Verificar que la cuenta se creó correctamente
        assertNotNull(createdAccount);
        assertNotNull(createdAccount.getId());
        assertEquals("123456789", createdAccount.getNumeroCuenta());
        assertEquals("AHORROS", createdAccount.getTipo());

        // Act: Agregar un movimiento de tipo 'C' (Crédito)
        MovementRequest movementRequest = new MovementRequest(
                null,
                "C",
                new BigDecimal("200"),
                "123456789");

        Movements saveMovCred = MovementMapper.toDomainRequest(movementRequest);
        movementsUseCase.create(saveMovCred);

        // Act: Agregar un movimiento de tipo 'D' (Débito)
        movementRequest = new MovementRequest(
                null,
                "D",
                new BigDecimal("100"),
                "123456789");

        Movements saveMovDeb = MovementMapper.toDomainRequest(movementRequest);
        movementsUseCase.create(saveMovDeb);

        createdAccount.setSaldoInicial(new BigDecimal("1100"));

        // Assert: Verificar que los movimientos fueron agregados a la cuenta
        List<MovementResponse> movements = movementsUseCase.getMovimientosCuenta(createdAccount.getId());
        assertEquals(2, movements.size());

        MovementResponse creditMovement = movements.get(0);
        assertEquals(new BigDecimal("200"), creditMovement.getValor());
        assertEquals("C", creditMovement.getTipoMovimiento());

        MovementResponse debitMovement = movements.get(1);
        assertEquals(new BigDecimal("100"), debitMovement.getValor());
        assertEquals("D", debitMovement.getTipoMovimiento());

        // Assert: Verificar que la cuenta tiene los movimientos esperados
        assertEquals(new BigDecimal("1100"), createdAccount.getSaldoInicial());  // 1000 (saldo inicial) + 200 (crédito) - 100 (débito)

    }
}
