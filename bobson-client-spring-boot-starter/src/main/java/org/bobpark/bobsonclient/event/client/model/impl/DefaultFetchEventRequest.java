package org.bobpark.bobsonclient.event.client.model.impl;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import org.bobpark.bobsonclient.event.client.model.FetchEventRequest;

@ToString
@Getter
public class DefaultFetchEventRequest implements FetchEventRequest {

    private final String eventName;
    private final String instanceId;

    @Builder
    private DefaultFetchEventRequest(String eventName, String instanceId) {
        this.eventName = eventName;
        this.instanceId = instanceId;
    }
}
