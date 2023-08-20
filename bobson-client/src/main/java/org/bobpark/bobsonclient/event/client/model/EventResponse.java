package org.bobpark.bobsonclient.event.client.model;

import java.util.Map;

public interface EventResponse {

    String getId();

    String getEventName();

    Map<String, Object> getEventData();

    EventStatus getStatus();

    default String getCreatedModuleName() {
        return null;
    }

    default String getCreatedIpAddress() {
        return null;
    }

    default String getExecutedModuleName() {
        return null;
    }

    default String getExecutedIpAddress() {
        return null;
    }

    default String getMessage() {
        return null;
    }
}
