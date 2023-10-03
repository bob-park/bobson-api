package org.bobpark.bobsonclient.event.client;

import org.bobpark.bobsonclient.event.client.model.CreateEventRequest;
import org.bobpark.bobsonclient.event.client.model.EventResponse;

public interface BobSonApiClient {

    EventResponse push(CreateEventRequest pushData);

    EventResponse fetch(String eventName);

    EventResponse complete(String eventId, boolean isSuccess, String detailMessage);

}
