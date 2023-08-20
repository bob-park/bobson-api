package org.bobpark.bobsonclient.event.client.model.impl;

import java.util.Map;

import lombok.Builder;
import lombok.ToString;

import org.bobpark.bobsonclient.event.client.model.CreateEventRequest;

@ToString
public class DefaultCreateEventRequest implements CreateEventRequest {

    private final String eventName;
    private final Map<String, Object> eventData;
    private final String createdModuleName;

    @Builder
    private DefaultCreateEventRequest(String eventName, Map<String, Object> eventData, String createdModuleName) {
        this.eventName = eventName;
        this.eventData = eventData;
        this.createdModuleName = createdModuleName;
    }

    public String eventName() {
        return eventName;
    }

    public Map<String, Object> eventData() {
        return eventData;
    }

    public String createdModuleName() {
        return createdModuleName;
    }

}
