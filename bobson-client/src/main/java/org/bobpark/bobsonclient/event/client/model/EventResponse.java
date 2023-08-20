package org.bobpark.bobsonclient.event.client.model;

import java.util.Map;

public interface EventResponse {

    String id();

    String eventName();

    Map<String, Object> eventData();

    EventStatus status();

    default String createdModuleName() {
        return null;
    }

    default String createdIpAddress() {
        return null;
    }

    default String executedModuleName() {
        return null;
    }

    default String executedIpAddress() {
        return null;
    }

    default String message() {
        return null;
    }
}
