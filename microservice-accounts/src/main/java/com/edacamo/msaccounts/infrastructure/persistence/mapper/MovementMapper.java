package com.edacamo.msaccounts.infrastructure.persistence.mapper;

import com.edacamo.msaccounts.domain.model.Account;
import com.edacamo.msaccounts.domain.model.Movements;
import com.edacamo.msaccounts.dto.MovementRequest;
import com.edacamo.msaccounts.dto.MovementResponse;
import com.edacamo.msaccounts.infrastructure.persistence.entity.AccountEntity;
import com.edacamo.msaccounts.infrastructure.persistence.entity.MovementEntity;

public class MovementMapper {

    public static MovementEntity toEntity(Movements movement) {

        MovementEntity movementEntity = new MovementEntity();
        movementEntity.setId(movement.getId());
        movementEntity.setFecha(movement.getFecha());
        movementEntity.setDescripcion(movement.getDescripcion());
        movementEntity.setSaldoInicial(movement.getSaldoInicial());

        if(movement.getAccount() != null) {
            AccountEntity acc = new AccountEntity();
            acc.setId(movement.getAccount().getId());
            acc.setEstado(movement.getAccount().getEstado());
            acc.setTipo(movement.getAccount().getTipo());
            acc.setNumeroCuenta(movement.getAccount().getNumeroCuenta());
            acc.setSaldoInicial(movement.getAccount().getSaldoInicial());
            movementEntity.setAccount(acc);
        }

        return movementEntity;
    }

    public static MovementEntity toEntity(Movements movement, AccountEntity accountEntity) {

        return new MovementEntity(
                movement.getId(),
                movement.getFecha(),
                movement.getTipoMovimiento(),
                movement.getDescripcion(),
                movement.getSaldoInicial(),
                movement.getValor(),
                movement.getSaldo(),
                accountEntity
        );
    }

    public static Movements toDomain(MovementEntity entity){
        Movements movement = new Movements();
        movement.setId(entity.getId());
        movement.setDescripcion(entity.getDescripcion());
        movement.setSaldoInicial(entity.getSaldoInicial());
        movement.setFecha(entity.getFecha());
        movement.setTipoMovimiento(entity.getTipoMovimiento());
        movement.setValor(entity.getValor());
        movement.setSaldo(entity.getSaldo());

        if(entity.getAccount() != null) {
            Account account = new Account();
            account.setId(entity.getAccount().getId());
            account.setEstado(entity.getAccount().getEstado());
            account.setTipo(entity.getAccount().getTipo());
            account.setNumeroCuenta(entity.getAccount().getNumeroCuenta());
            account.setSaldoInicial(entity.getAccount().getSaldoInicial());
            movement.setAccount(account);
        }

        return movement;
    }

    public static Movements toDomainRequest(MovementRequest dto) {
        Account account = new Account(dto.getNumeroCuenta());
        return new Movements(
                null,
                dto.getFecha(),
                dto.getTipoMovimiento(),
                null,
                null,
                dto.getValor(),
                null,
                account
        );
    }

    public static MovementResponse toResponse(Movements movement) {
        MovementResponse response = new MovementResponse();

        if(movement.getAccount() != null) {
            response.setNumeroCuenta(movement.getAccount().getNumeroCuenta());
            response.setEstado(movement.getAccount().getEstado());
            response.setTipo(movement.getAccount().getTipo());
        }

        response.setSaldoInicial(movement.getSaldoInicial());
        response.setMovimiento(movement.getDescripcion());
        response.setValor(movement.getValor());
        response.setTipoMovimiento(movement.getTipoMovimiento());

        return response;
    }
}
