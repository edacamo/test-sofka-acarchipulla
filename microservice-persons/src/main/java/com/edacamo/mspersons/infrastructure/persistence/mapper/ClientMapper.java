package com.edacamo.mspersons.infrastructure.persistence.mapper;

import com.edacamo.mspersons.domain.model.Client;
import com.edacamo.mspersons.dto.RegisterRequest;
import com.edacamo.mspersons.dto.RegisterResponse;
import com.edacamo.mspersons.infrastructure.persistence.entity.ClientEntity;

public class ClientMapper {

    public static ClientEntity toEntity(Client client) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setId(client.getId());
        clientEntity.setNombre(client.getNombre());
        clientEntity.setGenero(client.getGenero());
        clientEntity.setEdad(client.getEdad());
        clientEntity.setIdentificacion(client.getIdentificacion());
        clientEntity.setDireccion(client.getDireccion());
        clientEntity.setTelefono(client.getTelefono());
        clientEntity.setClienteId(client.getClienteId());
        clientEntity.setContrasenia(client.getContrasenia());
        clientEntity.setEstado(client.getEstado());
        return clientEntity;
    }

    public static Client toDomain(ClientEntity clientEntity) {
        Client client = new Client();
        client.setId(clientEntity.getId());
        client.setNombre(clientEntity.getNombre());
        client.setGenero(clientEntity.getGenero());
        client.setEdad(clientEntity.getEdad());
        client.setIdentificacion(clientEntity.getIdentificacion());
        client.setDireccion(clientEntity.getDireccion());
        client.setTelefono(clientEntity.getTelefono());
        client.setClienteId(clientEntity.getClienteId());
        client.setContrasenia(clientEntity.getContrasenia());
        client.setEstado(clientEntity.getEstado());

        return client;
    }

    public static ClientEntity toRequestSaveClient(RegisterRequest request) {
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClienteId(request.getUsuario());
        clientEntity.setContrasenia(request.getPassword());
        clientEntity.setEstado(request.getEstado());
        clientEntity.setNombre(request.getNombre());
        clientEntity.setGenero(request.getGenero());
        clientEntity.setEdad(request.getEdad());
        clientEntity.setIdentificacion(request.getIdentificacion());
        clientEntity.setDireccion(request.getDireccion());
        clientEntity.setTelefono(request.getTelefono());

        return clientEntity;
    }


}
