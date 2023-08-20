package org.bobpark.bobsonclient.event.client.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import org.bobpark.bobsonclient.configure.properties.BobsonClientProperties;
import org.bobpark.bobsonclient.event.client.BobSonApiClient;
import org.bobpark.bobsonclient.event.client.model.CreateEventRequest;
import org.bobpark.bobsonclient.event.client.model.EventResponse;
import org.bobpark.bobsonclient.event.client.model.impl.DefaultCreateEventRequest;
import org.bobpark.bobsonclient.event.client.model.impl.DefaultEventResponse;
import org.bobpark.bobsonclient.event.exception.FailedPushEventException;

@Slf4j
@RequiredArgsConstructor
public class DefaultEventStoreApiClient implements BobSonApiClient<DefaultCreateEventRequest, DefaultEventResponse> {

    private static final String PUSH_API = "/event";

    private final BobsonClientProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public DefaultEventResponse push(DefaultCreateEventRequest pushData) {

        RequestEntity<DefaultCreateEventRequest> requestEntity =
            RequestEntity.post(properties.getHost() + PUSH_API)
                .body(pushData);

        ResponseEntity<DefaultEventResponse> response = restTemplate.exchange(requestEntity,
            DefaultEventResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new FailedPushEventException();
        }

        DefaultEventResponse body = response.getBody();

        log.debug("pushed event data. (id={})", body.id());

        return body;
    }

}
