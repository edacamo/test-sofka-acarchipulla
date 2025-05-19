package com.edacamo.msaccounts.infrastructure.persistence.mapper;

import com.edacamo.msaccounts.domain.events.ClientCreatedEvent;
import com.edacamo.msaccounts.domain.model.ClientSnapshot;
import com.edacamo.msaccounts.infrastructure.persistence.entity.ClientSnapshotEntity;

public class ClientSnapshotMapper {

    public static ClientSnapshotEntity toEntity(ClientSnapshot snapshot) {
        ClientSnapshotEntity entity = new ClientSnapshotEntity();
        entity.setId(snapshot.getId());
        entity.setClienteId(snapshot.getClienteId());
        entity.setNombre(snapshot.getNombre());
        entity.setGenero(snapshot.getGenero());
        entity.setEdad(snapshot.getEdad());
        entity.setIdentificacion(snapshot.getIdentificacion());
        entity.setDireccion(snapshot.getDireccion());
        entity.setTelefono(snapshot.getTelefono());
        entity.setEstado(snapshot.getEstado());

        return entity;
    }

    public static ClientSnapshot toDomain(ClientSnapshotEntity entity){
        ClientSnapshot  snapshot = new ClientSnapshot();
        snapshot.setId(entity.getId());
        snapshot.setClienteId(entity.getClienteId());
        snapshot.setNombre(entity.getNombre());
        snapshot.setGenero(entity.getGenero());
        snapshot.setEdad(entity.getEdad());
        snapshot.setIdentificacion(entity.getIdentificacion());
        snapshot.setDireccion(entity.getDireccion());
        snapshot.setTelefono(entity.getTelefono());
        snapshot.setEstado(entity.getEstado());

        return snapshot;
    }

    public static ClientSnapshot toDomain(ClientCreatedEvent client){
        ClientSnapshot  snapshot = new ClientSnapshot();
        snapshot.setClienteId(client.getClienteId());
        snapshot.setNombre(client.getNombre());
        snapshot.setGenero(client.getGenero());
        snapshot.setEdad(client.getEdad());
        snapshot.setIdentificacion(client.getIdentificacion());
        snapshot.setDireccion(client.getDireccion());
        snapshot.setTelefono(client.getTelefono());
        snapshot.setEstado(client.getEstado());

        return snapshot;
    }
}
