package org.bobpark.bobsonclient.event.client.model;

import java.util.Map;

public interface CreateEventRequest {

    String getEventName();

    Map<String, Object> getEventData();

    String getCreatedModuleName();
}
