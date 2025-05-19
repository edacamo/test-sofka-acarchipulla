package com.edacamo.mspersons.domain.repositories;

import com.edacamo.mspersons.domain.model.Client;
import com.edacamo.mspersons.dto.RegisterRequest;
import com.edacamo.mspersons.dto.RegisterResponse;
import com.edacamo.mspersons.infrastructure.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {
    Optional<Client> findByClienteId(String clientId);

    List<Client> findAll();

    int deleteById(Long id);

    Client registerUser(RegisterRequest request);

    Client updateUser(RegisterRequest request);

    int deleteUser(String clientId);
}
