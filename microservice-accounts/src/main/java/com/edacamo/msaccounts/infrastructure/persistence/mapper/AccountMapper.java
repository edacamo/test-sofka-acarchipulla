package com.edacamo.msaccounts.infrastructure.persistence.mapper;

import com.edacamo.msaccounts.domain.model.Account;
import com.edacamo.msaccounts.domain.model.ClientSnapshot;
import com.edacamo.msaccounts.domain.model.Movements;
import com.edacamo.msaccounts.dto.AccountRegisterReponse;
import com.edacamo.msaccounts.dto.AccountRegisterRequest;
import com.edacamo.msaccounts.infrastructure.persistence.entity.AccountEntity;
import com.edacamo.msaccounts.infrastructure.persistence.entity.ClientSnapshotEntity;
import com.edacamo.msaccounts.infrastructure.persistence.entity.MovementEntity;

import java.util.List;
import java.util.stream.Collectors;

public class AccountMapper {

    public static AccountEntity toEntity(Account acc) {
        AccountEntity entity = new AccountEntity();
        entity.setId(acc.getId());
        entity.setNumeroCuenta(acc.getNumeroCuenta());
        entity.setTipo(acc.getTipo());
        entity.setSaldoInicial(acc.getSaldoInicial());
        entity.setEstado(acc.getEstado());

        if (acc.getClient() != null) {
            ClientSnapshotEntity snapshot = new ClientSnapshotEntity();
            snapshot.setClienteId(acc.getClient().getClienteId());
            snapshot.setNombre(acc.getClient().getNombre());
            snapshot.setGenero(acc.getClient().getGenero());
            snapshot.setEdad(acc.getClient().getEdad());
            snapshot.setIdentificacion(acc.getClient().getIdentificacion());
            snapshot.setDireccion(acc.getClient().getDireccion());
            snapshot.setTelefono(acc.getClient().getTelefono());
            snapshot.setEstado(acc.getClient().getEstado());
            entity.setClient(snapshot);
        }

        if (acc.getMovements() != null) {
            List<MovementEntity> movements = acc.getMovements().stream().map(m -> {
                MovementEntity me = new MovementEntity();
                me.setFecha(m.getFecha());
                me.setTipoMovimiento(m.getTipoMovimiento());
                me.setDescripcion(m.getDescripcion());
                me.setSaldoInicial(m.getSaldoInicial());
                me.setValor(m.getValor());
                me.setSaldo(m.getSaldo());
                me.setAccount(entity);
                return me;
            }).collect(Collectors.toList());
            entity.setMovements(movements);
        }

        return entity;
    }

    public static AccountEntity toEntity(Account account, ClientSnapshotEntity clientEntity) {
        return new AccountEntity(
                account.getId(),
                account.getNumeroCuenta(),
                account.getTipo(),
                account.getSaldoInicial(),
                account.getEstado(),
                clientEntity,
                null
        );
    }

    public static Account toDomain(AccountEntity entity) {
        Account acc = new Account();
        acc.setId(entity.getId());
        acc.setNumeroCuenta(entity.getNumeroCuenta());
        acc.setTipo(entity.getTipo());
        acc.setSaldoInicial(entity.getSaldoInicial());
        acc.setEstado(entity.getEstado());

        if (entity.getClient() != null) {
            ClientSnapshot client = new ClientSnapshot(
                    entity.getClient().getId(),
                    entity.getClient().getClienteId(),
                    entity.getClient().getNombre(),
                    entity.getClient().getGenero(),
                    entity.getClient().getEdad(),
                    entity.getClient().getIdentificacion(),
                    entity.getClient().getDireccion(),
                    entity.getClient().getTelefono(),
                    entity.getClient().getEstado()
            );
            acc.setClient(client);
        }

        if (entity.getMovements() != null) {
            List<Movements> movements = entity.getMovements().stream().map(m -> new Movements(
                    m.getId(), m.getFecha(), m.getTipoMovimiento(),
                    m.getDescripcion(), m.getSaldoInicial(),
                    m.getValor(), m.getSaldo(), null
            )).collect(Collectors.toList());
            acc.setMovements(movements);
        }

        return acc;
    }

    public static Account toDomainRequest(AccountRegisterRequest dto) {
        ClientSnapshot client = new ClientSnapshot(dto.getClienteId());
        return new Account(
                null,
                dto.getNumeroCuenta(),
                dto.getTipo(),
                dto.getSaldoInicial(),
                dto.getEstado(),
                client,
                null
        );
    }

    public static AccountRegisterReponse toResponse(Account account) {
        AccountRegisterReponse response = new AccountRegisterReponse();
        response.setNumerCuenta(account.getNumeroCuenta());
        response.setTipo(account.getTipo());
        response.setSaldoInicial(account.getSaldoInicial());
        response.setEstado(account.getEstado());

        if (account.getClient() != null) {
            response.setCliente(account.getClient().getNombre());
        }

        return response;
    }
}
