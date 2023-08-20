package org.bobpark.bobsonserverapi.domain.eventstore.cqrs.event;

import java.util.Map;

import lombok.Builder;

import org.bobpark.bobsonserverapi.domain.eventstore.entity.EventId;

@Builder
public record CreatedEvent(EventId id,
                           String eventName,
                           Map<String, Object> eventData,
                           String createdModuleName,
                           String createdIpAddress) {
}
