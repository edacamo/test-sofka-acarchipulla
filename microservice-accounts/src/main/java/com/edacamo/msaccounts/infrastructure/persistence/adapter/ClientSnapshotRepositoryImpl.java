package com.edacamo.msaccounts.infrastructure.persistence.adapter;

import com.edacamo.msaccounts.domain.model.ClientSnapshot;
import com.edacamo.msaccounts.domain.repositories.ClientSnapshotRepository;
import com.edacamo.msaccounts.infrastructure.persistence.entity.ClientSnapshotEntity;
import com.edacamo.msaccounts.infrastructure.persistence.jpa.JpaClientSnapshotRepository;
import com.edacamo.msaccounts.infrastructure.persistence.mapper.ClientSnapshotMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ClientSnapshotRepositoryImpl implements ClientSnapshotRepository {

    private final JpaClientSnapshotRepository jpaClientSnapshotRepository;

    @Override
    public Optional<ClientSnapshot> findByClienteId(String clienteId) {
        return jpaClientSnapshotRepository.findByClienteId(clienteId)
                .map(ClientSnapshotMapper::toDomain);
    }

    @Override
    public ClientSnapshot save(ClientSnapshot clientSnapshot) {

        ClientSnapshotEntity clientEntity = ClientSnapshotMapper.toEntity(clientSnapshot);

        ClientSnapshotEntity saved = jpaClientSnapshotRepository.save(clientEntity);

        return ClientSnapshotMapper.toDomain(saved);
    }

    @Override
    public int delete(ClientSnapshot clientSnapshot) {
        if (!jpaClientSnapshotRepository.existsById(clientSnapshot.getId())) return 0;
        jpaClientSnapshotRepository.delete(ClientSnapshotMapper.toEntity(clientSnapshot));
        return 1;
    }
}
