package com.edacamo.msaccounts.infrastructure.services;

import com.edacamo.msaccounts.application.ClientSnapshotUseCase;
import com.edacamo.msaccounts.domain.events.ClientDeletedEvent;
import com.edacamo.msaccounts.domain.events.ClientCreatedEvent;
import com.edacamo.msaccounts.infrastructure.persistence.entity.ClientSnapshotEntity;
import com.edacamo.msaccounts.domain.repositories.ClientSnapshotRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class ClientSnapshotEventsService {

    private final ClientSnapshotUseCase useCase;

    public ClientSnapshotEventsService(ClientSnapshotUseCase useCase) {
        this.useCase = useCase;
    }

    @KafkaListener(topics = "client-created",
            containerFactory = "kafkaListenerContainerFactory",
            groupId = "ms-accounts-client-created")
    public void consume(ClientCreatedEvent event) {
        try {
            log.info("Received ClientCreatedEvent: {}", event.toString());
            useCase.handleClientCreated(event);
        } catch (Exception e) {
            log.error("Error processing ClientCreatedEvent with clienteId={}", event.getClienteId(), e);
            // Aquí puedes aplicar alguna lógica extra si deseas: alertas, reintentos, DLQ, etc.
        }
    }

    // Consumidor para ClientDeletedEvent (cuando se elimina un cliente)
    @KafkaListener(topics = "client-deleted",
            containerFactory = "clientDeletedEventKafkaListenerContainerFactory",
            groupId = "ms-accounts-client-deleted")
    public void consume(ClientDeletedEvent event) {
        try {
            log.info("Received ClientDeletedEvent: {}", event.toString());
            useCase.handleClientDeleted(event);
        } catch (Exception e) {
            log.error("Error processing ClientDeletedEvent with clienteId={}", event.getClienteId(), e);
            // Aquí también podrías aplicar lógica extra como alertas, reintentos, DLQ, etc.
        }
    }
}
