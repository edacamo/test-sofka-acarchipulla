package com.edacamo.mspersons.infrastructure.persistence.jpa;

import com.edacamo.mspersons.infrastructure.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaClientRepository extends JpaRepository<ClientEntity, Long> {
    ClientEntity findByClienteId(String clienteId);
}
