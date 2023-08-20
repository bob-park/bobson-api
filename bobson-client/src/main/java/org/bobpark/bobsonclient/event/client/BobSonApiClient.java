package org.bobpark.bobsonclient.event.client;

import org.bobpark.bobsonclient.event.client.model.CreateEventRequest;
import org.bobpark.bobsonclient.event.client.model.EventResponse;

public interface BobSonApiClient<B extends CreateEventRequest, R extends EventResponse> {

    R push(B pushData);

}
