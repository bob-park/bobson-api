package org.bobpark.bobsonserverapi.domain.eventstore.service;

import org.bobpark.bobsonserverapi.domain.eventstore.entity.EventId;
import org.bobpark.bobsonserverapi.domain.eventstore.model.CompleteEventRequest;
import org.bobpark.bobsonserverapi.domain.eventstore.model.CreateEventRequest;
import org.bobpark.bobsonserverapi.domain.eventstore.model.EventResponse;

public interface EventService {

    EventResponse createEvent(CreateEventRequest createRequest);

    EventResponse fetch(String eventName);

    EventResponse complete(EventId id, CompleteEventRequest completeRequest);
}
