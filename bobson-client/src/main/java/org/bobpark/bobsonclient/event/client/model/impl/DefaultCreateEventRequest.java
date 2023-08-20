package org.bobpark.bobsonclient.event.client.model.impl;

import java.util.Map;

import lombok.Builder;

import org.bobpark.bobsonclient.event.client.model.CreateEventRequest;

@Builder
public record DefaultCreateEventRequest(String eventName,
                                        Map<String, Object> eventData,
                                        String createdModuleName) implements CreateEventRequest {
}
