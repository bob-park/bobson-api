package org.bobpark.bobsonclient.event.client.model.impl;

import java.util.Map;

import org.bobpark.bobsonclient.event.client.model.EventResponse;
import org.bobpark.bobsonclient.event.client.model.EventStatus;

public record DefaultEventResponse(String id,
                                   String eventName,
                                   Map<String, Object> eventData,
                                   EventStatus status) implements EventResponse {

}
