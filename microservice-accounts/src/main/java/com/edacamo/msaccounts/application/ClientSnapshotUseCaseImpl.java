package com.edacamo.msaccounts.application;

import com.edacamo.msaccounts.domain.events.ClientCreatedEvent;
import com.edacamo.msaccounts.domain.events.ClientDeletedEvent;
import com.edacamo.msaccounts.domain.repositories.ClientSnapshotRepository;
import com.edacamo.msaccounts.infrastructure.persistence.mapper.ClientSnapshotMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientSnapshotUseCaseImpl implements ClientSnapshotUseCase {

    private final ClientSnapshotRepository repository;

    @Override
    public void handleClientCreated(ClientCreatedEvent event) {
        repository.save(ClientSnapshotMapper.toDomain(event));
        log.info("Snapshot saved for clienteId={}", event.getClienteId());
    }

    @Override
    public void handleClientDeleted(ClientDeletedEvent event) {
        repository.findByClienteId(event.getClienteId()).ifPresentOrElse(snapshot -> {
            repository.delete(snapshot);
            log.info("Snapshot deleted for clientId={}", event.getClienteId());
        }, () -> log.warn("Snapshot not found for clientId={}", event.getClienteId()));
    }
}
