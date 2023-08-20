package org.bobpark.bobsonclient.event.client.model.impl;

import java.util.Map;

import lombok.ToString;

import org.bobpark.bobsonclient.event.client.model.EventResponse;
import org.bobpark.bobsonclient.event.client.model.EventStatus;

@ToString
public class DefaultEventResponse implements EventResponse {

    private String id;
    private String eventName;
    private Map<String, Object> eventData;
    private EventStatus status;

    public String id() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String eventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Map<String, Object> eventData() {
        return eventData;
    }

    public void setEventData(Map<String, Object> eventData) {
        this.eventData = eventData;
    }

    public EventStatus status() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
