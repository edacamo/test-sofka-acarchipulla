package com.edacamo.msaccounts.domain.repositories;

import com.edacamo.msaccounts.domain.model.ClientSnapshot;
import java.util.Optional;

public interface ClientSnapshotRepository{
    Optional<ClientSnapshot> findByClienteId(String clienteId);

    ClientSnapshot save(ClientSnapshot clientSnapshot);

    int delete(ClientSnapshot clientSnapshot);
}
