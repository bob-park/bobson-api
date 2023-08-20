package org.bobpark.bobsonserverapi.domain.eventstore.model;

import java.util.Map;

import lombok.Builder;

import org.bobpark.bobsonserverapi.common.type.EventStatus;

@Builder
public record EventResponse(String id,
                            String eventName,
                            Map<String, Object> eventData,
                            EventStatus status,
                            String createdModuleName,
                            String createdIpAddress,
                            String executedModuleName,
                            String executedIpAddress,
                            String message) {

    public static EventResponse empty() {
        return EventResponse.builder().build();
    }
}
