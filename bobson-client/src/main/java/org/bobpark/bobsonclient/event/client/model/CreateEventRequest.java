package org.bobpark.bobsonclient.event.client.model;

import java.util.Map;

public interface CreateEventRequest {

    String eventName();

    Map<String, Object> eventData();

    String createdModuleName();
}
