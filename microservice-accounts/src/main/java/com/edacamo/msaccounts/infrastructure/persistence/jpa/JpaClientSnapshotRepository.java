package com.edacamo.msaccounts.infrastructure.persistence.jpa;

import com.edacamo.msaccounts.infrastructure.persistence.entity.ClientSnapshotEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaClientSnapshotRepository extends JpaRepository<ClientSnapshotEntity, Long> {
    Optional<ClientSnapshotEntity> findByClienteId(String clienteId);
}
