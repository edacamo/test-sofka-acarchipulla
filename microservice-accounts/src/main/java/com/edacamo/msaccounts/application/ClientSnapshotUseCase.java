package com.edacamo.msaccounts.application;

import com.edacamo.msaccounts.domain.events.ClientCreatedEvent;
import com.edacamo.msaccounts.domain.events.ClientDeletedEvent;

public interface ClientSnapshotUseCase {

    void handleClientCreated(ClientCreatedEvent event);
    void handleClientDeleted(ClientDeletedEvent event);
}
