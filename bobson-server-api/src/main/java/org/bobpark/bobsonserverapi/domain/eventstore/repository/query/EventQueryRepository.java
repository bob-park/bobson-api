package org.bobpark.bobsonserverapi.domain.eventstore.repository.query;

import java.util.Optional;

import org.bobpark.bobsonserverapi.domain.eventstore.entity.Event;

public interface EventQueryRepository {

    Optional<Event> fetchEvent(String eventName, String ipAddress);
}
