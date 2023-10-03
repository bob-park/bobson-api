package org.bobpark.bobsonclient.event.client.model.impl;

import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.bobpark.bobsonclient.event.client.model.EventResponse;
import org.bobpark.bobsonclient.event.client.model.EventStatus;

@ToString
@Getter
@Setter
public class DefaultEventResponse implements EventResponse {

    private String id;
    private String eventName;
    private Map<String, Object> eventData;
    private EventStatus status;

}
