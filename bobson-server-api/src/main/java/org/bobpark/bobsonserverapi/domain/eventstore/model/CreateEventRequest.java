package org.bobpark.bobsonserverapi.domain.eventstore.model;

import java.util.Map;

public record CreateEventRequest(String eventName,
                                 Map<String, Object> eventData,
                                 String createdModuleName,
                                 String createdIpAddress) {
}
